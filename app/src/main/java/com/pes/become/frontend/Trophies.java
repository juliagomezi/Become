package com.pes.become.frontend;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Trophies extends Fragment {

    private static Trophies instance;

    private View view;
    private Context global;

    private final DomainAdapter DA = DomainAdapter.getInstance();

    public Trophies() {}

    /**
     * Funcio del Trophies que s'executa al crear-la
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.trophies, container, false);
        super.onCreate(savedInstanceState);
        instance = this;
        global = this.getActivity();



        return view;
    }
}