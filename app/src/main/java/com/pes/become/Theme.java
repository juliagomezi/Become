package com.pes.become;

/**
 * Classe que conte la informacio d'un tema
 */
public class Theme {
    /**
     * Nom del tema
     */
    private ThemeType name;

    /**
     * Creadora de la classe
     * @param name ThemeType que definira el tema
     */
    public Theme(ThemeType name) {
        this.name = name;
    }

    /**
     * Obte el nom del tema
     * @return nom del tema com a ThemeType
     */
    public ThemeType getName() {
        return name;
    }
}
