package com.spots.bella.activity.messages_activity;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spots.bella.R;
import com.spots.bella.activity.messages_activity.fragments.MainMessagesFragment;
import com.spots.bella.activity.messages_activity.fragments.MainMessagesFragment.OnMainMessagesFragmentInteractionListener;
import com.spots.bella.di.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.spots.bella.constants.Common.getStatusBarHeight;
import static com.spots.bella.constants.Common.setTranslucentStatusBar;
import static com.spots.bella.constants.Common.syncToolbar;

public class MessagesActivity extends BaseActivity implements
        OnMainMessagesFragmentInteractionListener {
    private static final String TAG = "MessagesActivity";

    @BindView(R.id.messages_activity_toolbar)
    Toolbar toolbar;

    @BindView(R.id.et_toolbar_title)
    TextView toolbar_title;

    @BindView(R.id.messages_toolbar_container)
    LinearLayout toolbar_container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_messages);

        ButterKnife.bind(this);

        initViews();

        showMainMessagesFragment();

    }

    private void showMainMessagesFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.messages_activity_container, new MainMessagesFragment(), getResources().getString(R.string.main_messages_fragment_tag));
        fragmentTransaction.commit();
    }

    private void initViews() {
        setupToolbar();
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
                Log.d(TAG, "initViews: status height = " + getStatusBarHeight(MessagesActivity.this));
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) toolbar_container.getLayoutParams();
                params.height = height + getStatusBarHeight(MessagesActivity.this);
                toolbar_container.setLayoutParams(params);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        syncToolbar(toolbar_title, Gravity.START, R.string.messages);
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMainMessagesFragmentOpened() {
        Log.d(TAG, "onMainMessagesFragmentOpened: ");
    }
}
