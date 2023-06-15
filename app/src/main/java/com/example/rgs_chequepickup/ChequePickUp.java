package com.example.rgs_chequepickup;

//import static com.example.rgs_chequepickup.OneTimePass.PERMISSION_SEND_SMS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsManager;
import android.util.Log;
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
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import SessionPackage.LocationManagement;
import SessionPackage.LocationSession;
import okhttp3.OkHttpClient;

public class ChequePickUp extends AppCompatActivity {
    String message1, message2;
    TextView icon_company, icon_name, icon_location, icon_number, icon_code, address, back_button, number;
    TextView company, person, addr, contact, code;
    Button go_button, arrived_button, cancel_button;
    RelativeLayout layout;
    String comp, per, add, cont, company_code;
    View v;
    HttpURLConnection conn;
    LocationCallback lcb;
    LocationRequest lr;
    FusedLocationProviderClient fspc;
    private static final int MAX_SMS_LENGTH = 160;
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

        //fspc.requestLocationUpdates(lr, locCallBack, Looper.getMainLooper());

        /*message1 = "Good Day! This is your Rider from RGS, I\'m messaging to inform you that I'm on my way to collect the cheque(s) from your location."
        + "Please ensure that you are available at the designated pickup location to hand over the cheque(s). If there are any specific instructions, please let me know immediately."
        +"For any further assistance or queries, please reach out or reply to me at this number."
        +"Thank you for choosing RGS!";*/
        ;
        message1 = "Good Day! This is your Rider from RGS, I\'m on my way to collect the cheque(s) from your location. Thank you!";
        message2 = "Good Day! This is your Rider from RGS, I\'ve arrived and already at your location. Thank you!";

        Intent details = getIntent();

        LocationManagement lm = new LocationManagement(ChequePickUp.this);

        /*String comp = details.getStringExtra("company");
        String per = details.getStringExtra("person");
        String add = details.getStringExtra("address");
        String cont = details.getStringExtra("contact");*/
        comp = lm.getComp();
        per = lm.getPer();
        add = lm.getAdd();
        cont = lm.getCont();
        company_code = lm.getCode();

        company = (TextView) findViewById(R.id.company);
        person = (TextView) findViewById(R.id.name);
        addr = (TextView) findViewById(R.id.address);
        contact = (TextView) findViewById(R.id.number);
        code = (TextView) findViewById(R.id.compcode);

        company.setText(comp);
        person.setText(per);
        addr.setText(add);
        contact.setText(cont);
        code.setText(company_code);

        //TEXTVIEWS
        icon_company = (TextView) findViewById(R.id.icon_company);
        icon_name = (TextView) findViewById(R.id.icon_name);
        icon_location = (TextView) findViewById(R.id.icon_location);
        icon_number = (TextView) findViewById(R.id.icon_number);
        icon_code = (TextView) findViewById(R.id.icon_code);
        //address = (TextView) findViewById(R.id.address);//Address
        //number = (TextView) findViewById(R.id.number);
        back_button = (TextView) findViewById(R.id.back_button);

        //BUTTONS
        go_button = (Button) findViewById(R.id.go_button);
        arrived_button = (Button) findViewById(R.id.arrived_button);
        cancel_button = (Button) findViewById(R.id.cancel_button);
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


        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contact.getText().toString()));
                startActivity(intent);
                /*if (intent.resolveActivity(getPackageManager()) != null) {

                }*/
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChequePickUp.this, CancelActivity.class);
                startActivity(intent);
                finish();
            }
        });
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
                        //sendSMS(cont, "Hello, this is a test message!");
                        sendSMS(cont, message1);
                        DisplayMap(add);
                    }
                }
                //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="));
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManagement lm = new LocationManagement(ChequePickUp.this);
                lm.removeLocation();
                Intent intent = new Intent(ChequePickUp.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();
            }
        });
    }

    /*public void onRequestPermissionsResultSMS(int requestCode, @NonNull String[] asd, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, asd, grantResults);
        if (requestCode == PERMISSION_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMS("Recipient's phone number", "Hello, this is a test message!");
            } else {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    private void sendSMS(String phoneNumber, String message) {
        char cpFirst = phoneNumber.charAt(0);
        if(phoneNumber.substring(0,4).contains("+63") || phoneNumber.substring(0,4).contains("63") ||
                phoneNumber.substring(0,4).contains("09")){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "SMS sent!", Toast.LENGTH_SHORT).show();
        }
        else if(cpFirst == '9'){
            phoneNumber = "0" + phoneNumber;
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "SMS sent!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Invalid cellphone number", Toast.LENGTH_SHORT).show();
        }
    }

    /*private void sendSMS(String phoneNumber, String message) {
        ArrayList<String> parts = new ArrayList<>();

        if(phoneNumber.substring(0,4).contains("+63") || phoneNumber.substring(0,4).contains("63") ||
                phoneNumber.substring(0,4).contains("09")) {
            StringBuilder sb = new StringBuilder();
            String[] words = message.split(" ");
            for (String word : words) {
                if (sb.length() + word.length() + 1 > MAX_SMS_LENGTH) {
                    parts.add(sb.toString());
                    sb = new StringBuilder();
                }
                sb.append(word).append(" ");
            }

            if (sb.length() > 0) {
                parts.add(sb.toString());
            }

            SmsManager smsManager = SmsManager.getDefault();
            for (String part : parts) {
                smsManager.sendTextMessage(phoneNumber, null, part, null, null);
            }

            Toast.makeText(this, "SMS sent!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Invalid cellphone number", Toast.LENGTH_SHORT).show();
        }
    }*/


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
                            dadd = gc.getFromLocationName(addr.getText().toString(), 1);
                            Location ep = new Location("destination");
                            des_lat = dadd.get(0).getLatitude();
                            des_long = dadd.get(0).getLongitude();
                            ep.setLatitude(des_lat);
                            ep.setLongitude(des_long);

                            double distance = sp.distanceTo(ep);
                            //double distance = 30.01;
                            //address.setText(String.valueOf(distance));
                            if(distance < 99999){
                                ArrivedPopupWindow();
                                //Toast.makeText(ChequePickUp.this, "You're 100m near at your destination", Toast.LENGTH_SHORT).show();
                                arrived_button.setActivated(true);
                                //arrived_button.setBackground(ContextCompat.getDrawable(ChequePickUp.this, R.drawable.btn_secondary));
                                //openCapturecheque();
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
                //getCurrentLocation();
            }
            else{
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
        layout = (RelativeLayout) findViewById(R.id.layout);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.popup_arrived, null);
        //TextView text = (TextView) popUpView.findViewById(R.id.textPick);
        //text.setText(String.valueOf(d));
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
        RelativeLayout overlay = (RelativeLayout) popUpView.findViewById(R.id.overlay);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ChequePickUp.this, Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) { // ASK PERMISSION
                    ActivityCompat.requestPermissions(ChequePickUp.this, new String[]{Manifest.permission.SEND_SMS},
                            PERMISSION_SEND_SMS_ARRIVED);
                } else {// PERMISSION GRANTED
                    //sendSMS(cont, "Hello, this is a test message!");
                    Toast.makeText(ChequePickUp.this, "SMS Sent", Toast.LENGTH_SHORT).show();
                    sendSMS(cont, message2);
                    Intent intent = new Intent(ChequePickUp.this, CaptureCheque.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        layout.post(new Runnable() {
            @Override
            public void run() {
                overlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
        });
    }

    private void NotArrivedPopupWindow(double d) {
        DecimalFormat df = new DecimalFormat("#.##");
        String distance = df.format(d);
        layout = (RelativeLayout) findViewById(R.id.layout);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.popup_not_arrived, null);

        TextView text = (TextView) popUpView.findViewById(R.id.distanceTrack);
        text.setText("Distance must be less than 20 meters\nCurrent Distance: " + distance + " meters");

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

    private void NoSignalPopupWindow() {
        layout = (RelativeLayout) findViewById(R.id.layout);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.popup_unstable, null);

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

        Button retry = (Button) popUpView.findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
                popupWindow.dismiss();
            }
        });
    }

    private void Loading(){
        layout = (RelativeLayout) findViewById(R.id.layout);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.popup_loading, null);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        int duration = 3000;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);
        layout.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Display the popup view
                popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

                // Delayed post to hide the popup after the specified duration
                layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Hide the popup view
                        popupWindow.dismiss();
                    }
                }, duration);
            }
        }, 0);
    }
}