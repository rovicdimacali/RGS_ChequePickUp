package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CaptureCheque extends AppCompatActivity {

    Button nxt_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_cheque);

        nxt_button = (Button) findViewById(R.id.nxt_button);

        nxt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEsign();
            }
        });
    }
    public void openEsign(){
        Intent intent = new Intent(this, ESignature.class);
        startActivity(intent);
    }
}