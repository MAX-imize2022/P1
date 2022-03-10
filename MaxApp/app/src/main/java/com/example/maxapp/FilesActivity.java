package com.example.maxapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilesActivity extends AppCompatActivity {

    private ProgressBar ProgressCircle;
    public ListView listView;
    public List<Upload> Uploads = new ArrayList<>();
    public Upload u;
    public EditText input;
    public String key;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);


        listView = findViewById(R.id.listviewFiles);
        ProgressCircle = findViewById(R.id.progress_circle);
        registerForContextMenu(listView);



            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    u = (Upload) parent.getItemAtPosition(position);
                    Toast.makeText(FilesActivity.this, u.getName(), Toast.LENGTH_SHORT).show();
                    key = u.getUploadKey();
                    return false;
                }
            });


            DBref.uploadRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange( @NonNull DataSnapshot dataSnapshot) {

                    Uploads.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Upload upload = postSnapshot.getValue(Upload.class);
                        if(upload != null){
                            if(Objects.equals(upload.getEmailUserKey(), DBref.Auth.getCurrentUser().getEmail().replace('.', ' '))){
                                Uploads.add(upload);
                            }
                        }
                    }

                    fileAdapter fileAdapter = new fileAdapter(FilesActivity.this,0,0,Uploads);
                    listView.setAdapter(fileAdapter);
                    ProgressCircle.setVisibility(View.INVISIBLE);
                    fileAdapter.notifyDataSetChanged();
                    listView.invalidateViews();
                }

                @Override
                public void onCancelled( @NonNull DatabaseError databaseError) {
                    Toast.makeText(FilesActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    ProgressCircle.setVisibility(View.INVISIBLE);
                }
            });



    }




    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("בחר");
        getMenuInflater().inflate(R.menu.menu_files, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.changeName:
                Toast.makeText(this, "שנה שם", Toast.LENGTH_SHORT).show();
                NewDialog();
                Toast.makeText(getApplicationContext(), input.getText().toString() , Toast.LENGTH_SHORT).show();

                return true;
            case R.id.delete:

                DBref.refUsers.child(u.getEmailUserKey()).child("uploadKey").child(u.getUploadKey()).removeValue();
                DBref.uploadRef.child(key).removeValue();

                Toast.makeText(this, "נמחק", Toast.LENGTH_SHORT).show();

                return true;
            default:
                return super.onContextItemSelected(item);
        }


    }


    public void NewDialog(){
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setTitle("שנה שם");
      builder.setIcon(R.drawable.ic_code_scanner_flash_on);
      builder.setMessage("שנה שם");

      input = new EditText(this);
      builder.setView(input);


      builder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {

              String txt = input.getText().toString();
              Toast.makeText(getApplicationContext(),txt, Toast.LENGTH_SHORT).show();
              DBref.uploadRef.child(key).child("name").setValue(txt);
              listView.invalidateViews();


          }
      }).setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              DBref.uploadRef.child(key).removeValue();
              listView.invalidateViews();
              dialog.dismiss();
          }
      });

      AlertDialog alertDialog = builder.create();
      alertDialog.show();

  }

}



