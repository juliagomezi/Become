package com.pes.become.backend.domain;

import java.util.ArrayList;

public class User {

    /**
     * Correu de l'usuari
     */
    private String mail;

    /**
     * Nom de l'usuari
     */
    private String name;

    /**
     * ID de la rutina activada per l'usuari
     */
    private String selectedRoutine;

    /**
     * IDs de les rutines de l'usuari
     */
    private ArrayList<String> routines;

    /**
     * Creadora per defecte d'un usuari
     */
    public User(String mail, String name, String selectedRoutine, ArrayList<String> routines) {
        this.mail = mail;
        this.name = name;
        this.selectedRoutine = selectedRoutine;
        this.routines.addAll(routines);
    }

}
