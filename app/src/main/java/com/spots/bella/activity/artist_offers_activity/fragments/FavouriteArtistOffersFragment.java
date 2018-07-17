package com.spots.bella.activity.artist_offers_activity.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spots.bella.R;
import com.spots.bella.adapters.FavouriteArtistOffersAdapter;
import com.spots.bella.di.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavouriteArtistOffersFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "FavouriteArtistOffersFr";
    private OnFavouriteArtistFragmentInteractionListener mListener;
    @BindView(R.id.rv_favourite_artist_offers)
    RecyclerView rv_favourite_artist_offers;

    @BindView(R.id.root_fragment_favourite_artist_offers)
    SwipeRefreshLayout root;

    private Unbinder unbinder;

    public FavouriteArtistOffersFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFavouriteArtistFragmentInteractionListener) {
            mListener = (OnFavouriteArtistFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFavouriteArtistFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favourite_artist_offers, container, false);
        unbinder = ButterKnife.bind(this, v);
        initViews(v);
        return v;
    }

    private void initViews(View v) {
        root.setOnRefreshListener(FavouriteArtistOffersFragment.this);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        rv_favourite_artist_offers.setLayoutManager(layoutManager);
        rv_favourite_artist_offers.setHasFixedSize(true);
        FavouriteArtistOffersAdapter mAdapter = new FavouriteArtistOffersAdapter(getContext());
        rv_favourite_artist_offers.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.onFavouriteArtistFragmentOpened();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh: ");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                root.setRefreshing(false);
            }
        }, 2000);
    }

    public interface OnFavouriteArtistFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFavouriteArtistFragmentOpened();
    }
}
