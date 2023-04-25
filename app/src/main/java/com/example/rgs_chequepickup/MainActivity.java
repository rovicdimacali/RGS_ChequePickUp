package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView home,  profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        home = (TextView) findViewById(R.id.icon_home);
        profile = (TextView) findViewById(R.id.icon_profile);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        home.setTypeface(font);
        profile.setTypeface(font);

        home.setText("\uf015");
        profile.setText("\uf007");

    }
}