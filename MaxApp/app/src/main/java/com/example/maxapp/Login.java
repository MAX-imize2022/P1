package com.example.maxapp;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.Objects;

public class Login extends AppCompatActivity {

    EditText etEmail;
    EditText etPassword;
    Button btnLogin;
    public String CurrentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();




        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);





        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });


    }

    private void Login(){
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        final ProgressDialog pd = ProgressDialog.show(this, "מתחבר" ,"...אנא המתן ",true);
        pd.show();
        DBref.Auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(Login.this, "מחובר", Toast.LENGTH_SHORT).show();
                            CurrentUser = Objects.requireNonNull(Objects.requireNonNull(DBref.Auth.getCurrentUser()).getEmail()).replace('.',' ');


                            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID", "YOUR_CHANNEL_NAME", NotificationManager.IMPORTANCE_DEFAULT);
                                channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DESCRIPTION");
                                mNotificationManager.createNotificationChannel(channel); }

                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID").setSmallIcon(R.drawable.ic_code_scanner_flash_on).setContentText("ההתחברות בוצעה בהצלחה") .setAutoCancel(true);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            PendingIntent pi = PendingIntent.getActivity(Login.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            mBuilder.setContentIntent(pi);
                            mNotificationManager.notify(0, mBuilder.build());

                            Intent i = new Intent(Login.this,Profile.class);
                            startActivity(i);




                        }
                        else {
                            Toast.makeText(Login.this, "משתמש לא קיים", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this,HomePage.class);
                            startActivity(intent);
                        }
                    }
                });
    }

}