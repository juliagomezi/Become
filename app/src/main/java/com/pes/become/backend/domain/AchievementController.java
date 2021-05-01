package com.pes.become.backend.domain;

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

    public void setCurrentUser(User currentUser){
        this.currentUser = currentUser;
    }
}
