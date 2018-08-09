package com.spots.bella.activity.profile;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.spots.bella.R;
import com.spots.bella.constants.Common;
import com.spots.bella.di.BaseFragment;
import com.spots.bella.models.BaseUser;
import com.spots.bella.models.Comment;
import com.spots.bella.models.Like;
import com.spots.bella.models.Photo;
import com.spots.bella.models.UserAccountSettings;
import com.spots.bella.utils.Heart;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


/**
 * Created by User on 8/12/2017.
 */

public class ViewPostFragment extends BaseFragment {

    private static final String TAG = "ViewPostFragment";


    public interface OnCommentThreadSelectedListener {
        void onCommentThreadSelectedListener(Photo photo);
    }

    OnCommentThreadSelectedListener mOnCommentThreadSelectedListener;

    public ViewPostFragment() {
        super();
        setArguments(new Bundle());
    }

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;


    //widgets
    private SquareImageView mPostImage;
    private TextView mBackLabel, mCaption, mUsername, mTimestamp, mLikes, mComments;
    private ImageView mBackArrow, mEllipses, mHeartRed, mHeartWhite, mProfileImage, mComment;


    //vars
    private Photo mPhoto;
    private int mActivityNumber = 0;
    private String photoUsername = "";
    private String profilePhotoUrl = "";
    private UserAccountSettings mUserAccountSettings;
    private GestureDetector mGestureDetector;
    private Heart mHeart;
    private Boolean mLikedByCurrentUser;
    private StringBuilder mUsers;
    private String mLikesString = "";
//    private BaseUser mCurrentUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_post, container, false);
        mPostImage = (SquareImageView) view.findViewById(R.id.post_image);
        mBackArrow = (ImageView) view.findViewById(R.id.backArrow);
        mBackLabel = (TextView) view.findViewById(R.id.tvBackLabel);
        mCaption = (TextView) view.findViewById(R.id.image_caption);
        mUsername = (TextView) view.findViewById(R.id.username);
        mTimestamp = (TextView) view.findViewById(R.id.image_time_posted);
        mEllipses = (ImageView) view.findViewById(R.id.ivEllipses);
        mHeartRed = (ImageView) view.findViewById(R.id.image_heart_red);
        mHeartWhite = (ImageView) view.findViewById(R.id.image_heart);
        mProfileImage = (ImageView) view.findViewById(R.id.profile_photo);
        mLikes = (TextView) view.findViewById(R.id.image_likes);
        mComment = (ImageView) view.findViewById(R.id.speech_bubble);
        mComments = (TextView) view.findViewById(R.id.image_comments_link);

        mHeart = new Heart(mHeartWhite, mHeartRed);
        mGestureDetector = new GestureDetector(getActivity(), new GestureListener());

        init();

        setupFirebaseAuth();


        return view;
    }

    private void init() {
        try {
            mPhoto = getPhotoFromBundle();
            Glide.with(context).load(getPhotoFromBundle().getImage_path()).into(mPostImage);
            getPhotoDetails();

            getLikesString();
//
//            String photo_id = getPhotoFromBundle().getPhoto_id();
//            Query query = mDatabase
//                    .child(Common.STRING_PHOTOS)
//                    .orderByChild(Common.STRING_PHOTO_ID)
//                    .equalTo(photo_id);
//            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
//                        Photo newPhoto = new Photo();
//                        Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();
//
//                        newPhoto.setCaption(objectMap.get(Common.STRING_CAPTION).toString());
//                        newPhoto.setTags(objectMap.get(Common.STRING_TAGS).toString());
//                        newPhoto.setPhoto_id(objectMap.get(Common.STRING_PHOTO_ID).toString());
//                        newPhoto.setUser_id(objectMap.get(Common.STRING_USER_ID).toString());
//                        newPhoto.setDate_created(objectMap.get(Common.STRING_DATE_CREATED).toString());
//                        newPhoto.setImage_path(objectMap.get(Common.STRING_IMAGE_PATH).toString());
//
//                        List<Comment> commentsList = new ArrayList<Comment>();
//                        for (DataSnapshot dSnapshot : singleSnapshot
//                                .child(Common.STRING_COMMENTS).getChildren()){
//                            Comment comment = new Comment();
//                            comment.setUser_id(dSnapshot.getValue(Comment.class).getUser_id());
//                            comment.setComment(dSnapshot.getValue(Comment.class).getComment());
//                            comment.setDate_created(dSnapshot.getValue(Comment.class).getDate_created());
//                            commentsList.add(comment);
//                        }
//                        newPhoto.setComments(commentsList);
//
//                        mPhoto = newPhoto;
//
////                        getCurrentUser();
//
//
//
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Log.d(TAG, "onCancelled: query cancelled.");
//                }
//            });

        } catch (NullPointerException e) {
            Log.e(TAG, "onCreateView: NullPointerException: " + e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (isAdded()) {
//            init();
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnCommentThreadSelectedListener = (OnCommentThreadSelectedListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
    }

    private void getLikesString() {
        Log.d(TAG, "getLikesString: getting likes string");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(Common.STRING_PHOTOS)
                .child(mPhoto.getPhoto_id())
                .child(Common.STRING_LIKES);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsers = new StringBuilder();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: singleSnapshot = " + mGson.toJson(singleSnapshot.getValue(Like.class)));
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    Query query = reference
                            .child(Common.USER_STRING)
                            .orderByChild(Common.STRING_USER_ID)
                            .equalTo(singleSnapshot.getValue(Like.class).getUser_id());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d(TAG, "onDataChange: dataSnapshot user = "+dataSnapshot);
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                Log.d(TAG, "onDataChange: found like: " +
                                        singleSnapshot.getValue(BaseUser.class).getUser_name());


                                mUsers.append(singleSnapshot.getValue(BaseUser.class).getUser_name());
                                mUsers.append(",");
                            }

                            String[] splitUsers = mUsers.toString().split(",");

                            if (mUsers.toString().contains(mUserAccountSettings.getUser_name() + ",")) {//mitch, mitchell.tabian
                                mLikedByCurrentUser = true;
                            } else {
                                mLikedByCurrentUser = false;
                            }

                            Log.d(TAG, "onDataChange: likes string: " + mLikesString);
                            Log.d(TAG, "onDataChange: splitUsers.length:" + splitUsers.length);
                            Log.d(TAG, "onDataChange: splitUsers:" + mGson.toJson(splitUsers));

                            int length = splitUsers.length;
                            if (length == 1) {
                                mLikesString = "Liked by " + splitUsers[0];
                            } else if (length == 2) {
                                mLikesString = "Liked by " + splitUsers[0]
                                        + " and " + splitUsers[1];
                            } else if (length == 3) {
                                mLikesString = "Liked by " + splitUsers[0]
                                        + ", " + splitUsers[1]
                                        + " and " + splitUsers[2];

                            } else if (length == 4) {
                                mLikesString = "Liked by " + splitUsers[0]
                                        + ", " + splitUsers[1]
                                        + ", " + splitUsers[2]
                                        + " and " + splitUsers[3];
                            } else if (length > 4) {
                                mLikesString = "Liked by " + splitUsers[0]
                                        + ", " + splitUsers[1]
                                        + ", " + splitUsers[2]
                                        + " and " + (splitUsers.length - 3) + " others";
                            }
                            Log.d(TAG, "onDataChange: likes string: " + mLikesString);
                            setupWidgets();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                if (!dataSnapshot.exists()) {
                    mLikesString = "";
                    mLikedByCurrentUser = false;
                    setupWidgets();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*private void getCurrentUser(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(Common.USER_STRING)
                .orderByChild(Common.STRING_USER_ID)
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    mCurrentUser = singleSnapshot.getValue(BaseUser.class);
                }
                getLikesString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });
    }*/

    public class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d(TAG, "onDoubleTap: double tap detected.");

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference
                    .child(Common.STRING_PHOTOS)
                    .child(mPhoto.getPhoto_id())
                    .child(Common.STRING_LIKES);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                        String keyID = singleSnapshot.getKey();

                        //TODO: case1: Then user already liked the photo
                        if (mLikedByCurrentUser &&
                                singleSnapshot.getValue(Like.class).getUser_id()
                                        .equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                            myRef.child(Common.STRING_PHOTOS)
                                    .child(mPhoto.getPhoto_id())
                                    .child(Common.STRING_LIKES)
                                    .child(keyID)
                                    .removeValue();
///
                            myRef.child(Common.USER_PHOTOS_STRING)
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child(mPhoto.getPhoto_id())
                                    .child(Common.STRING_LIKES)
                                    .child(keyID)
                                    .removeValue();

                            mHeart.toggleLike();
                            getLikesString();
                        }
                        //case2: The user has not liked the photo
                        else if (!mLikedByCurrentUser) {
                            //add new like
                            addNewLike();
                            break;
                        }
                    }
                    if (!dataSnapshot.exists()) {
                        //add new like
                        addNewLike();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return true;
        }
    }

    private void addNewLike() {
        Log.d(TAG, "addNewLike: adding new like");

        String newLikeID = myRef.push().getKey();
        Like like = new Like();
        like.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());

        myRef.child(Common.STRING_PHOTOS)
                .child(mPhoto.getPhoto_id())
                .child(Common.STRING_LIKES)
                .child(newLikeID)
                .setValue(like);

        myRef.child(Common.USER_PHOTOS_STRING)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(mPhoto.getPhoto_id())
                .child(Common.STRING_LIKES)
                .child(newLikeID)
                .setValue(like);

        mHeart.toggleLike();
        getLikesString();
    }

    private void getPhotoDetails() {
        Log.d(TAG, "getPhotoDetails: retrieving photo details.");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(Common.USER_ACCOUNT_SETTINGS)
                .orderByChild(Common.STRING_USER_ID)
                .equalTo(mPhoto.getUser_id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    mUserAccountSettings = singleSnapshot.getValue(UserAccountSettings.class);
                }
                //setupWidgets();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });
    }


    private void setupWidgets() {
        String timestampDiff = getTimestampDifference();
        if (!timestampDiff.equals("0")) {
            mTimestamp.setText(timestampDiff + " DAYS AGO");
        } else {
            mTimestamp.setText("TODAY");
        }
        Glide.with(context).load(mUserAccountSettings.getProfile_photo()).into(mProfileImage);
        mUsername.setText(mUserAccountSettings.getUser_name());
        mLikes.setText(mLikesString);
        mCaption.setText(mPhoto.getCaption());

//
//        if (mPhoto.getComments().size() > 0) {
//            mComments.setText("View all " + mPhoto.getComments().size() + " comments");
//        } else {
//            mComments.setText("");
//        }
//
//        mComments.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: navigating to comments thread");
//
//                mOnCommentThreadSelectedListener.onCommentThreadSelectedListener(mPhoto);
//
//            }
//        });
//
        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back");
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
//
        mComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back");
                mOnCommentThreadSelectedListener.onCommentThreadSelectedListener(mPhoto);

            }
        });

        if (mLikedByCurrentUser) {
            mHeartWhite.setVisibility(View.GONE);
            mHeartRed.setVisibility(View.VISIBLE);
            mHeartRed.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.d(TAG, "onTouch: red heart touch detected.");
                    return mGestureDetector.onTouchEvent(event);
                }
            });

        } else {
            mHeartWhite.setVisibility(View.VISIBLE);
            mHeartRed.setVisibility(View.GONE);
            mHeartWhite.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.d(TAG, "onTouch: white heart touch detected.");
                    return mGestureDetector.onTouchEvent(event);
                }
            });
        }

    }

    /**
     * Returns a string representing the number of days ago the post was made
     *
     * @return
     */
    private String getTimestampDifference() {
        Log.d(TAG, "getTimestampDifference: getting timestamp difference.");

        String difference = "";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA);
        sdf.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));//google 'android list of timezones'
        Date today = c.getTime();
        sdf.format(today);
        Date timestamp;
        final String photoTimestamp = mPhoto.getDate_created();
        try {
            timestamp = sdf.parse(photoTimestamp);
            difference = String.valueOf(Math.round(((today.getTime() - timestamp.getTime()) / 1000 / 60 / 60 / 24)));
        } catch (ParseException e) {
            Log.e(TAG, "getTimestampDifference: ParseException: " + e.getMessage());
            difference = "0";
        }
        return difference;
    }

    /**
     * retrieve the activity number from the incoming bundle from profileActivity interface
     *
     * @return
     */
    private int getActivityNumFromBundle() {
        Log.d(TAG, "getActivityNumFromBundle: arguments: " + getArguments());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            return bundle.getInt(getString(R.string.activity_number));
        } else {
            return 0;
        }
    }

    /**
     * retrieve the photo from the incoming bundle from profileActivity interface
     *
     * @return
     */
    private Photo getPhotoFromBundle() {
        Log.d(TAG, "getPhotoFromBundle: arguments: " + getArguments());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            return bundle.getParcelable(getString(R.string.photo));
        } else {
            return null;
        }
    }


       /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}



















