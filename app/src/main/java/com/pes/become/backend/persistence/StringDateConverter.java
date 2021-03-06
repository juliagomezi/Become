package com.pes.become.backend.persistence;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringDateConverter {

    /**
     * Retorna la data en format per la ID de la base de dades
     * @param date data a convertir
     * @return data convertida a String
     */
    public static String dateToString(Date date)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * Retorna la el string en format data
     * @param date la data a convertir
     * @return string convertit a data
     */
    public static Date stringToDate(String date)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
