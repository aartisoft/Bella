package com.spots.bella.activity.login_activity.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shawnlin.numberpicker.NumberPicker;
import com.spots.bella.R;
import com.spots.bella.constants.Common;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistQuestionsFragment extends Fragment {


    private static final String TAG = ArtistQuestionsFragment.class.getSimpleName();
    private static String UID;
    private Unbinder unbinder;
    private OnArtistQuestionsFragmentInteractionListener mListener;
    private String city = null;
    private int orders_no;

    public ArtistQuestionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnArtistQuestionsFragmentInteractionListener) {
            mListener = (OnArtistQuestionsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAboutFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        assert b != null;
        UID = b.getString("uid");
    }

    @BindView(R.id.number_picker)
    NumberPicker numberPicker;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_artist_questions, container, false);

        unbinder = ButterKnife.bind(this, v);
//// Set divider color
//        numberPicker.setDividerColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
//        numberPicker.setDividerColorResource(R.color.colorPrimary);
//
//// Set formatter
//        numberPicker.setFormatter(getString(R.string.number_picker_formatter));
//        numberPicker.setFormatter(R.string.number_picker_formatter);
//
//// Set selected text color
//        numberPicker.setSelectedTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
//        numberPicker.setSelectedTextColorResource(R.color.colorPrimary);
//
//// Set selected text size
//        numberPicker.setSelectedTextSize(getResources().getDimension(R.dimen.selected_text_size));
//        numberPicker.setSelectedTextSize(R.dimen.selected_text_size);
//
//// Set text color
//        numberPicker.setTextColor(ContextCompat.getColor(this, R.color.dark_grey));
//        numberPicker.setTextColorResource(R.color.dark_grey);
//
//// Set text size
//        numberPicker.setTextSize(getResources().getDimension(R.dimen.text_size));
//        numberPicker.setTextSize(R.dimen.text_size);
//
//// Set typeface
//        numberPicker.setTypeface(Typeface.create(getString(R.string.roboto_light), Typeface.NORMAL));
//        numberPicker.setTypeface(getString(R.string.roboto_light), Typeface.NORMAL);
//        numberPicker.setTypeface(getString(R.string.roboto_light));
//        numberPicker.setTypeface(R.string.roboto_light, Typeface.NORMAL);
//        numberPicker.setTypeface(R.string.roboto_light);
//
//// Set value
//        numberPicker.setMaxValue(59);
//        numberPicker.setMinValue(0);
//        numberPicker.setValue(3);
//
//// Using string values
//// IMPORTANT! setMinValue to 1 and call setDisplayedValues after setMinValue and setMaxValue
//        String[] data = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
//        numberPicker.setMinValue(1);
//        numberPicker.setMaxValue(data.length);
//        numberPicker.setDisplayedValues(data);
//        numberPicker.setValue(7);
//
//// Set fading edge enabled
//        numberPicker.setFadingEdgeEnabled(true);
//
//// Set scroller enabled
//        numberPicker.setScrollerEnabled(true);
//
//// Set wrap selector wheel
//        numberPicker.setWrapSelectorWheel(true);
//
//// OnClickListener
//        numberPicker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "Click on current value");
//            }
//        });
//
// OnValueChangeListener

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.d(TAG, String.format(Locale.US, "oldVal: %d, newVal: %d", oldVal, newVal));
                orders_no = newVal;
            }
        });
        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        mListener.onArtistQuestionsFragmentOpened();
    }

    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnArtistQuestionsFragmentInteractionListener {
        // TODO: Update argument type and name
        void onArtistQuestionsFragmentOpened();

        void onArtistQuestionsFragmentBtnClickFinish(String UID, int i, String city);
    }

    @OnClick(R.id.city_artist_questions_fragment_btn)
    public void selectGovernoment(View view) {
        CreateAlertDialogWithRadioButtonGroup(view);
    }

    @OnClick(R.id.finish_artist_questions_fragment_btn)
    public void finish() {
        if (city == null) {
            Common.showShortMessage("Choose your city", getActivity().findViewById(android.R.id.content));
        } else
            mListener.onArtistQuestionsFragmentBtnClickFinish(UID, orders_no, city);
    }

    private void CreateAlertDialogWithRadioButtonGroup(final View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MaterialThemeDialog);
        final CharSequence[] values = getActivity().getResources().getStringArray(R.array.governoments);

        builder.setTitle("Government");
        builder.setSingleChoiceItems(values, 0, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                Log.d(TAG, "onClick: ");
                city = (String) values[item];
                ((Button) view).setText(city);
                dialog.dismiss();
            }
        });
        AlertDialog dialog_genre = builder.create();
        dialog_genre.show();
    }
}
