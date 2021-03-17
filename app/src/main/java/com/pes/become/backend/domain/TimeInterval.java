package com.pes.become.backend.domain;

import com.pes.become.backend.exceptions.InvalidTimeException;
import com.pes.become.backend.exceptions.InvalidTimeIntervalException;

/**
 * Classe que representa un interval de temps
 */
public class TimeInterval {
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
        if(Time.firstBeforeSecond(startTime, endTime)){
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
}
