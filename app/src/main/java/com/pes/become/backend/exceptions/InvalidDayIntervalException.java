package com.pes.become.backend.exceptions;

/**
 * Excepcio que es llençara quan s'intenti crear una activitat que comença en un dia posterior al dia que finalitza
 */
public class InvalidDayIntervalException extends Exception{

    public InvalidDayIntervalException() { super(); }

    public String getMessage() { return "Error: el dia de fi és anterior al dia d'inici"; }

}
