package com.pes.become.frontend;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;

import java.util.ArrayList;

public class Trophies extends Fragment {

    private static Trophies instance;

    private View view;
    private Context global;

    private final DomainAdapter DA = DomainAdapter.getInstance();

    ArrayList<ArrayList<String>> trophiesList;
    ArrayList<Boolean> obtainedTrophiesList;


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

        setTrophiesList();
        getObtainedTrophyList();

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

    /**
     * Funcio per setejar la llista de trofeus
     */
    public void setTrophiesList() {
        trophiesList = new ArrayList<>();
        ArrayList<String> trophy = new ArrayList<>();
        trophy.add(getString(R.string.CreateFirstRoutine));
        trophy.add(getString(R.string.CreateFirstRoutineDescription));
        trophiesList.add(trophy);
        trophy = new ArrayList<>();
        trophy.add(getString(R.string.HourMusic5));
        trophy.add(getString(R.string.HourMusic5Description));
        trophiesList.add(trophy);
        trophy = new ArrayList<>();
        trophy.add(getString(R.string.HourSport5));
        trophy.add(getString(R.string.HourSport5Description));
        trophiesList.add(trophy);
        trophy = new ArrayList<>();
        trophy.add(getString(R.string.HourSleeping5));
        trophy.add(getString(R.string.HourSleeping5Description));
        trophiesList.add(trophy);
        trophy = new ArrayList<>();
        trophy.add(getString(R.string.HourCooking5));
        trophy.add(getString(R.string.HourCooking5Description));
        trophiesList.add(trophy);
        trophy = new ArrayList<>();
        trophy.add(getString(R.string.HourWorking5));
        trophy.add(getString(R.string.HourWorking5Description));
        trophiesList.add(trophy);
        trophy = new ArrayList<>();
        trophy.add(getString(R.string.HourEntertainment5));
        trophy.add(getString(R.string.HourEntertainment5Description));
        trophiesList.add(trophy);
        trophy = new ArrayList<>();
        trophy.add(getString(R.string.HourPlants5));
        trophy.add(getString(R.string.HourPlants5Description));
        trophiesList.add(trophy);
        trophy = new ArrayList<>();
        trophy.add(getString(R.string.HourOther5));
        trophy.add(getString(R.string.HourOther5Description));
        trophiesList.add(trophy);
        trophy = new ArrayList<>();
        trophy.add(getString(R.string.HourMusic10));
        trophy.add(getString(R.string.HourMusic10Description));
        trophiesList.add(trophy);
        trophy = new ArrayList<>();
        trophy.add(getString(R.string.HourSport10));
        trophy.add(getString(R.string.HourSport10Description));
        trophiesList.add(trophy);
        trophy = new ArrayList<>();
        trophy.add(getString(R.string.HourSleeping10));
        trophy.add(getString(R.string.HourSleeping10Description));
        trophiesList.add(trophy);
        trophy = new ArrayList<>();
        trophy.add(getString(R.string.HourCooking10));
        trophy.add(getString(R.string.HourCooking10Description));
        trophiesList.add(trophy);
        trophy = new ArrayList<>();
        trophy.add(getString(R.string.HourWorking10));
        trophy.add(getString(R.string.HourWorking10Description));
        trophiesList.add(trophy);
        trophy = new ArrayList<>();
        trophy.add(getString(R.string.HourEntertainment10));
        trophy.add(getString(R.string.HourEntertainment10Description));
        trophiesList.add(trophy);
        trophy = new ArrayList<>();
        trophy.add(getString(R.string.HourPlants10));
        trophy.add(getString(R.string.HourPlants10Description));
        trophiesList.add(trophy);
        trophy = new ArrayList<>();
        trophy.add(getString(R.string.HourOther10));
        trophy.add(getString(R.string.HourOther10Description));
        trophiesList.add(trophy);
    }

    /**
     * Funcio per obtenir la llista de trofeus obtinguts
     */
    public void getObtainedTrophyList() {
        obtainedTrophiesList = DA.getUserAchievements();
    }

}