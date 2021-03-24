package com.pes.become;

import com.pes.become.backend.domain.Time;

import org.junit.Test;

import static org.junit.Assert.*;

public class TimeTest {

    /*
    @Test(expected= InvalidTimeException.class)
    public void hoursSmallerThan0ThrowsException() throws InvalidTimeException {
        Time t = new Time(-1,0);
    }

    @Test(expected= InvalidTimeException.class)
    public void minutesSmallerThan0ThrowsException() throws InvalidTimeException {
        Time t = new Time(0,-1);
    }

    @Test(expected= InvalidTimeException.class)
    public void hoursBiggerOrEqualThan24ThrowsException() throws InvalidTimeException {
        Time t = new Time(24,0);
    }

    @Test(expected= InvalidTimeException.class)
    public void minutesBiggerOrEqualThan60ThrowsException() throws InvalidTimeException {
        Time t = new Time(0,60);
    }
     */


    @Test
    public void comparatorDifferentHours() {
        Time t1 = new Time(17,0);
        Time t2 = new Time(18,0);
        int res1 = t1.compareTo(t2);
        int res2 = t2.compareTo(t1);
        assertTrue(res1 < 0);
        assertTrue(res2 > 0);
    }

    @Test
    public void comparatorSameHours() {
        Time t1 = new Time(17,0);
        Time t2 = new Time(17,30);
        int res1 = t1.compareTo(t2);
        int res2 = t2.compareTo(t1);
        assertTrue(res1 < 0);
        assertTrue(res2 > 0);
    }

    @Test
    public void comparatorSameHoursAndMinutes() {
        Time t1 = new Time(17,0);
        Time t2 = new Time(17,0);
        assertEquals(0, t1.compareTo(t2));
    }
}