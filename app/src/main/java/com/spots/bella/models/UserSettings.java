package com.spots.bella.models;

/**
 * Created by User on 6/30/2017.
 */

public class UserSettings {

    private BaseUser user;
    private UserAccountSettings settings;

    public UserSettings(BaseUser user, UserAccountSettings settings) {
        this.user = user;
        this.settings = settings;
    }

    public UserSettings() {

    }


    public BaseUser getUser() {
        return user;
    }

    public void setUser(BaseUser user) {
        this.user = user;
    }

    public UserAccountSettings getSettings() {
        return settings;
    }

    public void setSettings(UserAccountSettings settings) {
        this.settings = settings;
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "user=" + user +
                ", settings=" + settings +
                '}';
    }
}
