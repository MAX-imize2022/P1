package com.example.maxapp;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class Profile extends AppCompatActivity {

    Button btnUF;
    Button btnSos;
    Button btnScan;
    Button btnLunch;
    Button btnSchedule;
    private static final int REQUEST_CALL = 1;
    public String CurrentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        CurrentUser = DBref.Auth.getCurrentUser().getEmail().replace('.',' ') ;

        btnSchedule = findViewById(R.id.btnTimeTable);
        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Schedule.class);
                startActivity(intent);
            }
        });

        btnLunch = findViewById(R.id.btnLunch);
        btnLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Lunch.class);
                startActivity(intent);
            }
        });

        btnUF = findViewById(R.id.btnUF);
        btnUF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, UploadFile.class);
                startActivity(intent);
            }
        });

        btnSos = findViewById(R.id.btnSos);
        btnSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( ContextCompat.checkSelfPermission(Profile.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(Profile.this,new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                }else {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:123456789"));
                    startActivity(callIntent);
                }



            }
        });

        btnScan = findViewById(R.id.btnScanner);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Profile.this, scanner.class);
                startActivity(intent1);
            }
        });

    }


    public boolean onOptionsItemSelected(MenuItem item) {

        Intent goToNextActivity = new Intent(getApplicationContext(), Profile.class);

        switch (item.getItemId()) {
            case R.id.editprofile:
                goToNextActivity.setClass(getApplicationContext(), EditProfile.class);
                startActivity(goToNextActivity);
                break;
            case R.id.logout:
                DBref.Auth.signOut();
                goToNextActivity.setClass(getApplicationContext(), MainActivity.class);
                startActivity(goToNextActivity);

        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.nenu, menu);
        return true;
    }
}