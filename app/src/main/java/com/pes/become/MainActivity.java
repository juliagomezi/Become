package com.pes.become;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.pes.become.backend.persistence.ControllerPersistence;
import com.pes.become.backend.persistence.ControllerRoutineDB;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ControllerPersistence ctrler = new ControllerPersistence();
        //ctrler.createActivity("RutinaDeProva","activitatProva", "esports", "descsr","dia","01:00","02:00");
        ctrler.getActivitiesByDay("RutinaDeProva","dia");
    }
}