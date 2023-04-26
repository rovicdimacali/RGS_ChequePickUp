package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ForgotPassword extends AppCompatActivity {

    private Button continue_button;

    TextView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        back_button = (TextView) findViewById(R.id.back_button);

        continue_button = (Button) findViewById(R.id.continue_button);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        back_button.setTypeface(font);
        back_button.setText("\uf060");

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openStartAct();
            }
        });

        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOtp();
            }
        });
    }
    public void openStartAct() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void openOtp(){
        Intent intent = new Intent (this, OneTimePass.class);
        startActivity(intent);
    }
}