package com.pes.become.backend.domain;

/**
 * Tupla amb la informacio d'una activitat que requereix la capa de presentacio
 */
public class InfoActivity {
    /**
     * Nom de l'activitat
     */
    public String name;
    /**
     * Descripcio de l'activitat
     */
    public String description;
    /**
     * Tema de l'activitat
     */
    public String theme;

    /**
     * Creadora de la tupla
     * @param name nom de l'activitat
     * @param description descripcio de l'activitat
     * @param theme tema de l'activitat
     */
    public InfoActivity(String name, String description, String theme){
        this.name = name;
        this.description = description;
        this.theme = theme;
    }
}
