package com.pes.become.backend.adapters;

import com.pes.become.backend.domain.Activity;
import com.pes.become.backend.domain.Day;
import com.pes.become.backend.domain.Theme;
import com.pes.become.backend.exceptions.InvalidDayIntervalException;
import com.pes.become.backend.exceptions.InvalidTimeException;
import com.pes.become.backend.exceptions.InvalidTimeIntervalException;

/**
 * Classe encarregada de gestionar l'acces a la classe Routine
 */
public class ActivityAdapter {

    /**
     * Unica instancia de la classe
     */
    private static ActivityAdapter instance;

    /**
     * Metode per obtenir la instancia de la classe
     * @return instancia
     */
    public static ActivityAdapter getInstance(){
        if(instance == null){
            instance = new ActivityAdapter();
        }
        return instance;
    }

    /**
     * Metode per crear una activitat
     * @param name nom de l'activitat
     * @param description descripcio de l'activitat
     * @param themeString tema de l'activitat
     * @param iniH hora d'inici de l'activitat
     * @param iniM minut d'inici de l'activitat
     * @param endH hora de fi de l'activitat
     * @param endM minut de fi de l'activitat
     * @param day dia de l'activitat
     * @throws InvalidTimeIntervalException es llença si el temps d'inici no es anterior al temps de fi
     * @throws InvalidTimeException es llença si les hores o minuts no tenen format valid
     */
    public Activity createActivity(String name, String description, String themeString, int iniH, int iniM, int endH, int endM, Day day) throws InvalidTimeIntervalException, InvalidTimeException {
        Theme theme = new Theme(themeString);
        return new Activity(name, description, theme, iniH, iniM, endH, endM, day);
    }
}
