package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class OneTimePass extends AppCompatActivity {

    TextView back_button, otptext;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_time_pass);

        back_button = (TextView) findViewById(R.id.back_button);
        //otptext = (TextView) findViewById(R.id.otp);
        intent = getIntent();

        String otp = intent.getStringExtra("otp");
        otptext.setText(otp);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        back_button.setTypeface(font);
        back_button.setText("\uf060");

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStartAct();
            }
        });
    }
    public void openStartAct() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}