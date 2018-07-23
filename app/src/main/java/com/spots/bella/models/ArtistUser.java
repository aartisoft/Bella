package com.spots.bella.models;

public class ArtistUser extends BaseUser {

    private MoreDetailsUserArtist more_details;

    public ArtistUser() {
    }

    public MoreDetailsUserArtist getMore_details() {
        return more_details;
    }

    public void setMore_details(MoreDetailsUserArtist more_details) {
        this.more_details = more_details;
    }
}
