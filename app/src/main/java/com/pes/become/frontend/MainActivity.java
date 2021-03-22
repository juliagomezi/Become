package com.pes.become.frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pes.become.R;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static MainActivity instance;

    // llistat d'activitats
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private List<ActivityDummy> activitiesList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout, new edit_routine()).commit(); //aquí es posa el fragment que vols que es vegi quan s'encén l'aplicació
    }

    //Listener navigation view
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.homeView:
                            selectedFragment = new edit_routine(); //aixo haura de ser el fragment del home
                            break;

                        case R.id.communityView:
                            selectedFragment = new edit_routine(); //aixo haura de ser el fragment de community
                            break;

                        case R.id.profileView:
                            selectedFragment = new edit_routine(); //aixo haura de ser el fragment del perfil
                            break;
                    }

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_layout, selectedFragment).commit();
                    return true;
                }
            };
    /**
     * Funció necessària pel correcte funcionament de l'aplicació
     * */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }

    /**
     * Funció necessària pel correcte funcionament del l'aplicació
     * */
    @Override
    public void onNothingSelected(AdapterView<?> parent) { }



}