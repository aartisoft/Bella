package com.spots.bella.activity.artist_offers_activity.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spots.bella.R;
import com.spots.bella.di.BaseFragment;

public class LatestArtistOffersFragment extends BaseFragment {

    private OnLatestArtistOffersFragmentInteractionListener mListener;

    public LatestArtistOffersFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLatestArtistOffersFragmentInteractionListener) {
            mListener = (OnLatestArtistOffersFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLatestArtistOffersFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_latest_artist_offers, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.onLatestArtistOffersFragmentOpened();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnLatestArtistOffersFragmentInteractionListener {
        // TODO: Update argument type and name
        void onLatestArtistOffersFragmentOpened();
    }
}
