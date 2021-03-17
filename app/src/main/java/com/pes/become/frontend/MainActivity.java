package com.pes.become.frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pes.become.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // llistat d'activiatats
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private List<ActivityDummy> activitiesList;

    // desplegable nova activitat
    private Button addActivity, doneButton, cancelButton;
    private BottomSheetDialog activitySheet;
    private Spinner spinnerTheme, spinnerStartDay, spinnerEndDay;
    private EditText activityName, activityDescr;
    private TextView startTime, endTime;
    private int startHour, startMinute, endHour, endMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initRecyclerView();

        addActivity = findViewById(R.id.addActivity);
        addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createActivitySheet();
            }
        });
    }

    /*
     * Funció per inicialitzar l'elelemnt que mostra el llistat d'activitats
     * Pre: ninguna (ha d'existir minim una activitat)??????????????????????
     * Post: s'ha inicialitzar el recycler amb el seu adapter corresponent
     * */
    private void initRecyclerView() {
        recyclerView = findViewById(R.id.activityList);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
        recyclerAdapter = new RecyclerAdapter(activitiesList);
        recyclerView.setAdapter(recyclerAdapter);
    }

    // Per provar, després ho borraré
    private void initData() {
        activitiesList = new ArrayList<>();
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Menjar", "Dilluns", "08:00", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Menjar", "Dilluns", "08:00", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Menjar", "Dilluns", "08:00", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Menjar", "Dilluns", "08:00", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Menjar", "Dilluns", "08:00", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Menjar", "Dilluns", "08:00", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Menjar", "Dilluns", "08:00", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Menjar", "Dilluns", "08:00", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Menjar", "Dilluns", "08:00", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Menjar", "Dilluns", "08:00", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Menjar", "Dilluns", "08:00", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Menjar", "Dilluns", "08:00", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Menjar", "Dilluns", "08:00", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Menjar", "Dilluns", "08:00", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Menjar", "Dilluns", "08:00", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Menjar", "Dilluns", "08:00", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Menjar", "Dilluns", "08:00", "08:15"));
        activitiesList.add(new ActivityDummy("Esmorzar", "nyamnyam", "Menjar", "Dilluns", "08:00", "08:15"));
    }

    /*
    * Funció per crear la pestanya de creació d'activitat
    * Pre: ninguna
    * Post: s'ha creat lapestanya de creació d'activitat
    * */
    private void createActivitySheet() {
        activitySheet = new BottomSheetDialog(MainActivity.this,R.style.BottomSheetTheme);
        View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_layout, findViewById(R.id.bottom_sheet));

        doneButton = sheetView.findViewById(R.id.doneButton);
        cancelButton = sheetView.findViewById(R.id.cancelButton);
        spinnerTheme = sheetView.findViewById(R.id.themeSpinner);
        spinnerStartDay = sheetView.findViewById(R.id.dayStartSpinner);
        spinnerEndDay = sheetView.findViewById(R.id.dayEndSpinner);
        activityName = sheetView.findViewById(R.id.nameText);
        activityDescr = sheetView.findViewById(R.id.descriptionText);
        startTime = sheetView.findViewById(R.id.startTime);
        endTime = sheetView.findViewById(R.id.endTime);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.themesValues, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTheme.setAdapter(adapter);
        spinnerTheme.setOnItemSelectedListener(MainActivity.this);

        adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.dayValues, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStartDay.setAdapter(adapter);
        spinnerStartDay.setOnItemSelectedListener(MainActivity.this);

        adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.dayValues, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEndDay.setAdapter(adapter);
        spinnerEndDay.setOnItemSelectedListener(MainActivity.this);


        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createActivity();
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
                    MainActivity.this,
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
                        MainActivity.this,
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    /*
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

}