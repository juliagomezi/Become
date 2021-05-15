package com.pes.become.frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pes.become.R;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static MainActivity instance;

    /**
     * Funció obtenir la instancia de la MainActivity actual
     * */
    public static MainActivity getInstance() { return instance; }

    /**
     * Funcio del MainActivity que s'executa al crear-lo
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        instance=this;

        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);
        navigation.setSelectedItemId(R.id.homeView);
        navigation.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction()
               .replace(R.id.fragment_layout, new RoutineView()).commit(); //aquí es posa el fragment que vols que es vegi quan s'encén l'aplicació

    }

    /**
     * Listener del navigation view
     */
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment selectedFragment;
        switch (item.getItemId()) {
            case R.id.homeView:
                selectedFragment = new RoutineView();
                break;
            case R.id.communityView:
                selectedFragment = new RoutineView();
                break;
            case R.id.profileView:
                selectedFragment = new Profile();
                break;
            default:
                selectedFragment = new RoutineView();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout, selectedFragment).commit();
        return true;
    };

    /**
     * Funció necessària pel correcte funcionament de l'aplicació
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }

    /**
     * Funció necessària pel correcte funcionament del l'aplicació
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    public void setEditRoutineScreen(String id, String routineName) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout, new RoutineEdit(id, routineName)).commit();
    }

    public void setProfileScreen() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout, new Profile()).commit();
    }

    public void setTrophiesScreen() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout, new Trophies()).commit();
    }

    /**
     * Metode que mostra el popup per notificar que s'ha guanyat un trofeu
     * @param text missatge o nom del trofeu guanyat que es mostra al popup
     */
    public void showTrophyWon(String text) {
        Dialog myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.popup);

        TextView OK = myDialog.findViewById(R.id.OKButton);
        OK.setOnClickListener(v -> myDialog.dismiss());

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    /**
     * Metode que sobreescriu el que es vol fer amb la tecla de "back"
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}