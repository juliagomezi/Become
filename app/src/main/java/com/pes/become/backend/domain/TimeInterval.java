package com.pes.become.backend.domain;

import com.pes.become.backend.exceptions.InvalidTimeIntervalException;

/**
 * Classe que representa un interval de temps
 */
public class TimeInterval implements Comparable<TimeInterval>{
    /**
     * Temps d'inici de l'interval
     */
    private final Time startTime;
    /**
     * Temps de fi de l'interval, sempre posterior al temps d'inici
     */
    private final Time endTime;

    /**
     * Creadora de la classe
     * @param iniH hora d'inici de l'interval
     * @param iniM minut d'inici de l'interval
     * @param endH hora de fi de l'interval
     * @param endM minut de fi de l'interval
     * @throws InvalidTimeIntervalException es llença si el temps d'inici no es anterior al temps de fi
     */
    public TimeInterval(int iniH, int iniM, int endH, int endM) throws InvalidTimeIntervalException {
        Time startTime = new Time(iniH, iniM);
        Time endTime = new Time(endH, endM);
        int compareTime = startTime.compareTo(endTime);
        if(compareTime < 0){
            this.startTime = startTime;
            this.endTime = endTime;
        }
        else throw new InvalidTimeIntervalException();
    }

    /**
     * Getter del temps d'inici
     * @return temps d'inici de l'interval
     */
    public Time getStartTime() {
        return startTime;
    }

    /**
     * Getter del temps de fi
     * @return temps de fi de l'interval
     */
    public Time getEndTime() {
        return endTime;
    }

    /**
     * Funcio que compara un interval de temps amb un altre
     * @param tI2 interval de temps amb que comparem
     * @return 0 si hi ha solapament, 1 altrament
     */
    @Override
    public int compareTo(TimeInterval tI2) {
        Time tIni = this.getStartTime();
        Time tEnd = this.getEndTime();
        Time t2Ini = tI2.getStartTime();
        Time t2End = tI2.getEndTime();

        if(tEnd.compareTo(t2Ini) < 0 || tIni.compareTo(t2End) > 0) return 1;
        return 0;
    }
}
