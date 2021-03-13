package com.pes.become;

/**
 * Excepcio que es llençara quan s'intenti crear un interval de temps amb una hora de fi igual o anterior a l'hora d'inici
 */
public class InvalidTimeIntervalException extends Exception{
    public InvalidTimeIntervalException(String e ){ super(e); }
}
