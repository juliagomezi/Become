package com.pes.become;

import com.pes.become.backend.domain.Day;
import com.pes.become.backend.domain.Theme;
import com.pes.become.backend.domain.User;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class UserTest {
    @Test
    public void testStatisticsInitialize(){
        User user = new User("test");
        ArrayList<ArrayList<Integer>> expected = new ArrayList<>();
        for(int i=0; i<8; ++i){
            ArrayList<Integer> hoursByDay = new ArrayList<>();
            for(int j=0; j<7; ++j){
                hoursByDay.add(j, 0);
            }
            expected.add(i, hoursByDay);
        }
        Assert.assertEquals(expected, user.getStatisticsSelectedRoutine());
    }

    @Test
    public void testStatisticsWithContent(){
        User user = new User("test");
        Map<Theme, Map<Day, Integer>> stats = new TreeMap<>();
        for(int t=0; t<Theme.values().length; ++t){
            Map<Day, Integer> emptyMap = new TreeMap<>();
            for(int d = 0; d< Day.values().length; ++d){
                emptyMap.put(Day.values()[d], t*d);
            }
            stats.put(Theme.values()[t], emptyMap);
        }
        user.setStatisticsSelectedRoutine(stats);

        ArrayList<ArrayList<Integer>> expected = new ArrayList<>();
        for(int i=0; i<8; ++i){
            ArrayList<Integer> hoursByDay = new ArrayList<>();
            for(int j=0; j<7; ++j){
                hoursByDay.add(j, j*i);
            }
            expected.add(i, hoursByDay);
        }
        Assert.assertEquals(expected, user.getStatisticsSelectedRoutine());
    }
}
