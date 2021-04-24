package com.pes.become.backend.adapters;

import com.pes.become.backend.domain.User;

public class UserAdapter {

    /**
     * Unica instancia de la classe
     */
    private static UserAdapter instance;

    /**
     * Obtenir la instancia de la classe
     * @return instancia
     */
    public static UserAdapter getInstance() {
        if(instance == null) {
            instance = new UserAdapter();
        }
        return instance;
    }

    public User createUser(String name){
        return new User(name);
    }

}
