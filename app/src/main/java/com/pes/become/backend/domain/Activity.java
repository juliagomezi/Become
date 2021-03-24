package com.pes.become.backend.domain;

import com.pes.become.backend.exceptions.InvalidTimeIntervalException;

/**
 * Classe que defineix les activitats que composen una rutina
 */
public class Activity implements Comparable<Activity>{
    /**
     * Nom de l'activitat
     */
    private String name;
    /**
     * Descripcio de l'activitat
     */
    private String description;
    /**
     * Tema de l'activitat
     */
    private Theme theme;
    /**
     * Interval de temps de l'activitat
     */
    private TimeInterval interval;
    /**
     * Dia de l'activitat
     */
    private Day day;

    /**
     * Creadora de l'activitat amb descripcio
     * @param name nom de l'activitat
     * @param description descripcio de l'activitat
     * @param theme tema de l'activitat
     * @param iniH hora d'inici de l'activitat
     * @param iniM minut d'inici de l'activitat
     * @param endH hora de fi de l'activitat
     * @param endM minut de fi de l'activitat
     * @param day dia de l'activitat
     * @throws InvalidTimeIntervalException es llença si el temps d'inici no es anterior al temps de fi
     */
    public Activity(String name, String description, Theme theme, int iniH, int iniM, int endH, int endM, Day day) throws InvalidTimeIntervalException {
        this.name = name;
        this.description = description;
        this.theme = theme;
        this.interval = new TimeInterval(iniH, iniM, endH, endM);
        this.day = day;
    }

    /**
     * Getter del nom
     * @return nom de l'activitat
     */
    public String getName() {
        return name;
    }

    /**
     * Setter del nom
     * @param name nou nom de l'activitat
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter de la descripcio
     * @return descripcio de l'activitat
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter de la descripcio
     * @param description nova descripcio de l'activitat
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter del tema
     * @return tema de l'activitat
     */
    public Theme getTheme() {
        return theme;
    }

    /**
     * Setter del tema
     * @param theme nou tema de l'activitat
     */
    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    /**
     * Getter de l'interval de temps
     * @return interval de temps de l'activitat
     */
    public TimeInterval getInterval() {
        return interval;
    }

    /**
     * Setter de l'interval de temps que crea un nou interval
     * @param iniH nova hora d'inici de l'activitat
     * @param iniM nou minut d'inici de l'activitat
     * @param endH nova hora de fi de l'activitat
     * @param endM nou minut de fi de l'activitat
     * @throws InvalidTimeIntervalException es llença si el temps d'inici no es anterior al temps de fi
     */
    public void setInterval(int iniH, int iniM, int endH, int endM) throws InvalidTimeIntervalException {
        this.interval = new TimeInterval(iniH, iniM, endH, endM);
    }

    /**
     * Getter del dia
     * @return dia de l'activitat
     */
    public Day getDay() {
        return day;
    }

    /**
     * Setter del dia
     * @param day nou dia de l'activitat
     */
    public void setDay(Day day) {
        this.day = day;
    }

    /**
     * Metode per actualitzar els parametres d'una activitat
     * @param name nou nom de l'activitat
     * @param description nova descripcio de l'activitat
     * @param theme nou tema de l'activitat
     * @param iniH nova hora d'inici de l'activitat
     * @param iniM nous minuts d'inici de l'activitat
     * @param endH nova hora de fi de l'activitat
     * @param endM nous minuts de fi de l'activitat
     * @param day nou dia de l'activitat
     * @throws InvalidTimeIntervalException es llença si el temps d'inici no es anterior al temps de fi
     */
    public void update(String name, String description, Theme theme, int iniH, int iniM, int endH, int endM, Day day) throws InvalidTimeIntervalException {
        setName(name);
        setDescription(description);
        setTheme(theme);
        setInterval(iniH, iniM, endH, endM);
        setDay(day);
    }

    /**
     * Funcio que compara una activitat amb una altra
     * @param a2 activitat amb que comparem
     * @return -1 l'activitat original es anterior a la de a2, 0 si son iguals i 1 si es posterior
     */
    @Override
    public int compareTo(Activity a2) {
        return this.getInterval().compareTo(a2.getInterval());
    }

}