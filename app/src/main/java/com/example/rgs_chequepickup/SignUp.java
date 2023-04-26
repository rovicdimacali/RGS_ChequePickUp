package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SignUp extends AppCompatActivity {

    private Button login_button;
    TextView signing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signing = (TextView) findViewById(R.id.signing);

        login_button = (Button) findViewById(R.id.login_button);

        signing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCongrats();
            }
        });

    }
    public void openLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openCongrats(){
        Intent intent = new Intent(this, Congratulations.class);
        startActivity(intent);
    }
}