package com.spots.bella.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spots.bella.R;
import com.spots.bella.activity.main_activity.MPresenter;
import com.spots.bella.activity.main_activity.MainActivity;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.MainRecyclerViewHolder> {

    MPresenter mPresenter;
    Context context;
    public MainRecyclerViewAdapter(Context context, MPresenter mMPresenter) {
        this.context = context;
        this.mPresenter = mMPresenter;
    }

    public static class MainRecyclerViewHolder extends RecyclerView.ViewHolder {

        public MainRecyclerViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public MainRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler_view_item, parent, false);
        return new MainRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MainRecyclerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
