package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import SessionPackage.SessionManagement;

public class StartActivity extends AppCompatActivity {
    Button login_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(v -> openLogin());
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
    }
    public void openMain(String email){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }

    public void openLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}