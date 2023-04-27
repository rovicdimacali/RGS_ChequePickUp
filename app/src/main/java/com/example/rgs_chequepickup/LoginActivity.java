package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    TextView back_button, forgotpassword, signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        back_button = (TextView) findViewById(R.id.back_button);

        signup = (TextView) findViewById(R.id.signup);

        forgotpassword = (TextView) findViewById(R.id.forgotpassword);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        back_button.setTypeface(font);
        back_button.setText("\uf060");

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
}