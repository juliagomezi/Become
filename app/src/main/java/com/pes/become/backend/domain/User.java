package com.pes.become.backend.domain;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collections;
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
    private Map<Theme, Map<Day, Double>> statisticsSelectedRoutine;
    /**
     * ID i nom de les rutines de l'usuari i un boolea indicant si la rutina esta publicada o no
     */
    private ArrayList<ArrayList<String>> routines;
    /**
     * Trofeus obtinguts per l'usuari
     */
    private final TreeMap<Achievement, Boolean> achievements;
    /**
     *
     */
    private ArrayList<Integer> calendarMonth;
    /**
     *
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
        clearStatistics();
        achievements = new TreeMap<>();
        for(int a = 0; a<Achievement.values().length; ++a){
            achievements.put(Achievement.values()[a], false);
        }
    }

    /**
     * Metode per inicialitzar el mes del calendari
     * @param daysMonth dies que te el mes
     */
    public void clearMonth(int daysMonth) {
        this.calendarMonth = new ArrayList<>(Collections.nCopies(daysMonth, -1));
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
    public ArrayList<ArrayList<Double>> getStatisticsSelectedRoutine() {
        ArrayList<ArrayList<Double>> result = new ArrayList<>();
        for(Map.Entry<Theme, Map<Day, Double>> themeEntry : statisticsSelectedRoutine.entrySet()){
            ArrayList<Double> hoursByDay = new ArrayList<>();
            for(Map.Entry<Day, Double> dayEntry : themeEntry.getValue().entrySet()){
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
    public double getHoursByTheme(Theme theme){
        Map<Day, Double> themeStats = statisticsSelectedRoutine.get(theme);
        double total = 0.0;
        for(Map.Entry<Day, Double> dayStats : themeStats.entrySet()){
            total += dayStats.getValue();
        }
        return total;
    }

    /**
     * Setter de les estadistiques de la rutina seleccionada
     * @param statisticsSelectedRoutine mapa que representa les estadistiques amb Tema com a clau i un segon mapa com a valor, amb Dia com a clau i les hores dedicades com a valor
     */
    public void setStatisticsSelectedRoutine(Map<Theme, Map<Day, Double>> statisticsSelectedRoutine) {
        this.statisticsSelectedRoutine = statisticsSelectedRoutine;
    }

    public void updateStatistics(Theme theme, Day day, int hours, int minutes, boolean add) {
        double time = (double)hours + (double)minutes/60.0;
        if(!add)
            time *= -1;
        time += statisticsSelectedRoutine.get(theme).get(day);
        statisticsSelectedRoutine.get(theme).put(day, time);
    }

    public void addAchievement(Achievement achievement) {
        achievements.put(achievement, true);
    }

    /**
     * Metode per obtenir tots els trofeus de l'usuari
     * @return mapa amb els trofeus de l'usuari on el trofeu es la key i el valor es cert si te el trofeu i fals si no
     */
    public TreeMap<Achievement, Boolean> getAllAchievementsStates() {
        return achievements;
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
     * Metode per comprovar si un usuari te una rutina amb el mateix nom
     * @param name nom de la rutina a comprovar
     * @return true si ja existeix, false altrament
     */
    public boolean hasRoutineWithName(String name){
        for(int i = 0; i < routines.size(); ++i) {
            if(routines.get(i).get(1).equals(name)) {
                return true;
            }
        }
        return false;
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
     * Metode per a compartir una rutina
     * @param id identificador de la rutina
     */
    public void shareRoutine(String id){
        for(int i = 0; i < routines.size(); ++i) {
            if(routines.get(i).get(0).equals(id)) {
                routines.get(i).set(2, "true");
                routines.get(i).set(3, "");
                break;
            }
        }
    }

    /**
     * Metode per a fer privada una rutina
     * @param id identificador de la rutina
     */
    public void unShareRoutine(String id){
        for(int i = 0; i < routines.size(); ++i) {
            if(routines.get(i).get(0).equals(id)) {
                routines.get(i).set(2, "false");
                routines.get(i).set(3, "");
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

    /**
     * Metode per a buidar les estadistiques
     */
    public void clearStatistics() {
        this.statisticsSelectedRoutine = new TreeMap<>();
        for(int t=0; t<Theme.values().length; ++t){
            Map<Day, Double> emptyMap = new TreeMap<>();
            for(int d=0; d<Day.values().length; ++d){
                emptyMap.put(Day.values()[d], 0.0);
            }
            statisticsSelectedRoutine.put(Theme.values()[t], emptyMap);
        }
    }

    /**
     * Metode per a carregar les dades d'un mes del calendari
     * @return un array amb el percentatge de completat de la rutina de cada dia
     */
    public ArrayList<Integer> getCalendarMonth() {
        return calendarMonth;
    }

    /**
     * Metode per a actualitzar les dades d'un dia del calendari
     * @param day dia a actualitzar
     * @param completion dades a introduir
     */
    public void setDayCalendar(int day, int completion) {
        calendarMonth.set(day, completion);
        for(int i = day-1; i >= 0; --i) {
            if(calendarMonth.get(i) != -1) {
                for(int j = i+1; j < day; ++j) {
                    calendarMonth.set(j,0);
                }
                return;
            }
        }
    }

    /**
     * Metode per obtenir la ratxa
     * @return nombre de dies de ratxa
     */
    public int getStreak() {
        return streak;
    }

    /**
     * Metode per a actualitzar la ratxa d'un usuari
     * @param streak dies de ratxa de l'usuari
     */
    public void setStreak(int streak) {
        this.streak = streak;
    }

    public boolean isRoutineShared(String routineID) {
        for(ArrayList<String> routine : routines) {
            if(routine.get(0).equals(routineID)) return routine.get(2).equals("true");
        }
        return false;
    }

}
