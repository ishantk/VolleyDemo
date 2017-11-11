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

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
