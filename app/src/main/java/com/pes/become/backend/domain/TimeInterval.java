package com.pes.become.backend.domain;

import com.pes.become.backend.exceptions.InvalidTimeException;
import com.pes.become.backend.exceptions.InvalidTimeIntervalException;

/**
 * Classe que representa un interval de temps
 */
public class TimeInterval implements Comparable<TimeInterval>{
    /**
     * Temps d'inici de l'interval
     */
    private Time startTime;
    /**
     * Temps de fi de l'interval, sempre posterior al temps d'inici
     */
    private Time endTime;

    /**
     * Creadora de la classe
     * @param iniH hora d'inici de l'interval
     * @param iniM minut d'inici de l'interval
     * @param endH hora de fi de l'interval
     * @param endM minut de fi de l'interval
     * @throws InvalidTimeIntervalException es llença si el temps d'inici no es anterior al temps de fi
     * @throws InvalidTimeException es llença si les hores o minuts no tenen format valid
     */
    public TimeInterval(int iniH, int iniM, int endH, int endM) throws InvalidTimeException, InvalidTimeIntervalException {
        Time startTime = new Time(iniH, iniM);
        Time endTime = new Time(endH, endM);
        int compareTime = startTime.compareTo(endTime);
        if(compareTime < 0){
            this.startTime = startTime;
            this.endTime = endTime;
        }
        else throw new InvalidTimeIntervalException("Error: el temps d'inici no es anterior al temps de fi");
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
     * @return -1 si el temps de fi de l'interval original es mes petit que el del tI2, 0 si son iguals i 1 si es mes gran
     */
    @Override
    public int compareTo(TimeInterval tI2) {
        return this.getEndTime().compareTo(tI2.getStartTime());
    }
}
