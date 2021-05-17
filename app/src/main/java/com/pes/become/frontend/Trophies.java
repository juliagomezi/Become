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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Trophies extends Fragment {

    private static Trophies instance;

    private View view;
    private Context global;

    private final DomainAdapter DA = DomainAdapter.getInstance();

    // don trec la llista de trofeus?
    ArrayList<ArrayList<String>> trophiesList;
    ArrayList<Boolean> obtainedTrophiesList;
    public void set() {
        trophiesList = new ArrayList<>();
        ArrayList<String> trophy = new ArrayList<>();
        trophy.add("Create your first routine");
        trophy.add("S'obté al crear la teva 1a rutina");
        trophiesList.add(trophy);
        trophy = new ArrayList<>();
        trophy.add("Dedicate 5 hours of your week to musical activities");
        trophy.add("S'obté al crear la teva 2a rutina");
        trophiesList.add(trophy);
        trophy = new ArrayList<>();
        trophy.add("Dedicate 5 hours of your week to sport");
        trophy.add("S'obté al obtenir una ratxa de 7 dies");
        trophiesList.add(trophy);
        trophy = new ArrayList<>();
        trophy.add("Sleep for 5 hours");
        trophy.add("S'obté al obtenir una ratxa de 7 dies");
        trophiesList.add(trophy);
        trophy = new ArrayList<>();
        trophy.add("Dedicate 5 hours to cooking");
        trophy.add("S'obté al obtenir una ratxa de 30 diesaaaaaaaaaaaaaaaaaaaaaa");
        trophiesList.add(trophy);
        trophy = new ArrayList<>();
        trophy.add("Work for 5 hours");
        trophy.add("S'obté al obtenir una ratxa de 30 diesaaaaaaaaaaaaaaaaaaaaaa");
        trophiesList.add(trophy);
        trophy = new ArrayList<>();
        trophy.add("RATXA MENSUAL");
        trophy.add("S'obté al obtenir una ratxa de 30 diesaaaaaaaaaaaaaaaaaaaaaa");
        trophiesList.add(trophy);
        trophy.add("RATXA MENSUAL");
        trophy.add("S'obté al obtenir una ratxa de 30 diesaaaaaaaaaaaaaaaaaaaaaa");
        trophiesList.add(trophy);
        trophy.add("RATXA MENSUAL");
        trophy.add("S'obté al obtenir una ratxa de 30 diesaaaaaaaaaaaaaaaaaaaaaa");
        trophiesList.add(trophy);
        trophy.add("RATXA MENSUAL");
        trophy.add("S'obté al obtenir una ratxa de 30 diesaaaaaaaaaaaaaaaaaaaaaa");
        trophiesList.add(trophy);
        trophy.add("RATXA MENSUAL");
        trophy.add("S'obté al obtenir una ratxa de 30 diesaaaaaaaaaaaaaaaaaaaaaa");
        trophiesList.add(trophy);
        trophy.add("RATXA MENSUAL");
        trophy.add("S'obté al obtenir una ratxa de 30 diesaaaaaaaaaaaaaaaaaaaaaa");
        trophiesList.add(trophy);
        trophy.add("RATXA MENSUAL");
        trophy.add("S'obté al obtenir una ratxa de 30 diesaaaaaaaaaaaaaaaaaaaaaa");
        trophiesList.add(trophy);
        trophy.add("RATXA MENSUAL");
        trophy.add("S'obté al obtenir una ratxa de 30 diesaaaaaaaaaaaaaaaaaaaaaa");
        trophiesList.add(trophy);
        trophy.add("RATXA MENSUAL");
        trophy.add("S'obté al obtenir una ratxa de 30 diesaaaaaaaaaaaaaaaaaaaaaa");
        trophiesList.add(trophy);
        trophy.add("RATXA MENSUAL");
        trophy.add("S'obté al obtenir una ratxa de 30 diesaaaaaaaaaaaaaaaaaaaaaa");
        trophiesList.add(trophy);
        trophy.add("RATXA MENSUAL");
        trophy.add("S'obté al obtenir una ratxa de 30 diesaaaaaaaaaaaaaaaaaaaaaa");
        trophiesList.add(trophy);
    }

    TrophiesRecyclerAdapter trophiesRecyclerAdapter;
    RecyclerView recyclerView;

    /**
     * Constructora per defecte de RoutinesList
     */
    public Trophies() {}

    /**
     * Funció obtenir la instancia de la RoutinesList actual
     * */
    public static Trophies getInstance() {
        return instance;
    }

    /**
     * Funcio del Trophies que s'executa al crear-la
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.trophies, container, false);
        super.onCreate(savedInstanceState);
        instance = this;
        global = this.getActivity();

        recyclerView = view.findViewById(R.id.trophiesList);

        set();
        getTrophyList();

        initRecyclerView();

        return view;
    }

    /**
     * Funció per inicialitzar l'element que mostra el llistat d'activitats
     */
    private void initRecyclerView() {
        recyclerView.setLayoutManager((new LinearLayoutManager(global)));
        trophiesRecyclerAdapter = new TrophiesRecyclerAdapter(trophiesList, obtainedTrophiesList, global);
        recyclerView.setAdapter(trophiesRecyclerAdapter);
    }

    private void getTrophyList() {
        obtainedTrophiesList = DA.getUserAchievements();
    }

}