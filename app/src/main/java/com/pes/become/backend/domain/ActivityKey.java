package com.pes.become.backend.domain;

/**
 * Tupla amb els atributs que formen la clau primaria d'una activitat
 */
public class ActivityKey implements Comparable<ActivityKey> {
    /**
     * Nom de l'activitat
     */
    public String name;
    /**
     * Nom de la rutina a la que pertany l'activitat
     */
    public String routineName;
    /**
     * Dia de l'activitat
     */
    public Day day;
    /**
     * Interval de temps de l'activitat
     */
    public TimeInterval timeInterval;

    /**
     * Creadora de la tupla
     * @param name nom de l'activitat
     * @param routineName nom de la rutina a la que pertany l'activitat
     * @param day dia de l'activitat
     * @param timeInterval interval de temps de l'activitat
     */
    public ActivityKey(String name, String routineName, Day day, TimeInterval timeInterval){
        this.name = name;
        this.routineName = routineName;
        this.day = day;
        this.timeInterval = timeInterval;
    }

    /**
     * Funcio que compara dues claus d'activitat
     * @param aC clau amb la que comparem
     * @return -1 si la primera activitat es temporalment anterior a la segona, 0 si son iguals i 1 si es posterior
     */
    @Override
    public int compareTo(ActivityKey aC) {
        int compareDays = this.day.compareTo(aC.day); //negatiu si iniDay<endDay; 0 si iguals; positiu si iniDay>endDay
        if(compareDays <= 0){
            return this.timeInterval.compareTo(aC.timeInterval);
        }
        return 1;
    }
}
