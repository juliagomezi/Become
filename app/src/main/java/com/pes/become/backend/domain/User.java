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
    /**
     * Foto de perfil de l'usuari
     */
    private Bitmap profilePic;
    /**
     * Instancia de la rutina activada per l'usuari
     */
    private Routine selectedRoutine;
    /**
     * ID i nom de les rutines de l'usuari
     */
    private ArrayList<ArrayList<String>> routines;

    /**
     * Creadora de la classe per a un nou usuari
     * @param name nom de l'usuari
     */
    public User(String name) {
        this.name = name;
        this.selectedRoutine = null;
        this.routines = new ArrayList<>();
    }

    /**
     * Getter de l'identificador de l'usuari
     * @return identificador de l'usuari
     */
    public String getID() {
        return id;
    }

    /**
     * Getter del nom de l'usuari
     * @return el nom de l'usuari
     */
    public String getName(){
        return name;
    }

    /**
     * Setter del id de l'usuari
     * @param id identificador de l'usuari
     */
    public void setID(String id){
        this.id = id;
    }

    /**
     * Setter del nom de l'usuari
     * @param name nom de l'usuari
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Setter de la foto de perfil de l'usuari
     * @param pfp foto de perfil de l'usuari
     */
    public void setPFP(Bitmap pfp) {
        this.profilePic = pfp;
    }

    /**
     * Getter de la foto de perfil de l'usuari
     * @return la foto de perfil de l'usuari
     */
    public Bitmap getProfilePic() {
        return profilePic;
    }

    /**
     * Getter de la rutina seleccionada
     * @return la rutina seleccionada
     */
    public Routine getSelectedRoutine(){
        return selectedRoutine;
    }

    /**
     * Setter de la rutina seleccionada
     * @param selectedRoutine rutina seleccionada
     */
    public void setSelectedRoutine(Routine selectedRoutine){
        this.selectedRoutine = selectedRoutine;
    }

    /**
     * Getter de les rutines
     * @return les rutines de l'usuari
     */
    public ArrayList<ArrayList<String>> getRoutines() {
        return routines;
    }

    /**
     * Setter de les rutines de l'usuari
     * @param routinesInfo identificador i nom de les rutines de l'usuari
     */
    public void setRoutines(ArrayList<ArrayList<String>> routinesInfo) {
        this.routines = new ArrayList<>();
        for(ArrayList<String> r : routinesInfo) {
            routines.add(r);
        }
    }

    /**
     * Metode per afegir una rutina a la llista de rutines de l'usuari
     * @param r rutina a afegir
     */
    public void addRoutine(ArrayList<String> r) {
        routines.add(0, r);
    }

    /**
     * Metode per eliminar una rutina de la llista de rutines de l'usuari
     * @param id ID de la rutina a eliminar
     */
    public void deleteRoutine(String id){
        routines.remove(id);
    }
}
