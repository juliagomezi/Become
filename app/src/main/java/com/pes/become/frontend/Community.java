package com.pes.become.frontend;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;

import java.util.ArrayList;

public class Community extends Fragment {

    private static Community instance;

    private View view;
    private Context global;

    private final DomainAdapter DA = DomainAdapter.getInstance();

    private ArrayList<ArrayList<Object>> communityRoutinesList;

    CommunityRecyclerAdapter communityRecyclerAdapter;
    RecyclerView recyclerView;

    EditText searchText;
    ImageButton searchButton;

    /**
     * Constructora per defecte de RoutinesList
     */
    public Community() {}

    /**
     * Funció obtenir la instancia de la RoutinesList actual
     * */
    public static Community getInstance() {
        return instance;
    }

    /**
     * Funcio del RoutinesList que s'executa al crear-la
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.community, container, false);
        super.onCreate(savedInstanceState);
        global = this.getActivity();
        instance = this;

        recyclerView = view.findViewById(R.id.communityRoutinesList);
        searchText = view.findViewById(R.id.searchText);
        searchButton = view.findViewById(R.id.searchButton);

        searchButton.setOnClickListener(view2 -> search());

        getCommunityRoutines();

        return view;
    }

    /**
     * Funcio que es crida al cercar
     */
    private void search() {
        String filter = searchText.getText().toString();
        getCommunityRoutines(filter);
    }

    /**
     * Funció per inicialitzar l'element que mostra el llistat d'activitats
     */
    private void initRecyclerView() {
        recyclerView.setLayoutManager((new LinearLayoutManager(global)));
        communityRecyclerAdapter = new CommunityRecyclerAdapter(communityRoutinesList, global);
        recyclerView.setAdapter(communityRecyclerAdapter);
    }

    /**
     * Funció per obtenir les rutines de la communitat endreçada per valoració i punts
     */
    public void getCommunityRoutines() {
        //communityRoutinesList = DA.getCommunityRoutines();

        ArrayList<Object> routine;
        communityRoutinesList = new ArrayList<>();
        routine = new ArrayList<>();
        routine.add("userId"); // (0) id del usuari propietari de la rutina
        routine.add(DA.getProfilePic()); // (1) foto perfil del usuari propietari de la rutina
        routine.add("routineId"); // (2) id de la rutina
        routine.add("routineName"); // (3) nom de la rutina
        routine.add(true); // (4) saved per el current user => true o false
        routine.add(5); // (5) valoracio mitjana de tots els usuaris [0,10] // null => no valorat
        communityRoutinesList.add(routine);
        routine = new ArrayList<>();
        routine.add("userId"); // (0) id del usuari propietari de la rutina
        routine.add(DA.getProfilePic()); // (1) foto perfil del usuari propietari de la rutina
        routine.add("routineId"); // (2) id de la rutina
        routine.add("routineName"); // (3) nom de la rutina
        routine.add(true); // (4) saved per el current user => true o false
        routine.add(5); // (5) valoracio mitjana de tots els usuaris [0,10] // null => no valorat
        communityRoutinesList.add(routine);
        routine = new ArrayList<>();
        routine.add("userId"); // (0) id del usuari propietari de la rutina
        routine.add(DA.getProfilePic()); // (1) foto perfil del usuari propietari de la rutina
        routine.add("routineId"); // (2) id de la rutina
        routine.add("routineName"); // (3) nom de la rutina
        routine.add(true); // (4) saved per el current user => true o false
        routine.add(5); // (5) valoracio mitjana de tots els usuaris [0,10] // null => no valorat
        communityRoutinesList.add(routine);
        routine = new ArrayList<>();
        routine.add("userId"); // (0) id del usuari propietari de la rutina
        routine.add(DA.getProfilePic()); // (1) foto perfil del usuari propietari de la rutina
        routine.add("routineId"); // (2) id de la rutina
        routine.add("routineName"); // (3) nom de la rutina
        routine.add(true); // (4) saved per el current user => true o false
        routine.add(5); // (5) valoracio mitjana de tots els usuaris [0,10] // null => no valorat
        communityRoutinesList.add(routine);

        initRecyclerView();
    }

    /**
     * Funció per obtenir les rutines de la communitat endreçada per valoració i punts i utilitzant el filtre
     * @param filter valor pel qual es filtra el resultat
     */
    public void getCommunityRoutines(String filter) {
        //communityRoutinesList = DA.getCommunityRoutines(filter);
        initRecyclerView();
    }
}
