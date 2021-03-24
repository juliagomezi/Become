package com.pes.become.backend.exceptions;

/**
 * Excepcio que es llençara quan s'intenti crear un Time amb valors incorrectes
 */
public class InvalidTimeException extends Exception{
    public InvalidTimeException(){ super(); }
    public String getMessage() {return "Error: invalid time values";}
}
