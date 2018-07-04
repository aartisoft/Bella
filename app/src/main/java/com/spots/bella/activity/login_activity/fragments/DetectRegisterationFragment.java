package com.spots.bella.activity.login_activity.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.spots.bella.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.spots.bella.constants.Common.ARTIST;
import static com.spots.bella.constants.Common.NORMAL_USER;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetectRegisterationFragment extends Fragment {


    private static final String TAG = DetectRegisterationFragment.class.getSimpleName();
    private OnDetectRegisterationFragmentInteractionListener mListener;
    private Unbinder unbinder;

    public DetectRegisterationFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.btn_detect_registeration_fragment_user)
    Button btn_detect_registeration_fragment_user;
    @BindView(R.id.btn_detect_registeration_fragment_makeup_atrist)
    Button btn_detect_registeration_fragment_makeup_atrist;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetectRegisterationFragmentInteractionListener) {
            mListener = (OnDetectRegisterationFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAboutFragmentInteractionListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detect_registeration, container, false);
        unbinder = ButterKnife.bind(this, v);

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        mListener.onDetectRegisterationFragmentOpened();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.btn_detect_registeration_fragment_user)
    public void user(View view) {
        Log.d(TAG, "onClick: normal user");
        mListener.onRegisterTypeBtnClicked(NORMAL_USER);
    }

    @OnClick(R.id.btn_detect_registeration_fragment_makeup_atrist)
    public void artist(View view) {
        Log.d(TAG, "onClick: artist");
        mListener.onRegisterTypeBtnClicked(ARTIST);
    }


    public interface OnDetectRegisterationFragmentInteractionListener {
        // TODO: Update argument type and name
        void onDetectRegisterationFragmentOpened();
        void onRegisterTypeBtnClicked(int type);
    }
}
