package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Congratulations extends AppCompatActivity {

    Button continue_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulations);

        continue_button = findViewById(R.id.continue_button);
        continue_button.setOnClickListener(v -> {
            Intent intent = new Intent(Congratulations.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}