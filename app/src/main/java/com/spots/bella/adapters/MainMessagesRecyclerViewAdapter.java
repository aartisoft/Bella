package com.spots.bella.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.spots.bella.R;

public class MainMessagesRecyclerViewAdapter extends RecyclerView.Adapter<MainMessagesRecyclerViewAdapter.MainMessagesRecyclerViewHolder> {

    Context context;

    public MainMessagesRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public static class MainMessagesRecyclerViewHolder extends RecyclerView.ViewHolder {
        private ImageView messages_state_view;

        public MainMessagesRecyclerViewHolder(View itemView) {
            super(itemView);
            messages_state_view = itemView.findViewById(R.id.message_state_indicator);
        }
    }

    @Override
    public MainMessagesRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_message_recycler_view_item, parent, false);
        return new MainMessagesRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MainMessagesRecyclerViewHolder holder, int position) {
        if (position == 2) {
            holder.messages_state_view.setImageResource(R.drawable.ic_message_state_unread);
        } if (position == 4) {
            holder.messages_state_view.setImageResource(R.drawable.ic_message_state_unread);
        }

    }

    @Override
    public int getItemCount() {
        return 5;
    }
}