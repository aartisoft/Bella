package com.spots.bella.activity.messages_activity.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spots.bella.R;
import com.spots.bella.adapters.MainMessagesRecyclerViewAdapter;
import com.spots.bella.adapters.MainRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MainMessagesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MainMessagesFragment";
    private OnMainMessagesFragmentInteractionListener mListener;
    @BindView(R.id.rv_main_messages)
    RecyclerView rv_main_messages;

    @BindView(R.id.root_main_messages_fragment)
    SwipeRefreshLayout root_layout;

    private Unbinder unBinder;

    public MainMessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMainMessagesFragmentInteractionListener) {
            mListener = (OnMainMessagesFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMainMessagesFragmentInteractionListener");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main_messages, container, false);
        unBinder = ButterKnife.bind(this, v);
        initViews();
        return v;
    }

    private void initViews() {
        root_layout.setOnRefreshListener(MainMessagesFragment.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_main_messages.setLayoutManager(layoutManager);
        rv_main_messages.setHasFixedSize(true);
        MainMessagesRecyclerViewAdapter mAdapter = new MainMessagesRecyclerViewAdapter(getContext());
        rv_main_messages.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.onMainMessagesFragmentOpened();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unBinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh: ");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                root_layout.setRefreshing(false);
            }
        },1000);
    }

    public interface OnMainMessagesFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMainMessagesFragmentOpened();
    }
}
