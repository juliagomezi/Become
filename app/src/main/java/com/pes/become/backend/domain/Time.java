package com.pes.become.backend.domain;

import com.pes.become.backend.exceptions.InvalidTimeException;

/**
 * Classe que representa temps com a hores i minuts
 */
public class Time {
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
     * @throws InvalidTimeException es llença si les hores o minuts no tenen format valid
     */
    public Time(int hours, int minutes) throws InvalidTimeException {
        if(0<=hours && hours<24 && 0<=minutes && minutes<60){
            this.hours = hours;
            this.minutes = minutes;
        }
        else throw new InvalidTimeException("Error: invalid time values");
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
     * Funcio que comprova que un temps es anterior a un altre
     * @param t1 primer temps
     * @param t2 segon temps
     * @return true si t1 es anterior a t2, false si no
     */
    public static boolean firstBeforeSecond(Time t1, Time t2){
        if(t1.getHours() < t2.getHours())
            return true;
        if(t1.getHours() == t2.getHours())
            return t1.getMinutes() < t2.getMinutes();
        return false;
    }
}
