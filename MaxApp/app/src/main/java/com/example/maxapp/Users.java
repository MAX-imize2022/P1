package com.example.maxapp;

import java.io.Serializable;
import java.util.HashMap;

public class Users implements Serializable {
    private String Email;
    private String Password;
    private String Name;
    private String bio;
    private Boolean arrive;
    private HashMap<String,Object> uploadKey = new HashMap<>();

    public Users(String userName, String password, String Name) {
        this.Email = userName;
        this.Password = password;
        this.Name = Name;
        this.bio = "no info";
        this.arrive = false;

    }

    public Users() { }

    public Boolean getArrive() {
        return arrive;
    }

    public void setArrive(Boolean arrive) {
        this.arrive = arrive;
    }

    public HashMap<String,Object> getUploadKey(){
        return uploadKey;
    }

    public void setUploadKey(HashMap<String,Object> postKey){
        this.uploadKey = postKey;
    }

    public void addUploadKey (String postKey){
        this.uploadKey.put(postKey,true);
        }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserName() {
        return Email;
    }

    public void setUserName(String userName) {
        Email = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}

