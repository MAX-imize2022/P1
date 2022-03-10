package com.example.maxapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class DBref {
    public static FirebaseAuth Auth = FirebaseAuth.getInstance();
    public static FirebaseDatabase DataBase = FirebaseDatabase.getInstance();
    public static FirebaseStorage storage = FirebaseStorage.getInstance();
    public static DatabaseReference refUsers = DataBase.getReference("Users");
    public static StorageReference refStorage = storage.getReference("profilePic");
    public static DatabaseReference uploadRef =  FirebaseDatabase.getInstance().getReference("uploads");
    public static StorageReference uploadsStorageRef =  FirebaseStorage.getInstance().getReference("uploads");
}