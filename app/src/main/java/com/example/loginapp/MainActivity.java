package com.example.loginapp;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    Button button_login;
    EditText username,password;
    TextView  button_sign,forgot;
    DBHelper DB;

    Spinner spinner;
    public static final String [] language={"Select Language","English","Hindi","Marathi"};

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
        setContentView(R.layout.activity_main);

        button_login = (Button)findViewById(R.id.button_login);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);

        button_sign = (TextView) findViewById(R.id.button_sign);

        //forgot = (TextView) findViewById(R.id.forgotpassword);
        spinner = findViewById(R.id.action_bar_spinner);

        DB = new DBHelper(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,language);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLang =  parent.getItemAtPosition(position).toString();
                if(selectedLang.equals("English")){
                    setLocal(MainActivity.this, "values");
                    finish();
                    startActivity(getIntent());
                }else if(selectedLang.equals("Hindi")){
                    setLocal(MainActivity.this, "hi");
                    finish();
                    startActivity(getIntent());

                }else if(selectedLang.equals("Marathi")){
                    setLocal(MainActivity.this, "mr");
                    finish();
                    startActivity(getIntent());

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



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




        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //emailValidator(username);
               // validatePassword(password);
                if(emailValidator(username) & validatePassword(password)){
                   // Intent main_manu_page = new Intent(MainActivity.this,Main_menu.class);
                    //startActivity(main_manu_page);




                    String user = username.getText().toString();
                    String pass = password.getText().toString();

                    if(user.equals("")||pass.equals(""))
                        Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                    else{
                        Boolean checkuserpass1 = DB.checkusernamepassword(user, pass);
                        if(true == checkuserpass1){
                            Toast.makeText(MainActivity.this, "Sign in successfull", Toast.LENGTH_SHORT).show();
                            Intent intent  = new Intent(getApplicationContext(), Main_menu.class);
                            intent.putExtra("Username",user);
                            startActivity(intent);
                        }else{
                            Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }

                }


            }

        });



        button_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), Register_page.class);
                startActivity(intent1);
                finish();
            }
        });


        /*forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(getApplicationContext(), PasswordActivity.class);
                startActivity(intent5);
            }
        });*/

    }

    private void setLocal(Activity activity, String langCode) {
        Locale locale = new Locale(langCode);
        locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
    }

    private boolean validatePassword(EditText password) {
        String passwordInput = password.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            password.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches() & passwordInput.length()>=8) {
            password.setError("Password too weak or short, please enter greater than 8 character");
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
    public void onBackPressed(){
        AlertDialog.Builder alertDialog = new MaterialAlertDialogBuilder(MainActivity.this);
        alertDialog.setTitle("Exit App");
        alertDialog.setMessage("Do you want to exit loginApp ? ");
        alertDialog.setPositiveButton("Yes ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        alertDialog.setNegativeButton("No ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


}