package com.pes.become;

import com.pes.become.backend.domain.Activity;
import com.pes.become.backend.domain.Day;
import com.pes.become.backend.domain.Routine;
import com.pes.become.backend.domain.Theme;
import com.pes.become.backend.domain.TimeInterval;
import com.pes.become.backend.exceptions.InvalidTimeIntervalException;
import com.pes.become.backend.exceptions.OverlappingActivitiesException;

import org.junit.Test;

import static org.junit.Assert.*;

public class RoutineTest {

    @Test
    public void addActivity() throws InvalidTimeIntervalException, OverlappingActivitiesException {
        Routine r = new Routine("test");
        Activity a1 = new Activity("testActivity", "testDescription", Theme.Sport, new TimeInterval(17,0,18,0), Day.Monday);
        r.createActivity(a1);
        assertEquals(a1,r.getActivitiesByDay(Day.valueOf("Monday")).get(0));
    }

    @Test
    public void updateActivity() throws InvalidTimeIntervalException, OverlappingActivitiesException {
        Routine r = new Routine("test");
        Activity a1 = new Activity("testActivity", "testDescription", Theme.Sport, new TimeInterval(17,0,18,0), Day.Monday);
        r.createActivity(a1);
        r.updateActivity(new TimeInterval(17,0,18,0), Day.Monday, new Activity("testActivityUpdated", "testDescriptionUpdated", Theme.Entertainment, new TimeInterval(11,0,12,0), Day.Tuesday));
        assertEquals("testActivityUpdated",r.getActivitiesByDay(Day.valueOf("Tuesday")).get(0).getName());
        assertEquals("testDescriptionUpdated",r.getActivitiesByDay(Day.valueOf("Tuesday")).get(0).getDescription());
        assertEquals(11,r.getActivitiesByDay(Day.valueOf("Tuesday")).get(0).getInterval().getStartTime().getHours());
        assertEquals(12,r.getActivitiesByDay(Day.valueOf("Tuesday")).get(0).getInterval().getEndTime().getHours());
        assertEquals(Theme.Entertainment, r.getActivitiesByDay(Day.valueOf("Tuesday")).get(0).getTheme());
    }

    @Test
    public void sortActivities() throws InvalidTimeIntervalException, OverlappingActivitiesException {
        Routine r = new Routine("test");
        Activity a1 = new Activity("testActivity", "testDescription", Theme.Sport, new TimeInterval(17,0,18,0), Day.Monday);
        r.createActivity(a1);
        Activity a2 = new Activity("testActivity", "testDescription", Theme.Sport, new TimeInterval(15,0,16,0), Day.Monday);
        r.createActivity(a2);
        Activity a3 = new Activity("testActivity", "testDescription", Theme.Sport, new TimeInterval(18,0,19,30), Day.Monday);
        r.createActivity(a3);
        Activity a4 = new Activity("testActivity", "testDescription", Theme.Sport, new TimeInterval(19,30,20,0), Day.Monday);
        r.createActivity(a4);
        assertTrue(r.getActivitiesByDay(Day.valueOf("Monday")).get(0).getInterval().compareTo(a2.getInterval()) == 0);
        assertTrue(r.getActivitiesByDay(Day.valueOf("Monday")).get(1).getInterval().compareTo(a1.getInterval()) == 0);
        assertTrue(r.getActivitiesByDay(Day.valueOf("Monday")).get(2).getInterval().compareTo(a3.getInterval()) == 0);
        assertTrue(r.getActivitiesByDay(Day.valueOf("Monday")).get(3).getInterval().compareTo(a4.getInterval()) == 0);
    }

    @Test (expected = OverlappingActivitiesException.class)
    public void overlappingActivitiesThrowsException() throws OverlappingActivitiesException, InvalidTimeIntervalException {
        Routine r = new Routine("test");
        Activity a1 = new Activity("testActivity", "testDescription", Theme.Sport, new TimeInterval(17,0,18,0), Day.Monday);
        r.createActivity(a1);
        Activity a2 = new Activity("testActivity2", "testDescription2", Theme.Sport, new TimeInterval(16,0,17,30), Day.Monday);
        r.createActivity(a2);
    }

}
