package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView home,  profile, icon_home, text_home, icon_profile, text_profile;
    LinearLayout home_btn, profile_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* ------------ START: Get textview to replace text with font awesome ------------ */

        home = (TextView) findViewById(R.id.icon_home);
        profile = (TextView) findViewById(R.id.icon_profile);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        home.setTypeface(font);
        profile.setTypeface(font);

        home.setText("\uf015");
        profile.setText("\uf007");

        /* ------------ END: Get textview to replace text with font awesome ------------ */

        /* ------------ START: Change Fragment and Change Color of Nav Buttons On Click Navbar Buttons ------------ */

        home_btn = (LinearLayout) findViewById(R.id.home_button);
        profile_btn= (LinearLayout) findViewById(R.id.profile_button);
        icon_home = (TextView) findViewById(R.id.icon_home);
        text_home = (TextView) findViewById(R.id.text_home);
        icon_profile = (TextView) findViewById(R.id.icon_profile);
        text_profile = (TextView) findViewById(R.id.text_profile);

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, HomeFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();

                text_home.setTextColor(Color.parseColor("#b0e32e"));
                icon_home.setTextColor(Color.parseColor("#b0e32e"));
                text_profile.setTextColor(Color.parseColor("#808080"));
                icon_profile.setTextColor(Color.parseColor("#808080"));

            }
        });

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, ProfileFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
                text_home.setTextColor(Color.parseColor("#808080"));
                icon_home.setTextColor(Color.parseColor("#808080"));
                text_profile.setTextColor(Color.parseColor("#b0e32e"));
                icon_profile.setTextColor(Color.parseColor("#b0e32e"));

            }
        });

        /* ------------ END: Change Fragment and Change Color of Nav Buttons On Click Navbar Buttons ------------ */
        
    }
}