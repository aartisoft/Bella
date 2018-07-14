package com.spots.bella.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.spots.bella.R;

public class MyReservationRecyclerViewAdapter extends RecyclerView.Adapter<MyReservationRecyclerViewAdapter.MyReservationRecyclerViewHolder> {
    Context context;

    public MyReservationRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public static class MyReservationRecyclerViewHolder extends RecyclerView.ViewHolder {

        public MyReservationRecyclerViewHolder(View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public MyReservationRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_reservation_recycler_view_item, parent, false);
        return new MyReservationRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyReservationRecyclerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
