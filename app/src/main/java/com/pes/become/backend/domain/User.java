package com.pes.become.backend.domain;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Classe que defineix els usuaris de l'aplicacio
 */
public class User {
    /**
     * ID de l'usuari
     */
    private String id;
    /**
     * Nom de l'usuari
     */
    private String name;
    private Bitmap profilePic;
    /**
     * Instancia de la rutina activada per l'usuari
     */
    private Routine selectedRoutine;
    /**
     * IDs de les rutines de l'usuari
     */
    private ArrayList<String> routines;

    /**
     * Creadora de la classe per a un nou usuari
     * @param name
     */
    public User(String name){
        this.name = name;
        this.selectedRoutine = null;
        this.routines = new ArrayList<>();
    }

    /**
     *
     * @return
     */
    public String getID() {
        return id;
    }

    /**
     *
     * @return
     */
    public String getName(){
        return name;
    }

    /**
     * Setter del id de l'usuari
     * @param id
     */
    public void setID(String id){
        this.id = id;
    }

    /**
     * Setter del nom de l'usuari
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     *
     * @param pfp
     */
    public void setPFP(Bitmap pfp) {
        this.profilePic = pfp;
    }

    public Bitmap getProfilePic() {
        return profilePic;
    }

    /**
     * Getter de la rutina seleccionada
     * @return
     */
    public Routine getSelectedRoutine(){
        return selectedRoutine;
    }

    /**
     * Setter de la rutina seleccionada
     * @param selectedRoutine
     */
    public void setSelectedRoutine(Routine selectedRoutine){
        this.selectedRoutine = selectedRoutine;
    }

    /**
     * Getter de les rutines
     * @return
     */
    public ArrayList<String> getRoutines() {
        return routines;
    }

    /**
     * Metode per afegir una rutina a la llista de rutines de l'usuari
     * @param id ID de la rutina a afegir
     */
    public void addRoutine(String id) {
        routines.add(id);
    }

    /**
     * Metode per eliminar una rutina de la llista de rutines de l'usuari
     * @param id ID de la rutina a eliminar
     */
    public void deleteRoutine(String id){
        routines.remove(id);
    }
}
