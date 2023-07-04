package com.example.rgs_chequepickup;

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
import java.util.Objects;
import java.util.Random;

import SessionPackage.LocationManagement;
import SessionPackage.ReceiptManagement;
import SessionPackage.SessionManagement;
import SessionPackage.SignatureManagement;
import SessionPackage.accountManagement;
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

public class ChequeReceived extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    Button back_button;
    FusedLocationProviderClient fspc;
    double cur_lat, cur_long;
    private final static int REQUEST_CODE = 100;
    OkHttpClient client;
    boolean isAdded = false;
    String responseData;
    TextView comp;
    Drawable defaultPic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_cheque_received);

        client = new OkHttpClient();
        back_button = findViewById(R.id.back_button);
        fspc = LocationServices.getFusedLocationProviderClient(ChequeReceived.this);
        comp = findViewById(R.id.complete);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
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
                        Geocoder gc = new Geocoder(ChequeReceived.this, Locale.getDefault());
                        List<Address> sadd;
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
        ActivityCompat.requestPermissions(ChequeReceived.this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    //SEND RESULTS TO THE API
    public void postResults(String longitude, String latitude){
        ReceiptManagement rm = new ReceiptManagement(ChequeReceived.this);
        SessionManagement sess_m = new SessionManagement(ChequeReceived.this);
        scenarioManagement scene_m = new scenarioManagement(ChequeReceived.this);
        accountManagement acc_m = new accountManagement(ChequeReceived.this);
        remarkManagement rem_m = new remarkManagement(ChequeReceived.this);
        LocationManagement loc_m = new LocationManagement(ChequeReceived.this);
        SignatureManagement sign_m = new SignatureManagement(ChequeReceived.this);
        chequeManagement cs = new chequeManagement(ChequeReceived.this);

        String chequePath = cs.getCheck();
        String[] explodePaths = chequePath.split(",");

        RequestBody rbody;
        MediaType mediaType = MediaType.parse("image/jpeg");

        if(scene_m.getStat().equals("Defective")){
            Resources res = getResources();
            defaultPic = res.getDrawable(R.drawable.cancel);

            Random ran = new Random();
            int transNum = ran.nextInt(900000) + 100000;

            //String transaction = "PU" + String.valueOf(transNum) + "_" + dateString;
            String transaction = "PU" + transNum;

            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            builder.addFormDataPart("chk_rider", sess_m.getSession());
            builder.addFormDataPart("chk_status", scene_m.getStat());
            builder.addFormDataPart("cancel_name", "none");
            builder.addFormDataPart("chk_sign", "", RequestBody.create(MediaType.parse("image/jpeg"),defaultPic.toString()));
            for(int i = 0; i < explodePaths.length; i++){
                if(!(explodePaths[i].contains("IMG-Cheque0"))){
                    File file = new File(explodePaths[i]);
                    builder.addFormDataPart("chk_pic"+i, cs.getCheck(), RequestBody.create(mediaType, file));
                    //Toast.makeText(ChequeReceived.this, "" + cs.getCheck(), Toast.LENGTH_SHORT).show();
                }
            }
            builder.addFormDataPart("chk_accno", "none");
            builder.addFormDataPart("chk_payee", "none");
            builder.addFormDataPart("chk_entity", "none");
            builder.addFormDataPart("chk_address", loc_m.getAdd());
            builder.addFormDataPart("chk_company", loc_m.getComp());
            builder.addFormDataPart("chk_code", loc_m.getCode());
            builder.addFormDataPart("chk_remark", "All Invalid: " + rem_m.getRemark());
            builder.addFormDataPart("chk_tin", "none");
            builder.addFormDataPart("chk_or", "none");
            builder.addFormDataPart("chk_date", "none");
            builder.addFormDataPart("chk_bcode", "none");
            builder.addFormDataPart("transaction_num", transaction);
            builder.addFormDataPart("chk_amount", "none");
            builder.addFormDataPart("chk_number", "none");
            builder.addFormDataPart("latitude", latitude);
            builder.addFormDataPart("longitude", longitude);

            rbody = builder.build();
        }
        else{
            Resources res = getResources();
            defaultPic = res.getDrawable(R.drawable.cancel);

            Random ran = new Random();
            int transNum = ran.nextInt(900000) + 100000;

            //String transaction = "PU" + String.valueOf(transNum) + "_" + dateString;
            String transaction = "PU" + transNum;

            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            builder.addFormDataPart("chk_rider", sess_m.getSession());
            builder.addFormDataPart("chk_status", scene_m.getStat());
            builder.addFormDataPart("cancel_name", "none");
            builder.addFormDataPart("chk_sign", "", RequestBody.create(MediaType.parse("image/jpeg"),defaultPic.toString()));
            //builder.addFormDataPart("chk_pic", "", RequestBody.create(MediaType.parse("image/jpeg"),defaultPic.toString()));
            for(int i = 0; i < explodePaths.length; i++){
                if(!(explodePaths[i].contains("IMG-Cheque0"))){
                    File file = new File(explodePaths[i]);
                    builder.addFormDataPart("chk_pic"+i, file.getName(), RequestBody.create(mediaType, file));
                    if(explodePaths[i].contains("INVALID-Cheque")){
                        builder.addFormDataPart("chk_accno", acc_m.getAccno()+",none");
                        builder.addFormDataPart("chk_or", rm.getOR()+",none");
                        builder.addFormDataPart("chk_payee", rm.getPayee()+",none");
                        builder.addFormDataPart("chk_amount", rm.getAmount()+",none");
                        isAdded = true;
                    }
                    else{
                        isAdded = false;
                    }
                }
            }

            if(isAdded != true){
                builder.addFormDataPart("chk_accno", acc_m.getAccno());
                builder.addFormDataPart("chk_or", rm.getOR());
                builder.addFormDataPart("chk_payee", rm.getPayee());
                builder.addFormDataPart("chk_amount", rm.getAmount());
                isAdded = false;
            }
            builder.addFormDataPart("chk_entity", "none");
            builder.addFormDataPart("chk_remark", "Invalid Cheques: " + rem_m.getRemark());
            builder.addFormDataPart("chk_address", loc_m.getAdd());
            builder.addFormDataPart("chk_company", loc_m.getComp());
            builder.addFormDataPart("chk_code", loc_m.getCode());
            builder.addFormDataPart("chk_tin", rm.getTin());
            builder.addFormDataPart("chk_date", "none");
            builder.addFormDataPart("chk_bcode", "none");
            builder.addFormDataPart("transaction_num", transaction);
            builder.addFormDataPart("chk_number", "none");
            builder.addFormDataPart("latitude", latitude);
            builder.addFormDataPart("longitude", longitude);

            rbody = builder.build();

        }
        Request req = new Request.Builder().url("http://203.177.49.26:28110/tracker/api/remarks").post(rbody).build();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChequeReceived.this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            responseData = Objects.requireNonNull(response.body()).
                                    string();
                            String value = specificValue(responseData);
                            if (value.equals("1")) { //DATA SENT BACK TO API SUCCESSFULLY
                                Toast.makeText(ChequeReceived.this, "Transaction Success", Toast.LENGTH_SHORT).show();
                                scene_m.removeScene();
                                acc_m.removeAcc();
                                rem_m.removeRemark();
                                loc_m.removeLocation();
                                sign_m.removeSign();
                                cs.removeCheck();
                                rm.removeReceipt();
                                Intent intent = new Intent(ChequeReceived.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                scene_m.removeScene();
                                acc_m.removeAcc();
                                rem_m.removeRemark();
                                loc_m.removeLocation();
                                sign_m.removeSign();
                                cs.removeCheck();
                                rm.removeReceipt();
                                comp.setText(value);
                                Toast.makeText(ChequeReceived.this, "Error: Data not sent to API", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            comp.setText(e.getMessage());
                            //throw new RuntimeException(e);
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

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(ESignature.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("RGS_Express Signs", "Directory not created");
        }
        return file;
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}