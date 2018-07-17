package com.spots.bella.activity.artist_offers_activity;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.spots.bella.R;
import com.spots.bella.activity.artist_offers_activity.fragments.FavouriteArtistOffersFragment;
import com.spots.bella.activity.artist_offers_activity.fragments.FavouriteArtistOffersFragment.OnFavouriteArtistFragmentInteractionListener;
import com.spots.bella.activity.artist_offers_activity.fragments.LatestArtistOffersFragment;
import com.spots.bella.activity.login_activity.fragments.LoginFragment;
import com.spots.bella.adapters.ArtistOffersViewPagerAdapter;
import com.spots.bella.di.BaseActivity;
import com.spots.bella.di.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.spots.bella.activity.artist_offers_activity.fragments.LatestArtistOffersFragment.*;
import static com.spots.bella.constants.Common.getStatusBarHeight;
import static com.spots.bella.constants.Common.setTranslucentStatusBar;
import static com.spots.bella.constants.Common.syncToolbar;

public class ArtistOffersActivity extends BaseActivity implements
        OnLatestArtistOffersFragmentInteractionListener,
        OnFavouriteArtistFragmentInteractionListener {

    private static final String TAG = "ArtistOffersActivity";

    @BindView(R.id.artist_offers_activity_toolbar)
    Toolbar toolbar;

    @BindView(R.id.et_toolbar_title)
    TextView toolbar_title;

    @BindView(R.id.artist_offers_toolbar_container)
    LinearLayout toolbar_container;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    ArtistOffersViewPagerAdapter viewPagerAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatusBar(ArtistOffersActivity.this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/clan_ot_book_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_artist_offers);


        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {
        setupToolbar();
        viewPagerAdapter = new ArtistOffersViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrament(new LatestArtistOffersFragment(), getResources().getString(R.string.fragment_latest_artist_offers_tag));
        viewPagerAdapter.addFrament(new FavouriteArtistOffersFragment(), getResources().getString(R.string.fragment_favourite_artist_offers_tag));
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupToolbar() {
        setupToolbarContainer();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        syncToolbar(toolbar_title, Gravity.START, R.string.artist_offers);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_artist_offers_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_notification_bell) {
            Toast.makeText(context, "IDK", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupToolbarContainer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
                int height = (int) TypedValue.complexToDimension(tv
                        .data, getResources().getDisplayMetrics());
                Log.d(TAG, "initViews: action height = " + height);
                Log.d(TAG, "initViews: status height = " + getStatusBarHeight(context));
                Log.d(TAG, "initViews: tab height = " + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics()));

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) toolbar_container.getLayoutParams();
                params.height = (int) (height + getStatusBarHeight(context) + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics()));
                toolbar_container.setLayoutParams(params);
            }
        }
    }

    @Override
    public void onLatestArtistOffersFragmentOpened() {
        Log.d(TAG, "onLatestArtistOffersFragmentOpened: ");
    }

    @Override
    public void onFavouriteArtistFragmentOpened() {
        Log.d(TAG, "onFavouriteArtistFragmentOpened: ");
    }
}
