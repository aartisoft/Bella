package com.spots.bella.activity.my_reservation_activity.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spots.bella.R;


public class MainReservationsFragment extends Fragment {

    private OnFragmentMainReservationsInteractionListener mListener;

    public MainReservationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_reservations, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentMainReservationsInteractionListener) {
            mListener = (OnFragmentMainReservationsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onFragmentMainReservationFragmentOpened");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListener != null) {
            mListener.onFragmentMainReservationFragmentOpened();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentMainReservationsInteractionListener {
        // TODO: Update argument type and name
        void onFragmentMainReservationFragmentOpened();
    }
}
