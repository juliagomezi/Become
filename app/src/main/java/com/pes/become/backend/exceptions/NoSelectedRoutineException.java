package com.pes.become.backend.exceptions;

/**
 * Excepcio que es llençara quan s'intenti crear un interval de temps amb una hora de fi igual o anterior a l'hora d'inici
 */
public class NoSelectedRoutineException extends Exception{
    public NoSelectedRoutineException() { super(); }

    public String getMessage() { return "Error: no hi ha cap rutina seleccionada"; }
}
