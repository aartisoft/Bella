package com.spots.bella.models;

public class MoreDetailsUserArtist {
    private String city;
    private String max_orders;

    public MoreDetailsUserArtist() {
    }

    public MoreDetailsUserArtist(String city, String max_orders) {
        this.city = city;
        this.max_orders = max_orders;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMax_orders() {
        return max_orders;
    }

    public void setMax_orders(String max_orders) {
        this.max_orders = max_orders;
    }
}
