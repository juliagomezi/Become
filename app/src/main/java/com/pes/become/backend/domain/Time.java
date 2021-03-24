package com.pes.become.backend.domain;

/**
 * Classe que representa temps com a hores i minuts
 */
public class Time implements Comparable<Time>{
    /**
     * Hores del temps
     */
    private int hours;
    /**
     * Minuts del temps
     */
    private int minutes;

    /**
     * Creadora de la classe
     * @param hours hores
     * @param minutes minuts
     */
    public Time(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    /**
     * Getter de les hores
     * @return hores del temps
     */
    public int getHours() {
        return hours;
    }

    /**
     * Getter dels minuts
     * @return minuts del temps
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Funcio que compara un temps amb un altre
     * @param t2 temps amb que comparem
     * @return -1 si t2 es posterior, 0 si es igual, 1 si es anterior
     */
    @Override
    public int compareTo(Time t2) {
        if(this.getHours() < t2.getHours())
            return -1;
        if(this.getHours() == t2.getHours()) {
            return Integer.compare(this.getMinutes(), t2.getMinutes());
        }
        return 1;
    }
}
