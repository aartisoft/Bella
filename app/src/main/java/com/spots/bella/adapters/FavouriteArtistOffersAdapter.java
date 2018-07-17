package com.spots.bella.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spots.bella.R;

public class FavouriteArtistOffersAdapter extends RecyclerView.Adapter {
    private final Context context;

    public FavouriteArtistOffersAdapter(Context context) {
        this.context = context;
    }

    public static class FavouriteArtistOffersViewHolder extends RecyclerView.ViewHolder {

        public FavouriteArtistOffersViewHolder(View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favoutite_artists_offers_recycler_view_item, parent, false);

        return new FavouriteArtistOffersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
