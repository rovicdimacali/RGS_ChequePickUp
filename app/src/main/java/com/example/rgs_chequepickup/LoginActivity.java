package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Database.SqlDatabase;
import SessionPackage.SessionManagement;
import SessionPackage.UserSession;
public class LoginActivity extends AppCompatActivity {

    TextView back_button, forgotpassword, signup;
    EditText inputemail, inputpassword;
    String email, pass;
    Button login_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        back_button = (TextView) findViewById(R.id.back_button);
        signup = (TextView) findViewById(R.id.signup);
        forgotpassword = (TextView) findViewById(R.id.forgotpassword);

        login_button = (Button) findViewById(R.id.login_button);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        //INPUTS
        inputemail = (EditText) findViewById(R.id.inputemail);
        //email = inputemail.getText().toString();

        inputpassword = (EditText) findViewById(R.id.inputpassword);
        //pass = inputpassword.getText().toString();

        ///////
        back_button.setTypeface(font);
        back_button.setText("\uf060");

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SqlDatabase sql = new SqlDatabase(LoginActivity.this);
                Cursor res = sql.checkAccount("",String.valueOf(inputemail.getText()),String.valueOf(inputpassword.getText()));

                if(res.getCount() == 0){ //NO ACCOUNT
                    Toast.makeText(LoginActivity.this, "Account does not exists. Try again.", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Logging in and save session
                    SessionManagement sm = new SessionManagement(LoginActivity.this);
                    UserSession us = new UserSession(String.valueOf(inputemail.getText()),String.valueOf(inputpassword.getText()));
                    sm.saveSession(us);

                    openMain();
                }
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openStartAct();
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForgotPass();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignup();
            }
        });
    }
    protected void onStart() {
        super.onStart();

        //check if user is logged in
        SessionManagement sm = new SessionManagement(LoginActivity.this);
        String isLoggedIn = sm.getSession();

        if(!(isLoggedIn.equals("none"))){
            openMain();
        }
        else if(isLoggedIn.equals("none")){
            //Intent intent = new Intent(this, LoginActivity.class);
            //startActivity(intent);
        }
    }
    public void openStartAct() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    public void openForgotPass(){
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
    }

    public void openSignup(){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    public void openMain(){
        //Move to main activity
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("email", String.valueOf(inputemail.getText()));
        //intent.putExtra("pass", String.valueOf(inputpassword.getText()));
        startActivity(intent);
    }
}