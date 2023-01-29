package com.example.loginapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register_page extends AppCompatActivity {
    DBHelper DB;
    private long backPressedTime;
    private Toast backToast;

    boolean passwordVisible;


    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");
    private static final String regexPattern = "^(.+)@(\\S+)$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        EditText name = findViewById(R.id.usernameRegiter);
        EditText email = findViewById(R.id.editTextTextEmailAddress);
        EditText password = findViewById(R.id.editTextTextPassword);
        EditText re_password = findViewById(R.id.editTextTextPassword2);

        Button regiter = findViewById(R.id.button);
        TextView login = findViewById(R.id.button1);
        DB = new DBHelper(this);

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right=2;
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(event.getRawX()>=password.getRight()-password.getCompoundDrawables()[Right].getBounds().width()){
                        int selection= password.getSelectionEnd();
                        if(passwordVisible){
                            //set drawable image here
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_off_24,0);
                            //for hide password
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible=false;
                        }else{
                            //set drawable image here
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_24,0);
                            //for visible password
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible=true;
                        }
                        password.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });


        re_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right=2;
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(event.getRawX()>=re_password.getRight()-re_password.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = re_password.getSelectionEnd();
                        if(passwordVisible){
                            //set drawable image here
                            re_password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_off_24,0);
                            //for hide password
                            re_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible=false;
                        }else{
                            //set drawable image here
                            re_password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_24,0);
                            //for visible password
                            re_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible=true;
                        }
                        re_password.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });


        regiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( emailValidator(email)  &  validatePassword(password)) {
                    // Intent main_manu_page = new Intent(Register_page.this, Main_menu.class);
                    // startActivity(main_manu_page);

                    //}

                    String email1 = email.getText().toString();
                    String user = name.getText().toString();
                    String pass = password.getText().toString();
                    String repass = re_password.getText().toString();


                    if (user.equals("") || email.equals("") || pass.equals("") || repass.equals(""))
                        Toast.makeText(Register_page.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                    else {
                        if (pass.equals(repass)) {
                            Boolean checkuser = DB.checkusername(user);
                            if (checkuser == false) {
                                Boolean checkemail = DB.checkusername(email1);
                                if(checkemail == false){
                                    Boolean insert = DB.insertData(user, email1, pass);
                                    if (insert == true) {
                                        Toast.makeText(Register_page.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), Main_menu.class);
                                        intent.putExtra("Username",user);
                                        intent.putExtra("Useremail",email1);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(Register_page.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                    }

                                }else{
                                    Toast.makeText(Register_page.this, "Email already exists! please sign in or enter different email", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Register_page.this, "User already exists! please sign in", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Register_page.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent2);
            }
        });


    }

    private boolean validatePassword(EditText password) {
        String passwordInput = password.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            password.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            password.setError("Password too weak");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }


    private boolean emailValidator(EditText username) {
        String emailToText = username.getText().toString();
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(emailToText);

        if (emailToText.isEmpty()) {
            username.setError("Field can't be empty");
            return false;
        } else if (!matcher.matches()) {
            username.setError(" Email address too weak");
            return false;
        } else {
            username.setError(null);
            return true;
        }

    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 1000 > System.currentTimeMillis()) {
            backToast.cancel();
            Intent intent4 = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent4);

        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to Login Page ", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }
}