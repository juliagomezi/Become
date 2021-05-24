package com.pes.become.backend.domain;

import java.util.ArrayList;

public class AchievementController {
    /**
     * Unica instancia de la classe
     */
    private static AchievementController instance;
    /**
     * Usuari autenticat que esta usant l'aplicació actualment
     */
    private User currentUser;

    /**
     * Obtenir la instancia de la classe
     * @return instancia
     */
    public static AchievementController getInstance() {
        if (instance == null) {
            instance = new AchievementController();
        }
        return instance;
    }

    /**
     * Setter de l'usuari autenticat al sistema
     * @param currentUser usuari auteniticat
     */
    public void setCurrentUser(User currentUser){
        this.currentUser = currentUser;
    }

    /**
     * Metode per comprovar si l'usuari ha guanyat un trofeu
     * @param achievement trofeu que volem comprovar
     * @return cert si l'ha guanyat, fals si no o ja el tenia
     */
    public boolean checkAchievement(Achievement achievement) {
        if(currentUser.getAchievementState(achievement)) return false;
        switch (achievement){
            case CreateFirstRoutine:
                return currentUser.gainAchievement(Achievement.CreateFirstRoutine);

            case HourMusic5:
                if(checkHoursPerTheme(Theme.Music, 5))
                    return currentUser.gainAchievement(Achievement.HourMusic5);
                else
                    return false;

            case HourSport5:
                if(checkHoursPerTheme(Theme.Sport, 5))
                    return currentUser.gainAchievement(Achievement.HourSport5);
                else
                    return false;

            case HourSleeping5:
                if(checkHoursPerTheme(Theme.Sleeping, 30))
                    return currentUser.gainAchievement(Achievement.HourSleeping5);
                else
                    return false;

            case HourCooking5:
                if(checkHoursPerTheme(Theme.Cooking, 5))
                    return currentUser.gainAchievement(Achievement.HourCooking5);
                else
                    return false;

            case HourWorking5:
                if(checkHoursPerTheme(Theme.Working, 5))
                    return currentUser.gainAchievement(Achievement.HourWorking5);
                else
                    return false;

            case HourEntertainment5:
                if(checkHoursPerTheme(Theme.Entertainment, 5))
                    return currentUser.gainAchievement(Achievement.HourEntertainment5);
                else
                    return false;

            case HourPlants5:
                if(checkHoursPerTheme(Theme.Plants, 1))
                    return currentUser.gainAchievement(Achievement.HourPlants5);
                else
                    return false;

            case HourOther5:
                if(checkHoursPerTheme(Theme.Other, 5))
                    return currentUser.gainAchievement(Achievement.HourOther5);
                else
                    return false;

            case HourMusic10:
                if(checkHoursPerTheme(Theme.Music, 10))
                    return currentUser.gainAchievement(Achievement.HourMusic10);
                else
                    return false;

            case HourSport10:
                if(checkHoursPerTheme(Theme.Sport, 10))
                    return currentUser.gainAchievement(Achievement.HourSport10);
                else
                    return false;

            case HourSleeping10:
                if(checkHoursPerTheme(Theme.Sleeping, 56))
                    return currentUser.gainAchievement(Achievement.HourSleeping10);
                else
                    return false;

            case HourCooking10:
                if(checkHoursPerTheme(Theme.Cooking, 10))
                    return currentUser.gainAchievement(Achievement.HourCooking10);
                else
                    return false;

            case HourWorking10:
                if(checkHoursPerTheme(Theme.Working, 10))
                    return currentUser.gainAchievement(Achievement.HourWorking10);
                else
                    return false;

            case HourEntertainment10:
                if(checkHoursPerTheme(Theme.Entertainment, 10))
                    return currentUser.gainAchievement(Achievement.HourEntertainment10);
                else
                    return false;

            case HourPlants10:
                if(checkHoursPerTheme(Theme.Plants, 5))
                    return currentUser.gainAchievement(Achievement.HourPlants10);
                else
                    return false;

            case HourOther10:
                if(checkHoursPerTheme(Theme.Other, 10))
                    return currentUser.gainAchievement(Achievement.HourOther10);
                else
                    return false;

            default:
                return false;
        }
    }

    /**
     * Metode per comprovar si l'usuari fa un cert nombre d'hores d'un tema concret en la seva rutina seleccionada
     * @param theme tema del que es volen mirar les hores
     * @param hours minim d'hores que ha de fer
     * @return cert si fa com a minim tantes hores, fals si no
     */
    private boolean checkHoursPerTheme(Theme theme, int hours) {
        Routine selRoutine = currentUser.getSelectedRoutine();
        Time totalTime = new Time(0,0);
        for(int d = 0; d<Day.values().length; ++d){
            ArrayList<Activity> actsDay = selRoutine.getActivitiesByDay(Day.values()[d]);
            for(Activity act : actsDay){
                if(act.getTheme() == theme){
                    Time duration = act.getInterval().getIntervalDuration();
                    int totalHours = totalTime.getHours() + duration.getHours();
                    int totalMinutes = totalTime.getMinutes() + duration.getMinutes();
                    totalTime = new Time(totalHours, totalMinutes);
                }
            }
        }
        int comparision = totalTime.compareTo(new Time(hours, 0));
        return comparision >= 0; // totalTime >= hours
    }
}
