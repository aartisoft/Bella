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
public class AccountValidationFragment extends Fragment {

    private static final String TAG = "AccountValidationFragme";
    private OnEmailVerificationFragmentInteractionListener mListener;
    private Unbinder unbinder;

    public AccountValidationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEmailVerificationFragmentInteractionListener) {
            mListener = (OnEmailVerificationFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEmailVerificationFragmentInteractionListener");
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.acccount_validation_fragment, container, false);
        unbinder = ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.onEmailVerificationFragmentOpened();
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

    @OnClick(R.id.btn_resend_code_fragment_confirm_code)
    public void send_verification() {
        mListener.onVerificationClicked();
    }
    
    public interface OnEmailVerificationFragmentInteractionListener {
        void onEmailVerificationFragmentOpened();

        void onVerificationClicked();
        }
}
