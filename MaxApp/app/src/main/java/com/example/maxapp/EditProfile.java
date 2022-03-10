package com.example.maxapp;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfile extends AppCompatActivity {

    ImageView profilePic;
    EditText etBio;
    Button btnSave;
    TextView txtBio;
    public  String CurrentUser = DBref.Auth.getCurrentUser().getEmail().replace('.',' ') ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        txtBio = findViewById(R.id.bio);
        etBio = findViewById(R.id.etBio);
        profilePic = findViewById(R.id.profileImage);




        DBref.refUsers.child(CurrentUser).child("bio").addValueEventListener(new ValueEventListener() {
           @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               Log.i(TAG, dataSnapshot.getValue(String.class));
                txtBio.setText( "חשוב לי שתדעו ש: " + dataSnapshot.getValue() );
           }
            @Override
           public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
       });

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = etBio.getText().toString();
                DBref.refUsers.child(CurrentUser).child("bio").setValue(info);
                txtBio.setText( "חשוב לי שתדעו ש: " + info );
                Toast.makeText(EditProfile.this, "נשמר", Toast.LENGTH_SHORT).show();
            }
        });



        DBref.refStorage.child(CurrentUser).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
           Picasso.with(EditProfile.this)
                   .load(uri)
                   .into(profilePic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), 100);

            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {
                Uri uri = data.getData();
                if (null != uri) {
                    ((ImageView) findViewById(R.id.profileImage)).setImageURI(uri);
                    String UserImageProfile=DBref.Auth.getCurrentUser().getEmail();
                    String email=UserImageProfile.replace('.',' ');

                    DBref.refStorage.child("/"+email).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            Toast.makeText(EditProfile.this,"save!!",Toast.LENGTH_SHORT).show();
                        }
                    }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(EditProfile.this,"error while saving ",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }

}