package com.spots.bella.activity.make_post;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.spots.bella.R;
import com.spots.bella.constants.Common;
import com.spots.bella.di.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.spots.bella.constants.Common.getStatusBarHeight;
import static com.spots.bella.constants.Common.syncToolbar;

public class MakePostActivity extends BaseActivity implements MakePostView {
    private static final String TAG = "MakePostActivity";
    private static Uri IMAGE_URI = null;

    @BindView(R.id.activity_post_toolbar)
    Toolbar toolbar;

    @BindView(R.id.activity_post_toolbar_container)
    LinearLayout toolbar_container;

    @BindView(R.id.et_toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.et_activity_make_post)
    EditText et_say_somthing;

    @BindView(R.id.iv_activity_make_post)
    ImageView iv_post_photo;

    @BindView(R.id.progress_activity_post)
    ProgressBar progress;

    MenuItem btn_share;

    MakePostPresenter mPresenter;
    private int image_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_post);
        ButterKnife.bind(this);
        mPresenter = new MakePostPresenterIMP(this, mDatabase, mAuth, mStorage);
        if (getIntent().getExtras() != null) {
            IMAGE_URI = getIntent().getParcelableExtra("post_image");
            Log.d(TAG, "onCreate: " + IMAGE_URI);
        }
        getImageCount();
        initViews();
    }

    private void getImageCount() {
        if (IMAGE_URI != null) {
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int count = 0;
                    for (DataSnapshot child : dataSnapshot.child(Common.USER_PHOTOS_STRING).child(mAuth.getCurrentUser()
                            .getUid()).getChildren()) {
                        count++;
                        Log.d(TAG, "onDataChange: Image count = " + count+" child = "+child.getValue());
                    }
                    image_count = count;
                    Log.d(TAG, "onDataChange: count = " + image_count);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        syncToolbar(toolbar_title, Gravity.START, R.string.create_post);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_post_menu, menu);
        btn_share = menu.findItem(R.id.menu_activity_post_share);
        if (IMAGE_URI != null) {
            btn_share.setEnabled(true);
        }
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
            finish();
            return true;
        } else if (id == R.id.menu_activity_post_share) {
            sharePost();
        }

        return super.onOptionsItemSelected(item);
    }

    private void sharePost() {
        String text = et_say_somthing.getText().toString().trim();
        mPresenter.onSharePostClicked(text, IMAGE_URI, image_count);
//        mPresenter.onSharePostClicked(text);
    }


    private void initViews() {
        Log.d(TAG, "initViews: ");
        setupToolbar();

        et_say_somthing.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged: ");
                if (IMAGE_URI == null) {
                    if (charSequence.length() >= 2)
                        btn_share.setEnabled(true);
                    else {
                        btn_share.setEnabled(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        if (IMAGE_URI != null) {
            Log.d(TAG, "initViews: 1");
            iv_post_photo.setImageURI(IMAGE_URI);
            iv_post_photo.setVisibility(View.VISIBLE);
            et_say_somthing.setHint(context.getString(R.string.say_something_about_this_photo));
        } else {
            Log.d(TAG, "initViews: 2");
            iv_post_photo.setVisibility(View.GONE);
            et_say_somthing.setHint(context.getString(R.string.what_is_on_your_mind));
        }

    }

    private void setupToolbar() {
        setupToolbarContainer();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupToolbarContainer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
                int height = (int) TypedValue.complexToDimension(tv
                        .data, getResources().getDisplayMetrics());
                Log.d(TAG, "initViews: action height = " + height);
                Log.d(TAG, "initViews: status height = " + getStatusBarHeight(context));
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) toolbar_container.getLayoutParams();
                params.height = height + getStatusBarHeight(context);
                toolbar_container.setLayoutParams(params);
            }
        }
    }

    @Override
    public void showPregress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePregress() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void showImageView() {
        iv_post_photo.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideIamgeView() {
        iv_post_photo.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String message, int code) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message, String err_msg) {
        Log.e(TAG, "showMessage: " + err_msg);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void closeActivity() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
