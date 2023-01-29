package com.example.loginapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Header_nav extends AppCompatActivity {
    ImageView imageView;



    private static final int PICK_IMAGE = 100;

    Uri imageUri;


     TextView nameperson,emailperson;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header);

        nameperson = (TextView) findViewById(R.id.nameuser);
        emailperson = (TextView) findViewById(R.id.username);


        String username = getIntent().getStringExtra("Username");
        String email1 = getIntent().getStringExtra("Useremail");
        nameperson.setText(username);
        emailperson.setText(email1);


       imageView = findViewById(R.id.imgssh);



        imageView.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                openGallery1();

            }

        });

    }


    private void openGallery1() {

        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        startActivityForResult(gallery, PICK_IMAGE);

    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){

            imageUri = data.getData();

            imageView.setImageURI(imageUri);

        }

    }

}
