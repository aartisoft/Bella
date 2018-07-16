package com.spots.bella.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by EhabSalah on 7/30/2017.
 */

public class ArtistOffersViewPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();

    public void addFrament(Fragment fragment,String title)
    {
        this.fragments.add(fragment);
        this.titles.add(title);
    }

    public ArtistOffersViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
