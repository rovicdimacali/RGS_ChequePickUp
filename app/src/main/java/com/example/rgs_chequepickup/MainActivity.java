package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import Database.SqlDatabase;
import SessionPackage.SessionManagement;
import SessionPackage.UserSession;
public class MainActivity extends AppCompatActivity {

    TextView home,  tv, profile, icon_home, text_home, icon_profile, text_profile;
    LinearLayout home_btn, profile_btn;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.text_home);
        intent = getIntent();

        /* ------------ START: Get textview to replace text with font awesome ------------ */
        String inputemail = intent.getStringExtra("email");
        //tv.setText(inputemail);
        if(!(inputemail.equals(" "))){
            Toast.makeText(MainActivity.this,"You are now logged in", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(MainActivity.this,"Failed to pass data", Toast.LENGTH_SHORT).show();
        }
        //String inputpass = intent.getStringExtra("pass");


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
    /*
    protected void onStart() {
        super.onStart();

        //check if user is logged in
        SessionManagement sm = new SessionManagement(MainActivity.this);
        String isLoggedIn = sm.getSession();

        if(!(isLoggedIn.equals("none"))){
            openLogin();
        }
        else if(isLoggedIn.equals("none")){
            //Intent intent = new Intent(this, LoginActivity.class);
            //startActivity(intent);
        }
    }
    public void openLogin(){
        //Move to main activity
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }*/
}