package com.pes.become.frontend;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;
import com.pes.become.backend.persistence.ControllerRoutineDB;

import java.util.ArrayList;

public class RoutinesList extends Fragment {

    private static RoutinesList instance;

    private View view;
    private Context global;

    private final DomainAdapter DA = DomainAdapter.getInstance();

    private ArrayList<ArrayList<String>> routinesList;
    private String selectedRoutineID;

    RoutinesListRecyclerAdapter routinesListRecyclerAdapter;
    RecyclerView recyclerView;
    TextView emptyView;

    private BottomSheetDialog routineSheet;
    private String id;
    private EditText routineName;

    /**
     * Constructora per defecte de RoutinesList
     */
    public RoutinesList() {}

    /**
     * Funció obtenir la instancia de la RoutinesList actual
     * */
    public static RoutinesList getInstance() {
        return instance;
    }

    /**
     * Funcio del RoutinesList que s'executa al crear-la
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.routines_list, container, false);
        super.onCreate(savedInstanceState);
        global = this.getActivity();
        instance = this;

        recyclerView = view.findViewById(R.id.activityList);
        emptyView = view.findViewById(R.id.emptyView);

        TextView addRoutine = view.findViewById(R.id.addRoutine);
        addRoutine.setOnClickListener(v -> createRoutineSheet());

        getRoutines();

        return view;
    }

    /**
     * Funció per inicialitzar l'element que mostra el llistat d'activitats
     */
    private void initRecyclerView() {
        recyclerView.setLayoutManager((new LinearLayoutManager(global)));
        routinesListRecyclerAdapter = new RoutinesListRecyclerAdapter(routinesList, selectedRoutineID);
        recyclerView.setAdapter(routinesListRecyclerAdapter);
    }

    /**
     * Funció per inicialitzar l'element que es mostra quan no hi ha activitats
     */
    private void initEmptyView() {
        recyclerView.setVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.VISIBLE);
    }

    /**
     * Funció per obtenir les rutines de l'usuari
     */
    public void getRoutines() {
        try {
            DA.getUserRoutines(this);
        } catch (NoSuchMethodException ignore) { }
    }

    /**
     * Funció per inicialitzar l'element que mostra el llistat de rutines
     * @param routinesListCallback llistat de rutines que retorna la BD
     * @param selectedRoutineID id de la rutina seleccionada de l'usuari que retorna la BD
     */
    public void getRoutinesCallback(ArrayList<ArrayList<String>> routinesListCallback, String selectedRoutineID) {
        this.selectedRoutineID = selectedRoutineID;
        routinesList = new ArrayList<>(routinesListCallback.size());
        routinesList.addAll(routinesListCallback);
        initRecyclerView();

        if(routinesList.isEmpty()) {
            initEmptyView();
        }
    }

    public void createRoutineSheet() {
        routineSheet = new BottomSheetDialog(global,R.style.BottomSheetTheme);
        View sheetView = LayoutInflater.from(getContext()).inflate(R.layout.routines_list_edit, view.findViewById(R.id.bottom_sheet));

        routineName = sheetView.findViewById(R.id.nameText);
        Button doneButton = sheetView.findViewById(R.id.doneButton);
        Button cancelButton = sheetView.findViewById(R.id.cancelButton);
        doneButton.setOnClickListener(v -> createRoutine());
        cancelButton.setOnClickListener(v -> routineSheet.dismiss());

        routineSheet.setContentView(sheetView);
        routineSheet.show();
    }

    private void createRoutine() {
        String name = routineName.getText().toString();
        if (name.isEmpty()) routineName.setError(getString(R.string.notNull));
        else {
            try {
                String id = DA.createRoutine(name);
                ArrayList<String> routine = new ArrayList<>(2);
                routine.add(id);
                routine.add(name);
                routinesList.add(0,routine);
                routinesListRecyclerAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), getString(R.string.routineCreated), Toast.LENGTH_SHORT).show();
                routineSheet.dismiss();
            }
            catch (Exception e) {
                e.printStackTrace(); // provisional!!!
                routineName.setError(getString(R.string.notNull));
            }
        }
    }
}