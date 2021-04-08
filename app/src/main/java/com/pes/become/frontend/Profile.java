package com.pes.become.frontend;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.pes.become.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Profile extends Fragment {

    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    /**
     * Pestanyes del TabLayout
     */
    private ArrayList<Fragment> tabs;

    /**
     * Constructora del Profile
     */
    public Profile() {}

    /**
     * Funcio del Profile que s'executa al crear-la
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile, container, false);
        super.onCreate(savedInstanceState);

        tabs = new ArrayList<>();
        tabs.add(new RoutinesList());
        tabs.add(new Stats());

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new MainAdapter(getFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.bookmark_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.stats_icon);

        return view;
    }

    /**
     * Classe que controla els canvis de pestanya
     */
    private class MainAdapter extends FragmentPagerAdapter {

        public MainAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return tabs.get(position);
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

    }
}