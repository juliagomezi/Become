package com.pes.become.backend.domain;

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
     * @param name String que definira el tema
     */
    public Theme(String name) {
        this.name = ThemeType.valueOf(name);
    }

    /**
     * Obte el nom del tema
     * @return nom del tema com a ThemeType
     */
    public ThemeType getName() {
        return name;
    }
}
