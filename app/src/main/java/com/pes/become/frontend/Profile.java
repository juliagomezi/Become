package com.pes.become.frontend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;
import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class Profile extends Fragment {

    private DomainAdapter DA;

    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CircleImageView profilePic;
    private ImageButton settingsButton;

    private Uri imageUri;

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

        DA = DomainAdapter.getInstance();

        tabs = new ArrayList<>();
        tabs.add(new RoutinesList());
        tabs.add(new Stats());

        profilePic = view.findViewById(R.id.profilePic);
        settingsButton = view.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> changeProfilePic());
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new MainAdapter(getChildFragmentManager()));
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

    private void changeProfilePic() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null) {
            imageUri = data.getData();
            profilePic.setImageURI(imageUri);
            DA.updateProfilePic(imageUri);
        }
    }
}