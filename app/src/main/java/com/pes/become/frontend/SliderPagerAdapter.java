package com.pes.become.frontend;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class SliderPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> fragments;

    public SliderPagerAdapter(FragmentManager fm, ArrayList<Fragment> frags) {
        super(fm);
        this.fragments = frags;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
