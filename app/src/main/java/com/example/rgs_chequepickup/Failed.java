package com.example.rgs_chequepickup;

import static com.example.rgs_chequepickup.ChequePickUp.PERMISSION_SEND_SMS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.UserManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import SessionPackage.LocationManagement;
import SessionPackage.ReceiptManagement;
import SessionPackage.SessionManagement;
import SessionPackage.SignatureManagement;
import SessionPackage.UserSession;
import SessionPackage.accountManagement;
import SessionPackage.cancelManagement;
import SessionPackage.chequeManagement;
import SessionPackage.remarkManagement;
import SessionPackage.scenarioManagement;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Failed extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    FusedLocationProviderClient fspc;
    double cur_lat, cur_long;
    private final static int REQUEST_CODE = 100;
    OkHttpClient client;

    Button back_button;
    String responseData, riderID;
    String status;
    String pointPerson;
    Intent i;
    SessionManagement sm;
    TextView comp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failed);
        //LocationManagement lm = new LocationManagement(Failed.this);

        sm  = new SessionManagement(Failed.this);
        riderID = sm.getSession();

        client = new OkHttpClient();
        back_button = (Button) findViewById(R.id.back_button);
        comp = (TextView) findViewById(R.id.complete);
        fspc = LocationServices.getFusedLocationProviderClient(Failed.this);

        //comp.setText(lm.getComp());
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
                //Intent intent = new Intent(Failed.this, MainActivity.class);
                //startActivity(intent);
            }
        });
    }
    private void getCurrentLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){
            fspc.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        Geocoder gc = new Geocoder(Failed.this, Locale.getDefault());
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

                            postResults(String.valueOf(cur_long), String.valueOf(cur_lat));
                        }
                        catch (IOException e) {
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
    private void askPermission(){
        ActivityCompat.requestPermissions(Failed.this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }
    public void postResults(String longitude, String latitude){
        RequestBody rbody;

        LocationManagement loc_m = new LocationManagement(Failed.this);
        SignatureManagement sign_m = new SignatureManagement(Failed.this);
        chequeManagement cs = new chequeManagement(Failed.this);
        cancelManagement can_m = new cancelManagement(Failed.this);

        Resources res = getResources();
        Drawable defaultPic = res.getDrawable(R.drawable.cancel);

        String imagePath = getAlbumStorageDir("RGS_Express Signs") + "/" + sign_m.getSign();
        File imageFile = new File(imagePath);

        if(can_m.getCancel().equals("Client/Customer Not Around")){
            if(!(imageFile.exists())){
                Toast.makeText(Failed.this, "Signature File does not exist", Toast.LENGTH_SHORT).show();
            }
            else{
                rbody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("chk_rider", sm.getSession())
                        .addFormDataPart("chk_status", can_m.getCancel())
                        .addFormDataPart("cancel_name", can_m.getPoint())
                        .addFormDataPart("chk_sign", sign_m.getSign(),RequestBody.create(MediaType.parse("image/jpeg"), imageFile))
                        .addFormDataPart("chk_pic", "",RequestBody.create(MediaType.parse("image/jpeg"), defaultPic.toString()))
                        .addFormDataPart("chk_accno", "none")
                        .addFormDataPart("chk_payee","none")
                        .addFormDataPart("chk_entity", "none")
                        .addFormDataPart("chk_remark", "none")
                        .addFormDataPart("chk_address", loc_m.getAdd())
                        .addFormDataPart("chk_company", loc_m.getComp())
                        .addFormDataPart("chk_code", loc_m.getCode())
                        .addFormDataPart("chk_tin", "none")
                        .addFormDataPart("chk_or", "none")
                        .addFormDataPart("chk_date", "none")
                        .addFormDataPart("chk_bcode", "none")
                        .addFormDataPart("transaction_num", "none")
                        .addFormDataPart("chk_amount", "none")
                        .addFormDataPart("chk_number", "none")
                        .addFormDataPart("latitude", latitude)
                        .addFormDataPart("longitude", longitude)
                        .build();

                sendAPI(rbody);
            }
        }
        else{
            rbody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("chk_rider", sm.getSession())
                    .addFormDataPart("chk_status", can_m.getCancel())
                    .addFormDataPart("cancel_name", can_m.getPoint())
                    .addFormDataPart("chk_sign", "",RequestBody.create(MediaType.parse("image/jpeg"), defaultPic.toString()))
                    .addFormDataPart("chk_pic", "",RequestBody.create(MediaType.parse("image/jpeg"), defaultPic.toString()))
                    .addFormDataPart("chk_accno", "none")
                    .addFormDataPart("chk_payee","none")
                    .addFormDataPart("chk_entity", "none")
                    .addFormDataPart("chk_remark", "none")
                    .addFormDataPart("chk_address", loc_m.getAdd())
                    .addFormDataPart("chk_company", loc_m.getComp())
                    .addFormDataPart("chk_code", loc_m.getCode())
                    .addFormDataPart("chk_tin", "none")
                    .addFormDataPart("chk_or", "none")
                    .addFormDataPart("chk_date", "none")
                    .addFormDataPart("chk_bcode", "none")
                    .addFormDataPart("transaction_num", "none")
                    .addFormDataPart("chk_amount", "none")
                    .addFormDataPart("chk_number", "none")
                    .addFormDataPart("latitude", latitude)
                    .addFormDataPart("longitude", longitude)
                    .build();

            sendAPI(rbody);
        }
    }

    private void sendAPI(RequestBody rbody){
        LocationManagement loc_m = new LocationManagement(Failed.this);
        SignatureManagement sign_m = new SignatureManagement(Failed.this);
        chequeManagement cs = new chequeManagement(Failed.this);
        cancelManagement can_m = new cancelManagement(Failed.this);

        Request req = new Request.Builder().url("http://203.177.49.26:28110/tracker/api/remarks").post(rbody).build();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Failed.this, "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            responseData = response.body().string();
                            String value = specificValue(responseData);
                            //value.replace("<br />", "");
                            if (value.equals("1")) { //DATA SENT BACK TO API SUCCESSFULLY
                                Toast.makeText(Failed.this, "Transaction Cancelled", Toast.LENGTH_SHORT).show();

                                String messageFailed = "Good Day! This is your Rider from RGS, I\'m messaging to inform you that the Pick-Up was cancelled due to a reason.";
                                if (ContextCompat.checkSelfPermission(Failed.this, Manifest.permission.SEND_SMS)
                                        != PackageManager.PERMISSION_GRANTED) { // ASK PERMISSION
                                    ActivityCompat.requestPermissions(Failed.this, new String[]{Manifest.permission.SEND_SMS},
                                            PERMISSION_SEND_SMS);
                                } else {// PERMISSION GRANTED
                                    //sendSMS(cont, "Hello, this is a test message!");
                                    sendSMS(loc_m.getCont(), messageFailed);
                                }
                                loc_m.removeLocation();
                                can_m.removeCancel();


                                Intent intent = new Intent(Failed.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(Failed.this, "Error: Data not sent to API", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
    }

    private String specificValue(String responseData){
        try{
            JSONObject json = new JSONObject(responseData);
            String value = json.getString("success");
            return value;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseData;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
            else{
                Toast.makeText(Failed.this,"Required Permission", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

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

}