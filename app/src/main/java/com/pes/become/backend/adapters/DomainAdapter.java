package com.pes.become.backend.adapters;

import com.pes.become.backend.domain.Activity;
import com.pes.become.backend.domain.Day;
import com.pes.become.backend.domain.Routine;
import com.pes.become.backend.domain.Theme;
import com.pes.become.backend.domain.TimeInterval;
import com.pes.become.backend.domain.User;
import com.pes.become.backend.exceptions.InvalidDayIntervalException;
import com.pes.become.backend.exceptions.InvalidTimeIntervalException;
import com.pes.become.backend.exceptions.OverlappingActivitiesException;
import com.pes.become.backend.persistence.ControllerPersistence;
import com.pes.become.frontend.RoutineEdit;
import com.pes.become.frontend.RoutineView;
import com.pes.become.frontend.RoutinesList;

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
     * Instancia de la classe routineEdit del frontend
     */
    private RoutineEdit routineEdit;
    /**
     * Instancia de la classe routineView del frontend
     */
    private RoutineView routineView;
    /**
     * Instancia de la classe routinesList del frontend
     */
    private RoutinesList routinesList;
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

            //Usuari Hardcoded
            currentUser = new User("usuari@usuari.com", "Usuari1");
            currentUser.setID("UsuariIdProva");
            Routine routine = new Routine("Rutina1");
            routine.setId("Z43eSoYskWoYkviKXpEH");
            currentUser.setSelectedRoutine(routine);
            routineAdapter.setCurrentRoutine(routine);

            //aixo no hauria d'anar aqui
            Class[] parameterTypes = new Class[1];
            parameterTypes[0] = ArrayList.class;
            Method method1 = null;
            try {
                method1 = DomainAdapter.class.getMethod("loadAllActivities", ArrayList.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            for(int d = 0; d<7; ++d){
                controllerPersistence.getActivitiesByDay(currentUser.getID(), currentUser.getSelectedRoutine().getId(), Day.values()[d].toString(), method1, DomainAdapter.getInstance());
            }
        }
        return instance;
    }

    /**
     * Metode per registrar un nou usuari
     * @param mail correu de l'usuari
     * @param password contrassenya de l'usuari
     * @param name nom de l'usuari
     */
    public void registerUser(String mail, String password, String name) {
        //controllerPersistence.registerUser(mail,password,name);
        //currentUser = UserAdapter.createUser(mail, name);
    }

    /**
     * Metode per iniciar la sessio d'un usuari existent
     * @param mail correu de l'usuari
     * @param password contrassenya de l'usuari
     * @param act Activity d'Android necessaria per la execucio del firebase
     */
    public void loginUser(String mail, String password, android.app.Activity act) throws NoSuchMethodException {
        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = ArrayList.class;
        Method method1 = DomainAdapter.class.getMethod("loginCallback", parameterTypes);
        controllerPersistence.loginUser(mail, password, act, method1, DomainAdapter.getInstance());
    }

    /**
     * Metode per iniciar sessio amb un compte de Google
     */
    public void loginGoogleUser() {

    }

    /**
     * Metode que rep la resposta a la crida "loginUser" de la base de dades
     * @param infoUser Array d'arrays, on la primera conte la ID, correu, nom de l'usuari i la ID de la rutina seleccionada
     */
    public void loginCallback(ArrayList<String> infoUser) throws NoSuchMethodException {
        currentUser = userAdapter.createUser(infoUser.get(1), infoUser.get(2));
        currentUser.setID(infoUser.get(0));
        selectRoutine(infoUser.get(3));
    }

    /**
     * Metode per tancar la sessio d'un usuari que previament havia iniciat sessio
     */
    public void logoutUser() {
        currentUser = null;
    }

    /**
     * Metode per donar de baixa un compte d'usuari
     * @param mail correu de l'usuari
     * @param password contrassenya de l'usuari
     */
    public void deleteUser(String mail, String password) {
        logoutUser();
        //controllerPersistence.deleteUser(mail, password);
    }

    /**
     * Metode per canviar la contrassenya d'un usuari
     * @param oldPassword contrassenya antiga
     * @param newPassword contrassenya nova
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
    public void createRoutine(String name) {
        String id = controllerPersistence.createRoutine(currentUser.getID(), name);
        currentUser.addRoutine(id);
    }

    /**
     * Metode per seleccionar una rutina ja existent
     * @param idRoutine identificador de la rutina
     */
    public void selectRoutine(String idRoutine) throws NoSuchMethodException {
        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = ArrayList.class;
        Method method1 = DomainAdapter.class.getMethod("setSelectedRoutine", parameterTypes);
        controllerPersistence.getUserRoutine(currentUser.getID(), idRoutine, method1, DomainAdapter.getInstance());
    }

    /**
     * Metode per seleccionar una rutina ja existent
     */
    public void selectRoutine() throws NoSuchMethodException {
        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = ArrayList.class;
        Method method1 = DomainAdapter.class.getMethod("setSelectedRoutine", parameterTypes);
        controllerPersistence.getUserRoutine(currentUser.getID(), currentUser.getSelectedRoutine().getId(), method1, DomainAdapter.getInstance());
    }

    /**
     * Metode per rebre la resposta de la DB a la consulta "getRoutine"
     * @param infoRoutine llista amb la informacio de la rutina
     */
    public void setSelectedRoutine(ArrayList<String> infoRoutine) throws NoSuchMethodException {
        Routine routine = routineAdapter.createRoutine(infoRoutine.get(1));
        routine.setId(infoRoutine.get(0));
        currentUser.setSelectedRoutine(routine);
        routineAdapter.setCurrentRoutine(currentUser.getSelectedRoutine());
        controllerPersistence.setSelectedRoutine(currentUser.getID(), currentUser.getSelectedRoutine().getId());

        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = ArrayList.class;
        Method method1 = DomainAdapter.class.getMethod("loadAllActivities", ArrayList.class);
        for(int d = 0; d < 7; ++d) {
            controllerPersistence.getActivitiesByDay(currentUser.getID(), currentUser.getSelectedRoutine().getId(), Day.values()[d].toString(), method1, DomainAdapter.getInstance());
        }
    }

    /**
     * Metode per rebre la resposta de la DB a la consulta "getActivitiesByDay" i que les carrega a la instancia de rutina
     * @param activities activitats de la rutina
     */
    public void loadAllActivities(ArrayList<ArrayList<String>> activities) throws InvalidTimeIntervalException, OverlappingActivitiesException {
        for(int i=0; i<activities.size(); ++i){
            String[] s = activities.get(i).get(5).split(":");
            String[] s2 = activities.get(i).get(6).split(":");
            int iniH = Integer.parseInt(s[0]);
            int iniM = Integer.parseInt(s[1]);
            int endH = Integer.parseInt(s2[0]);
            int endM = Integer.parseInt(s2[1]);
            Activity activity = new Activity(activities.get(i).get(1), activities.get(i).get(2), Theme.valueOf(activities.get(i).get(3)), new TimeInterval(iniH, iniM, endH, endM), Day.valueOf(activities.get(i).get(4)));
            activity.setId(activities.get(i).get(0));
            routineAdapter.createActivity(activity);
        }
    }

    /**
     * Metode per obtenir totes les rutines de l'usuari
     * @param rl instància de RoutinesList
      */
    public void getUserRoutines(RoutinesList rl) throws NoSuchMethodException {
        routinesList = rl;
        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = ArrayList.class;
        Method method1 = DomainAdapter.class.getMethod("setUserRoutines", parameterTypes);
        controllerPersistence.getUserRoutines(currentUser.getID(), method1, DomainAdapter.getInstance());
    }

    /**
     * Rebre la resposta de la DB amb les rutines de l'usuari per la routineView
     * @param routines rutines
     * @throws InvalidTimeIntervalException l'interval de temps es incorrecte
     * @throws OverlappingActivitiesException la nova activitat es solapa amb altres
     */
    public void setUserRoutines(ArrayList<ArrayList<String>> routines) throws InvalidTimeIntervalException, OverlappingActivitiesException {
        if (!routines.isEmpty()) {
            for (int i = 0; i < routines.size(); ++i) {
                currentUser.addRoutine(routines.get(i).get(0));
            }
            Routine selectedRoutine = currentUser.getSelectedRoutine();
            if(selectedRoutine != null)
                routinesList.getRoutinesCallback(routines, selectedRoutine.getId());
            else
                routinesList.getRoutinesCallback(routines, "");
        }
        else {
            routinesList.getRoutinesCallback(new ArrayList<>(0), "");
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
     * @param id identificador de la rutina
     */
    public void deleteRoutine(String id) {
        currentUser.deleteRoutine(id);
        //controllerPersistence.deleteRoutine(id);
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
     */
    public void createActivity(String name, String description, String theme, String startDayString, String endDayString, String iniH, String iniM, String endH, String endM) throws InvalidTimeIntervalException, InvalidDayIntervalException, OverlappingActivitiesException {
        Day startDay = Day.values()[Integer.parseInt(startDayString)];
        Day endDay = Day.values()[Integer.parseInt(endDayString)];
        int comparison = startDay.compareTo(endDay);
        if (comparison < 0) {
            // activitat dia 1
            Activity newActDay1 = new Activity(name, description, Theme.values()[Integer.parseInt(theme)], new TimeInterval (Integer.parseInt(iniH), Integer.parseInt(iniM), 23, 59), startDay);
            Activity newActDay2 = new Activity(name, description, Theme.values()[Integer.parseInt(theme)], new TimeInterval (0, 0, Integer.parseInt(endH), Integer.parseInt(endM)), endDay);
            if(!routineAdapter.checkOverlappings(newActDay1) && !routineAdapter.checkOverlappings(newActDay2)) {
                String beginTime = iniH + ":" + iniM;
                String endTime = "23:59";
                String id = controllerPersistence.createActivity(currentUser.getID(), currentUser.getSelectedRoutine().getId(), name, Theme.values()[Integer.parseInt(theme)].toString(), description, startDay.toString(), beginTime, endTime);
                newActDay1.setId(id);
                routineAdapter.createActivity(newActDay1);
                // activitat dia 2
                beginTime = "00:00";
                endTime = endH + ":" + endM;
                id = controllerPersistence.createActivity(currentUser.getID(), currentUser.getSelectedRoutine().getId(), name, Theme.values()[Integer.parseInt(theme)].toString(), description, startDay.toString(), beginTime, endTime);
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
     */
    public void updateActivity(String id, String name, String description, String theme, String startDayString, String endDayString, String iniH, String iniM, String endH, String endM) throws InvalidDayIntervalException, InvalidTimeIntervalException, OverlappingActivitiesException {
        Day startDay = Day.values()[Integer.parseInt(startDayString)];
        Day endDay = Day.values()[Integer.parseInt(endDayString)];
        int comparison = startDay.compareTo(endDay);
        if (comparison < 0) {
            // activitat dia 1
            Activity updatedActivity1 = new Activity(name, description, Theme.values()[Integer.parseInt(theme)], new TimeInterval(Integer.parseInt(iniH), Integer.parseInt(iniM), 23, 59), startDay);
            updatedActivity1.setId(id);
            routineAdapter.updateActivity(updatedActivity1);
            String beginTime = iniH + ":" + iniM;
            String endTime = "23:59";
            controllerPersistence.updateActivity(currentUser.getID(), currentUser.getSelectedRoutine().getId(), name, description, Theme.values()[Integer.parseInt(theme)].toString(), beginTime, endTime, startDay.toString(), id);
            // activitat dia 2
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
     * Demanar les activitats d'un dia per la routineView
     * @param dayString dia de les activitats
     * @param rv instància de RoutineView
     * @throws NoSuchMethodException el mètode no existeix
     */
    public void getActivitiesByDayToView(String dayString, RoutineView rv) throws NoSuchMethodException {
        routineView = rv;
        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = ArrayList.class;
        Method method1 = DomainAdapter.class.getMethod("setActivitiesByDayToView", parameterTypes);
        controllerPersistence.getActivitiesByDay(currentUser.getID(), currentUser.getSelectedRoutine().getId(), dayString, method1, DomainAdapter.getInstance());
    }

    /**
     * Rebre la resposta de la DB amb les activitats d'una rutina per la routineView
     * @param acts activitats de la rutina
     * @throws InvalidTimeIntervalException l'interval de temps es incorrecte
     * @throws OverlappingActivitiesException la nova activitat es solapa amb altres
     */
    public void setActivitiesByDayToView(ArrayList<ArrayList<String>> acts) throws InvalidTimeIntervalException, OverlappingActivitiesException {
        if (!acts.isEmpty()) {
            String day = acts.get(0).get(4);
            routineAdapter.clearActivities(Day.valueOf(day));
            for (ArrayList<String> act : acts) {
                String[] s = act.get(5).split(":");
                String[] s2 = act.get(6).split(":");
                int iniH = Integer.parseInt(s[0]);
                int iniM = Integer.parseInt(s[1]);
                int endH = Integer.parseInt(s2[0]);
                int endM = Integer.parseInt(s2[1]);
                Activity activity = new Activity(act.get(1), act.get(2), Theme.valueOf(act.get(3)), new TimeInterval(iniH, iniM, endH, endM), Day.valueOf(act.get(4)));
                activity.setId(act.get(0));
                routineAdapter.createActivity(activity);
            }
            routineView.getActivitiesCallback(routineAdapter.getActivitiesByDay(day));
        }
        else {
            routineView.getActivitiesCallback(new ArrayList<>(0));
        }
    }

    /**
     * Demanar les activitats d'un dia per la routineEdit
     * @param dayString dia de les activitats
     * @param re instància de RoutineEdit
     * @throws NoSuchMethodException el mètode no existeix
     */
    public void getActivitiesByDayToEdit(String dayString, RoutineEdit re) throws NoSuchMethodException {
        routineEdit = re;
        Class[] parameterTypes = new Class[1];
        parameterTypes[0] = ArrayList.class;
        Method method1 = DomainAdapter.class.getMethod("setActivitiesByDayToEdit", parameterTypes);
        controllerPersistence.getActivitiesByDay(currentUser.getID(), currentUser.getSelectedRoutine().getId(), dayString, method1, DomainAdapter.getInstance());
    }

    /**
     * Rebre la resposta de la DB amb les activitats d'una rutina per la routineEdit
     * @param acts activitats de la rutina
     * @throws InvalidTimeIntervalException l'interval de temps es incorrecte
     * @throws OverlappingActivitiesException la nova activitat es solapa amb altres
     */
    public void setActivitiesByDayToEdit(ArrayList<ArrayList<String>> acts) throws InvalidTimeIntervalException, OverlappingActivitiesException {
        if (!acts.isEmpty()) {
            String day = acts.get(0).get(4);
            routineAdapter.clearActivities(Day.valueOf(day));
            for (ArrayList<String> act : acts) {
                String[] s = act.get(5).split(":");
                String[] s2 = act.get(6).split(":");
                int iniH = Integer.parseInt(s[0]);
                int iniM = Integer.parseInt(s[1]);
                int endH = Integer.parseInt(s2[0]);
                int endM = Integer.parseInt(s2[1]);
                Activity activity = new Activity(act.get(1), act.get(2), Theme.valueOf(act.get(3)), new TimeInterval(iniH, iniM, endH, endM), Day.valueOf(act.get(4)));
                activity.setId(act.get(0));
                routineAdapter.createActivity(activity);
            }
            routineEdit.getActivitiesCallback(routineAdapter.getActivitiesByDay(day));
        }
        else {
            routineEdit.getActivitiesCallback(new ArrayList<>(0));
        }
    }

    /**
     * Eliminar una activitat
     * @param id identificador de l'activitat
     * @param day dia de l'activitat
     */
    public void deleteActivity(String id, String day) {
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
}
