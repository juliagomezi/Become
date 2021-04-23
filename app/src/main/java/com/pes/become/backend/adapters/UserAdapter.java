package com.pes.become.backend.adapters;

import com.pes.become.backend.domain.Routine;
import com.pes.become.backend.domain.User;

import java.util.ArrayList;

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

    public User createUser(String mail, String name){
        return new User(mail, name);
    }

    /**
     * Metode per encriptar contrassenyes
     * @param pswd
     * @return
     */
    /*private String encryptPassword(String pswd) {

    }*/

}
