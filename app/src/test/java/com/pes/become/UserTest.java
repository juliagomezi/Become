package com.pes.become;

import com.pes.become.backend.domain.Activity;
import com.pes.become.backend.domain.Day;
import com.pes.become.backend.domain.Routine;
import com.pes.become.backend.domain.Theme;
import com.pes.become.backend.domain.TimeInterval;
import com.pes.become.backend.domain.User;
import com.pes.become.backend.exceptions.InvalidTimeIntervalException;
import com.pes.become.backend.exceptions.OverlappingActivitiesException;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

public class UserTest {

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

    @Test
    public void testStatisticsInitialize(){
        User user = new User("test");
        ArrayList<ArrayList<Double>> expected = new ArrayList<>();
        for(int i=0; i<8; ++i){
            ArrayList<Double> hoursByDay = new ArrayList<>();
            for(int j=0; j<7; ++j){
                hoursByDay.add(j, 0.0);
            }
            expected.add(i, hoursByDay);
        }
        assertEquals(expected, user.getStatisticsSelectedRoutine());
    }

    @Test
    public void testStatisticsWithContent(){
        User user = new User("test");
        Map<Theme, Map<Day, Double>> stats = new TreeMap<>();
        for(int t=0; t<Theme.values().length; ++t){
            Map<Day, Double> emptyMap = new TreeMap<>();
            for(int d = 0; d< Day.values().length; ++d){
                emptyMap.put(Day.values()[d], (double)t*d);
            }
            stats.put(Theme.values()[t], emptyMap);
        }
        user.setStatisticsSelectedRoutine(stats);

        ArrayList<ArrayList<Double>> expected = new ArrayList<>();
        for(int i=0; i<8; ++i){
            ArrayList<Double> hoursByDay = new ArrayList<>();
            for(int j=0; j<7; ++j){
                hoursByDay.add(j, (double)j*i);
            }
            expected.add(i, hoursByDay);
        }
        assertEquals(expected, user.getStatisticsSelectedRoutine());
    }

    @Test
    public void testUpdateStatisticsPositive(){
        User user = new User("test");
        Map<Theme, Map<Day, Double>> stats = new TreeMap<>();
        for(int t=0; t<Theme.values().length; ++t){
            Map<Day, Double> emptyMap = new TreeMap<>();
            for(int d = 0; d< Day.values().length; ++d){
                emptyMap.put(Day.values()[d], (double)t*d);
            }
            stats.put(Theme.values()[t], emptyMap);
        }

        user.setStatisticsSelectedRoutine(stats);

        user.updateStatistics(Theme.Other, Day.Monday, 4, 15, true);

        ArrayList<ArrayList<Double>> expected = new ArrayList<>();
        for(int i=0; i<8; ++i){
            ArrayList<Double> hoursByDay = new ArrayList<>();
            for(int j=0; j<7; ++j){
                if(i == 7 && j == 0)
                    hoursByDay.add(j, (double)j*i+4.25);
                else
                    hoursByDay.add(j, (double)j*i);
            }
            expected.add(i, hoursByDay);
        }
        assertEquals(expected, user.getStatisticsSelectedRoutine());
    }

    @Test
    public void testUpdateStatisticsNegative() {
        User user = new User("test");
        Map<Theme, Map<Day, Double>> stats = new TreeMap<>();
        for(int t=0; t<Theme.values().length; ++t){
            Map<Day, Double> emptyMap = new TreeMap<>();
            for(int d = 0; d< Day.values().length; ++d){
                emptyMap.put(Day.values()[d], (double)t*d);
            }
            stats.put(Theme.values()[t], emptyMap);
        }

        user.setStatisticsSelectedRoutine(stats);

        user.updateStatistics(Theme.Other, Day.Friday, 4, 30, false);

        ArrayList<ArrayList<Double>> expected = new ArrayList<>();
        for(int i=0; i<8; ++i){
            ArrayList<Double> hoursByDay = new ArrayList<>();
            for(int j=0; j<7; ++j){
                if(i == 7 && j == 4)
                    hoursByDay.add(j, (double)j*i-4.5);
                else
                    hoursByDay.add(j, (double)j*i);
            }
            expected.add(i, hoursByDay);
        }
        assertEquals(expected, user.getStatisticsSelectedRoutine());
    }
}
