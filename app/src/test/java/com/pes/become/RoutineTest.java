package com.pes.become;

import com.pes.become.backend.domain.Day;
import com.pes.become.backend.domain.TimeInterval;
import com.pes.become.backend.exceptions.InvalidTimeException;
import com.pes.become.backend.exceptions.InvalidTimeIntervalException;

import org.junit.Test;

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

    @Test
    public void updateActivity() throws InvalidTimeIntervalException, InvalidTimeException {
        Routine r = new Routine("test");
        Activity old = new Activity("testActivity",new Theme("Sport"),17,0,18,0, Day.Monday);
        Activity updated = new Activity("testActivityUpdated","sampleDescription", new Theme("Entertainment"),18,30,19,30, Day.Tuesday);
        ActivityKey ak = new ActivityKey("testActivity", "test", Day.Monday, new TimeInterval(17,0,18,0));
        r.addActivity(old);
        r.updateActivity(ak,"testActivityUpdated","sampleDescription",18,30,19,30, "Tuesday");
        Activity res = r.getActivity(ak);
        assertEquals(res,updated);
    }

}
