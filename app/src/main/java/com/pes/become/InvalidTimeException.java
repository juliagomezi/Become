package com.pes.become;

/**
 * Excepcio que es llençara quan s'intenti crear un Time amb valors incorrectes
 */
public class InvalidTimeException extends Exception{
    public InvalidTimeException(String e ){ super(e); }
}
