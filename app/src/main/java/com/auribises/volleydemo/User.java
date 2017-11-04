package com.auribises.volleydemo;

import java.io.Serializable;

/**
 * Created by ishantkumar on 03/11/17.
 */

public class User implements Serializable{

    int uid;
    String name;
    String phone;
    String email;

    public User(){

    }

    public User(int uid, String name, String phone, String email) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
}
