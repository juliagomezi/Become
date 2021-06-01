package com.pes.become.backend.exceptions;

/**
 * Excepcio que es llençara quan l'usuari intenti descarregar una rutina publica que te un nom igual al d'una de les seves rutines, violant la clau externa de rutina (email usuari, nom rutina)
 */
public class RoutinePrimaryKeyException extends Exception {
    public RoutinePrimaryKeyException() { super(); }
}
