package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OfficialReceipt extends AppCompatActivity {

    TextView back_button;
    Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_receipt);

        back_button = (TextView) findViewById(R.id.back_button);
        submit_btn = (Button) findViewById(R.id.submit_button);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        back_button.setTypeface(font);
        back_button.setText("\uf060");

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEsignature();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OfficialReceipt.this, "Transaction Completed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OfficialReceipt.this, ChequeReceived.class);
                startActivity(intent);
            }
        });
    }
    public void openEsignature() {
        Intent intent = new Intent(this, ESignature.class);
        startActivity(intent);
    }
}