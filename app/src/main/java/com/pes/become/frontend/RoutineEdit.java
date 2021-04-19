package com.pes.become.frontend;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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
import java.util.Calendar;
import java.util.Date;

public class RoutineEdit extends Fragment implements AdapterView.OnItemSelectedListener{

    private static RoutineEdit instance;

    private View view;
    private Context global;

    private final DomainAdapter DA = DomainAdapter.getInstance();

    private TextView routineDay;
    private int seeingDay;
    private ArrayList<ArrayList<String>> activitiesList;

    private BottomSheetDialog activitySheet;
    private Spinner spinnerTheme, spinnerStartDay, spinnerEndDay;
    private EditText activityName, activityDescr;
    private TextView startTime;
    private TextView endTime;
    private TextView sheetLabel;
    private int startHour, startMinute, endHour, endMinute;
    private String id;

    /**
     * Constructora del RoutineEdit
     */
    public RoutineEdit() { }

    /**
     * Funcio del RoutineEdit que s'executa al crear-la
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.routine_edit, container, false);
        super.onCreate(savedInstanceState);
        instance = this;
        global = this.getActivity();

        seeingDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        setDay();
        getActivitiesByDay(getWeekDay(seeingDay));

        TextView addActivity = view.findViewById(R.id.addActivity);
        addActivity.setOnClickListener(v -> createActivitySheet(false));

        ImageButton previousDayButton = view.findViewById(R.id.previousDayButton);
        previousDayButton.setOnClickListener(v -> showPreviousDay());
        ImageButton nextDayButton = view.findViewById(R.id.nextDayButton);
        nextDayButton.setOnClickListener(v -> showNextDay());

        return view;
    }

    private void showPreviousDay() {
        Log.d("PREV", "PREV");
        if (seeingDay > 1) {
            seeingDay--;
            setDay();
            getActivitiesByDay(getWeekDay(seeingDay));
        }
    }

    private void showNextDay() {
        Log.d("NEXT", "NEXT");
        if (seeingDay < 7) {
            seeingDay++;
            setDay();
            getActivitiesByDay(getWeekDay(seeingDay));
        }
    }

    /**
     * Funció obtenir la instancia de la RoutineEdit actual
     * */
    public static RoutineEdit getInstance() {
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
     * Funció per inicialitzar l'element que mostra el llistat d'activitats
     */
    private void initRecyclerView() {
        RecyclerView recyclerView = view.findViewById(R.id.activityList);
        recyclerView.setLayoutManager((new LinearLayoutManager(global)));
        RoutineEditRecyclerAdapter routineEditRecyclerAdapter = new RoutineEditRecyclerAdapter(activitiesList);
        recyclerView.setAdapter(routineEditRecyclerAdapter);
    }

    /**
     * Funció per crear la pestanya de creacio/modificacio d'activitat
     * @param modify boolea que indica si s'esta modificicant una activitat existent o creant una de nova
     */
    public void createActivitySheet(boolean modify) {
        activitySheet = new BottomSheetDialog(global,R.style.BottomSheetTheme);
        View sheetView = LayoutInflater.from(getContext()).inflate(R.layout.activity_edit, view.findViewById(R.id.bottom_sheet));

        Button doneButton = sheetView.findViewById(R.id.doneButton);
        Button cancelButton = sheetView.findViewById(R.id.cancelButton);
        spinnerTheme = sheetView.findViewById(R.id.themeSpinner);
        spinnerStartDay = sheetView.findViewById(R.id.dayStartSpinner);
        spinnerEndDay = sheetView.findViewById(R.id.dayEndSpinner);
        activityName = sheetView.findViewById(R.id.nameText);
        activityDescr = sheetView.findViewById(R.id.descriptionText);
        startTime = sheetView.findViewById(R.id.startTime);
        endTime = sheetView.findViewById(R.id.endTime);
        sheetLabel = sheetView.findViewById(R.id.newTitle);

        // desplegable nova activitat
        ArrayAdapter<CharSequence> adapterTheme = ArrayAdapter.createFromResource(global, R.array.themesValues, R.layout.spinner_selected_item);
        adapterTheme.setDropDownViewResource(R.layout.spinner_item_dropdown);
        spinnerTheme.setAdapter(adapterTheme);
        spinnerTheme.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) global);

        ArrayAdapter<CharSequence> adapterStartDay = ArrayAdapter.createFromResource(global, R.array.dayValues, R.layout.spinner_selected_item);
        adapterStartDay.setDropDownViewResource(R.layout.spinner_item_dropdown);
        spinnerStartDay.setAdapter(adapterStartDay);
        spinnerStartDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerEndDay.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //no es necessari reescriure el mètode
            }
        });

        ArrayAdapter<CharSequence> adapterEndDay = ArrayAdapter.createFromResource(global, R.array.dayValues, R.layout.spinner_selected_item);
        adapterEndDay.setDropDownViewResource(R.layout.spinner_item_dropdown);
        spinnerEndDay.setAdapter(adapterEndDay);
        spinnerEndDay.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) global);

        doneButton.setOnClickListener(v -> {
            if (modify) {
                updateActivity();
            }
            else createActivity();
        });

        cancelButton.setOnClickListener(v -> activitySheet.dismiss());

        startTime.setOnClickListener(v -> {
            TimePickerDialog selectTime = new TimePickerDialog(
                    global,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    (view, hour, minute) -> {
                        startHour = hour;
                        startMinute = minute;
                        String time = startHour + ":" + startMinute;
                        SimpleDateFormat hformat = new SimpleDateFormat("HH:mm");
                        try {
                            Date date = hformat.parse(time);
                            startTime.setText(hformat.format(date));
                        } catch (ParseException e) {
                            System.out.println("Format de temps incorrecte");
                        }
                    }, 12, 0, true);
            selectTime.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            selectTime.updateTime(startHour, startMinute);
            selectTime.show();
        });

        endTime.setOnClickListener(v -> {
            TimePickerDialog selectTime = new TimePickerDialog(
                    global,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    (view, hour, minute) -> {
                        endHour = hour;
                        endMinute = minute;
                        String time = endHour + ":" + endMinute;
                        SimpleDateFormat hformat = new SimpleDateFormat("HH:mm");
                        try {
                            Date date = hformat.parse(time);
                            endTime.setText(hformat.format(date));
                        } catch (ParseException e) {
                            System.out.println("Format de temps incorrecte");
                        }
                    }, 12, 0, true);
            selectTime.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            selectTime.updateTime(endHour, endMinute);
            selectTime.show();
        });
        activitySheet.setContentView(sheetView);
        activitySheet.show();
    }

    /**
     * Funció per posar els valors a la pestanya de modificació d'activitat
     * @param id identificador de l'activitat
     * @param name nom de l'activitat
     * @param description descripció de l'activitat
     * @param theme tema de l'activitat
     * @param startDay dia d'inici de l'activitat
     * @param startTime temps d'inici de l'activitat
     * @param endTime temps de fi de l'activitat
     */
    public void fillActivitySheet(String id, String name, String description, String theme, String startDay, String startTime, String endTime) {
        this.id = id;
        this.sheetLabel.setText(R.string.modifytext);
        this.activityName.setText(name);
        this.activityDescr.setText(description);
        this.spinnerTheme.setSelection(findPositionInAdapterTheme(theme));
        this.spinnerStartDay.setSelection(findPositionInAdapterDay(startDay));
        //this.spinnerEndDay.setSelection(findPositionInAdapter(adapterEndDay, endDay));
        this.startTime.setText(startTime);
        this.endTime.setText(endTime);
    }

    /**
     * Funcio per saber la posicio del tema al spinner
     * @param element nom del tema
     * @return posicio del tema al spinner
     */
    private int findPositionInAdapterTheme(String element) {
       return DA.getPositionTheme(element);
    }

    /**
     * Funcio per saber la posició del dia al spinner
     * @param element nom del dia
     * @return posició del dia al spinner
     */
    private int findPositionInAdapterDay(String element) {
        return DA.getPositionDay(element);
    }
    
    /**
     * Funció necessària pel correcte funcionament dels spinners
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }

    /**
     * Funció necessària pel correcte funcionament dels spinners
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    /**
     * Funció per crear una nova activitat i afegir-la a la rutina que està sent editada
     */
    private void createActivity() {
        String name = activityName.getText().toString();
        String description = activityDescr.getText().toString();
        String theme = String.valueOf(spinnerTheme.getSelectedItemPosition());
        String startDay = String.valueOf(spinnerStartDay.getSelectedItemPosition());
        String endDay = String.valueOf(spinnerEndDay.getSelectedItemPosition());

        if (name.isEmpty()) activityName.setError(getString(R.string.notNull));
        else {
            try {
                DA.createActivity(name, description, theme, startDay, endDay, String.format("%02d", startHour), String.format("%02d", startMinute), String.format("%02d", endHour), String.format("%02d",endMinute));
                Toast.makeText(getContext(), getString(R.string.activityCreated), Toast.LENGTH_SHORT).show();
                activitySheet.dismiss();
            } catch (InvalidTimeIntervalException e) {
                Toast.makeText(getContext(), getString(R.string.errorTime), Toast.LENGTH_SHORT).show();
                startTime.setBackground(getContext().getResources().getDrawable(R.drawable.spinner_background_error));
                endTime.setBackground(getContext().getResources().getDrawable(R.drawable.spinner_background_error));
            } catch (InvalidDayIntervalException e) {
                Toast.makeText(getContext(), getString(R.string.errorDay), Toast.LENGTH_SHORT).show();
                spinnerStartDay.setBackground(getContext().getResources().getDrawable(R.drawable.spinner_background_error));
                spinnerEndDay.setBackground(getContext().getResources().getDrawable(R.drawable.spinner_background_error));
            } catch (OverlappingActivitiesException e) {
                Toast.makeText(getContext(), getString(R.string.overlapping), Toast.LENGTH_SHORT).show();
                startTime.setBackground(getContext().getResources().getDrawable(R.drawable.spinner_background_error));
                endTime.setBackground(getContext().getResources().getDrawable(R.drawable.spinner_background_error));
            }
        }
    }

    /**
     * Funció per modificar una activitat existent i afegir-la a la rutina que està sent editada
     */
    private void updateActivity() {
        String name = activityName.getText().toString();
        String description = activityDescr.getText().toString();
        String theme = String.valueOf(spinnerTheme.getSelectedItemPosition());
        String startDay = String.valueOf(spinnerStartDay.getSelectedItemPosition());
        String endDay = String.valueOf(spinnerEndDay.getSelectedItemPosition());

        String[] startTime = this.startTime.getText().toString().split(":");
        String[] endTime = this.endTime.getText().toString().split(":");
        String startHour = startTime[0];
        String startMinute = startTime[1];
        String endHour = endTime[0];
        String endMinute = endTime[1];

        try {
            DA.updateActivity(id, name, description, theme, startDay, endDay, startHour, startMinute, endHour, endMinute);
            Toast.makeText(getContext(), getString(R.string.activityModified), Toast.LENGTH_SHORT).show();
            activitySheet.dismiss();
        } catch (InvalidTimeIntervalException e) {
            Toast.makeText(getContext(), getString(R.string.errorTime), Toast.LENGTH_SHORT).show();
            this.startTime.setBackground(getContext().getResources().getDrawable(R.drawable.spinner_background_error));
            this.endTime.setBackground(getContext().getResources().getDrawable(R.drawable.spinner_background_error));
        } catch (InvalidDayIntervalException e) {
            Toast.makeText(getContext(), getString(R.string.errorDay), Toast.LENGTH_SHORT).show();
            spinnerStartDay.setBackground(getContext().getResources().getDrawable(R.drawable.spinner_background_error));
            spinnerEndDay.setBackground(getContext().getResources().getDrawable(R.drawable.spinner_background_error));
        } catch (OverlappingActivitiesException e) {
            Toast.makeText(getContext(), getString(R.string.overlapping), Toast.LENGTH_SHORT).show();
            this.startTime.setBackground(getContext().getResources().getDrawable(R.drawable.spinner_background_error));
            this.endTime.setBackground(getContext().getResources().getDrawable(R.drawable.spinner_background_error));
        }
    }

    /**
     * Funció per obtenir les activitats de dia de la rutina
     */
    public void getActivitiesByDay(String day) {
        try {
            DA.getActivitiesByDayToEdit(day, this);
        } catch (NoSuchMethodException ignored) { }
    }

    /**
     * Funció per inicialitzar l'element que mostra el llistat d'activitats
     * @param activitiesListCallback llistat d'activitats que retorna la BD
     */
    public void getActivitiesCallback(ArrayList<ArrayList<String>> activitiesListCallback) {
        activitiesList = new ArrayList<>(activitiesListCallback.size());
        activitiesList.addAll(activitiesListCallback);
        initRecyclerView();
    }

}