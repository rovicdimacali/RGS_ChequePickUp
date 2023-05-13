package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePassword extends AppCompatActivity {
    TextView back_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        back_button = (TextView) findViewById(R.id.back_button);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        back_button.setTypeface(font);
        back_button.setText("\uf060");

        CardView emailcard = (CardView) findViewById(R.id.emailcard);
        CardView phonecard = (CardView) findViewById(R.id.phonecard);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStartAct();
            }
        });

        emailcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"Email Option Tapped",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangePassword.this, ForgotEmail.class);
                intent.putExtra("type", "change");
                startActivity(intent);
            }
        });

        // Onclick for Phone Card View
        phonecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Phone Option Tapped",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void openStartAct() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}