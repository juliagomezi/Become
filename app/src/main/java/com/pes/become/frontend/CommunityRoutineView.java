package com.pes.become.frontend;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;
import com.pes.become.backend.exceptions.RoutinePrimaryKeyException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CommunityRoutineView extends Fragment {

    private View view;
    private Context global;

    private final DomainAdapter DA = DomainAdapter.getInstance();

    private int seeingDay;
    private String routineId;
    private String routineName;
    private HashMap<String, ArrayList<ArrayList<String>>> activitiesList;
    private ArrayList<ArrayList<String>> activitiesListDay;

    private int voted, numvotes;
    private double average;

    private RecyclerView recyclerView;
    private TextView emptyView;
    TextView routineDay;
    ImageButton previousDayButton, nextDayButton, saveRoutine;
    ImageButton star1, star2, star3, star4, star5;

    /**
     * Classe per afegir padding al final del recycler view
     */
    class BottomItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1)
                outRect.bottom = 250;
        }
    }

    /**
     * Constructora del RoutineView
     */
    public CommunityRoutineView(String routineId, String routineName, HashMap<String, ArrayList<ArrayList<String>>> activitiesList, int voted, double avg, int numvotes) {
        this.routineId = routineId;
        this.routineName = routineName;
        this.activitiesList = activitiesList;
        this.voted = voted;
        this.average = avg;
        this.numvotes = numvotes;
    }

    /**
     * Funcio del RoutineView que s'executa al crear-la
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.community_routine_view, container, false);
        super.onCreate(savedInstanceState);
        global = this.getActivity();

        recyclerView = view.findViewById(R.id.activityList);
        emptyView = view.findViewById(R.id.emptyView);
        previousDayButton = view.findViewById(R.id.previousDayButton);
        previousDayButton.setOnClickListener(v -> showPreviousDay());
        nextDayButton = view.findViewById(R.id.nextDayButton);
        nextDayButton.setOnClickListener(v -> showNextDay());
        saveRoutine = view.findViewById(R.id.saveRoutine);
        saveRoutine.setOnClickListener(v -> saveRoutine());
        TextView routineName = view.findViewById(R.id.routineName);
        routineName.setText(this.routineName);

        initStars();

        seeingDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        seeingDay = translateDay(seeingDay);
        setDay();

        getActivitiesByDay();

        return view;
    }

    /**
     * Metode que inicialitza les estrelles
     */
    private void initStars() {
        if(voted == 0) {
            star1 = view.findViewById(R.id.star1);
            star1.setOnClickListener(v -> {
                star1.setImageResource(R.drawable.ic_star_filled);
                DA.voteRoutine(this.routineId, 1, average, numvotes);
            });
            star2 = view.findViewById(R.id.star2);
            star2.setOnClickListener(v -> {
                star1.setImageResource(R.drawable.ic_star_filled);
                star2.setImageResource(R.drawable.ic_star_filled);
                DA.voteRoutine(this.routineId, 2, average, numvotes);
            });
            star3 = view.findViewById(R.id.star3);
            star3.setOnClickListener(v -> {
                star1.setImageResource(R.drawable.ic_star_filled);
                star2.setImageResource(R.drawable.ic_star_filled);
                star3.setImageResource(R.drawable.ic_star_filled);
                DA.voteRoutine(this.routineId, 3, average, numvotes);
            });
            star4 = view.findViewById(R.id.star4);
            star4.setOnClickListener(v -> {
                star1.setImageResource(R.drawable.ic_star_filled);
                star2.setImageResource(R.drawable.ic_star_filled);
                star3.setImageResource(R.drawable.ic_star_filled);
                star4.setImageResource(R.drawable.ic_star_filled);
                DA.voteRoutine(this.routineId, 4, average, numvotes);
            });
            star5 = view.findViewById(R.id.star5);
            star5.setOnClickListener(v -> {
                star1.setImageResource(R.drawable.ic_star_filled);
                star2.setImageResource(R.drawable.ic_star_filled);
                star3.setImageResource(R.drawable.ic_star_filled);
                star4.setImageResource(R.drawable.ic_star_filled);
                star5.setImageResource(R.drawable.ic_star_filled);
                DA.voteRoutine(this.routineId, 5, average, numvotes);
            });
        } else {
            if (average > 0.5) star1.setImageResource(R.drawable.ic_star_filled);
            if (average > 1.5) star2.setImageResource(R.drawable.ic_star_filled);
            if (average > 2.5) star3.setImageResource(R.drawable.ic_star_filled);
            if (average > 3.5) star4.setImageResource(R.drawable.ic_star_filled);
            if (average > 4.5) star5.setImageResource(R.drawable.ic_star_filled);
            if(voted > 0) {
                star1.setOnClickListener(v -> { Toast.makeText(global, global.getApplicationContext().getString(R.string.routineAlreadyVoted), Toast.LENGTH_SHORT).show(); });
                star2.setOnClickListener(v -> { Toast.makeText(global, global.getApplicationContext().getString(R.string.routineAlreadyVoted), Toast.LENGTH_SHORT).show(); });
                star3.setOnClickListener(v -> { Toast.makeText(global, global.getApplicationContext().getString(R.string.routineAlreadyVoted), Toast.LENGTH_SHORT).show(); });
                star4.setOnClickListener(v -> { Toast.makeText(global, global.getApplicationContext().getString(R.string.routineAlreadyVoted), Toast.LENGTH_SHORT).show(); });
                star5.setOnClickListener(v -> { Toast.makeText(global, global.getApplicationContext().getString(R.string.routineAlreadyVoted), Toast.LENGTH_SHORT).show(); });
            } else {
                star1.setOnClickListener(v -> { Toast.makeText(global, R.string.voteOwnRoutine, Toast.LENGTH_SHORT).show(); });
                star2.setOnClickListener(v -> { Toast.makeText(global, R.string.voteOwnRoutine, Toast.LENGTH_SHORT).show(); });
                star3.setOnClickListener(v -> { Toast.makeText(global, R.string.voteOwnRoutine, Toast.LENGTH_SHORT).show(); });
                star4.setOnClickListener(v -> { Toast.makeText(global, R.string.voteOwnRoutine, Toast.LENGTH_SHORT).show(); });
                star5.setOnClickListener(v -> { Toast.makeText(global, R.string.voteOwnRoutine, Toast.LENGTH_SHORT).show(); });
            }
        }
    }

    /**
     * Funcio per transformar el numero del dia
     */
    private int translateDay(int day) {
        day -= 2;
        if(day == -1)
            day = 6;
        return day;
    }

    /**
     * Funcio per posar el dia actual a la vista
     */
    private void setDay() {
        routineDay = view.findViewById(R.id.routineDay);
        routineDay.setText(getResources().getStringArray(R.array.dayValues)[seeingDay]);
    }

    /**
     * Funcio per obtenir el string del dia
     * @param day int del dia
     */
    private String getWeekDay(int day) {
        switch (day) {
            case 0:
                return "Monday";
            case 1:
                return "Tuesday";
            case 2:
                return "Wednesday";
            case 3:
                return "Thursday";
            case 4:
                return "Friday";
            case 5:
                return "Saturday";
            case 6:
                return "Sunday";
        }
        return "";
    }

    /**
     * Funcio per veure les activitats del dia anterior
     */
    private void showPreviousDay() {
        if (seeingDay == 0) seeingDay = 6;
        else seeingDay--;
        setDay();
        getActivitiesByDay();
    }

    /**
     * Funcio per veure les activitats del seguent dia
     */
    private void showNextDay() {
        if (seeingDay == 6) seeingDay = 0;
        else seeingDay++;
        setDay();
        getActivitiesByDay();
    }

    /**
     * Funcio per inicialitzar l'element que mostra el llistat d'activitats
     */
    private void initRecyclerView() {
        recyclerView.setLayoutManager((new LinearLayoutManager(global)));
        CommunityRoutineViewRecyclerAdapter communityRoutineViewRecyclerAdapter = new CommunityRoutineViewRecyclerAdapter(activitiesListDay);
        recyclerView.setAdapter(communityRoutineViewRecyclerAdapter);
        recyclerView.addItemDecoration(new CommunityRoutineView.BottomItemDecoration());

        routineDay.setVisibility(View.VISIBLE);
        previousDayButton.setVisibility(View.VISIBLE);
        nextDayButton.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.INVISIBLE);
    }

    /**
     * Funcio per inicialitzar l'element que es mostra quan no hi ha activitats
     * @param text text que mostrara la vista
     */
    private void initEmptyView(String text) {
        if (text.equals(getString(R.string.noActivities))) {
            routineDay.setVisibility(View.VISIBLE);
            previousDayButton.setVisibility(View.VISIBLE);
            nextDayButton.setVisibility(View.VISIBLE);
        }
        else {
            routineDay.setVisibility(View.GONE);
            previousDayButton.setVisibility(View.GONE);
            nextDayButton.setVisibility(View.GONE);
        }

        emptyView.setText(text);

        recyclerView.setVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.VISIBLE);
    }

    /**
     * Funcio per actualitzar la llista d'activitats
     */
    private void getActivitiesByDay() {
        activitiesListDay = activitiesList.get(getWeekDay(seeingDay));
        initRecyclerView();
        if (activitiesListDay.isEmpty()) initEmptyView(getString(R.string.noActivities));
    }

    /**
     * Funcio per guardar una rutina
     */
    private void saveRoutine() {
        try {
            DA.downloadSharedRoutine(routineId, routineName);
            Toast.makeText(global, global.getApplicationContext().getString(R.string.downOk), Toast.LENGTH_SHORT).show();
        } catch (RoutinePrimaryKeyException e) {
            Toast.makeText(global, getString(R.string.existingRoutineName), Toast.LENGTH_SHORT).show();
        }
    }
}
