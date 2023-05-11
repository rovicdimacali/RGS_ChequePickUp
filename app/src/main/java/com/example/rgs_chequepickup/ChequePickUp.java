package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ChequePickUp extends AppCompatActivity {

    TextView icon_name, icon_location, icon_number, address, back_button;
    Button go_button, arrived_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheque_pick_up);
        Fragment fragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();

        //TEXTVIEWS
        icon_name = (TextView) findViewById(R.id.icon_name);
        icon_location = (TextView) findViewById(R.id.icon_location);
        icon_number = (TextView) findViewById(R.id.icon_number);
        address = (TextView) findViewById(R.id.address);//Address

        back_button = (TextView) findViewById(R.id.back_button);

        //BUTTONS
        go_button = (Button) findViewById(R.id.go_button);
        arrived_button = (Button) findViewById(R.id.arrived_button);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        icon_name.setTypeface(font);
        icon_location.setTypeface(font);
        icon_number.setTypeface(font);
        back_button.setTypeface(font);

        icon_name.setText("\uf007");
        icon_location.setText("\uf015");
        icon_number.setText("\uf2a0");
        back_button.setText("\uf060");

        arrived_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCapturecheque();
            }
        });

        //LAUNCH GMAPS BASED ON ADDRESS
        go_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String add = address.getText().toString();
                if(add.equals("")){
                    Toast.makeText(getApplicationContext(),"No location found", Toast.LENGTH_SHORT).show();
                }
                else{
                    DisplayMap(add);
                }
                //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="));
            }
        });

        arrived_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChequePickUp.this, CaptureCheque.class);
                startActivity(intent);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChequePickUp.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void DisplayMap(String address){
        try{
            Uri uri = Uri.parse("google.navigation:q=" + address);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        catch(ActivityNotFoundException e){
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void openCapturecheque(){
        Intent intent = new Intent(this, CaptureCheque.class);
        startActivity(intent);
    }
}