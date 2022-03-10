package com.example.maxapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import java.util.Objects;


public class scanner extends AppCompatActivity {

    private CodeScanner codeScanner;
    public String CurrentUser = Objects.requireNonNull(Objects.requireNonNull(DBref.Auth.getCurrentUser()).getEmail()).replace('.',' ');
    final int REQUEST_PERMISSION_CODE = 1000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        Objects.requireNonNull(getSupportActionBar()).hide();

        if (!checkPermissionFromDevice())
            requestPermission();


        CodeScannerView scannerView = findViewById(R.id.scanner);
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });
        codeScanner = new CodeScanner(this,scannerView);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(result.getText().toString().equals("max")){
                            DBref.refUsers.child(CurrentUser).child("arrive").setValue(true);
                            Toast.makeText(getApplicationContext(), "שמחים שהגעת!! ", Toast.LENGTH_LONG).show();
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    synchronized (this) {
                                        wait(3000);
                                        finish();
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                Intent intent = new Intent(scanner.this, Profile.class);
                                startActivity(intent);


                            }
                        }).start();
                        if(!(result.getText().toString().equals("max"))){
                            Toast.makeText(getApplicationContext(), "כשל בסריקה ", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.CAMERA
        }, REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private boolean checkPermissionFromDevice() {
        int camera_result = ContextCompat.
                checkSelfPermission(this, Manifest.permission.CAMERA);
        return camera_result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }
}

