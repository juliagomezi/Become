package com.pes.become.backend.exceptions;

/**
 * Excepcio que es llençara quan s'intenti crear una activitat que comença en un dia posterior al dia que finalitza
 */
public class ExistingRoutineException extends Exception {

    public ExistingRoutineException() { super(); }

    public String getMessage() { return "Error: una altra rutina amb aquest nom ja existeix"; }

}
