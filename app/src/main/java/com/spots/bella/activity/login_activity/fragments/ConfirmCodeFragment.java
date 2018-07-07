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
public class ConfirmCodeFragment extends Fragment {

    private static final String TAG = "ConfirmCodeFragment";
    private static String EMAIL;
    private OnConfirmCodeFragmentInteractionListener mListener;
    private Unbinder unbinder;

    public ConfirmCodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnConfirmCodeFragmentInteractionListener) {
            mListener = (OnConfirmCodeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnConfirmCodeFragmentInteractionListener");
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
        View v = inflater.inflate(R.layout.fragment_confirm_code, container, false);
        unbinder = ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.onConfirmCodeOpened();
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
    public void resend() {
        mListener.onResendCodeClicked(EMAIL);
    }
    @OnClick(R.id.btn_confirm_code_fragment_next)
    public void next() {
        mListener.onConfirmCodeNextClicked("123xyz",EMAIL);
    }

    public interface OnConfirmCodeFragmentInteractionListener {
        void onConfirmCodeOpened();

        void onResendCodeClicked(String email);

        void onConfirmCodeNextClicked(String code, String email);
    }
}
