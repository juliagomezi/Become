package com.pes.become;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Button addActivity, doneButton, cancelButton;
    private BottomSheetDialog activitySheet;
    private Spinner spinnerTheme, spinnerDay;
    private EditText activityName, activityDescr;
    private TextView startTime, endTime;
    private int startHour, startMinute, endHour, endMinute;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addActivity= findViewById(R.id.addActivity);

        addActivity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                activitySheet = new BottomSheetDialog(MainActivity.this,R.style.BottomSheetTheme);
                View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_layout, findViewById(R.id.bottom_sheet));

                doneButton = sheetView.findViewById(R.id.doneButton);
                cancelButton = sheetView.findViewById(R.id.cancelButton);
                spinnerTheme = sheetView.findViewById(R.id.themeSpinner);
                spinnerDay = sheetView.findViewById(R.id.daySpinner);
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
                spinnerDay.setAdapter(adapter);
                spinnerDay.setOnItemSelectedListener(MainActivity.this);


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
        });




    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /*
    * Funció per crear una nova activitat i afegir-la a la rutina que està sent editada
    * Pre: ninguna
    * Post: s'ha creat una nova activitat a la rutuina
    * */
    private void createActivity() {
        String name = activityName.getText().toString();
        String descr = activityDescr.getText().toString();
        String theme = spinnerTheme.getSelectedItem().toString();
        String day = spinnerDay.getSelectedItem().toString();

        if (name.isEmpty()) activityName.setError("This field cannot be null");
        else {
        /*
            crear activitat
            adapter.createActivity(name ,descr, theme, day, startHour, startMinute, endHour, endMinute);
         */

        }

    }
}