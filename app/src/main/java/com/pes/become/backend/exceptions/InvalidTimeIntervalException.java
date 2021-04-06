package com.pes.become.backend.exceptions;

/**
 * Excepcio que es llençara quan s'intenti crear un interval de temps amb una hora de fi igual o anterior a l'hora d'inici
 */
public class InvalidTimeIntervalException extends Exception {

    public InvalidTimeIntervalException() { super(); }

    public String getMessage() { return "Error: el temps d'inici no es anterior al temps de fi"; }

}
