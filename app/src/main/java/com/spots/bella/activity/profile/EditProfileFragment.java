package com.spots.bella.activity.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.spots.bella.R;
import com.spots.bella.di.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends BaseFragment {
    private static final String TAG = "EditProfileFragment";
    @BindView(R.id.profile_photo)
    CircleImageView profile_photo;
    @BindView(R.id.backArrow)
    ImageView back_arrow;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_editprofile, container, false);
        unbinder = ButterKnife.bind(this, v);
        setProfileImage();

        // back arrow
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to profile activity.");
                getActivity().finish();
            }
        });
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setProfileImage(){
        Glide.with(context).load(R.drawable.ic_profile).into(profile_photo);
    }
}
