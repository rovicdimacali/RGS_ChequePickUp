package com.example.rgs_chequepickup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ChequePickUp extends AppCompatActivity {

    TextView icon_name, icon_location, icon_number, address, back_button, number;
    Button go_button, arrived_button;
    RelativeLayout layout;
    FusedLocationProviderClient fspc;
    private final static int REQUEST_CODE = 100;
    double cur_lat, cur_long, des_lat, des_long;
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
        number = (TextView) findViewById(R.id.number);
        back_button = (TextView) findViewById(R.id.back_button);

        //BUTTONS
        go_button = (Button) findViewById(R.id.go_button);
        arrived_button = (Button) findViewById(R.id.arrived_button);
        arrived_button.setActivated(false);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        icon_name.setTypeface(font);
        icon_location.setTypeface(font);
        icon_number.setTypeface(font);
        back_button.setTypeface(font);

        icon_name.setText("\uf007");
        icon_location.setText("\uf015");
        icon_number.setText("\uf2a0");
        back_button.setText("\uf060");

        fspc = LocationServices.getFusedLocationProviderClient(this);
        arrived_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
                //openCapturecheque();
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

    private void getCurrentLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){
            fspc.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        Geocoder gc = new Geocoder(ChequePickUp.this, Locale.getDefault());
                        List<Address> sadd = null;
                        List<Address> dadd = null;
                        try {
                            //CURRENT LOCATION
                            sadd = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            Location sp = new Location("currLoc");
                            cur_lat = sadd.get(0).getLatitude();
                            cur_long = sadd.get(0).getLongitude();
                            sp.setLatitude(cur_lat);
                            sp.setLongitude(cur_long);
                            //DESTINATION
                            dadd = gc.getFromLocationName(address.getText().toString(), 1);
                            Location ep = new Location("destination");
                            des_lat = dadd.get(0).getLatitude();
                            des_long = dadd.get(0).getLongitude();
                            ep.setLatitude(des_lat);
                            ep.setLongitude(des_long);

                            double distance = sp.distanceTo(ep);
                            //address.setText(String.valueOf(distance));
                            if(distance < 10){
                                ArrivedPopupWindow();
                                //Toast.makeText(ChequePickUp.this, "You're 100m near at your destination", Toast.LENGTH_SHORT).show();
                                arrived_button.setActivated(true);
                                //arrived_button.setBackground(ContextCompat.getDrawable(ChequePickUp.this, R.drawable.btn_secondary));
                                //openCapturecheque();
                            }
                            else{
                                NotArrivedPopupWindow();
                                arrived_button.setActivated(false);
                                //Toast.makeText(ChequePickUp.this, "Must be near the destination first", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            });
        }
        else{
            askPermission();
        }
    }
    public void openCapturecheque(){
        Intent intent = new Intent(this, CaptureCheque.class);
        startActivity(intent);
    }

    private void askPermission(){
        ActivityCompat.requestPermissions(ChequePickUp.this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
            else{
                Toast.makeText(ChequePickUp.this,"Required Permission", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void ArrivedPopupWindow() {
        layout = (RelativeLayout) findViewById(R.id.layout);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.popup_arrived, null);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);
        layout.post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            }
        });

        Button capture = (Button) popUpView.findViewById(R.id.capture_button);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChequePickUp.this, CaptureCheque.class);
                startActivity(intent);
            }
        });
    }

    private void NotArrivedPopupWindow() {
        layout = (RelativeLayout) findViewById(R.id.layout);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.popup_not_arrived, null);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);
        layout.post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            }
        });

        Button dismiss = (Button) popUpView.findViewById(R.id.dismiss_button);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    popupWindow.dismiss();
            }
        });
    }
}