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
        a1.setId("1");
        r.createActivity(a1);
        assertEquals(a1,r.getActivitiesByDay(Day.valueOf("Monday")).get(0));
    }

    @Test
    public void updateActivity() throws InvalidTimeIntervalException, OverlappingActivitiesException {
        Routine r = new Routine("test");
        Activity a1 = new Activity("testActivity", "testDescription", Theme.Sport, new TimeInterval(17,0,18,0), Day.Monday);
        a1.setId("1");
        r.createActivity(a1);
        Activity updatedActivity = new Activity("testActivityUpdated", "testDescriptionUpdated", Theme.Entertainment, new TimeInterval(11,0,12,0), Day.Tuesday);
        updatedActivity.setId("1");
        r.updateActivity(updatedActivity);
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
        a1.setId("1");
        r.createActivity(a1);
        Activity a2 = new Activity("testActivity", "testDescription", Theme.Sport, new TimeInterval(15,0,16,0), Day.Monday);
        a2.setId("2");
        r.createActivity(a2);
        Activity a3 = new Activity("testActivity", "testDescription", Theme.Sport, new TimeInterval(18,0,19,30), Day.Monday);
        a3.setId("3");
        r.createActivity(a3);
        Activity a4 = new Activity("testActivity", "testDescription", Theme.Sport, new TimeInterval(19,30,20,0), Day.Monday);
        a4.setId("4");
        r.createActivity(a4);
        assertEquals("2",r.getActivitiesByDay(Day.valueOf("Monday")).get(0).getId());
        assertEquals("1",r.getActivitiesByDay(Day.valueOf("Monday")).get(1).getId());
        assertEquals("3",r.getActivitiesByDay(Day.valueOf("Monday")).get(2).getId());
        assertEquals("4",r.getActivitiesByDay(Day.valueOf("Monday")).get(3).getId());
        r.deleteActivity("2", Day.Monday);
        assertEquals("1",r.getActivitiesByDay(Day.valueOf("Monday")).get(0).getId());
        assertEquals("3",r.getActivitiesByDay(Day.valueOf("Monday")).get(1).getId());
        assertEquals("4",r.getActivitiesByDay(Day.valueOf("Monday")).get(2).getId());
    }

    @Test (expected = OverlappingActivitiesException.class)
    public void overlappingActivitiesThrowsException() throws InvalidTimeIntervalException, OverlappingActivitiesException {
        Routine r = new Routine("test");
        Activity a1 = new Activity("testActivity", "testDescription", Theme.Sport, new TimeInterval(17,0,18,0), Day.Monday);
        a1.setId("1");
        r.createActivity(a1);
        Activity a2 = new Activity("testActivity2", "testDescription2", Theme.Sport, new TimeInterval(16,0,17,30), Day.Monday);
        a2.setId("2");
        r.createActivity(a2);
    }

}
