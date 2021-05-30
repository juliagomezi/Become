package com.pes.become.backend.adapters;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.pes.become.backend.domain.*;
import com.pes.become.backend.exceptions.*;
import com.facebook.AccessToken;
import com.pes.become.backend.persistence.ControllerPersistence;
import com.pes.become.backend.persistence.StringDateConverter;
import com.pes.become.frontend.Community;
import com.pes.become.frontend.CommunityRecyclerAdapter;
import com.pes.become.frontend.ForgotPassword;
import com.pes.become.frontend.Login;
import com.pes.become.frontend.LogoScreen;
import com.pes.become.frontend.MainActivity;
import com.pes.become.frontend.Profile;
import com.pes.become.frontend.Signup;
import com.pes.become.frontend.Stats;
import com.pes.become.frontend.Trophies;

import java.lang.reflect.Method;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Classe que gestiona la comunicacio entre la capa de presentacio i la capa de domini, i la creacio dels adaptadors de cada classe de domini
 */
public class DomainAdapter {
    /**
     * Unica instancia de la classe
     */
    private static DomainAdapter instance;
    /**
     * Unica instancia del controlador de persistencia
     */
    private static final ControllerPersistence controllerPersistence = ControllerPersistence.getInstance();
    /**
     * Unica instancia de l'adaptador de la classe Rutina
     */
    private static final RoutineAdapter routineAdapter = RoutineAdapter.getInstance();
    /**
     * Unica instancia de l'adaptador de la classe Usuari
     */
    private static final UserAdapter userAdapter = UserAdapter.getInstance();
    /**
     * Unica instancia del controlador de trofeus
     */
    private static final AchievementController achievementController = AchievementController.getInstance();
    /**
     * Instancia de la classe login del frontend
     */
    private Login login;
    /**
     * Instancia de la classe login del frontend
     */
    private LogoScreen logoScreen;
    /**
     * Instancia de la classe signup del frontend
     */
    private Signup signup;
    /**
     * Instancia de la classe forgotPassword del frontend
     */
    private ForgotPassword forgotPass;
    /**
     * Usuari autenticat que esta usant l'aplicació actualment
     */
    private static User currentUser;
    /**
     * Instancia de la classe profile del frontend
     */
    private Profile profile;
    /**
     * Instancia de la classe stats del frontend
     */
    private Stats stats;
    /**
     * Indica si l'usuari ha seleccionat directament una rutina o s'ha seleccionat des del codi pel correcte funcionament de l'aplicacio
     */
    private boolean selectingRoutine;

    /**
     * Obtenir la instancia de la classe
     * @return instancia
     */
    public static DomainAdapter getInstance() {
        if (instance == null) {
            instance = new DomainAdapter();
        }
        return instance;
    }

    /**
     * Metode per registrar un nou usuari
     * @param mail correu de l'usuari
     * @param password contrassenya de l'usuari
     * @param name nom de l'usuari
     */
    public void registerUser(String mail, String password, String name, android.app.Activity act) {
        Class[] parameterTypes = new Class[4];
        parameterTypes[0] = boolean.class;
        parameterTypes[1] = String.class;
        parameterTypes[2] = String.class;
        parameterTypes[3] = String.class;
        Method method;
        signup = (Signup) act;
        try {
            method = DomainAdapter.class.getMethod("registerCallback", parameterTypes);
            controllerPersistence.registerUser(mail, password, name, act, method, DomainAdapter.getInstance());
        } catch (NoSuchMethodException ignore) {}
    }

    /**
     * Metode per carregar un usuari
     * @param act activitat
     */
    public void loadUser (android.app.Activity act) {
        Class[] parameterTypes = new Class[8];
        parameterTypes[0] = boolean.class;
        parameterTypes[1] = String.class;
        parameterTypes[2] = String.class;
        parameterTypes[3] = String.class;
        parameterTypes[4] = Bitmap.class;
        parameterTypes[5] = ArrayList.class;
        parameterTypes[6] = Map.class;
        parameterTypes[7] = int.class;
        Method method;
        logoScreen = (LogoScreen)act;
        try {
            method = DomainAdapter.class.getMethod("authUser", parameterTypes);
            controllerPersistence.loadUser(method, DomainAdapter.getInstance());
        } catch(NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metode per iniciar la sessio d'un usuari existent
     * @param mail correu de l'usuari
     * @param password contrassenya de l'usuari
     * @param act Activity d'Android necessaria per la execucio del firebase
     */
    public void loginUser(String mail, String password, android.app.Activity act) {
        Class[] parameterTypes = new Class[8];
        parameterTypes[0] = boolean.class;
        parameterTypes[1] = String.class;
        parameterTypes[2] = String.class;
        parameterTypes[3] = String.class;
        parameterTypes[4] = Bitmap.class;
        parameterTypes[5] = ArrayList.class;
        parameterTypes[6] = Map.class;
        parameterTypes[7] = int.class;
        Method method;
        login = (Login)act;
        try {
            method = DomainAdapter.class.getMethod("loginCallback", parameterTypes);
            controllerPersistence.loginUser(mail, password, act, method, DomainAdapter.getInstance());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    public void loginFacebookUser(AccessToken accessToken, android.app.Activity act) {
        Class[] parameterTypes = new Class[8];
        parameterTypes[0] = boolean.class;
        parameterTypes[1] = String.class;
        parameterTypes[2] = String.class;
        parameterTypes[3] = String.class;
        parameterTypes[4] = Bitmap.class;
        parameterTypes[5] = ArrayList.class;
        parameterTypes[6] = Map.class;
        parameterTypes[7] = int.class;
        Method method;
        login = (Login)act;

        try {
            method = DomainAdapter.class.getMethod("loginCallback", parameterTypes);
            controllerPersistence.loginUserFacebook(accessToken, method, DomainAdapter.getInstance());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metode per iniciar sessio amb un compte de Google
     * @param idToken token
     * @param act activitat
     */
    public void loginGoogleUser(String idToken, android.app.Activity act) {
        Class[] parameterTypes = new Class[8];
        parameterTypes[0] = boolean.class;
        parameterTypes[1] = String.class;
        parameterTypes[2] = String.class;
        parameterTypes[3] = String.class;
        parameterTypes[4] = Bitmap.class;
        parameterTypes[5] = ArrayList.class;
        parameterTypes[6] = Map.class;
        parameterTypes[7] = int.class;
        Method method;
        login = (Login)act;

        try {
            method = DomainAdapter.class.getMethod("loginCallback", parameterTypes);
            controllerPersistence.loginUserGoogle(idToken, method, DomainAdapter.getInstance());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metode que rep la resposta a la crida "loginUser" de la base de dades
     * @param success resultat de l'operacio
     * @param userId identificador de l'usuari
     * @param username nom d'usuari
     * @param selectedRoutineId rutina seleccionada de l'usuari
     * @param pfp imatge de perfil de l'usuari
     * @param streak dies de ratxa
     */
    public void loginCallback(boolean success, String userId, String username, String selectedRoutineId, Bitmap pfp, ArrayList<ArrayList<String>> routineInfo, Map<String, Map<String, Double>> stats, int streak) {
        if (success) {
            currentUser = userAdapter.createUser(username);
            currentUser.setID(userId);
            currentUser.setPFP(pfp);
            currentUser.setStreak(streak);
            if(!routineInfo.isEmpty()) currentUser.setRoutines(routineInfo);
            ArrayList<String> routine = new ArrayList<>();
            for(ArrayList<String> r : routineInfo) {
                if(r.get(0).equals(selectedRoutineId)) {
                    routine = r;
                    break;
                }
            }
            if(!selectedRoutineId.equals("")) selectRoutine(routine, false);
            else {
                login.loginCallback();
                login = null;
            }

            if(stats != null) {
                Map<Theme,Map<Day, Double>> statistics = new TreeMap<>();
                for(Map.Entry<String,Map<String,Double>> themeStats : stats.entrySet()) {
                    Map<Day, Double> s = new TreeMap<>();
                    for (Map.Entry<String, Double> dayStats : themeStats.getValue().entrySet()) {
                        Day day = Day.valueOf(dayStats.getKey());
                        s.put(day, dayStats.getValue());
                    }
                    Theme theme = Theme.valueOf(themeStats.getKey());
                    statistics.put(theme,s);
                }

                currentUser.setStatisticsSelectedRoutine(statistics);
            }
            else{
                currentUser.clearStatistics();
            }

            achievementController.setCurrentUser(currentUser);

            try {
                Method method = DomainAdapter.class.getMethod("getAchievementsCallback", ArrayList.class);
                controllerPersistence.getTrophies(currentUser.getID(), method, DomainAdapter.getInstance());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        else {
            login.loginCallbackFailed();
        }
    }

    /**
     * Metode que autentifica un usuari ja loguejat
     * @param success resultat de l'operacio
     * @param userId identificador de l'usuari
     * @param username nom d'usuari
     * @param selectedRoutineId rutina seleccionada de l'usuari
     * @param pfp imatge de perfil de l'usuari
     * @param streak dies de ratxa
     */
    public void authUser(boolean success, String userId, String username, String selectedRoutineId, Bitmap pfp, ArrayList<ArrayList<String>> routineInfo, Map<String, Map<String, Double>> stats, int streak) {
        if (success) {
            currentUser = userAdapter.createUser(username);
            currentUser.setID(userId);
            currentUser.setPFP(pfp);
            currentUser.setStreak(streak);
            if(!routineInfo.isEmpty()) currentUser.setRoutines(routineInfo);
            ArrayList<String> routine = new ArrayList<>();
            for(ArrayList<String> r : routineInfo) {
                if(r.get(0).equals(selectedRoutineId)) {
                    routine = r;
                    break;
                }
            }
            if (!selectedRoutineId.equals("")) selectRoutine(routine, false);
            else {
                logoScreen.loginCallback();
                logoScreen = null;
            }

            if(stats != null) {
                Map<Theme,Map<Day, Double>> statistics = new TreeMap<>();
                for(Map.Entry<String,Map<String,Double>> themeStats : stats.entrySet()) {
                    Map<Day, Double> s = new TreeMap<>();
                    for (Map.Entry<String, Double> dayStats : themeStats.getValue().entrySet()) {
                        Day day = Day.valueOf(dayStats.getKey());
                        s.put(day, dayStats.getValue());
                    }
                    Theme theme = Theme.valueOf(themeStats.getKey());
                    statistics.put(theme,s);
                }

                currentUser.setStatisticsSelectedRoutine(statistics);
            }
            else{
                currentUser.clearStatistics();
            }

            achievementController.setCurrentUser(currentUser);

            try {
                Method method = DomainAdapter.class.getMethod("getAchievementsCallback", ArrayList.class);
                controllerPersistence.getTrophies(currentUser.getID(), method, DomainAdapter.getInstance());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        else {
            logoScreen.loginCallbackFailed();
        }
    }

    /**
     * Metode que rep la resposta de registrar un usuari
     * @param success resultat de l'operacio
     * @param userId identificador de l'usuari
     * @param username nom d'usuari
     * @param selectedRoutineId rutina seleccionada de l'usuari
     */
    public void registerCallback(boolean success, String userId, String username, String selectedRoutineId) {
        if (success) {
            currentUser = userAdapter.createUser(username);
            currentUser.setID(userId);
            if (!selectedRoutineId.equals("")) loadSelectedRoutine(); //això és impossible que passi!!!
            signup.registerCallback();
        }
        else {
            signup.registerCallbackFailed();
        }
    }

    /**
     * Metode per tancar la sessio d'un usuari
     */
    public void logoutUser() {
        currentUser = null;
        routineAdapter.setCurrentRoutine(null);
        controllerPersistence.signOut();
    }

    /**
     * Metode per donar de baixa un compte d'usuari
     * @param password contrassenya de l'usuari
     * @param profile instancia Fragment del front-end que renderitza el perfil
     */
    public void deleteUser(String password, Profile profile) {
        this.profile = profile;
        try {
            Method method = DomainAdapter.class.getMethod("deleteCallback", boolean.class);
            controllerPersistence.deleteUser(password, method, DomainAdapter.getInstance());
        } catch (NoSuchMethodException ignore) {
        }
    }

    /**
     * Metode per rebre la resposta de l'esborrat del compte
     * @param success resultat de la reautenticacio
     */
    public void deleteCallback(boolean success) {
        currentUser = null;
        profile.deleteCallback(success);
    }

    /**
     * Metode per obtenir el provider de l'usuari
     * @return el provider de l'usuari
     */
    public String getUserProvider() {
        return controllerPersistence.getUserProvider();
    }

    /**
     * Metode per canviar la contrasenya d'un usuari
     * @param oldPassword contrasenya antiga
     * @param newPassword contrasenya nova
     */
    public void changePassword(String oldPassword, String newPassword, Profile profile) {
        this.profile = profile;
        try {
            Method method = DomainAdapter.class.getMethod("changePasswordCallback", boolean.class);
            controllerPersistence.changePassword(oldPassword, newPassword, method, this);
        } catch(Exception ignore) {}
    }

    /**
     * Metode per rebre el resultat del canvi de contrasenya
     * @param success resultat de la operacio
     */
    public void changePasswordCallback(boolean success) {
        profile.changePasswordCallback(success);
    }

    /**
     * Metode per canviar el nom de l'usuari
     * @param newName nou nom de l'usuari
     */
    public void changeUserName(String newName) {
        controllerPersistence.changeUsername(currentUser.getID(),newName);
        currentUser.setName(newName);
    }

    /**
     * Metode per obtenir les estadistiques de la rutina seleccionada de l'usuari
     * @return Array d'Arrays d'enters on la primera array representa els temes, la segona els dies i la tercera les hores
     */
    public ArrayList<ArrayList<Double>> getStatisticsSelectedRoutine(){
        return currentUser.getStatisticsSelectedRoutine();
    }

    /**
     * Metode per obtenir les hores dedicades a cada tema en la rutina seleccionada
     * @return ArrayList on a cada posicio hi han les hores dedicades al tema equivalent a la posicio (i.e. a la posicio 0 hi ha les hores dedicades a Music)
     */
    public ArrayList<Double> getHoursByTheme() {
        ArrayList<Double> hoursByTheme = new ArrayList<>();
        for(int theme=0; theme<Theme.values().length; ++theme){
            double hours = currentUser.getHoursByTheme(Theme.values()[theme]);
            hoursByTheme.add(theme, hours);
        }
        return hoursByTheme;
    }

    /**
     * Metode per comprovar si l'usuari ha guanyat un trofeu
     * @param achievement nom del trofeu que volem comprovar
     * @return cert si l'ha guanyat, fals si no o ja el tenia
     */
    public boolean checkAchievement(String achievement){
        if (achievementController.checkAchievement(Achievement.valueOf(achievement))) {
            controllerPersistence.addTrophy(currentUser.getID(), achievement);
            return true;
        }
        return false;
    }

    /**
     * Metode per obtenir els trofeus que te l'usuari
     * @return mapa amb el nom del trofeu com a clau i un boolea que indica si te el trofeu o no com a valor
     */
    public ArrayList<Boolean> getUserAchievements(){
        TreeMap<Achievement, Boolean> achievements = currentUser.getAllAchievementsStates();
        ArrayList<Boolean> result = new ArrayList<>();
        for(TreeMap.Entry<Achievement, Boolean> achievement : achievements.entrySet()){
            result.add(achievement.getValue());
        }
        return result;
    }

    /**
     * Crear una rutina
     * @param name nom de la rutina
     * @throws ExistingRoutineException si l'usuari ja té una altra rutina amb aquest nom
     */
    public void createRoutine(String name) throws ExistingRoutineException {
        if(!currentUser.existsRoutine(name)) {
            String id = controllerPersistence.createRoutine(currentUser.getID(), name);
            ArrayList<String> routine = new ArrayList<>();
            routine.add(id);
            routine.add(name);
            currentUser.addRoutine(routine);
        } else {
            throw new ExistingRoutineException();
        }
    }

    /**
     * Metode per actualitzar el calendari
     * @param month mes
     * @param year any
     * @param s estat
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateCalendar(int month, int year, Stats s) {
        stats = s;
        YearMonth object = YearMonth.of(year,month);
        int daysInMonth = object.lengthOfMonth();
        currentUser.clearMonth(daysInMonth);
        try {
            Method method = DomainAdapter.class.getMethod("calendarCallback", ArrayList.class);
            controllerPersistence.getAvailableDays(currentUser.getID(), month, year, method, DomainAdapter.getInstance());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metode de callback del calendari
     * @param calendar calendari
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void calendarCallback(ArrayList<HashMap<String,String>> calendar) {
        for(HashMap<String,String> day : calendar) {
            int dayOfMonth = Integer.parseInt(day.get("day"));
            int completition = 0;
            if(!day.get("numTotalActivities").equals("0"))
                completition = (int)((Double.parseDouble(day.get("numActivitiesDone"))/Double.parseDouble(day.get("numTotalActivities"))) * 100);
            currentUser.setDayCalendar(dayOfMonth-1, completition);
        }
        stats.calendarCallback(currentUser.getCalendarMonth());
    }

    /**
     * Metode per obtenir les ratxes de l'usuari
     * @return la ratxa de l'usuari
     */
    public int getUserStreak(){
        return currentUser.getStreak();
    }

    /**
     * Metode per seleccionar una rutina ja existent
     * @param infoRoutine llista amb la informacio de la rutina a seleccionar
     * @param selecting true si la rutina esta sent seleccionada directament per l'usuari, false altrament
     */
    public void selectRoutine(ArrayList<String> infoRoutine, boolean selecting) {
        selectingRoutine = selecting;
        if(infoRoutine!=null) {
            Routine routine = routineAdapter.createRoutine(infoRoutine.get(1));
            routine.setId(infoRoutine.get(0));
            currentUser.setSelectedRoutine(routine);
            routineAdapter.setCurrentRoutine(currentUser.getSelectedRoutine());
            controllerPersistence.setSelectedRoutine(currentUser.getID(), currentUser.getSelectedRoutine().getId());
            loadSelectedRoutine();
        } else {
            routineAdapter.setCurrentRoutine(null);
            currentUser.setSelectedRoutine(null);
            currentUser.clearStatistics();
            controllerPersistence.setSelectedRoutine(currentUser.getID(),"");
            if(this.stats!=null) this.stats.setRoutineStats();
        }
    }

    /**
     * Metode per carregar la rutina seleccionada
     */
    public void loadSelectedRoutine() {
        try {
            routineAdapter.clearActivities();
            Method method = DomainAdapter.class.getMethod("loadSelectedRoutineCallback", HashMap.class);
            Method method1 = DomainAdapter.class.getMethod("loadStatisticsCallback", Map.class);
            controllerPersistence.getActivitiesRoutine(currentUser.getID(), currentUser.getSelectedRoutine().getId(), method, DomainAdapter.getInstance());
            controllerPersistence.getAllStatisticsRoutine(currentUser.getID(), currentUser.getSelectedRoutine().getId(), method1, DomainAdapter.getInstance());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metode de callback dels estadistics
     * @param stats estadistics
     */
    public void loadStatisticsCallback(Map<String, Map<String, Double>> stats){
        if(stats != null) {
            Map<Theme,Map<Day, Double>> statistics = new TreeMap<>();
            for(Map.Entry<String,Map<String,Double>> themeStats : stats.entrySet()) {
                Map<Day, Double> s = new TreeMap<>();
                for (Map.Entry<String, Double> dayStats : themeStats.getValue().entrySet()) {
                    Day day = Day.valueOf(dayStats.getKey());
                    s.put(day, dayStats.getValue());
                }
                Theme theme = Theme.valueOf(themeStats.getKey());
                statistics.put(theme,s);
            }
            currentUser.setStatisticsSelectedRoutine(statistics);
        }
        else
            currentUser.clearStatistics();

        if(this.stats!=null) this.stats.setRoutineStats();
    }

    /**
     * Metode per rebre les activitats de la rutina seleccionada
     * @param activitiesList activitats de la rutina
     * @throws InvalidTimeIntervalException si l'interval de temps no és valid
     */
    public void loadSelectedRoutineCallback(HashMap<String, ArrayList<ArrayList<String>>> activitiesList) throws InvalidTimeIntervalException {
        for (HashMap.Entry<String, ArrayList<ArrayList<String>>> dayActivities : activitiesList.entrySet()) {
            ArrayList<ArrayList<String>> activities = dayActivities.getValue();
            if (!activities.isEmpty()) {
                ArrayList<Activity> acts = new ArrayList<>();
                for (int i = 0; i < activities.size(); ++i) {
                    String[] s = activities.get(i).get(5).split(":");
                    String[] s2 = activities.get(i).get(6).split(":");
                    int iniH = Integer.parseInt(s[0]);
                    int iniM = Integer.parseInt(s[1]);
                    int endH = Integer.parseInt(s2[0]);
                    int endM = Integer.parseInt(s2[1]);
                    Activity activity = new Activity(activities.get(i).get(1), activities.get(i).get(2), Theme.valueOf(activities.get(i).get(3)), new TimeInterval(iniH, iniM, endH, endM), Day.valueOf(activities.get(i).get(4)));
                    activity.setId(activities.get(i).get(0));
                    activity.setDoneToday((activities.get(i).get(7)).equals("true"));
                    acts.add(activity);
                }
                routineAdapter.setActivitiesByDay(acts, acts.get(0).getDay());
            }
        }

        if (!selectingRoutine) {
            if (login != null) {
                login.loginCallback();
            }
            else if (logoScreen != null) {
                logoScreen.loginCallback();
            } else {
                MainActivity.getInstance().setEditRoutineScreen(currentUser.getSelectedRoutine().getId(),currentUser.getSelectedRoutine().getName());
            }
            login = null;
            logoScreen = null;
        }
    }

    /**
     * Metode que es crida quan sobte els trofeus
     * @param trophies llistat de trofeus obtinguts
     */
    public void getAchievementsCallback(ArrayList<String> trophies){
        for (int i = 0; i < trophies.size(); ++i) {
            currentUser.addAchievement(Achievement.valueOf(trophies.get(i)));
        }
        Trophies.getInstance().getObtainedTrophyList();
    }

    /**
     * Metode per canviar el nom d'una rutina existent
     * @param id identificador de la rutina
     * @param name nou nom de la rutina
     * @throws ExistingRoutineException si l'usuari ja té una altra rutina amb aquest nom
     */
    public void changeRoutineName(String id, String name) throws ExistingRoutineException {
        if(!currentUser.existsRoutine(name)) {
            controllerPersistence.changeRoutineName(currentUser.getID(), id, name, currentUser.getSelectedRoutine().isShared());
            routineAdapter.changeName(id, name);
            currentUser.changeRoutineName(id,name);
        } else {
            throw new ExistingRoutineException();
        }
    }

    /**
     * Metode per esborrar una rutina existent
     * @param routineId identificador de la rutina
     */
    public void deleteRoutine(String routineId) {
        currentUser.deleteRoutine(routineId);
        controllerPersistence.deleteRoutine(currentUser.getID(), routineId, currentUser.getSelectedRoutine().isShared());
    }

    /**
     * Crear una activitat
     * @param name nom de l'activitat
     * @param description descripcio de l'activitat
     * @param theme tema de l'activitat
     * @param startDayString dia d'inici de l'activitat
     * @param endDayString dia de fi de l'activitat
     * @param iniH hora d'inici de l'activitat
     * @param iniM minut d'inici de l'activitat
     * @param endH hora de fi de l'activitat
     * @param endM minut de fi de l'activitat
     * @throws InvalidTimeIntervalException es llença si el temps d'inici no es anterior al temps de fi
     * @throws InvalidDayIntervalException es llença si el dia de fi es anterior al dia d'inici
     * @throws OverlappingActivitiesException la nova activitat es solapa amb altres
     * @throws NoSelectedRoutineException si l'usuari no té cap rutina seleccionada
     */
    public void createActivity(String name, String description, String theme, String startDayString, String endDayString, String iniH, String iniM, String endH, String endM) throws InvalidTimeIntervalException, InvalidDayIntervalException, OverlappingActivitiesException, NoSelectedRoutineException {
        Day startDay = Day.values()[Integer.parseInt(startDayString)];
        Day endDay = Day.values()[Integer.parseInt(endDayString)];
        int comparison = startDay.compareTo(endDay);
        if (comparison < 0) {
            Activity newActDay1 = new Activity(name, description, Theme.values()[Integer.parseInt(theme)], new TimeInterval (Integer.parseInt(iniH), Integer.parseInt(iniM), 23, 59), startDay);
            Activity newActDay2 = new Activity(name, description, Theme.values()[Integer.parseInt(theme)], new TimeInterval (0, 0, Integer.parseInt(endH), Integer.parseInt(endM)), endDay);
            if(!routineAdapter.checkOverlappings(newActDay1) && !routineAdapter.checkOverlappings(newActDay2)) {
                String beginTime = iniH + ":" + iniM;
                String endTime = "23:59";
                String id = controllerPersistence.createActivity(currentUser.getID(), currentUser.getSelectedRoutine().getId(), name, Theme.values()[Integer.parseInt(theme)].toString(), description, startDay.toString(), beginTime, endTime, currentUser.getSelectedRoutine().isShared());
                newActDay1.setId(id);
                routineAdapter.createActivity(newActDay1);
                currentUser.updateStatistics(Theme.values()[Integer.parseInt(theme)],startDay, 23-Integer.parseInt(iniH), 59-Integer.parseInt(iniM), true);
                beginTime = "00:00";
                endTime = endH + ":" + endM;
                id = controllerPersistence.createActivity(currentUser.getID(), currentUser.getSelectedRoutine().getId(), name, Theme.values()[Integer.parseInt(theme)].toString(), description, endDay.toString(), beginTime, endTime, currentUser.getSelectedRoutine().isShared());
                newActDay2.setId(id);
                routineAdapter.createActivity(newActDay2);
                currentUser.updateStatistics(Theme.values()[Integer.parseInt(theme)],endDay, Integer.parseInt(endH), Integer.parseInt(endM), true);
            } else throw new OverlappingActivitiesException();
        }
        else if (comparison == 0) {
            Activity a = new Activity(name, description, Theme.values()[Integer.parseInt(theme)], new TimeInterval (Integer.parseInt(iniH), Integer.parseInt(iniM), Integer.parseInt(endH), Integer.parseInt(endM)), startDay);
            if(!routineAdapter.checkOverlappings(a)) {
                String beginTime = iniH + ":" + iniM;
                String endTime = endH + ":" + endM;
                String id = controllerPersistence.createActivity(currentUser.getID(), currentUser.getSelectedRoutine().getId(), name, Theme.values()[Integer.parseInt(theme)].toString(), description, startDay.toString(), beginTime, endTime, currentUser.getSelectedRoutine().isShared());
                a.setId(id);
                routineAdapter.createActivity(a);
                currentUser.updateStatistics(Theme.values()[Integer.parseInt(theme)],startDay, Integer.parseInt(endH)-Integer.parseInt(iniH), Integer.parseInt(endM)-Integer.parseInt(iniM), true);
            } else throw new OverlappingActivitiesException();
        }
        else throw new InvalidDayIntervalException();

    }

    /**
     * Actualitzar els parametres d'una activitat d'una rutina
     * @param name nou nom de l'activitat
     * @param description nova descripcio de l'activitat
     * @param theme nou tema de l'activitat
     * @param startDayString nou dia d'inici de l'activitat
     * @param endDayString nou dia de fi de l'activitat
     * @param iniH nova hora d'inici de l'activitat
     * @param iniM nous minuts d'inici de l'activitat
     * @param endH nova hora de fi de l'activitat
     * @param endM nous minuts de fi de l'activitat
     * @throws InvalidTimeIntervalException es llença si el temps d'inici no es anterior al temps de fi
     * @throws InvalidDayIntervalException es llença si el dia d'inici es posterior al dia de fi
     * @throws OverlappingActivitiesException la nova activitat es solapa amb altres
     * @throws NoSelectedRoutineException si l'usuari no té cap rutina seleccionada
     */
    public void updateActivity(String id, String name, String description, String theme, String startDayString, String endDayString, String iniH, String iniM, String endH, String endM) throws InvalidDayIntervalException, InvalidTimeIntervalException, OverlappingActivitiesException, NoSelectedRoutineException {
        Day startDay = Day.values()[Integer.parseInt(startDayString)];
        Day endDay = Day.values()[Integer.parseInt(endDayString)];
        int comparison = startDay.compareTo(endDay);
        Activity oldActivity = currentUser.getSelectedRoutine().getActivity(id);
        Time duration = oldActivity.getInterval().getIntervalDuration();
        currentUser.updateStatistics(oldActivity.getTheme(),oldActivity.getDay(),duration.getHours(),duration.getMinutes(),false);
        if (comparison < 0) {
            Activity updatedActivity1 = new Activity(name, description, Theme.values()[Integer.parseInt(theme)], new TimeInterval(Integer.parseInt(iniH), Integer.parseInt(iniM), 23, 59), startDay);
            updatedActivity1.setId(id);
            routineAdapter.updateActivity(updatedActivity1);
            currentUser.updateStatistics(Theme.values()[Integer.parseInt(theme)],startDay, 23-Integer.parseInt(iniH), 59-Integer.parseInt(iniM), true);
            String beginTime = iniH + ":" + iniM;
            String endTime = "23:59";
            controllerPersistence.updateActivity(currentUser.getID(), currentUser.getSelectedRoutine().getId(), id, name, description, startDay.toString(), Theme.values()[Integer.parseInt(theme)].toString(), beginTime, endTime, currentUser.getSelectedRoutine().isShared());
            createActivity(name, description, theme, endDayString, endDayString, "0","0", endH, endM);
        }
        else if (comparison == 0) {
            Activity updatedActivity = new Activity(name, description, Theme.values()[Integer.parseInt(theme)], new TimeInterval(Integer.parseInt(iniH), Integer.parseInt(iniM), Integer.parseInt(endH), Integer.parseInt(endM)), startDay);
            updatedActivity.setId(id);
            routineAdapter.updateActivity(updatedActivity);
            currentUser.updateStatistics(Theme.values()[Integer.parseInt(theme)],startDay, Integer.parseInt(endH)-Integer.parseInt(iniH), Integer.parseInt(endM)-Integer.parseInt(iniM), true);
            String beginTime = iniH + ":" + iniM;
            String endTime = endH + ":" + endM;
            controllerPersistence.updateActivity(currentUser.getID(), currentUser.getSelectedRoutine().getId(), id, name, description, startDay.toString(), Theme.values()[Integer.parseInt(theme)].toString(), beginTime, endTime, currentUser.getSelectedRoutine().isShared());
        }
        else throw new InvalidDayIntervalException();
    }

    /**
     * Eliminar una activitat
     * @param id identificador de l'activitat
     * @param day dia de l'activitat
     * @throws NoSelectedRoutineException si l'usuari no té cap rutina seleccionada
     */
    public void deleteActivity(String id, String day) throws NoSelectedRoutineException {
        Activity deleted = currentUser.getSelectedRoutine().getActivity(id);
        Time duration = deleted.getInterval().getIntervalDuration();
        currentUser.updateStatistics(deleted.getTheme(),deleted.getDay(),duration.getHours(),duration.getMinutes(),false);
        routineAdapter.deleteActivity(id, Day.valueOf(day));
        controllerPersistence.deleteActivity(currentUser.getID(), currentUser.getSelectedRoutine().getId(), id, currentUser.getSelectedRoutine().isShared());
    }

    /**
     * Metode per marcar una activitat com a feta
     * @param activityID identificador de l'activitat
     * @param isDone boolea que indica si es marca o desmarca
     */
    public void markActivityAsDone(String activityID, boolean isDone, int day) {
        boolean wasDone = false;
        if(routineAdapter.isCompleted(Day.values()[day])) wasDone = true;
        routineAdapter.markActivityAsDone(activityID, isDone);
        if (isDone) {
            if(routineAdapter.isCompleted(Day.values()[day])) {
                currentUser.setStreak(currentUser.getStreak()+1);
            }
            controllerPersistence.markActivityAsDone(currentUser.getID(), currentUser.getSelectedRoutine().getId(), StringDateConverter.dateToString(Calendar.getInstance().getTime()), activityID, currentUser.getSelectedRoutine().getActivitiesByDay(Day.values()[day]).size());
        } else {
            if(wasDone) currentUser.setStreak(currentUser.getStreak()-1);
            controllerPersistence.markActivityAsDone(currentUser.getID(), currentUser.getSelectedRoutine().getId(), null, activityID, currentUser.getSelectedRoutine().getActivitiesByDay(Day.values()[day]).size());
        }

    }

    /**
     * Obtenir la posicio del tema al spinner
     * @param element nom del tema
     * @return posicio del tema al spinner
     */
    public int getPositionTheme(String element) {
        for (int i =0; i<Theme.values().length; ++i) {
            if (element.equals(Theme.values()[i].toString())) return i;
        }
        return 0;
    }

    /**
     * Obtenir la posició del dia al spinner
     * @param element nom del dia
     * @return posició del dia al spinner
     */
    public int getPositionDay(String element) {
        for (int i =0; i<Day.values().length; ++i) {
            if (element.equals(Day.values()[i].toString())) return i;
        }
        return 0;
    }

    /**
     * Metode per penjar una foto de perfil des de la galeria de l'usuari
     * @param imageUri uri de la imatge a penjar
     * @param imageBm Bitmap de la imatge
     */
    public void updateProfilePic(Uri imageUri, Bitmap imageBm) {
        controllerPersistence.updateProfilePic(currentUser.getID(), imageUri);
        currentUser.setPFP(imageBm);
    }

    /**
     * Metode per carregar la informacio d'un usuari
     * @return el nom d'usuari i la seva foto de perfil
     */
    public ArrayList<Object> loadUserInfo() {
        ArrayList<Object> res = new ArrayList<>();
        res.add(currentUser.getName());
        Bitmap pfp = currentUser.getProfilePic();
        res.add(pfp);
        return res;
    }

    /**
     * Metode per obtenir la llista de rutines de l'usuari
     * @return identificador i nom de les rutines de l'usuari
     */
    public ArrayList<ArrayList<String>> getUserRoutines() {
        return currentUser.getRoutines();
    }

    /**
     * Metode per obtenir l'identificador rutina seleccionada de l'usuari
     * @return identificador de la rutina
     */
    public String getSelectedRoutineId() {
        if (currentUser.getSelectedRoutine() != null) return currentUser.getSelectedRoutine().getId();
        else return "";
    }

    /**
     * Metode per obtenir les activitats d'un dia de la rutina seleccionada
     * @param weekDay dia de la setmana
     * @return activitats del dia
     * @throws NoSelectedRoutineException si l'usuari no té cap rutina seleccionada
     */
    public ArrayList<ArrayList<String>> getActivitiesByDay(String weekDay) throws NoSelectedRoutineException {
        return routineAdapter.getActivitiesByDay(weekDay);
    }

    /**
     * Metode per recuperar la contrasenya d'un usuari
     * @param mail mail del compte a recuperar
     * @param act instància de l'activitat que solicita la recuperació de contrasenya
     */
    public void sendPassResetEmail(String mail, android.app.Activity act) {
        forgotPass = (ForgotPassword)act;
        try {
            Method method = DomainAdapter.class.getMethod("passResetCallback", boolean.class);
            controllerPersistence.sendPassResetEmail(mail, method, this);
        } catch (Exception ignore) {}
    }

    /**
     * Metode per rebre la resposta de la base de dades del mail de recuperacio de la contrasenya
     * @param success resultat de l'operacio
     */
    public void passResetCallback(boolean success) {
        forgotPass.passResetCallback(success);
    }

    /**
     * Metode per obtenir totes les rutines compartides del sistema
     */
    public void getSharedRoutines(){
        try {
            Method method = DomainAdapter.class.getMethod("sharedRoutinesCallback", ArrayList.class);
            controllerPersistence.getSharedRoutines(method, DomainAdapter.getInstance());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metode que rep la resposta a la crida que "getSharedRoutines" fa a la base de dades
     * @param sharedRoutinesInfo ArrayList de informacio de les rutines compartides, representada com una ArrayList d'Objects que conte a cada posicio:
     *                           0 - String dmb la ID de la rutina compartida (ID autor + ID rutina)
     *                           1 - String amb el nom de la rutina
     *                           2 - Array amb les IDs dels usuaris que han votat la rutina
     *                           3 - Puntacio mitjana de la rutina
     *                           4 - Nombre d'usuaris que han votat la rutina
     */
    public void sharedRoutinesCallback(ArrayList<ArrayList<Object>> sharedRoutinesInfo){
        for(ArrayList<Object> routineInfo : sharedRoutinesInfo){
            ArrayList<String> usersVoted = (ArrayList<String>) routineInfo.get(2);
            boolean hasVoted = usersVoted.contains(currentUser.getID());
            routineInfo.set(2, hasVoted);
        }
        Community.getInstance().getSharedRoutinesCallback(sharedRoutinesInfo);
    }

    /**
     * Metode per descarregar una rutina compartida al sistema
     * @param sharedRoutineID ID de la rutina a descarregar
     */
    public void downloadSharedRoutine(String sharedRoutineID, String sharedRoutineName) throws RoutinePrimaryKeyException {
        if(currentUser.hasRoutineWithName(sharedRoutineName))
            throw new RoutinePrimaryKeyException();
        controllerPersistence.downloadSharedRoutine(currentUser.getID(), sharedRoutineID);
        ArrayList<String> routine = new ArrayList<>();
        routine.add(0, sharedRoutineID);
        routine.add(1, sharedRoutineName);
        routine.add(2, "false");
        currentUser.addRoutine(routine);
    }

    /**
     * Metode per valorar una rutina compartida al sistema
     * @param sharedRoutineID ID de la rutina a descarregar
     * @param points puntuacio donada
     * @param currentAverage puntacio mitjana de la rutina abans d'aquesta votacio
     * @param numberOfUsersVoted usuaris que han votat la rutina abams d'aquesta votacio
     */
    public void voteRoutine(String sharedRoutineID, int points, double currentAverage, int numberOfUsersVoted){
        double average = ((currentAverage * numberOfUsersVoted) + points) / (numberOfUsersVoted + 1);
        controllerPersistence.voteRoutine(currentUser.getID(), sharedRoutineID, average);
    }

    /**
     * Metode per compartir una rutina
     * @param routineID ID de la rutina a compartir
     * @param isPublic boolea que es cert si es publica la rutina i fals si es fa privada
     */
    public void shareRoutine(String routineID, boolean isPublic){
        if(isPublic){
            controllerPersistence.shareRoutine(currentUser.getID(), routineID);
            currentUser.shareRoutine(routineID);
        }
        else{
            controllerPersistence.unShareRoutine(currentUser.getID(), routineID);
            currentUser.unShareRoutine(routineID);
        }
    }

    public void getSharedRoutineActivities(String sharedRoutineID){
        try {
            Method method = DomainAdapter.class.getMethod("getSharedRoutineActivitiesCallback", HashMap.class);
            controllerPersistence.getActivitiesSharedRoutine(sharedRoutineID,method,DomainAdapter.getInstance());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void getSharedRoutineActivitiesCallback(HashMap<String, ArrayList<ArrayList<String>>> activitiesList) throws InvalidTimeIntervalException {
        CommunityRecyclerAdapter.getInstance().getSharedRoutineActivitiesCallback(activitiesList);
    }

    public ArrayList<Integer> getRecommendations(){
        ArrayList<Integer> recommendations = new ArrayList<>();
        Time totalTime;
        int moreThanMin;
        int lessThanMax;

        //Music
        totalTime = currentUser.getSelectedRoutine().getTotalTimeTheme(Theme.Music);
        moreThanMin = totalTime.compareTo(new Time(0, 0));
        lessThanMax = totalTime.compareTo(new Time(0, 0));
        if(moreThanMin < 0){
            recommendations.add(0, -1);
        }
        else if(lessThanMax > 0){
            recommendations.add(0, 1);
        }
        else{
            recommendations.add(0, 0);
        }

        return recommendations;
    }

}
