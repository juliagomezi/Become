package com.pes.become;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Button addActivity;
    private BottomSheetDialog activitySheet;
    private Spinner spinnerTheme;
    private Spinner spinnerDay;
    private Button doneButton;
    private Button cancelButton;
    private EditText activityName;
    private EditText activityDescr;



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
        String act = activityName.getText().toString();
        String descr = activityDescr.getText().toString();
        String theme = spinnerTheme.getSelectedItem().toString();
        String day = spinnerDay.getSelectedItem().toString();

        if (act.isEmpty()) activityName.setError("This field cannot be null");
        else {
        //crear activitat

        }

    }
}