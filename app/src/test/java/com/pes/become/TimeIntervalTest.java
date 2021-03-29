package com.pes.become;

import com.pes.become.backend.domain.TimeInterval;
import com.pes.become.backend.exceptions.InvalidTimeIntervalException;

import org.junit.Test;

import static org.junit.Assert.*;

public class TimeIntervalTest {

    @Test (expected = InvalidTimeIntervalException.class)
    public void invalidTimeInterval() throws InvalidTimeIntervalException {
        TimeInterval ti = new TimeInterval(18,30, 18, 15);
        System.out.println(ti.getStartTime());
    }

    @Test
    public void comparatorDifferentIntervals() throws InvalidTimeIntervalException {
        TimeInterval t1 = new TimeInterval(17,0,18,0);
        TimeInterval t2 = new TimeInterval(19,0,20,0);
        assertTrue(t1.compareTo(t2) != 0);
        assertTrue(t2.compareTo(t1) != 0);
    }

    @Test
    public void comparatorAdjacentIntervals() throws InvalidTimeIntervalException {
        TimeInterval t1 = new TimeInterval(17,0,18,0);
        TimeInterval t2 = new TimeInterval(18,0,19,0);
        assertTrue(t1.compareTo(t2) != 0);
        assertTrue(t2.compareTo(t1) != 0);
    }

    @Test
    public void comparatorOverlappingIntervals() throws InvalidTimeIntervalException {
        TimeInterval t1 = new TimeInterval(17,0,18,0);
        TimeInterval t2 = new TimeInterval(17,30,19,0);
        assertTrue(t1.compareTo(t2) == 0);
        assertTrue(t2.compareTo(t1) == 0);
    }

}
