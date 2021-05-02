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
    public boolean checkAchievement(Achievement achievement){
        switch (achievement){
            case CreateRoutine: //aquest no te logica complexa
                return currentUser.gainAchievement(Achievement.CreateRoutine);
            case HourMusic5:
                if(checkHoursPerTheme(Theme.Music, 5))
                    return currentUser.gainAchievement(Achievement.HourMusic5);
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
