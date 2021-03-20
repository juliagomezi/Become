package com.pes.become.backend.domain;

/**
 * Struc amb els atributs que formen la clau primaria d'una activitat
 */
public class ActivityKey implements Comparable<ActivityKey> {
    public String name;
    public String routineName;
    public Day day;
    public TimeInterval timeInterval;

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
