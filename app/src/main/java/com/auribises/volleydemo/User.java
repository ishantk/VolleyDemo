package com.auribises.volleydemo;

/**
 * Created by ishantkumar on 03/11/17.
 */

public class User {

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
