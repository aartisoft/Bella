package com.spots.bella.activity.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toolbar;

import com.spots.bella.R;
import com.spots.bella.utils.SectionsStatePagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountSettingsActivity extends AppCompatActivity {
    private static final String TAG = "AccountSettingsActivity";

    @BindView(R.id.lvAccountSettings)
    ListView listView;
    @BindView(R.id.backArrow)
    ImageView backArrow;
    @BindView(R.id.profileToolBar)
    android.support.v7.widget.Toolbar toolbar;

    SectionsStatePagerAdapter pagerAdapter;

    @BindView(R.id.viewpager_container)
    ViewPager viewPager;

    @BindView(R.id.layout1)
    LinearLayout mLLinearLayout;

    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        ButterKnife.bind(this);
        mContext = AccountSettingsActivity.this;
        Log.d(TAG, "onCreate: started");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        setupSettingList();
        setUpFragments();
        //setup the backarrow
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to profile Activity");

            }
        });
        setUpFragments();
    }

    private void setViewPager (int fragmentNumber){
        Log.d(TAG, "setViewPager: navigating to fragment  #: "+fragmentNumber);
        mLLinearLayout.setVisibility(View.GONE);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(fragmentNumber);
    }
    private void setUpFragments() {
        pagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new EditProfileFragment(),getString(R.string.edit_profile)); // fragment 0
        pagerAdapter.addFragment(new SignOutFragment(),getString(R.string.sign_out));// fragment 1
    }

    private void setupSettingList() {
        Log.d(TAG, "setupSettingList: initializing 'Account Settings.' list");
        ArrayList<String> options = new ArrayList<>();
        options.add(getString(R.string.edit_profile));// fragment 0
        options.add(getString(R.string.sign_out));// fragment 1
        ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, options);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setViewPager(position);
            }
        });
    }
}
