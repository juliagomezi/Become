package com.pes.become;

import com.pes.become.backend.domain.Time;
import com.pes.become.backend.exceptions.InvalidTimeException;

import org.junit.Test;

import static org.junit.Assert.*;

public class TimeTest {

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

    @Test
    public void firstBeforeSecondDifferentHours() throws InvalidTimeException {
        Time t1 = new Time(17,0);
        Time t2 = new Time(18,0);
        assertEquals(t1.firstBeforeSecond(t1,t2),true);
        assertEquals(t1.firstBeforeSecond(t2,t1),false);
    }

    @Test
    public void firstBeforeSecondSameHours() throws InvalidTimeException {
        Time t1 = new Time(17,0);
        Time t2 = new Time(17,30);
        assertEquals(t1.firstBeforeSecond(t1,t2),true);
        assertEquals(t1.firstBeforeSecond(t2,t1),false);
    }

    @Test
    public void firstBeforeSecondSameHoursAndMinutes() throws InvalidTimeException {
        Time t1 = new Time(17,0);
        Time t2 = new Time(17,0);
        assertEquals(t1.firstBeforeSecond(t1,t2),false);
        assertEquals(t1.firstBeforeSecond(t2,t1),false);
    }
}