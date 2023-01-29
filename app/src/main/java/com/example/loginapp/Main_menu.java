package com.example.loginapp;

import static com.example.loginapp.R.*;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;




public class Main_menu extends AppCompatActivity {


    Uri imageUri;
    TextView txt,profilename;
    public ImageView imgProfile;

    private static final int PICK_IMAGE = 100;
    private static final int pic_id = 123;

    private static final int GalleryPick = 1;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    String cameraPermission[];
    String storagePermission[];




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main_menu);

        MaterialToolbar toolbar = findViewById(id.topAppBar);
        DrawerLayout drawerLayout = findViewById(id.drawer_layout);
        NavigationView navigationView = findViewById(id.navigation_view);

        // allowing permissions of gallery and camera
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        // View headerViewListAdapter = findViewById(id.Header_nav_matter);
        txt = (TextView) findViewById(R.id.txt);
        //user = (TextView) findViewById(R.id.nameuser);

        String username = getIntent().getStringExtra("Username");

        txt.setText("Hello : "+username);


        View headView = navigationView.getHeaderView(0);
        imgProfile = headView.findViewById(id.imgssh);
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePicDialog();
            }
        });





        //View headView1 = navigationView.getHeaderView(1);
        profilename = headView.findViewById(id.nameuser);
        String userprofile = getIntent().getStringExtra("Username");
        profilename.setText(userprofile);




        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.START);

            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                int id = item.getItemId();
                item.setChecked(true);
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id)
                {

                    case R.id.nav_home:
                        Toast.makeText(Main_menu.this, "Home is Clicked", Toast.LENGTH_SHORT).show();
                        replaceFragment(new Home());
                        break;

                    case R.id.nav_message:
                        Toast.makeText(Main_menu.this, "Message is Clicked",Toast.LENGTH_SHORT).show();
                        replaceFragment(new Message());break;
                    case R.id.nav_synch:
                        Toast.makeText(Main_menu.this, "Synch is Clicked",Toast.LENGTH_SHORT).show();
                        replaceFragment(new Synch());break;
                    case R.id.nav_trash:
                        Toast.makeText(Main_menu.this, "Trash is Clicked",Toast.LENGTH_SHORT).show();
                        replaceFragment(new Trash());
                        break;
                    case R.id.nav_settings:
                        Toast.makeText(Main_menu.this, "Settings is Clicked",Toast.LENGTH_SHORT).show();
                        replaceFragment(new Settings());
                        break;
                    case R.id.nav_profile:
                        Toast.makeText(Main_menu.this, "Profile is Clicked",Toast.LENGTH_SHORT).show();
                        Intent intent5 = new Intent(getApplicationContext(), Profile.class);
                        startActivity(intent5);
                        break;
                    case R.id.nav_share:
                        Toast.makeText(Main_menu.this, "Share is clicked",Toast.LENGTH_SHORT).show();
                        replaceFragment(new Share());
                        break;
                    case R.id.nav_rate:
                        Toast.makeText(Main_menu.this, "Rate us is Clicked",Toast.LENGTH_SHORT).show();
                        replaceFragment(new RateUs());break;
                    case R.id.nav_logout:
                        Toast.makeText(Main_menu.this, "Log Out is Clicked",Toast.LENGTH_SHORT).show();
                        Intent intent3 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent3);
                        break;

                    default:
                        return true;

                }
                return true;
            }
        });


    }



    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id.framelayout,fragment);
        fragmentTransaction.commit();
    }










    private void showImagePicDialog() {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromGallery();
                    }
                } else if (which == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    // checking storage permissions
    private Boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    // Requesting gallery permission
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    // checking camera permissions
    private Boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    // Requesting camera permission
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    // Requesting camera and gallery
    // permission if not given
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted && writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }
    }

    // Here we will pick image from gallery or camera
    private void pickFromGallery() {
        CropImage.activity().start(Main_menu.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Picasso.with(this).load(resultUri).into(imgProfile);
            }
        }
    }







}