package com.pes.become;

import com.pes.become.backend.domain.TimeInterval;
import com.pes.become.backend.exceptions.InvalidTimeException;
import com.pes.become.backend.exceptions.InvalidTimeIntervalException;

import org.junit.Test;

import static org.junit.Assert.*;

public class TimeIntervalTest {

    @Test
    public void comparatorDifferentIntervals() throws InvalidTimeIntervalException, InvalidTimeException {
        TimeInterval t1 = new TimeInterval(17,0,18,0);
        TimeInterval t2 = new TimeInterval(19,0,20,0);
        assertTrue(t1.compareTo(t2) < 0);
        assertTrue(t2.compareTo(t1) > 0);
    }

    @Test
    public void comparatorAdjacentIntervals() throws InvalidTimeIntervalException, InvalidTimeException {
        TimeInterval t1 = new TimeInterval(17,0,18,0);
        TimeInterval t2 = new TimeInterval(18,0,19,0);
        assertEquals(0, t1.compareTo(t2));
        assertTrue(t2.compareTo(t1) > 0);
    }

}
