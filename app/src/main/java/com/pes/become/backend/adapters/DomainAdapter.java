package com.pes.become.backend.adapters;

import android.graphics.Bitmap;
import android.net.Uri;

import com.pes.become.backend.domain.Activity;
import com.pes.become.backend.domain.Day;
import com.pes.become.backend.domain.Routine;
import com.pes.become.backend.domain.Theme;
import com.pes.become.backend.domain.TimeInterval;
import com.pes.become.backend.domain.User;
import com.pes.become.backend.exceptions.InvalidDayIntervalException;
import com.pes.become.backend.exceptions.InvalidTimeIntervalException;
import com.pes.become.backend.exceptions.NoSelectedRoutineException;
import com.pes.become.backend.exceptions.OverlappingActivitiesException;
import com.pes.become.backend.persistence.ControllerPersistence;
import com.pes.become.frontend.LogoScreen;
import com.pes.become.frontend.Login;
import com.pes.become.frontend.Signup;

import java.lang.reflect.Method;
import java.util.ArrayList;

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
    private static final ControllerPersistence controllerPersistence = new ControllerPersistence();
    /**
     * Unica instancia de l'adaptador de la classe Rutina
     */
    private static final RoutineAdapter routineAdapter = RoutineAdapter.getInstance();
    /**
     * Unica instancia de l'adaptador de la classe Usuari
     */
    private static final UserAdapter userAdapter = UserAdapter.getInstance();
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
     * Usuari autenticat que esta usant l'aplicació actualment
     */
    private static User currentUser;

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
        Method method1;
        signup = (Signup) act;
        try {
            method1 = DomainAdapter.class.getMethod("registerCallback", parameterTypes);
            controllerPersistence.registerUser(mail, password, name, act, method1, DomainAdapter.getInstance());
        } catch (NoSuchMethodException ignore) {}
    }

    /**
     * Metode per carregar un usuari
     * @param act activitat
     */
    public void loadUser (android.app.Activity act) {
        Class[] parameterTypes = new Class[6];
        parameterTypes[0] = boolean.class;
        parameterTypes[1] = String.class;
        parameterTypes[2] = String.class;
        parameterTypes[3] = String.class;
        parameterTypes[4] = Bitmap.class;
        parameterTypes[5] = ArrayList.class;
        Method method1;
        logoScreen = (LogoScreen)act;
        try {
            method1 = DomainAdapter.class.getMethod("authUser", parameterTypes);
            controllerPersistence.loadUser(method1, DomainAdapter.getInstance());
        } catch(NoSuchMethodException ignore) {}
    }

    /**
     * Metode per iniciar la sessio d'un usuari existent
     * @param mail correu de l'usuari
     * @param password contrassenya de l'usuari
     * @param act Activity d'Android necessaria per la execucio del firebase
     */
    public void loginUser(String mail, String password, android.app.Activity act) {
        Class[] parameterTypes = new Class[6];
        parameterTypes[0] = boolean.class;
        parameterTypes[1] = String.class;
        parameterTypes[2] = String.class;
        parameterTypes[3] = String.class;
        parameterTypes[4] = Bitmap.class;
        parameterTypes[5] = ArrayList.class;
        Method method1;
        login = (Login)act;
        try {
            method1 = DomainAdapter.class.getMethod("loginCallback", parameterTypes);
            controllerPersistence.loginUser(mail, password, act, method1, DomainAdapter.getInstance());
        } catch (NoSuchMethodException ignore) {}

    }

    /**
     * Metode per iniciar sessio amb un compte de Google
     * @param idToken token
     * @param act activitat
     */
    public void loginGoogleUser(String idToken, android.app.Activity act) {
        Class[] parameterTypes = new Class[6];
        parameterTypes[0] = boolean.class;
        parameterTypes[1] = String.class;
        parameterTypes[2] = String.class;
        parameterTypes[3] = String.class;
        parameterTypes[4] = Bitmap.class;
        parameterTypes[5] = ArrayList.class;
        Method method1;
        login = (Login)act;

        try {
            method1 = DomainAdapter.class.getMethod("loginCallback", parameterTypes);
            controllerPersistence.loginUserGoogle(idToken, method1, DomainAdapter.getInstance());
        } catch (NoSuchMethodException ignore) {}
    }

    /**
     * Metode que rep la resposta a la crida "loginUser" de la base de dades
     * @param success resultat de l'operacio
     * @param userId identificador de l'usuari
     * @param username nom d'usuari
     * @param selectedRoutineId rutina seleccionada de l'usuari
     * @param pfp imatge de perfil de l'usuari
     * @throws NoSuchMethodException si el metode passat no existeix
     */
    public void loginCallback(boolean success, String userId, String username, String selectedRoutineId, Bitmap pfp, ArrayList<ArrayList<String>> routineInfo) throws NoSuchMethodException {
        if (success) {
            currentUser = userAdapter.createUser(username);
            currentUser.setID(userId);
            currentUser.setPFP(pfp);
            if(!routineInfo.isEmpty()) currentUser.setRoutines(routineInfo);
            ArrayList<String> routine = new ArrayList<>();
            for(ArrayList<String> r : routineInfo) {
                if(r.get(0).equals(selectedRoutineId)) {
                    routine = r;
                    break;
                }
            }
            if(!selectedRoutineId.equals("")) selectRoutine(routine);
            login.loginCallback();
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
     * @throws NoSuchMethodException si el metode passat no existeix
     */
    public void authUser(boolean success, String userId, String username, String selectedRoutineId, Bitmap pfp, ArrayList<ArrayList<String>> routineInfo) throws NoSuchMethodException {
        if (success) {
            currentUser = userAdapter.createUser(username);
            currentUser.setID(userId);
            currentUser.setPFP(pfp);
            if(!routineInfo.isEmpty()) currentUser.setRoutines(routineInfo);
            ArrayList<String> routine = new ArrayList<>();
            for(ArrayList<String> r : routineInfo) {
                if(r.get(0).equals(selectedRoutineId)) {
                    routine = r;
                    break;
                }
            }
            if(!selectedRoutineId.equals("")) selectRoutine(routine);
            logoScreen.loginCallback();
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
     * @throws NoSuchMethodException si el metode passat no existeix
     */
    public void registerCallback(boolean success, String userId, String username, String selectedRoutineId) throws NoSuchMethodException {
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
        controllerPersistence.signOut();
    }

    /**
     * Metode per donar de baixa un compte d'usuari
     * @param mail correu de l'usuari
     * @param password contrasenya de l'usuari
     */
    public void deleteUser(String mail, String password) {
        logoutUser();
        //controllerPersistence.deleteUser(mail, password);
    }

    /**
     * Metode per canviar la contrasenya d'un usuari
     * @param oldPassword contrasenya antiga
     * @param newPassword contrasenya nova
     */
    public void changePassword(String oldPassword, String newPassword) {
        //controllerPersistence.changePassword(oldPassword, newPassword);
    }

    /**
     * Metode per canviar el nom de l'usuari
     * @param newname nou nom de l'usuari
     */
    public void changeUserName(String newname) {
        currentUser.setName(newname);
    }

    /**
     * Crear una rutina
     * @param name nom de la rutina
     */
    public String createRoutine(String name) {

        //check name not existing!!!

        String id = controllerPersistence.createRoutine(currentUser.getID(), name);
        ArrayList<String> routine = new ArrayList<>();
        routine.add(id);
        routine.add(name);
        currentUser.addRoutine(routine);
        return id;
    }

    /**
     * Metode per seleccionar una rutina ja existent
     * @param infoRoutine llista amb la informacio de la rutina a seleccionar
     * @throws NoSuchMethodException si el metode no existeix
     */
    public void selectRoutine(ArrayList<String> infoRoutine) throws NoSuchMethodException {
        Routine routine = routineAdapter.createRoutine(infoRoutine.get(1));
        routine.setId(infoRoutine.get(0));
        currentUser.setSelectedRoutine(routine);
        routineAdapter.setCurrentRoutine(currentUser.getSelectedRoutine());
        controllerPersistence.setSelectedRoutine(currentUser.getID(), currentUser.getSelectedRoutine().getId());
        loadSelectedRoutine();
    }

    /**
     * Metode per carregar la rutina seleccionada
     * @throws NoSuchMethodException si el metode no existeix
     */
    public void loadSelectedRoutine() throws NoSuchMethodException {
        routineAdapter.clearActivities();
        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = ArrayList.class;
        Method method1 = DomainAdapter.class.getMethod("loadSelectedRoutineCallback", parameterTypes);
        for(int d = 0; d < 7; ++d) {
            controllerPersistence.getActivitiesByDay(currentUser.getID(), currentUser.getSelectedRoutine().getId(), Day.values()[d].toString(), method1, DomainAdapter.getInstance());
        }
    }

    /**
     * Metode per rebre les activitats de la rutina seleccionada
     * @param activities activitats de la rutina
     * @throws InvalidTimeIntervalException si l'interval de temps no és valid
     * @throws OverlappingActivitiesException si alguna activitat es solapa amb altres
     * @throws NoSelectedRoutineException si l'usuari no té cap rutina seleccionada
     */
    public void loadSelectedRoutineCallback(ArrayList<ArrayList<String>> activities) throws InvalidTimeIntervalException, OverlappingActivitiesException, NoSelectedRoutineException {
        if(!activities.isEmpty()) {
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
                acts.add(activity);
            }
            routineAdapter.setActivitiesByDay(acts, acts.get(0).getDay());
        }
    }

    /**
     * Metode per canviar el nom d'una rutina existent
     * @param id identificador de la rutina
     * @param name nou nom de la rutina
     */
    public void changeRoutineName(String id, String name) {
        //controllerPersistence.changeName(id, name);
        routineAdapter.changeName(id, name);
    }

    /**
     * Metode per esborrar una rutina existent
     * @param routineId identificador de la rutina
     */
    public void deleteRoutine(String routineId) {
        currentUser.deleteRoutine(routineId);
        controllerPersistence.deleteRoutine(currentUser.getID(), routineId);
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
                String id = controllerPersistence.createActivity(currentUser.getID(), currentUser.getSelectedRoutine().getId(), name, Theme.values()[Integer.parseInt(theme)].toString(), description, startDay.toString(), beginTime, endTime);
                newActDay1.setId(id);
                routineAdapter.createActivity(newActDay1);
                beginTime = "00:00";
                endTime = endH + ":" + endM;
                id = controllerPersistence.createActivity(currentUser.getID(), currentUser.getSelectedRoutine().getId(), name, Theme.values()[Integer.parseInt(theme)].toString(), description, endDay.toString(), beginTime, endTime);
                newActDay2.setId(id);
                routineAdapter.createActivity(newActDay2);
            } else throw new OverlappingActivitiesException();
        }
        else if (comparison == 0) {
            Activity a = new Activity(name, description, Theme.values()[Integer.parseInt(theme)], new TimeInterval (Integer.parseInt(iniH), Integer.parseInt(iniM), Integer.parseInt(endH), Integer.parseInt(endM)), startDay);
            if(!routineAdapter.checkOverlappings(a)) {
                String beginTime = iniH + ":" + iniM;
                String endTime = endH + ":" + endM;
                String id = controllerPersistence.createActivity(currentUser.getID(), currentUser.getSelectedRoutine().getId(), name, Theme.values()[Integer.parseInt(theme)].toString(), description, startDay.toString(), beginTime, endTime);
                a.setId(id);
                routineAdapter.createActivity(a);
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
        if (comparison < 0) {
            Activity updatedActivity1 = new Activity(name, description, Theme.values()[Integer.parseInt(theme)], new TimeInterval(Integer.parseInt(iniH), Integer.parseInt(iniM), 23, 59), startDay);
            updatedActivity1.setId(id);
            routineAdapter.updateActivity(updatedActivity1);
            String beginTime = iniH + ":" + iniM;
            String endTime = "23:59";
            controllerPersistence.updateActivity(currentUser.getID(), currentUser.getSelectedRoutine().getId(), name, description, Theme.values()[Integer.parseInt(theme)].toString(), beginTime, endTime, startDay.toString(), id);
            createActivity(name, description, theme, endDayString, endDayString, "0","0", endH, endM);
        }
        else if (comparison == 0) {
            Activity updatedActivity = new Activity(name, description, Theme.values()[Integer.parseInt(theme)], new TimeInterval(Integer.parseInt(iniH), Integer.parseInt(iniM), Integer.parseInt(endH), Integer.parseInt(endM)), startDay);
            updatedActivity.setId(id);
            routineAdapter.updateActivity(updatedActivity);
            String beginTime = iniH + ":" + iniM;
            String endTime = endH + ":" + endM;
            controllerPersistence.updateActivity(currentUser.getID(), currentUser.getSelectedRoutine().getId(), name, description, Theme.values()[Integer.parseInt(theme)].toString(), beginTime, endTime, startDay.toString(), id);
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
        routineAdapter.deleteActivity(id, Day.valueOf(day));
        controllerPersistence.deleteActivity(currentUser.getID(), currentUser.getSelectedRoutine().getId(), id);
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
     */
    public void updateProfilePic(Uri imageUri) {
        controllerPersistence.updateProfilePic(currentUser.getID(), imageUri);
    }

    public void updateProfilePic(Bitmap imageBm){
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
     * @throws NoSelectedRoutineException
     */
    public ArrayList<ArrayList<String>> getActivitiesByDay(String weekDay) throws NoSelectedRoutineException {
        return routineAdapter.getActivitiesByDay(weekDay);
    }
}
