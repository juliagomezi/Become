package com.pes.become.frontend;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;
import com.pes.become.backend.exceptions.InvalidDayIntervalException;
import com.pes.become.backend.exceptions.InvalidTimeIntervalException;
import com.pes.become.backend.exceptions.OverlappingActivitiesException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RoutinesList extends Fragment {

    private static RoutinesList instance;

    private View view;
    private Context global;

    private final DomainAdapter DA = DomainAdapter.getInstance();

    private ArrayList<ArrayList<String>> routinesList;

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

        TextView addRoutine = view.findViewById(R.id.addRoutine);
        addRoutine.setOnClickListener(v -> createRoutineSheet());

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

        //temporalment hardcodejat START
        routinesList = new ArrayList<>();
        for(int i=1; i<5;++i) {
            ArrayList<String> routine = new ArrayList<>();
            routine.add(String.valueOf(i));
            routine.add("Rutina "+String.valueOf(i));
            routinesList.add(routine);
        }
        initRecyclerView();
        //temporalment hardcodejat END

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
                //DA.createRoutine();
                Toast.makeText(getContext(), getString(R.string.routineCreated), Toast.LENGTH_SHORT).show();
                routineSheet.dismiss();
            }
            catch (Exception e) {
                routineName.setError(getString(R.string.notNull));
            }
        }
    }

    /**
     * Funció per posar els valors a la pestanya de modificació d'activitat
     * @param name nom de l'activitat
     */
    public void fillRoutineSheet(String id, String name) {
        this.id = id;
        this.routineName.setText(name);
    }
}