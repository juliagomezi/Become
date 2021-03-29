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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapterFactory;
import com.pes.become.backend.exceptions.InvalidDayIntervalException;
import com.pes.become.backend.exceptions.InvalidTimeIntervalException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RoutineEdit extends Fragment implements AdapterView.OnItemSelectedListener{

    private static RoutineEdit instance;

    private View view;
    private Context global;

    private final DomainAdapterFactory DAF = DomainAdapterFactory.getInstance();

    private ArrayList<ArrayList<String>> activitiesList;

    // desplegable nova activitat
    private ArrayAdapter<CharSequence> adapterTheme;
    private ArrayAdapter<CharSequence> adapterStartDay;
    private BottomSheetDialog activitySheet;
    private Spinner spinnerTheme, spinnerStartDay, spinnerEndDay;
    private EditText activityName, activityDescr;
    private TextView startTime;
    private TextView endTime;
    private TextView sheetLabel;
    private int startHour, startMinute, endHour, endMinute;
    private String oldStartTime, oldEndTime;

    public RoutineEdit() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.routine_edit, container, false);
        super.onCreate(savedInstanceState);
        instance = this;
        global = this.getActivity();

        getActivities();

        TextView addActivity = view.findViewById(R.id.addActivity);
        addActivity.setOnClickListener(v -> createActivitySheet(false));

        return view;
    }

    /**
     * Funció obtenir la isntància de la MainActivity actual
     * */
    public static RoutineEdit getInstance() {
        return instance;
    }

    /**
     * Funció per inicialitzar l'element que mostra el llistat d'activitats
     */
    private void initRecyclerView() {
        // llistat d'activitats
        RecyclerView recyclerView = view.findViewById(R.id.activityList);
        recyclerView.setLayoutManager((new LinearLayoutManager(global)));
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(activitiesList);
        recyclerView.setAdapter(recyclerAdapter);
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

        adapterTheme = ArrayAdapter.createFromResource(global, R.array.themesValues, R.layout.spinner_selected_item);
        adapterTheme.setDropDownViewResource(R.layout.spinner_item_dropdown);
        spinnerTheme.setAdapter(adapterTheme);
        spinnerTheme.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) global);

        adapterStartDay = ArrayAdapter.createFromResource(global, R.array.dayValues, R.layout.spinner_selected_item);
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
            activitySheet.dismiss();
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
     * @param name nom de l'activitat
     * @param description descripció de l'activitat
     * @param theme tema de l'activitat
     * @param startDay dia d'inici de l'activitat
     * @param startTime temps d'inici de l'activitat
     * @param endTime temps de fi de l'activitat
     */
    public void fillActivitySheet(String name, String description, String theme, String startDay, String startTime, String endTime) {
        this.sheetLabel.setText(R.string.modifytext);
        this.activityName.setText(name);
        this.activityDescr.setText(description);
        this.spinnerTheme.setSelection(findPositionInAdapterTheme(theme));
        this.spinnerStartDay.setSelection(findPositionInAdapterDay(startDay));
        //this.spinnerEndDay.setSelection(findPositionInAdapter(adapterEndDay, endDay));
        this.startTime.setText(startTime);
        this.endTime.setText(endTime);
        this.oldStartTime = startTime;
        this.oldEndTime = endTime;
    }

    /**
     * Funcio per saber la posicio del tema al spinner
     * @param element nom del tema
     * @return posicio del tema al spinner
     */
    private int findPositionInAdapterTheme(String element) {
       return DAF.getPositionTheme(element);
    }

    /**
     * Funcio per saber la posició del dia al spinner
     * @param element nom del dia
     * @return posició del dia al spinner
     */
    private int findPositionInAdapterDay(String element) {
        return DAF.getPositionDay(element);
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
        String descr = activityDescr.getText().toString();
        String theme = String.valueOf(spinnerTheme.getSelectedItemPosition());
        String dayStart = String.valueOf(spinnerStartDay.getSelectedItemPosition());
        String dayEnd = String.valueOf(spinnerEndDay.getSelectedItemPosition());

        if (name.isEmpty()) activityName.setError("This field cannot be null");
        else {
            try {
                DAF.createActivity(name, descr, theme, String.format("%02d", startHour), String.format("%02d", startMinute), String.format("%02d", endHour), String.format("%02d",endMinute), dayStart, dayEnd);
                Toast.makeText(getContext(), "Activity created", Toast.LENGTH_SHORT).show();
            } catch (InvalidTimeIntervalException e) {
                Toast.makeText(getContext(), "Error: Start time cannot be subsequent to end time", Toast.LENGTH_SHORT).show();
                startTime.setBackground(getContext().getResources().getDrawable(R.drawable.spinner_background_error));
                endTime.setBackground(getContext().getResources().getDrawable(R.drawable.spinner_background_error));

            } catch (InvalidDayIntervalException e) {
                Toast.makeText(getContext(), "Error: Start day cannot be subsequent to end day", Toast.LENGTH_SHORT).show();
                spinnerStartDay.setBackground(getContext().getResources().getDrawable(R.drawable.spinner_background_error));
                spinnerEndDay.setBackground(getContext().getResources().getDrawable(R.drawable.spinner_background_error));
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

        String[] oldStartTime = this.oldStartTime.split(":");
        String[] oldEndTime = this.oldEndTime.split(":");
        String oldStartHour = oldStartTime[0];
        String olStartMinute = oldStartTime[1];
        String oldEndHour = oldEndTime[0];
        String oldEndMinute = oldEndTime[1];

        try {
            DAF.updateActivity(name, description, theme, oldStartHour, olStartMinute, oldEndHour, oldEndMinute, startHour, startMinute, endHour, endMinute, startDay, endDay);
        } catch (InvalidTimeIntervalException e) {
            Toast.makeText(getContext(), "Error: Start time cannot be subsequent to end time", Toast.LENGTH_SHORT).show();
        } catch (InvalidDayIntervalException e) {
            Toast.makeText(getContext(), "Error: Start day cannot be subsequent to end day", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Funció per obtenir les activitats de dia de la rutina
     */
    public void getActivities() {
        /*
        int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        String day = "";
        switch (today) {
            case Calendar.MONDAY:
                day = "Monday";
                break;
            case Calendar.TUESDAY:
                day = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                day = "Wednesday";
                break;
            case Calendar.THURSDAY:
                day = "Thursday";
                break;
            case Calendar.FRIDAY:
                day = "Friday";
                break;
            case Calendar.SATURDAY:
                day = "Saturday";
                break;
            case Calendar.SUNDAY:
                day = "Sunday";
                break;
        }*/

        activitiesList = new ArrayList<>(); //temporal
        try {
            DAF.getActivitiesFromDB("Monday", this);
        } catch (NoSuchMethodException e) {
            Log.d("METHODEXCEPTION", e.getMessage());
        }
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