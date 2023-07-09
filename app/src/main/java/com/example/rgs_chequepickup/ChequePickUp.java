package com.example.rgs_chequepickup;

//import static com.example.rgs_chequepickup.OneTimePass.PERMISSION_SEND_SMS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
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


import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import SessionPackage.LocationManagement;


public class ChequePickUp extends AppCompatActivity {
    String message1, message2;
    TextView icon_company, icon_name, icon_location, icon_number, icon_code, back_button;
    TextView company, person, addr, contact, code;
    Button go_button, arrived_button, cancel_button;
    RelativeLayout layout;
    String comp, per, add, cont, company_code;
    FusedLocationProviderClient fspc;
    private final static int REQUEST_CODE = 100;
    static final int PERMISSION_SEND_SMS_GO = 101;
    static final int PERMISSION_SEND_SMS_ARRIVED = 102;
    double cur_lat, cur_long, des_lat, des_long;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheque_pick_up);

        Fragment fragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
        Loading();

        fspc = LocationServices.getFusedLocationProviderClient(this);

        message1 = "Good Day! This is your Rider from RGS, I'm on my way to collect the cheque(s) from your location. Thank you!";
        message2 = "Good Day! This is your Rider from RGS, I've arrived and already at your location. Thank you!";

        LocationManagement lm = new LocationManagement(ChequePickUp.this);

        comp = lm.getComp();
        per = lm.getPer();
        add = lm.getAdd();
        cont = lm.getCont();
        company_code = lm.getCode();

        company = findViewById(R.id.company);
        person = findViewById(R.id.name);
        addr = findViewById(R.id.address);
        contact = findViewById(R.id.number);
        code = findViewById(R.id.compcode);

        company.setText(comp);
        person.setText(per);
        addr.setText(add);
        contact.setText(cont);
        code.setText(company_code);

        //TEXTVIEWS
        icon_company = findViewById(R.id.icon_company);
        icon_name = findViewById(R.id.icon_name);
        icon_location = findViewById(R.id.icon_location);
        icon_number = findViewById(R.id.icon_number);
        icon_code = findViewById(R.id.icon_code);
        back_button = findViewById(R.id.back_button);

        //BUTTONS
        go_button = findViewById(R.id.go_button);
        arrived_button = findViewById(R.id.arrived_button);
        cancel_button = findViewById(R.id.cancel_button);
        arrived_button.setActivated(false);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        icon_company.setTypeface(font);
        icon_name.setTypeface(font);
        icon_location.setTypeface(font);
        icon_number.setTypeface(font);
        icon_code.setTypeface(font);
        back_button.setTypeface(font);

        icon_company.setText("\uf1ad");
        icon_name.setText("\uf007");
        icon_location.setText("\uf015");
        icon_number.setText("\uf2a0");
        icon_code.setText("\uf25d");
        back_button.setText("\uf060");

        contact.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + contact.getText().toString()));
            startActivity(intent);
        });
        cancel_button.setOnClickListener(v -> {
            Intent intent = new Intent(ChequePickUp.this, CancelActivity.class);
            startActivity(intent);
            finish();
        });

        arrived_button.setOnClickListener(v -> getCurrentLocation());

        //LAUNCH GMAPS BASED ON ADDRESS
        go_button.setOnClickListener(v -> {
            String add = addr.getText().toString();
            if(add.equals("")){
                Toast.makeText(getApplicationContext(),"No location found", Toast.LENGTH_SHORT).show();
            }
            else{
                if (ContextCompat.checkSelfPermission(ChequePickUp.this, Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) { // ASK PERMISSION
                    ActivityCompat.requestPermissions(ChequePickUp.this, new String[]{Manifest.permission.SEND_SMS},
                            PERMISSION_SEND_SMS_GO);
                } else {// PERMISSION GRANTED
                    sendSMS(cont, message1);
                    DisplayMap(add);
                }
            }
        });

        back_button.setOnClickListener(v -> {
            LocationManagement lm1 = new LocationManagement(ChequePickUp.this);
            lm1.removeLocation();
            Intent intent = new Intent(ChequePickUp.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //finish();
        });
    }


    private void sendSMS(String phoneNumber, String message) {
        char cpFirst = phoneNumber.charAt(0);
        if(phoneNumber.substring(0,4).contains("+63") || phoneNumber.substring(0,4).contains("63") ||
                phoneNumber.substring(0,4).contains("09")){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            //Toast.makeText(this, "SMS sent!", Toast.LENGTH_SHORT).show();
        }
        else if(cpFirst == '9'){
            phoneNumber = "0" + phoneNumber;
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            //Toast.makeText(this, "SMS sent!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Invalid cellphone number", Toast.LENGTH_SHORT).show();
        }
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
            fspc.getLastLocation().addOnSuccessListener(location -> {
                if(location != null){
                    Geocoder gc = new Geocoder(ChequePickUp.this, Locale.getDefault());
                    List<Address> sadd;
                    List<Address> dadd;
                    try {
                        //CURRENT LOCATION
                        sadd = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        Location sp = new Location("currLoc");
                        cur_lat = sadd.get(0).getLatitude();
                        cur_long = sadd.get(0).getLongitude();
                        sp.setLatitude(cur_lat);
                        sp.setLongitude(cur_long);
                        //DESTINATION
                        dadd = gc.getFromLocationName(addr.getText().toString(), 1);
                        Location ep = new Location("destination");
                        des_lat = dadd.get(0).getLatitude();
                        des_long = dadd.get(0).getLongitude();
                        ep.setLatitude(des_lat);
                        ep.setLongitude(des_long);

                        double distance = sp.distanceTo(ep);
                        if(distance < 99999){
                            ArrivedPopupWindow();
                            //Toast.makeText(ChequePickUp.this, "You're 100m near at your destination", Toast.LENGTH_SHORT).show();
                            arrived_button.setActivated(true);
                        }
                        else{
                            NotArrivedPopupWindow(distance);
                            arrived_button.setActivated(false);
                            //Toast.makeText(ChequePickUp.this, "Must be near the destination first", Toast.LENGTH_SHORT).show();
                        }
                     } catch (IOException e) {
                        NoSignalPopupWindow();
                        //throw new RuntimeException(e); //RUNTIME ERROR
                    }

                }
            });
        }
        else{
            askPermission();
        }
    }
    private void askPermission(){
        ActivityCompat.requestPermissions(ChequePickUp.this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE){
            if(!(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
               Toast.makeText(ChequePickUp.this,"Required Permission", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == PERMISSION_SEND_SMS_GO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //sendSMS(cont, "Hello, this is a test message!");
                sendSMS(cont, message1);
                DisplayMap(addr.getText().toString());
            } else {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == PERMISSION_SEND_SMS_ARRIVED) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //sendSMS(cont, "Hello, this is a test message!");
                sendSMS(cont, message2);
                Intent intent = new Intent(ChequePickUp.this, CheckList.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void ArrivedPopupWindow() {
        layout = findViewById(R.id.layout);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.popup_arrived, null);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);
        layout.post(() -> popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0));

        Button capture = popUpView.findViewById(R.id.capture_button);
        RelativeLayout overlay = popUpView.findViewById(R.id.overlay);
        capture.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(ChequePickUp.this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) { // ASK PERMISSION
                ActivityCompat.requestPermissions(ChequePickUp.this, new String[]{Manifest.permission.SEND_SMS},
                        PERMISSION_SEND_SMS_ARRIVED);
            } else {// PERMISSION GRANTED
                sendSMS(cont, message2);
                Intent intent = new Intent(ChequePickUp.this, CheckList.class);
                startActivity(intent);
                finish();
            }
        });

        layout.post(() -> overlay.setOnClickListener(v -> popupWindow.dismiss()));
    }

    private void NotArrivedPopupWindow(double d) {
        DecimalFormat df = new DecimalFormat("#.##");
        String distance = df.format(d);
        layout = findViewById(R.id.layout);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.popup_not_arrived, null);

        TextView text = popUpView.findViewById(R.id.distanceTrack);
        text.setText("Distance must be less than 20 meters\nCurrent Distance: " + distance + " meters");

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);
        layout.post(() -> popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0));

        Button dismiss = popUpView.findViewById(R.id.dismiss_button);
        dismiss.setOnClickListener(v -> popupWindow.dismiss());
    }

    private void NoSignalPopupWindow() {
        layout = findViewById(R.id.layout);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.popup_unstable, null);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);
        layout.post(() -> popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0));

        Button retry = popUpView.findViewById(R.id.retry);
        retry.setOnClickListener(v -> {
            recreate();
            popupWindow.dismiss();
        });
    }

    private void Loading(){
        layout = findViewById(R.id.layout);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.popup_loading, null);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        int duration = 3000;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);
        layout.postDelayed(() -> {
            // Display the popup view
            popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

            // Delayed post to hide the popup after the specified duration
            // Hide the popup view
            layout.postDelayed(popupWindow::dismiss, duration);
        }, 0);
    }
}