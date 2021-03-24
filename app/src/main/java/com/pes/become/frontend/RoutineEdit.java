package com.pes.become.frontend;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapterFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RoutineEdit extends Fragment implements AdapterView.OnItemSelectedListener{

    private DomainAdapterFactory DAF = DomainAdapterFactory.getInstance();
    private static RoutineEdit instance;
    private View view;
    private Context global;

    // llistat d'activitats
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private ArrayList<ArrayList<String>> activitiesList;
    private CardView cardActivityDisplay;

    // desplegable nova activitat
    private ArrayAdapter<CharSequence> adapterTheme;
    private ArrayAdapter<CharSequence> adapterStartDay;
    private ArrayAdapter<CharSequence> adapterEndDay;
    private Button doneButton, cancelButton;
    private BottomSheetDialog activitySheet;
    private Spinner spinnerTheme, spinnerStartDay, spinnerEndDay;
    private EditText activityName, activityDescr;
    private TextView startTime, endTime, addActivity, sheetLabel;
    private int startHour, startMinute, endHour, endMinute;

    public RoutineEdit() {
        // Required empty public constructor
    }

    /*
    // Per provar, després ho borraré
    private void initData() {
        activitiesList = new ArrayList<>();
        activitiesList.add(new ActivityDummy("Have Breakfast", "nyamnyam", "Cooking", "Monday", "08:00", "Monday", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Entertainment", "Monday", "08:00","Monday",  "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Music", "Monday", "08:00", "Monday", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Other", "Monday", "08:00", "Monday", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Sleeping", "Monday", "08:00", "Monday", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Sport", "Monday", "08:00", "Monday", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Studying", "Monday", "08:00", "Monday", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Working", "Monday", "08:00", "Monday", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Sleeping", "Monday", "08:00", "Monday", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Sleeping", "Monday", "08:00", "Monday", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Sleeping", "Monday", "08:00", "Monday", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Sleeping", "Monday", "08:00", "Monday", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Sleeping", "Monday", "08:00", "Monday", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Sleeping", "Monday", "08:00", "Monday", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Sleeping", "Monday", "08:00", "Monday",  "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Sleeping", "Monday", "08:00", "Monday", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Sleeping", "Monday", "08:00", "Monday", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Sleeping", "Monday", "08:00", "Monday", "08:15"));
    }

     */


    /**
     * Funció per crear la pestanya de creació d'activitat
     * Pre: ninguna
     * Post: s'ha creat la pestanya de creació d'activitat
     * */
    public void createActivitySheet(boolean modify) {
        activitySheet = new BottomSheetDialog(global,R.style.BottomSheetTheme);
        View sheetView = LayoutInflater.from(getContext()).inflate(R.layout.activity_edit, view.findViewById(R.id.bottom_sheet));

        doneButton = sheetView.findViewById(R.id.doneButton);
        cancelButton = sheetView.findViewById(R.id.cancelButton);
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

            }
        });

        adapterEndDay = ArrayAdapter.createFromResource(global, R.array.dayValues, R.layout.spinner_selected_item);
        adapterEndDay.setDropDownViewResource(R.layout.spinner_item_dropdown);
        spinnerEndDay.setAdapter(adapterEndDay);
        spinnerEndDay.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) global);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modify) modifyActivity();
                else createActivity();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activitySheet.dismiss();
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog selectTime = new TimePickerDialog(
                        global,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hour, int minute) {
                                startHour = hour;
                                startMinute = minute;
                                String time = startHour + ":" + startMinute;
                                SimpleDateFormat hformat = new SimpleDateFormat("HH:mm");
                                try {
                                    Date date = hformat.parse(time);
                                    startTime.setText(hformat.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 12, 0, true);
                selectTime.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                selectTime.updateTime(startHour, startMinute);
                selectTime.show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog selectTime = new TimePickerDialog(
                        global,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hour, int minute) {
                                endHour = hour;
                                endMinute = minute;
                                String time = endHour + ":" + endMinute;
                                SimpleDateFormat hformat = new SimpleDateFormat("HH:mm");
                                try {
                                    Date date = hformat.parse(time);
                                    endTime.setText(hformat.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 12, 0, true);
                selectTime.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                selectTime.updateTime(endHour, endMinute);
                selectTime.show();
            }
        });
        activitySheet.setContentView(sheetView);
        activitySheet.show();
    }

    /**
     * Funció per posar els valors a la pestanya de creació d'activitat
     * Pre: ninguna
     * Post: s'han posat els valors a la pestanya de creació d'activitat
     * */
    public void fillActivitySheet(String name, String description, String theme, String startDay, String startTime, String endTime) {
        this.sheetLabel.setText("Modify activity");
        this.activityName.setText(name);
        this.activityDescr.setText(description);
        this.spinnerTheme.setSelection(findPositionInAdapter(adapterTheme, theme));
        this.spinnerStartDay.setSelection(findPositionInAdapter(adapterStartDay, startDay));
        this.startTime.setText(startTime);
        //this.spinnerEndDay.setSelection(findPositionInAdapter(adapterEndDay, endDay));
        this.endTime.setText(endTime);
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
        /*
            crear activitat
            adapter.createActivity(name ,descr, theme, dayStart, startHour, startMinute, dayEnd, endHour, endMinute);
         */

        }

    }

    /**
     * Funció per modificar una activitat existent i afegir-la a la rutina que està sent editada
     * Pre: ninguna
     * Post: s'ha modificat l'activitat a la rutina
     * */
    private void modifyActivity() {

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
        activitiesList = DAF.getActivitiesByDay("Monday");
    }

    /**
     * Funció per inicialitzar l'element que mostra el llistat d'activitats
     * Pre: ninguna
     * Post: s'ha inicialitzar el recycler amb el seu adapter corresponent
     * */
    private void initRecyclerView() {
        recyclerView = view.findViewById(R.id.activityList);
        recyclerView.setLayoutManager((new LinearLayoutManager(global)));
        recyclerAdapter = new RecyclerAdapter(activitiesList);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.routine_edit, container, false);
        super.onCreate(savedInstanceState);
        this.instance = this;
        global = this.getActivity();

        getActivities();
        initRecyclerView();

        addActivity = view.findViewById(R.id.addActivity);
        addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createActivitySheet(false);
            }
        });

        return view;
    }
}