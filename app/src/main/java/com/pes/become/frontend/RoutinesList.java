package com.pes.become.frontend;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.pes.become.backend.domain.Achievement;
import com.pes.become.backend.exceptions.ExistingRoutineException;

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
    private EditText routineName;

    /**
     * Classe per afegir padding al final del recycler view
     */
    class BottomItemDecoration  extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1)
                outRect.bottom = 250;
        }
    }

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
        emptyView.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager((new LinearLayoutManager(global)));
        routinesListRecyclerAdapter = new RoutinesListRecyclerAdapter(routinesList, selectedRoutineID, global);
        recyclerView.setAdapter(routinesListRecyclerAdapter);
        recyclerView.addItemDecoration(new BottomItemDecoration());
    }

    /**
     * Funció per inicialitzar l'element que es mostra quan no hi ha activitats
     */
    public void initEmptyView() {
        recyclerView.setVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.VISIBLE);
    }

    /**
     * Funció per obtenir les rutines de l'usuari
     */
    public void getRoutines() {
        routinesList = DA.getUserRoutines();
        selectedRoutineID = DA.getSelectedRoutineId();
        initRecyclerView();
        if(routinesList.isEmpty()) initEmptyView();
    }

    /**
     * Funció per crear la pestanya de creacio de rutina
     */
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

    /**
     * Funció per crear una nova rutina
     */
    private void createRoutine() {
        String name = routineName.getText().toString();
        if (name.isEmpty()) routineName.setError(getString(R.string.notNull));
        else {
            try {
                if (routinesList.isEmpty()) initRecyclerView();
                DA.createRoutine(name);
                routinesListRecyclerAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), getString(R.string.routineCreated), Toast.LENGTH_SHORT).show();
                routineSheet.dismiss();
                if (DA.checkAchievement(Achievement.values()[0].toString())) MainActivity.getInstance().showTrophyWon(getString(R.string.CreateFirstRoutine));
            } catch (ExistingRoutineException e) {
                routineName.setError(getString(R.string.existingRoutineName));
            }
        }
    }
}