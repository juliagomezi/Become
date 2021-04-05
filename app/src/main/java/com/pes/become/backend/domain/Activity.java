package com.pes.become.backend.domain;

/**
 * Classe que defineix les activitats que composen una rutina
 */
public class Activity implements Comparable<Activity>{
    /**
     *  Identificador de l'activitat
     */
    private String id;
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
     * @param ti interval de temps de l'activitat
     * @param day dia de l'activitat
     */
    public Activity(String name, String description, Theme theme, TimeInterval ti, Day day) {
        id = "-1";
        this.name = name;
        this.description = description;
        this.theme = theme;
        this.interval = ti;
        this.day = day;
    }

    /**
     * Getter de la id
     * @return id de l'activitat
     */
    public String getId() {
        return id;
    }

    /**
     * Setter de la id
     * @param id nova id de l'activitat
     */
    public void setId(String id) {
        this.id = id;
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
     * @param ti nou interval de temps
     */
    public void setInterval(TimeInterval ti) {
        this.interval = ti;
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
     * @param theme tema de la nova activitat
     * @param ti nou interval de temps de l'activitat
     * @param day nou dia de l'activitat
     */
    public void updateInfo(String name, String description, Theme theme, TimeInterval ti, Day day) {
        setName(name);
        setDescription(description);
        setTheme(theme);
        setInterval(ti);
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