package com.pes.become.frontend;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class RoutineView extends Fragment implements AdapterView.OnItemSelectedListener{

    private static RoutineView instance;

    private View view;
    private Context global;

    private final DomainAdapter DA = DomainAdapter.getInstance();

    private TextView routineDay;
    private int seeingDay;
    private ArrayList<ArrayList<String>> activitiesList;

    RecyclerView recyclerView;
    RoutineViewRecyclerAdapter routineViewRecyclerAdapter;
    TextView emptyView;

    /**
     * Constructora del RoutineView
     */
    public RoutineView() { }

    /**
     * Funcio del RoutineView que s'executa al crear-la
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.routine_view, container, false);
        super.onCreate(savedInstanceState);
        instance = this;
        global = this.getActivity();

        seeingDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        setDay();
        getActivitiesByDay(getWeekDay(seeingDay));

        ImageButton previousDayButton = view.findViewById(R.id.previousDayButton);
        previousDayButton.setOnClickListener(v -> showPreviousDay());
        ImageButton nextDayButton = view.findViewById(R.id.nextDayButton);
        nextDayButton.setOnClickListener(v -> showNextDay());

        return view;
    }

    /**
     * Funció obtenir la instancia de la RoutineView actual
     * */
    public static RoutineView getInstance() {
        return instance;
    }

    /**
     * Funcio per posar el dia actual a la vista
     */
    private void setDay() {
        routineDay = view.findViewById(R.id.routineDay);
        routineDay.setText(getWeekDay(seeingDay));
    }

    /**
     * Funcio per obtenir el string del dia
     * @param day int del dia
     */
    private String getWeekDay(int day) {
        switch (day) {
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            case Calendar.SUNDAY:
                return "Sunday";
        }
        return "";
    }

    /**
     * Funcio per veure les activitats del dia anterior
     */
    private void showPreviousDay() {
        if (seeingDay > 1) {
            seeingDay--;
            setDay();
            getActivitiesByDay(getWeekDay(seeingDay));
        }
    }

    /**
     * Funcio per veure les activitats del seguent dia
     */
    private void showNextDay() {
        if (seeingDay < 7) {
            seeingDay++;
            setDay();
            getActivitiesByDay(getWeekDay(seeingDay));
        }
    }

    /**
     * Funció per inicialitzar l'element que mostra el llistat d'activitats
     */
    private void initRecyclerView() {
        recyclerView = view.findViewById(R.id.activityList);
        recyclerView.setLayoutManager((new LinearLayoutManager(global)));
        routineViewRecyclerAdapter = new RoutineViewRecyclerAdapter(activitiesList);
        recyclerView.setAdapter(routineViewRecyclerAdapter);

        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.INVISIBLE);

        // Swipe right i left
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * Funció per inicialitzar l'element que es mostra quan no hi ha activitats
     */
    private void initEmptyView() {
        emptyView = view.findViewById(R.id.emptyView);

        recyclerView.setVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.VISIBLE);
    }

    /**
     * Implementacio del que es fa quan es fa swipe right i left als elements del recycler view
     */
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT: // <-

                    break;
                case ItemTouchHelper.RIGHT: // ->

                    break;
            }
        }
    };

    /**
     * Funció per obtenir les activitats de dia de la rutina
     */
    public void getActivitiesByDay(String day) {
        try {
            DA.getActivitiesByDayToView(day, this);
        } catch (NoSuchMethodException ignored) { }
    }

    /**
     * Funció per inicialitzar l'element que mostra el llistat d'activitats
     * @param activitiesListCallback llistat d'activitats que retorna la BD
     */
    public void getActivitiesCallback(ArrayList<ArrayList<String>> activitiesListCallback) {
        if (!activitiesListCallback.isEmpty()) {
            activitiesList = new ArrayList<>(activitiesListCallback.size());
            activitiesList.addAll(activitiesListCallback);
            initRecyclerView();
        }

        else {
            initEmptyView();
        }
    }

    /**
     * Funció necessària pel correcte funcionament de l'app
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }

    /**
     * Funció necessària pel correcte funcionament de l'app
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) { }



}