package com.pes.become.frontend;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pes.become.R;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction()
               .replace(R.id.fragment_layout, new RoutineEdit()).commit(); //aquí es posa el fragment que vols que es vegi quan s'encén l'aplicació
    }

    //Listener navigation view
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.homeView:
                selectedFragment = new RoutineEdit(); //aixo haura de ser el fragment del home
                break;

            case R.id.communityView:
                selectedFragment = new RoutineEdit(); //aixo haura de ser el fragment de community
                break;

            case R.id.profileView:
                selectedFragment = new RoutineEdit(); //aixo haura de ser el fragment del perfil
                break;
            default:
                selectedFragment = new RoutineEdit(); //aixo haura de ser el fragment de de la pantalla incial
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout, selectedFragment).commit();
        return true;
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