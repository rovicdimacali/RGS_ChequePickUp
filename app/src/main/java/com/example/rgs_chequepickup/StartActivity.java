package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import SessionPackage.SessionManagement;
import SessionPackage.UserSession;

public class StartActivity extends AppCompatActivity {
    private Button start_button;
    private Button login_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        start_button = (Button) findViewById(R.id.start_button);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUp();
            }
        });

        login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });
    }
     protected void onStart() {
        super.onStart();

        //check if user is logged in
        SessionManagement sm = new SessionManagement(StartActivity.this);
        String isLoggedIn = sm.getSession();

        if(!(isLoggedIn.equals("none"))){
            //String email = sm.getEmail();
            openMain(isLoggedIn);
        }
        else if(isLoggedIn.equals("none")){
            //Intent intent = new Intent(this, LoginActivity.class);
            //startActivity(intent);
        }
    }
    public void openSignUp() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    public void openMain(String email){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    public void openLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}