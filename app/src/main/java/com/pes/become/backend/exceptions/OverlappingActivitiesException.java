package com.pes.become.backend.exceptions;

/**
 * Excepció que es llençarà quan s'intenti posar una activitat en un interval de temps on
 * ja hi hagi una altra.
 */
public class OverlappingActivitiesException extends Exception {

    public OverlappingActivitiesException() { super(); }
}
