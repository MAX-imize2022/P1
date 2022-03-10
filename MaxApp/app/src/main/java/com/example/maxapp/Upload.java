package com.example.maxapp;

import com.google.firebase.database.Exclude;

public class Upload {
    private String Name = "no name";
    private String mImageUrl;
    private String mKey;
    private String EmailUserKey;
    private  String uploadKey;

    public Upload(String name, String imageUrl) {
        if ( ! name.trim().equals(" ")) {
            Name = name;
        }
        mImageUrl = imageUrl;
    }

    public Upload() { }

    public String getUploadKey() {
        return uploadKey;
    }

    public void setUploadKey(String uploadKey) {
        this.uploadKey = uploadKey;
    }

    public String getEmailUserKey() {
        return EmailUserKey;
    }

    public void setEmailUserKey(String emailUser) {
        EmailUserKey = emailUser;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
}

    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String key) {
        mKey = key;
    }
}
