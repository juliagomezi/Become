package com.pes.become;

import com.pes.become.backend.domain.Activity;
import com.pes.become.backend.domain.ActivityKey;
import com.pes.become.backend.domain.Day;
import com.pes.become.backend.domain.Routine;
import com.pes.become.backend.domain.Theme;
import com.pes.become.backend.domain.TimeInterval;
import com.pes.become.backend.exceptions.InvalidTimeException;
import com.pes.become.backend.exceptions.InvalidTimeIntervalException;

import org.junit.Test;

import static org.junit.Assert.*;

public class RoutineTest {

    @Test
    public void addActivity() throws InvalidTimeIntervalException, InvalidTimeException {
        Routine r = new Routine("test");
        Activity a = new Activity("testActivity",new Theme("Sport"),17,0,18,0, Day.Monday);
        ActivityKey ak = new ActivityKey("testActivity", "test", Day.Monday, new TimeInterval(17,0,18,0));
        r.addActivity(a);
        Activity res = r.getActivity(ak);
        assertEquals(a,res);
    }

}
