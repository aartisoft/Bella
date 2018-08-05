package com.spots.bella.activity.profile;

import android.content.Intent;
import android.graphics.PointF;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.spots.bella.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
//    private static final int NUM_GRID_COLUMNS = 3;
//    @BindView(R.id.profileToolBar)
//    Toolbar toolbar;
//    @BindView(R.id.profileMenu)
//    ImageView profileMenu;
//
//    @BindView(R.id.profileProgressBar)
//    ProgressBar progressBar;
//
//    @BindView(R.id.profile_photo)
//    CircleImageView profile_photo;
//
//    @BindView(R.id.profileGride)
//    GridView gide_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//        ButterKnife.bind(this);

//
//        setupToolbar();
//
//        setupActivityWidgets();
//
//        setProfileImage();
//        tempGridSetup();
        init();
    }

    //
//    private void tempGridSetup(){
//        ArrayList<String> imgURLs = new ArrayList<>();
//        imgURLs.add("https://pbs.twimg.com/profile_images/616076655547682816/6gMRtQyY.jpg");
//        imgURLs.add("https://i.redd.it/9bf67ygj710z.jpg");
//        imgURLs.add("https://c1.staticflickr.com/5/4276/34102458063_7be616b993_o.jpg");
//        imgURLs.add("http://i.imgur.com/EwZRpvQ.jpg");
//        imgURLs.add("http://i.imgur.com/JTb2pXP.jpg");
//        imgURLs.add("https://i.redd.it/59kjlxxf720z.jpg");
//        imgURLs.add("https://i.redd.it/pwduhknig00z.jpg");
//        imgURLs.add("https://i.redd.it/clusqsm4oxzy.jpg");
//        imgURLs.add("https://i.redd.it/svqvn7xs420z.jpg");
//        imgURLs.add("http://i.imgur.com/j4AfH6P.jpg");
//        imgURLs.add("https://i.redd.it/89cjkojkl10z.jpg");
//        imgURLs.add("https://i.redd.it/aw7pv8jq4zzy.jpg");
//
//        setupImageGrid(imgURLs);
//    }
//
//    private void setupImageGrid(ArrayList<String> imgURLs){
//
//        int gridWidth = getResources().getDisplayMetrics().widthPixels;
//        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
//        gide_view.setColumnWidth(imageWidth);
//
//        GridImageAdapter adapter = new GridImageAdapter(this, R.layout.layout_grid_imageview, "", imgURLs);
//        gide_view.setAdapter(adapter);
//    }
//
//    private void setProfileImage() {
//        Glide.with(this).load(R.drawable.ic_profile).into(profile_photo);
//    }
//
//    private void setupActivityWidgets() {
//        Log.d(TAG, "setupActivityWidgets: setting profile photo.");
//        progressBar.setVisibility(View.GONE);
//
//    }
//

    private void init() {
        Log.d(TAG, "init: inflating " + getString(R.string.profile));

        ProfileFragment profileFragment = new ProfileFragment();
        FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, profileFragment);
        transaction.addToBackStack(getString(R.string.profile));
        transaction.commit();
    }


}
