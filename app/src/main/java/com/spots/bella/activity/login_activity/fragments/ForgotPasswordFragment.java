package com.spots.bella.activity.login_activity.fragments;


import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.spots.bella.R;
import com.spots.bella.constants.Common;
import com.spots.bella.constants.OnKeyboardVisibilityListener;
import com.spots.bella.constants.Utils;
import com.spots.bella.di.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.spots.bella.constants.Common.emailPattern;
import static com.spots.bella.constants.Common.showShortMessage;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFragment extends BaseFragment implements OnKeyboardVisibilityListener {

    private static final String TAG = "ForgotPasswordFragment";
    private OnForgotPasswordFragmentInteractionListener mListener;
    private Unbinder unbinder;
    @BindView(R.id.et_forgot_password_fragment_email)
    TextInputEditText et_email;

    @BindView(R.id.tv_aha_user_forgot_password_fragment)
    TextView tv_aha;

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
        setKeyboardVisibilityListener(this);
        mListener.onForgotPasswordFragmentOpened();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();
        setKeyboardVisibilityListener(null);
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
        Log.d(TAG, "next: ");

        isValidFields();

    }

    private void isValidFields() {
        String email = et_email.getText().toString().trim();
        if (email.isEmpty() || !emailPattern.matcher(email).matches()) {
            showShortMessage("Enter your email!", getActivity().findViewById(android.R.id.content));
            return;
        }
        if (Utils.isNetworkAvailable(context)) {
            mListener.onForgotPasswordNextClicked(email);
        } else {
            showShortMessage("No Internet Connection!", getActivity().findViewById(android.R.id.content));
        }
    }

    public interface OnForgotPasswordFragmentInteractionListener {
        void onForgotPasswordFragmentOpened();

        void onForgotPasswordAlreadyHaveAccountClicked();

        void onForgotPasswordNextClicked(String email);
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
//        Toast.makeText(LoginActivity.this, visible ? "Keyboard is active" : "Keyboard is Inactive", Toast.LENGTH_SHORT).show();
        if (visible) { // soft-keyboard is active
            if (tv_aha != null)
                tv_aha.setVisibility(View.GONE);
        } else // soft-keyboard is active
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (tv_aha != null)
                        tv_aha.setVisibility(View.VISIBLE);
                }
            }, 50);
        }
    }

    private void setKeyboardVisibilityListener(final OnKeyboardVisibilityListener onKeyboardVisibilityListener) {
        final View parentView = ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
        parentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            private boolean alreadyOpen;
            private final int defaultKeyboardHeightDP = 100;
            private final int EstimatedKeyboardDP = defaultKeyboardHeightDP + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);
            private final Rect rect = new Rect();

            @Override
            public void onGlobalLayout() {
                if (onKeyboardVisibilityListener != null) {
                    int estimatedKeyboardHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, parentView.getResources().getDisplayMetrics());
                    parentView.getWindowVisibleDisplayFrame(rect);
                    int heightDiff = parentView.getRootView().getHeight() - (rect.bottom - rect.top);
                    boolean isShown = heightDiff >= estimatedKeyboardHeight;

                    if (isShown == alreadyOpen) {
                        Log.i("Keyboard state", "Ignoring global layout change...");
                        return;
                    }
                    alreadyOpen = isShown;
                    onKeyboardVisibilityListener.onVisibilityChanged(isShown);
                }
            }
        });
    }

}
