package com.pes.become;

import com.pes.become.backend.domain.Activity;
import com.pes.become.backend.domain.Day;
import com.pes.become.backend.domain.Routine;
import com.pes.become.backend.domain.Theme;
import com.pes.become.backend.exceptions.InvalidTimeIntervalException;

import org.junit.Test;

import static org.junit.Assert.*;

public class RoutineTest {

    @Test
    public void addActivity() throws InvalidTimeIntervalException {
        Routine r = new Routine("test");
        Activity a1 = new Activity("testActivity", "testDescription", Theme.Sport,17,0,18,0, Day.Monday);
        r.addActivity(a1);
        assertEquals(a1,r.getActivitiesByDay("Monday").get(0));
    }

    @Test
    public void updateActivity() throws InvalidTimeIntervalException {
        Routine r = new Routine("test");
        Activity a1 = new Activity("testActivity", "testDescription", Theme.Sport,17,0,18,0, Day.Monday);
        r.addActivity(a1);
        r.updateActivity("testActivityUpdated", "testDescriptionUpdated", Theme.Sport, 17, 0,18,0,11,0,12,0, Day.Tuesday);
        assertEquals("testActivityUpdated",r.getActivitiesByDay("Tuesday").get(0).getName());
        assertEquals("testDescriptionUpdated",r.getActivitiesByDay("Tuesday").get(0).getDescription());
        assertEquals(11,r.getActivitiesByDay("Tuesday").get(0).getInterval().getStartTime().getHours());
        assertEquals(12,r.getActivitiesByDay("Tuesday").get(0).getInterval().getEndTime().getHours());
    }

    @Test
    public void sortActivities() throws InvalidTimeIntervalException {
        Routine r = new Routine("test");
        Activity a1 = new Activity("testActivity", "testDescription", Theme.Sport,17,0,18,0, Day.Monday);
        r.addActivity(a1);
        Activity a2 = new Activity("testActivity", "testDescription", Theme.Sport,15,0,16,0, Day.Monday);
        r.addActivity(a2);
        Activity a3 = new Activity("testActivity", "testDescription", Theme.Sport,18,0,19,30, Day.Monday);
        r.addActivity(a3);
        Activity a4 = new Activity("testActivity", "testDescription", Theme.Sport,19,30,20,0, Day.Monday);
        r.addActivity(a4);
        assertEquals(a2,r.getActivitiesByDay("Monday").get(0));
        assertEquals(a1,r.getActivitiesByDay("Monday").get(1));
        assertEquals(a3,r.getActivitiesByDay("Monday").get(2));
        assertEquals(a4,r.getActivitiesByDay("Monday").get(3));
    }

}
