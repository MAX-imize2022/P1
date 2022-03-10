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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;


public class Reg extends AppCompatActivity {

    EditText etNewEmail;
    EditText etNewPassword;
    EditText etxName;
    Button btnReg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        etNewEmail = findViewById(R.id.etNewEmail);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnReg = findViewById(R.id.btnReg);
        etxName = findViewById(R.id.etxName);


        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    RegisterUser();
            }
        });

    }


    private void RegisterUser() {
        final String email=etNewEmail.getText().toString();
        final String password=etNewPassword.getText().toString();
        final String name = etxName.getText().toString();

        final ProgressDialog pd = ProgressDialog.show(this, "מבצע הרשמה" ,"הרשמה מתבצעת...", true);
        pd.show();

        DBref.Auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener
                (this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Users u=new Users(email,password,name);
                            DBref.refUsers.child(email.replace('.',' ')).setValue(u);
                            Toast.makeText(Reg.this, "הרשמה בוצעה בהצלחה ", Toast.LENGTH_LONG).show();
                            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID", "YOUR_CHANNEL_NAME", NotificationManager.IMPORTANCE_DEFAULT);
                                channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DESCRIPTION");
                                mNotificationManager.createNotificationChannel(channel); }
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID").setSmallIcon(R.drawable.ic_code_scanner_flash_on).setContentText("ההרשמה בוצעה בהצלחה") .setAutoCancel(true);

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            PendingIntent pi = PendingIntent.getActivity(Reg.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            mBuilder.setContentIntent(pi);
                            mNotificationManager.notify(0, mBuilder.build());


                            Intent i = new Intent(Reg.this,Profile.class);
                            startActivity(i);

                        }
                        else
                        {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                Toast.makeText(Reg.this,"אימייל בשימוש ", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(Reg.this, "שגיאה בהרשמה ", Toast.LENGTH_LONG).show();
                            pd.dismiss();
                        }
                    }
                });

    }

}
