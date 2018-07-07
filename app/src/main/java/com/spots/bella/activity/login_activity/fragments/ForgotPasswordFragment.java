package com.spots.bella.activity.login_activity.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.spots.bella.R;
import com.spots.bella.constants.Common;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFragment extends Fragment {


    private OnForgotPasswordFragmentInteractionListener mListener;
    private Unbinder unbinder;
    @BindView(R.id.et_forgot_password_fragment_email)
    TextInputEditText et_forgot_password_fragment_email;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnForgotPasswordFragmentInteractionListener) {
            mListener = (OnForgotPasswordFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnForgotPasswordFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        unbinder = ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.onForgotPasswordFragmentOpened();
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

    @OnClick(R.id.tv_aha_user_forgot_password_fragment)
    public void aha() {
        mListener.onForgotPasswordAlreadyHaveAccountClicked();
    }

    @OnClick(R.id.btn_forgot_password_fragment_next)
    public void next() {
       /* String email = et_forgot_password_fragment_email.getText().toString().trim();
        if (!TextUtils.isEmpty(email)) {
            if (Common.isValidEmail(email)) {
                //valid email
            } else {
                // not valid
            }

        }
        else
            {
            Toast.makeText(getActivity(), "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }*/
        mListener.onForgotPasswordNextClicked("xyx@gmail.com");
    }


    public interface OnForgotPasswordFragmentInteractionListener {
        void onForgotPasswordFragmentOpened();

        void onForgotPasswordAlreadyHaveAccountClicked();

        void onForgotPasswordNextClicked(String email);
    }
}
