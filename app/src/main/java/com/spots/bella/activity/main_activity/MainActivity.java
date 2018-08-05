package com.spots.bella.activity.main_activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.spots.bella.R;
import com.spots.bella.activity.artist_offers_activity.ArtistOffersActivity;
import com.spots.bella.activity.make_post.MakePostActivity;
import com.spots.bella.activity.messages_activity.MessagesActivity;
import com.spots.bella.activity.my_reservation_activity.MyReservationActivity;
import com.spots.bella.activity.post_activity.PostActivity;
import com.spots.bella.activity.profile.ProfileActivity;
import com.spots.bella.adapters.MainRecyclerViewAdapter;
import com.spots.bella.constants.Common;
import com.spots.bella.di.BaseActivity;
import com.spots.bella.models.Post;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.spots.bella.constants.Common.GALLERY_PICK_POST_IMAGE;
import static com.spots.bella.constants.Common.getStatusBarHeight;
import static com.spots.bella.constants.Common.setMenuCounter;
import static com.spots.bella.constants.Common.syncToolbar;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, MView, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_container)
    LinearLayout toolbar_container;


    @BindView(R.id.et_toolbar_title)
    TextView toolbar_title;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.rv_content_main)
    RecyclerView rv_content_main;

    @BindView(R.id.swipe_root_content_main)
    SwipeRefreshLayout swipe_root_content_main;


    private MPresenter mMPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //must be called before setContentView(...)
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMPresenter = new MPresenterIMP(this);

        initViews();

//        Toast.makeText(context, "Welcome " + pM.readeString(PreferenceManager.USER_TYPE) + " " + pM.readeString(PreferenceManager.USER_FULL_NAME), Toast.LENGTH_SHORT).show();

    }


    private void initViews() {
        View header = navigationView.getHeaderView(0);
        CircleImageView iv_profile_pic = header.findViewById(R.id.iv_profile);
        iv_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser() != null)
                    startActivity(new Intent(context, ProfileActivity.class));
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        swipe_root_content_main.setOnRefreshListener(this);
        //TODO: Init Toolbar
        setupToolbar();
        //TODO: Init main RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_content_main.setLayoutManager(layoutManager);
        rv_content_main.setHasFixedSize(true);
        ArrayList<Post> posts_list = new ArrayList<>();
        MainRecyclerViewAdapter mAdapter = new MainRecyclerViewAdapter(this, mMPresenter, posts_list);
        rv_content_main.setAdapter(mAdapter);
    }

    private void setupToolbar() {
        setupToolbarContainer();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        toggle.setDrawerIndicatorEnabled(false);
//        toolbar.setNavigationIcon(R.drawable.h);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupToolbarContainer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
                int height = (int) TypedValue.complexToDimension(tv
                        .data, getResources().getDisplayMetrics());
                Log.d(TAG, "initViews: action height = " + height);
                Log.d(TAG, "initViews: status height = " + getStatusBarHeight(MainActivity.this));
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) toolbar_container.getLayoutParams();
                params.height = height + getStatusBarHeight(MainActivity.this);
                toolbar_container.setLayoutParams(params);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        syncToolbar(toolbar_title, Gravity.CENTER, R.string.app_name);
        setMenuCounter(navigationView, R.id.drawer_menu_reservation, 3); // check the show this number
        setMenuCounter(navigationView, R.id.drawer_menu_messages, 18); // check the show this number
        mMPresenter.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Toast.makeText(MainActivity.this, "Not Ready!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        final int id = item.getItemId();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (id == R.id.drawer_menu_messages) {
                    startActivity(new Intent(MainActivity.this, MessagesActivity.class));
                } else if (id == R.id.drawer_menu_reservation) {
                    startActivity(new Intent(MainActivity.this, MyReservationActivity.class));
                } else if (id == R.id.drawer_artist_offers) {
                    startActivity(new Intent(MainActivity.this, ArtistOffersActivity.class));
                } else if (id == R.id.drawer_menu_map) {
                    Toast.makeText(MainActivity.this, "Not Ready!", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.drawer_menu_setting) {
                    Toast.makeText(MainActivity.this, "Not Ready!", Toast.LENGTH_SHORT).show();
                }
            }
        }, getResources().getInteger(R.integer.time_drawer_item_clicks_milis));


        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK_POST_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri image_uri = data.getData();
            startActivity(new Intent(this, MakePostActivity.class).putExtra("post_image", image_uri));
        }
    }

    //    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public static void setStatusBarGradiant(Activity activity) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = activity.getWindow();
//            Drawable background = activity.getResources().getDrawable(R.drawable.toolbar_background);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
////            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
//            window.setBackgroundDrawable(background);
//        }
//    }


    @Override
    public void showMessage(String message, int code) {
        final Snackbar snack = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
        snack.setAction("I Know", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        snack.show();
                    }
                }, Common.LOADING_DURATION);
            }
        });
        snack.show();
    }

    @Override
    public void showLoading() {
        swipe_root_content_main.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipe_root_content_main.setRefreshing(false);
    }

    @Override
    public void showRecyclerView() {
        rv_content_main.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        mMPresenter.onRefresh();
    }
}
