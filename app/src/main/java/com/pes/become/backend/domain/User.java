package com.pes.become.backend.domain;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

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
     * Estadistiques de la rutina seleccionada de l'usuari
     */
    private Map<Theme, Map<Day, Integer>> statisticsSelectedRoutine;
    /**
     * ID i nom de les rutines de l'usuari
     */
    private ArrayList<ArrayList<String>> routines;
    /**
     * Trofeus obtinguts per l'usuari
     */
    private Map<Achievement, Boolean> achievements;
    /**
     * Ratxa actual de l'usuari
     */
    private int streak;

    /**
     * Creadora de la classe per a un nou usuari
     * @param name nom de l'usuari
     */
    public User(String name) {
        this.name = name;
        this.selectedRoutine = null;
        this.routines = new ArrayList<>();
        this.statisticsSelectedRoutine = new TreeMap<>();
        for(int t=0; t<Theme.values().length; ++t){
            Map<Day, Integer> emptyMap = new TreeMap<>();
            for(int d=0; d<Day.values().length; ++d){
                emptyMap.put(Day.values()[d], 0);
            }
            statisticsSelectedRoutine.put(Theme.values()[t], emptyMap);
        }
        achievements = new TreeMap<>();
        for(int a = 0; a<Achievement.values().length; ++a){
            achievements.put(Achievement.values()[a], false);
        }
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
     * Getter de les estadistiques de la rutina seleccionada
     * @return Array d'Arrays d'enters on la primera array representa els temes, la segona els dies i la tercera les hores
     */
    public ArrayList<ArrayList<Integer>> getStatisticsSelectedRoutine() {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        for(Map.Entry<Theme, Map<Day, Integer>> themeEntry : statisticsSelectedRoutine.entrySet()){
            ArrayList<Integer> hoursByDay = new ArrayList<>();
            for(Map.Entry<Day, Integer> dayEntry : themeEntry.getValue().entrySet()){
                hoursByDay.add(dayEntry.getKey().ordinal(), dayEntry.getValue());
            }
            result.add(themeEntry.getKey().ordinal(), hoursByDay);
        }
        return result;
    }

    /**
     * Metode per obtenir totes les hores que hi han dedicades a un tema en la rutina seleccionada
     * @param theme tema del que es volen les hores
     * @return hores setmanals dedicades al tema
     */
    public int getHoursByTheme(Theme theme){
        Map<Day, Integer> themeStats = statisticsSelectedRoutine.get(theme);
        int total = 0;
        for(Map.Entry<Day, Integer> dayStats : themeStats.entrySet()){
            total += dayStats.getValue();
        }
        return total;
    }

    /**
     * Setter de les estadistiques de la rutina seleccionada
     * @param statisticsSelectedRoutine mapa que representa les estadistiques amb Tema com a clau i un segon mapa com a valor, amb Dia com a clau i les hores dedicades com a valor
     */
    public void setStatisticsSelectedRoutine(Map<Theme, Map<Day, Integer>> statisticsSelectedRoutine) {
        this.statisticsSelectedRoutine = statisticsSelectedRoutine;
    }

    /**
     * Metode per obtenir tots els trofeus de l'usuari
     * @return mapa amb els trofeus de l'usuari on el trofeu es la key i el valor es cert si te el trofeu i fals si no
     */
    public Map<Achievement, Boolean> getAllAchievementsStates() {
        return achievements;
    }

    /**
     * Metode per saber l'estat d'un trofeu de l'usuari
     * @param achievement trofeu del que es vol l'estat
     * @return cert si te el trofeu, fals si no
     */
    public boolean getAchievementState(Achievement achievement){
        return achievements.get(achievement);
    }

    /**
     * Metode perque l'usuari obtingui un trofeu
     * @param achievement trofeu a obtenir
     * @return cert si no tenia el trofeu i per tant l'ha guanyat, fals si ja el tenia
     */
    public boolean gainAchievement(Achievement achievement){
        if(!achievements.get(achievement)){
            achievements.put(achievement, true);
            return true;
        }
        return false;
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
        routines.addAll(routinesInfo);
    }

    /**
     * Metode per eliminar una rutina de la llista de rutines de l'usuari
     * @param id ID de la rutina a eliminar
     */
    public void changeRoutineName(String id, String name){
        for(int i = 0; i < routines.size(); ++i) {
            if(routines.get(i).get(0).equals(id)) {
                routines.get(i).set(1,name);
                break;
            }
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
        for(int i = 0; i < routines.size(); ++i) {
            if(routines.get(i).get(0).equals(id)) {
                routines.remove(i);
                break;
            }
        }
    }

    /**
     * Metode per saber si l'usuari ja te una rutina amb aquest nom existent
     * @param name nom a comprovar si ja exiteix
     * @return true si existeix, false altrament
     */
    public boolean existsRoutine(String name) {
        for(ArrayList<String> routine : routines) {
            if(routine.get(1).equals(name)) return true;
        }
        return false;
    }
}
