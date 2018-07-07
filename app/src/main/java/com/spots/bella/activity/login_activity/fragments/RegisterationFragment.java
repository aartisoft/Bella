package com.spots.bella.activity.login_activity.fragments;


import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.spots.bella.R;
import com.spots.bella.activity.OnKeyboardVisibilityListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.spots.bella.constants.Common.NORMAL_USER;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterationFragment extends Fragment implements OnKeyboardVisibilityListener {

    private static final String TAG = RegisterationFragment.class.getSimpleName();
    private Unbinder unbinder;
    @BindView(R.id.tv_aha_user_registeration_fragment)
    TextView tv_aha_user_registeration_fragment;
    @BindView(R.id.iv_user_register_fragment_logo)
    CircleImageView iv_user_register_fragment_logo;
    private static int TYPE;
    private OnRegisterFragmentInteractionListener mListener;

    public RegisterationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterFragmentInteractionListener) {
            mListener = (OnRegisterFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b == null) {
            getActivity().onBackPressed();
        } else
            TYPE = b.getInt("type");
//        Log.d(TAG, "onCreate: TYPE = "+(TYPE==NORMAL_USER?"NORMAL USER":"ARTIST"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_registeration, container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.onRegisterFragmentOpened();
        setKeyboardVisibilityListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setKeyboardVisibilityListener(null);

        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.btn_register_fragment_next)
    public void next() {
        Log.d(TAG, "onClick: next");
        mListener.onRegisterNextBtnClicked(TYPE);
    }

    public interface OnRegisterFragmentInteractionListener {
        // TODO: Update argument type and name
        void onRegisterFragmentOpened();

        void onRegisterNextBtnClicked(int TYPE);
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

    @Override
    public void onVisibilityChanged(boolean visible) {
        if (visible) { // soft-keyboard is active
            if (tv_aha_user_registeration_fragment != null)
                tv_aha_user_registeration_fragment.setVisibility(View.GONE);
            if (iv_user_register_fragment_logo != null)
                iv_user_register_fragment_logo.setVisibility(View.GONE);
        } else // soft-keyboard is in-active
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (tv_aha_user_registeration_fragment != null)
                        tv_aha_user_registeration_fragment.setVisibility(View.VISIBLE);
                    if (iv_user_register_fragment_logo != null)

                        iv_user_register_fragment_logo.setVisibility(View.VISIBLE);

                }
            }, 50);
        }
    }
}
