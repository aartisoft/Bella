package com.spots.bella.activity.login_activity.fragments;


import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
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
import de.hdodenhof.circleimageview.CircleImageView;

import static com.spots.bella.constants.Common.capitalizeFristLetter;
import static com.spots.bella.constants.Common.emailPattern;
import static com.spots.bella.constants.Common.isWordContainsDigit;
import static com.spots.bella.constants.Common.phonePattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterationFragment extends BaseFragment implements OnKeyboardVisibilityListener {

    private static final String TAG = RegisterationFragment.class.getSimpleName();
    private Unbinder unbinder;

    @BindView(R.id.tv_aha_user_registeration_fragment)
    TextView tv_aha_user_registeration_fragment;

    @BindView(R.id.iv_user_register_fragment_logo)
    CircleImageView iv_user_register_fragment_logo;

    @BindView(R.id.et_full_name_register_fragment)
    TextInputEditText et_full_name;

    /*@BindView(R.id.et_last_name_register_fragment)
    TextInputEditText et_last_name;
*/
    @BindView(R.id.et_email_register_fragment)
    TextInputEditText et_email;

    @BindView(R.id.et_phone_register_fragment)
    TextInputEditText et_phone;

    @BindView(R.id.et_password_register_fragment)
    TextInputEditText et_password;

    @BindView(R.id.et_password_confirmation_register_fragment)
    TextInputEditText et_password_confirmation;


    @BindView(R.id.et_terms_checkbox_register_fragment)
    CheckBox terms_checkbox;


    private static int USER_TYPE;
    private OnRegisterFragmentInteractionListener mListener;
    private boolean fieldsValid;

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
            USER_TYPE = b.getInt("type");
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
        isFieldsValid();
    }


    public void isFieldsValid() {

        String full_name, phone, email, password, password_confirmation;
        full_name = et_full_name.getText().toString().trim();
//        last_name = et_last_name.getText().toString().trim();
        phone = et_phone.getText().toString().trim();
        email = et_email.getText().toString().trim();
        password = et_password.getText().toString();
        password_confirmation = et_password_confirmation.getText().toString();

        if (isWordContainsDigit(full_name) || TextUtils.isEmpty(full_name) || full_name.length() < 2) {
            Common.showShortMessage("Invalid Name!", getActivity().findViewById(android.R.id.content));
            Log.d(TAG, "isFieldsValid: first_name problem");
            return;
        }
//        if (isWordContainsDigit(last_name)|| TextUtils.isEmpty(last_name)|| last_name.length()<2) {
//            Common.showShortMessage("Invalid Last Name!", getActivity().findViewById(android.R.id.content));
//            Log.d(TAG, "isFieldsValid: first_name problem");
//            return;
//        }
        if (!phonePattern.matcher(phone).matches()) {
            Common.showShortMessage("Invalid Phone Number!", getActivity().findViewById(android.R.id.content));
            Log.d(TAG, "isFieldsValid: Phone not match");
            return;
        }
        //check email validation
        if (!emailPattern.matcher(email).matches()) {
            Common.showShortMessage("Invalid Email!", getActivity().findViewById(android.R.id.content));
            Log.d(TAG, "isFieldsValid: email not match");
            return;
        }
        //check password validation
        if (password.length() < 6) {
            Common.showShortMessage("Short Password!", getActivity().findViewById(android.R.id.content));
            Log.d(TAG, "isFieldsValid: password is short");
            return;
        }
        if (!(password).equals(password_confirmation)) {
            Common.showShortMessage("Passwords Not Match!", getActivity().findViewById(android.R.id.content));
            Log.d(TAG, "isFieldsValid: passwords not match");
            return;
        }
        if (!terms_checkbox.isChecked()) {
            Common.showShortMessage("You must accept our terms!", getActivity().findViewById(android.R.id.content));
            Log.d(TAG, "isFieldsValid: terms check box is not checked");
            return;
        }
        if (Utils.isNetworkAvailable(context)) {
            mListener.onRegisterNextBtnClicked(capitalizeFristLetter(full_name), email, phone, password, USER_TYPE); // data true => Register
        } else
            Common.showShortMessage("No internet connection!", getActivity().findViewById(android.R.id.content));


    }


    public interface OnRegisterFragmentInteractionListener {
        // TODO: Update argument type and name
        void onRegisterFragmentOpened();

        void onRegisterNextBtnClicked(String first_name, String email, String phone, String password, int TYPE);
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
