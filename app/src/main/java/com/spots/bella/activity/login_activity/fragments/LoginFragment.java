package com.spots.bella.activity.login_activity.fragments;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.spots.bella.R;
import com.spots.bella.activity.OnKeyboardVisibilityListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class LoginFragment extends Fragment implements OnKeyboardVisibilityListener {

    private static final String TAG = "LoginFragment";
    private OnLoginFragmentInteractionListener mListener;
    private Unbinder unbinder;

    public LoginFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.et_login_fragment_password)
    TextInputEditText et_login_fragment_password;

    @BindView(R.id.et_login_fragment_email)
    TextInputEditText et_login_fragment_email;

    @BindView(R.id.login_activity_footer_layout)
    FrameLayout login_activity_footer_layout;

    @BindView(R.id.iv_logo_login_fragment)
    CircleImageView iv_logo_login_fragment;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentInteractionListener) {
            mListener = (OnLoginFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        unbinder = ButterKnife.bind(this, v);


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.onLoginFragmentOpened();
        setKeyboardVisibilityListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        setKeyboardVisibilityListener(null);
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

    @OnClick(R.id.btn_login_fragment_login)
    public void login(View view) {
        Log.d(TAG, "onClick: login");
        mListener.onLoginBtnClicked();
    }

    @OnClick(R.id.btn_login_fragment_register)
    public void register(View view) {
        Log.d(TAG, "onClick: login");
        mListener.onRegisterBtnClicked();
    }

    @OnClick(R.id.tv_forgot_password_Login_fragment)
    public void forgotPassword() {
        Log.d(TAG, "forgotPassword: ");
        mListener.onForgotPasswordBtnClicked();
    }

    public interface OnLoginFragmentInteractionListener {
        // TODO: Update argument type and name
        void onLoginFragmentOpened();
        void onRegisterBtnClicked();
        void onLoginBtnClicked();
        void onForgotPasswordBtnClicked();
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
//        Toast.makeText(LoginActivity.this, visible ? "Keyboard is active" : "Keyboard is Inactive", Toast.LENGTH_SHORT).show();
        if (visible) { // soft-keyboard is active
            hideFooter();
        } else // soft-keyboard is active
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showFooter();

                }
            }, 50);
        }
    }

    private void showFooter() {
        if (login_activity_footer_layout != null)
            login_activity_footer_layout.setVisibility(View.VISIBLE);
    }

    private void hideFooter() {
        if (login_activity_footer_layout != null)
            login_activity_footer_layout.setVisibility(View.GONE);
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
