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

    private final DomainAdapterFactory DAF = DomainAdapterFactory.getInstance();
    private static RoutineEdit instance;

    private View view;
    private Context global;

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
    private String oldIniTime, oldEndTime;

    public RoutineEdit() {
        // Required empty public constructor
    }

    /**
     * Funció per crear la pestanya de creació d'activitat
     * Pre: ninguna
     * Post: s'ha creat la pestanya de creació d'activitat
     * */
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
                modifyActivity();
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
     * Pre: ninguna
     * Post: s'han posat els valors a la pestanya de modificació d'activitat
     * */
    public void fillActivitySheet(String name, String description, String theme, String startDay, String startTime, String endTime) {
        this.sheetLabel.setText(R.string.modifytext);
        this.activityName.setText(name);
        this.activityDescr.setText(description);
        this.spinnerTheme.setSelection(findPositionInAdapter(adapterTheme, theme));
        this.spinnerStartDay.setSelection(findPositionInAdapter(adapterStartDay, startDay));
        this.startTime.setText(startTime);
        //this.spinnerEndDay.setSelection(findPositionInAdapter(adapterEndDay, endDay));
        this.endTime.setText(endTime);
        oldIniTime = startTime;
        oldEndTime = endTime;
    }

    /**
     * Funció per buscar la posicio del string dins de l'adapter
     * Pre: ninguna
     * Post: retorna la posició del string dins l'adapter
     * */
    private int findPositionInAdapter(ArrayAdapter<CharSequence> adapter, String element) {
        for (int i = 0; i < adapter.getCount(); ++i) {
            if (adapter.getItem(i).toString().equals(element)) return i;
        }
        return 0;
    }

    /**
     * Funció necessària pel correcte funcionament dels spinners
     * */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }

    /**
     * Funció necessària pel correcte funcionament dels spinners
     * */
    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    /**
     * Funció per crear una nova activitat i afegir-la a la rutina que està sent editada
     * Pre: ninguna
     * Post: s'ha creat una nova activitat a la rutina
     * */
    private void createActivity() {

            String name = activityName.getText().toString();
            String descr = activityDescr.getText().toString();
            String theme = spinnerTheme.getSelectedItem().toString();
            String dayStart = spinnerStartDay.getSelectedItem().toString();
            String dayEnd = spinnerEndDay.getSelectedItem().toString();

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
     * Pre: ninguna
     * Post: s'ha modificat l'activitat a la rutina
     * */
    private void modifyActivity() {
        String name = activityName.getText().toString();
        String descr = activityDescr.getText().toString();
        String theme = String.valueOf(spinnerTheme.getSelectedItemPosition());
        String dayStart = String.valueOf(spinnerStartDay.getSelectedItemPosition());
        String dayEnd = String.valueOf(spinnerEndDay.getSelectedItemPosition());
        String[] iniTime = startTime.getText().toString().split(":");
        String[] finishTime = endTime.getText().toString().split(":");
        String initialHour = iniTime[0];
        String initialMinute= iniTime[1];
        String finishHour = finishTime[0];
        String finishMinute = finishTime[1];

        String[] oldiniTime = oldIniTime.split(":");
        String[] oldfinishTime = oldEndTime.split(":");
        String oldinitialHour = oldiniTime[0];
        String oldinitialMinute= oldiniTime[1];
        String oldfinishHour = oldfinishTime[0];
        String oldfinishMinute = oldfinishTime[1];

        try {
            DAF.updateActivity(name, descr, theme, oldinitialHour, oldinitialMinute, oldfinishHour, oldfinishMinute, initialHour, initialMinute, finishHour, finishMinute, dayStart, dayEnd);
        } catch (InvalidTimeIntervalException e) {
            Toast.makeText(getContext(), "Error: Start time cannot be subsequent to end time", Toast.LENGTH_SHORT).show();
        } catch (InvalidDayIntervalException e) {
            Toast.makeText(getContext(), "Error: Start day cannot be subsequent to end day", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Funció obtenir la isntància de la MainActivity actual
     * Pre: ninguna
     * Post: retorna la MainActivity
     * */
    public static RoutineEdit getInstance() {
        return instance;
    }

    /**
     * Funció per obtenir les activitats de dia de la rutina
     * Pre: ninguna
     * Post: s'obtenen les activitats del dia de la rutina
     * */
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
     * Pre: ninguna
     * Post: s'ha inicialitzat el recycler amb el seu adapter corresponent
     * */
    public void getActivitiesCallback(ArrayList<ArrayList<String>> activitiesListCallback) {
        //Convertir missatge a activities list

        activitiesList = new ArrayList<>(activitiesListCallback.size());

        activitiesList.addAll(activitiesListCallback);
        initRecyclerView();
    }

    /**
     * Funció per inicialitzar l'element que mostra el llistat d'activitats
     * Pre: ninguna
     * Post: s'ha inicialitzar el recycler amb el seu adapter corresponent
     * */
    private void initRecyclerView() {
        // llistat d'activitats
        RecyclerView recyclerView = view.findViewById(R.id.activityList);
        recyclerView.setLayoutManager((new LinearLayoutManager(global)));
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(activitiesList);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.routine_edit, container, false);
        super.onCreate(savedInstanceState);
        instance = this;
        global = this.getActivity();

        getActivities();
        //

        TextView addActivity = view.findViewById(R.id.addActivity);
        addActivity.setOnClickListener(v -> createActivitySheet(false));

        return view;
    }
}