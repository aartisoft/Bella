package com.spots.bella.activity.login_activity.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spots.bella.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends Fragment {

    private static final String TAG = "ResetPasswordFragment";
    private static String EMAIL;
    private OnResetPasswordFragmentInteractionListener mListener;
    private Unbinder unbinder;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnResetPasswordFragmentInteractionListener) {
            mListener = (OnResetPasswordFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnResetPasswordFragmentInteractionListener");
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b == null) {
            getActivity().onBackPressed();
        } else
            EMAIL = b.getString("email");
        Log.d(TAG, "onCreate: EMAIL = " + EMAIL);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reset_password, container, false);
        unbinder = ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.onResetPasswordFragmentOpened();
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

    @OnClick(R.id.btn_reset_password_fragment_next)
    public void save() {
        mListener.onSaveNewPasswordClicked();
    }

    public interface OnResetPasswordFragmentInteractionListener {
        void onResetPasswordFragmentOpened();

        void onSaveNewPasswordClicked();
    }
}
