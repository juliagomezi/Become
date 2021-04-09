package com.pes.become.frontend;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;

import java.util.ArrayList;

public class RoutinesList extends Fragment {

    private View view;
    private Context global;

    private final DomainAdapter DA = DomainAdapter.getInstance();

    private ArrayList<ArrayList<String>> routinesList;

    /**
     * Constructora per defecte de RoutinesList
     */
    public RoutinesList() {}

    /**
     * Funcio del RoutinesList que s'executa al crear-la
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.routines_list, container, false);
        super.onCreate(savedInstanceState);
        global = this.getActivity();

        getRoutines();

        return view;
    }

    /**
     * Funció per inicialitzar l'element que mostra el llistat d'activitats
     */
    private void initRecyclerView() {
        RecyclerView recyclerView = view.findViewById(R.id.activityList);
        recyclerView.setLayoutManager((new LinearLayoutManager(global)));
        RecyclerAdapterRoutinesList recyclerAdapter = new RecyclerAdapterRoutinesList(routinesList);
        recyclerView.setAdapter(recyclerAdapter);
    }

    /**
     * Funció per obtenir les rutines de l'usuari
     */
    public void getRoutines() {

        //temporalment hardcodejat
        routinesList = new ArrayList<>();
        for(int i=1; i<5;++i) {
            ArrayList<String> routine = new ArrayList<>();
            routine.add(String.valueOf(i));
            routine.add("Rutina "+String.valueOf(i));
            routinesList.add(routine);
        }
        initRecyclerView();

        //DA.getRoutines();
    }

    /**
     * Funció per inicialitzar l'element que mostra el llistat de rutines
     * @param routinesListCallback llistat de rutines que retorna la BD
     */
    public void getRoutinesCallback(ArrayList<ArrayList<String>> routinesListCallback) {
        routinesList = new ArrayList<>(routinesListCallback.size());
        routinesList.addAll(routinesListCallback);
        initRecyclerView();
    }
}