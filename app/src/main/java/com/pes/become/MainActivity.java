package com.pes.become;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.service.controls.Control;
import android.util.Log;

import com.pes.become.backend.exceptions.InvalidTimeException;
import com.pes.become.backend.exceptions.OverlappingActivitiesException;
import com.pes.become.backend.persistence.ControllerPersistence;
import com.pes.become.backend.persistence.ControllerRoutineDB;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ControllerPersistence c = new ControllerPersistence();

        /*try {
            c.getActivitiesByDay("RutinaDeProva", "dia");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        /* Crear activitat prova
        try {
            c.createActivity("RutinaDeProva", "activitat2", "esports", "descr", "dia", "08:00", "09:00");
        } catch (OverlappingActivitiesException e) {
            e.printStackTrace();
        } catch (InvalidTimeException e) {
            e.printStackTrace();
        }
        */
    }
}