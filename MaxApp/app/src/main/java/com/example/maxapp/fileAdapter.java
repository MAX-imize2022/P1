package com.example.maxapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class fileAdapter extends ArrayAdapter<Upload> {
      final private Context context;
      final private List<Upload> Uploads;



    public fileAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int
            textViewResourceId, @NonNull List<Upload> Uploads) {
        super(context, resource, textViewResourceId, Uploads);
        this.context = context;
        this.Uploads = Uploads;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.file_item, parent, false);
        Upload u = Uploads.get(position);

        TextView txFileName = (TextView) view.findViewById(R.id.text_view_name);
        final ImageView imgUser = (ImageView) view.findViewById(R.id.image_view_upload);




        txFileName.setText(u.getName());

        DBref.uploadsStorageRef.child(u.getUploadKey()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context)
                        .load(uri)
                        .fit()
                        .centerCrop()
                        .into(imgUser);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });


        return view;
    }




}




