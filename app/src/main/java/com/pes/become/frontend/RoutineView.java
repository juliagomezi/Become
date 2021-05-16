package com.pes.become.frontend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;
import com.pes.become.backend.exceptions.NoSelectedRoutineException;

import java.util.ArrayList;
import java.util.Calendar;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class RoutineView extends Fragment implements AdapterView.OnItemSelectedListener{

    private static RoutineView instance;

    private View view;
    private Context global;

    private final DomainAdapter DA = DomainAdapter.getInstance();

    private int seeingDay;
    private ArrayList<ArrayList<String>> activitiesList;

    private RecyclerView recyclerView;
    private TextView emptyView;
    TextView routineDay;
    ImageButton previousDayButton;
    ImageButton nextDayButton;

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

        recyclerView = view.findViewById(R.id.activityList);
        emptyView = view.findViewById(R.id.emptyView);
        previousDayButton = view.findViewById(R.id.previousDayButton);
        previousDayButton.setOnClickListener(v -> showPreviousDay());
        nextDayButton = view.findViewById(R.id.nextDayButton);
        nextDayButton.setOnClickListener(v -> showNextDay());

        seeingDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        translateSeeingDay();
        setDay();

        updateActivitiesList();

        return view;
    }

    /**
     * Funció obtenir la instancia de la RoutineView actual
     * */
    public static RoutineView getInstance() {
        return instance;
    }

    /**
     * Funcio per transformar el numero del dia
     */
    private void translateSeeingDay() {
        seeingDay -= 2;
        if(seeingDay == -1)
            seeingDay = 6;
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
        updateActivitiesList();
    }

    /**
     * Funcio per veure les activitats del seguent dia
     */
    private void showNextDay() {
        if (seeingDay == 6) seeingDay = 0;
        else seeingDay++;
        setDay();
        updateActivitiesList();
    }

    /**
     * Funció per inicialitzar l'element que mostra el llistat d'activitats
     */
    private void initRecyclerView() {
        recyclerView.setLayoutManager((new LinearLayoutManager(global)));
        RoutineViewRecyclerAdapter routineViewRecyclerAdapter = new RoutineViewRecyclerAdapter(activitiesList);
        recyclerView.setAdapter(routineViewRecyclerAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        routineDay.setVisibility(View.VISIBLE);
        previousDayButton.setVisibility(View.VISIBLE);
        nextDayButton.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.INVISIBLE);
    }

    /**
     * Funció per inicialitzar l'element que es mostra quan no hi ha activitats
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
    private void updateActivitiesList() {
        try {
            activitiesList = DA.getActivitiesByDay(getWeekDay(seeingDay));
            initRecyclerView();
            if (activitiesList.isEmpty()) initEmptyView(getString(R.string.noActivities));
        } catch (NoSelectedRoutineException e) {
            initEmptyView(getString(R.string.noRoutineSelected));
        }
    }

    String doneActivityID = null;
    String doneActivityName = null;

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
                    if(!Boolean.valueOf(activitiesList.get(position).get(7))) {
                        doneActivityID = activitiesList.get(position).get(0);
                        doneActivityName = activitiesList.get(position).get(1);

                        DA.markActivityAsDone(doneActivityID, true, seeingDay);
                        updateActivitiesList();

                        Snackbar.make(recyclerView, doneActivityName, BaseTransientBottomBar.LENGTH_LONG)
                                .setAction("Undo", v -> DA.markActivityAsDone(doneActivityID, false, seeingDay)).show();
                    }
                    break;

                case ItemTouchHelper.RIGHT: // ->
                    if(Boolean.valueOf(activitiesList.get(position).get(7))) {
                        doneActivityID = activitiesList.get(position).get(0);
                        doneActivityName = activitiesList.get(position).get(1);

                        DA.markActivityAsDone(doneActivityID, false, seeingDay);
                        updateActivitiesList();

                        Snackbar.make(recyclerView, doneActivityName, BaseTransientBottomBar.LENGTH_LONG)
                                .setAction("Undo", v -> DA.markActivityAsDone(doneActivityID, true, seeingDay)).show();
                    }
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(global, R.color.green25))
                    .addSwipeLeftLabel("DONE")
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(global, R.color.softred))
                    .addSwipeRightLabel("UNDONE")
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            /*try {
                Bitmap icon;

                View itemView = viewHolder.itemView;
                float height = (float) itemView.getBottom() - (float) itemView.getTop();
                float width = height / 5;
                viewHolder.itemView.setTranslationX(dX / 5);

                Paint paint = new Paint(Color.parseColor("#ccffcc"));
                RectF background = new RectF((float) itemView.getRight() + dX / 5, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                c.drawRect(background, paint);
                icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_email);
                RectF icon_dest = new RectF((float) (itemView.getRight() + dX /7), (float) itemView.getTop()+width, (float) itemView.getRight()+dX/20, (float) itemView.getBottom()-width);
                c.drawBitmap(icon, null, icon_dest, paint);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
    };

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