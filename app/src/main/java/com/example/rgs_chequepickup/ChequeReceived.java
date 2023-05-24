package com.example.rgs_chequepickup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import SessionPackage.SessionManagement;
import SessionPackage.SignatureManagement;
import SessionPackage.SignatureSession;
import SessionPackage.UserSession;
import SessionPackage.accountManagement;
import SessionPackage.chequeManagement;
import SessionPackage.remarkManagement;
import SessionPackage.scenarioManagement;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChequeReceived extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    Button back_button;
    FusedLocationProviderClient fspc;
    double cur_lat, cur_long;
    private final static int REQUEST_CODE = 100;
    OkHttpClient client;
    String responseData;
    TextView comp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_cheque_received);

        client = new OkHttpClient();
        back_button = (Button) findViewById(R.id.back_button);
        fspc = LocationServices.getFusedLocationProviderClient(ChequeReceived.this);
        comp = (TextView) findViewById(R.id.complete);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
                //Intent intent = new Intent(ChequeReceived.this, MainActivity.class);
                /*LocationManagement lm = new LocationManagement(ChequeReceived.this);
                lm.removeLocation();
                startActivity(intent);*/
            }
        });
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ChequeReceived.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);*/
    }

    private void getCurrentLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){
            fspc.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        Geocoder gc = new Geocoder(ChequeReceived.this, Locale.getDefault());
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
        ActivityCompat.requestPermissions(ChequeReceived.this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    //SEND RESULTS TO THE API
    public void postResults(String longitude, String latitude){
        MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");

        SessionManagement sess_m = new SessionManagement(ChequeReceived.this);
        scenarioManagement scene_m = new scenarioManagement(ChequeReceived.this);
        accountManagement acc_m = new accountManagement(ChequeReceived.this);
        remarkManagement rem_m = new remarkManagement(ChequeReceived.this);
        LocationManagement loc_m = new LocationManagement(ChequeReceived.this);
        SignatureManagement sign_m = new SignatureManagement(ChequeReceived.this);
        chequeManagement cs = new chequeManagement(ChequeReceived.this);

        String imagePath = getAlbumStorageDir("RGS_Express Signs") + "/" + sign_m.getSign();
        String chequePath = cs.getCheck();

        File imageFile = new File(imagePath);
        File chequeFile = new File(chequePath);

        if(!(chequeFile.exists())){
            Toast.makeText(ChequeReceived.this, "Cheque Image Missing", Toast.LENGTH_LONG).show();
        }
        else {
            RequestBody rbody;
            if(!(imageFile.exists())){
                rbody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("chk_rider", sess_m.getSession())
                        .addFormDataPart("chk_status", scene_m.getStat())
                        .addFormDataPart("chk_sign", "", RequestBody.create(MediaType.parse("image/jpeg"), chequeFile))
                        .addFormDataPart("chk_pic", cs.getCheck(), RequestBody.create(MediaType.parse("image/jpeg"), chequeFile))
                        .addFormDataPart("chk_accno", acc_m.getAccno())
                        .addFormDataPart("chk_entity", acc_m.getEntity())
                        .addFormDataPart("chk_remark", rem_m.getRemark())
                        .addFormDataPart("chk_address", loc_m.getAdd())
                        .addFormDataPart("chk_company", loc_m.getComp())
                        .addFormDataPart("chk_code", loc_m.getCode())
                        .addFormDataPart("latitude", latitude)
                        .addFormDataPart("longitude", longitude)
                        .build();
            }
            //sign_m.getSign(), RequestBody.create(MEDIA_TYPE_JPEG, new File(imagePath))
            else{
                rbody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("chk_rider", sess_m.getSession())
                        .addFormDataPart("chk_status", scene_m.getStat())
                        .addFormDataPart("chk_sign", sign_m.getSign(), RequestBody.create(MediaType.parse("image/jpeg"), imageFile))
                        .addFormDataPart("chk_pic", cs.getCheck(), RequestBody.create(MediaType.parse("image/jpeg"), chequeFile))
                        .addFormDataPart("chk_accno", acc_m.getAccno())
                        .addFormDataPart("chk_entity", acc_m.getEntity())
                        .addFormDataPart("chk_remark", rem_m.getRemark())
                        .addFormDataPart("chk_address", loc_m.getAdd())
                        .addFormDataPart("chk_company", loc_m.getComp())
                        .addFormDataPart("chk_code", loc_m.getCode())
                        .addFormDataPart("latitude", latitude)
                        .addFormDataPart("longitude", longitude)
                        .build();
            }
            Request req = new Request.Builder().url("http://203.177.49.26:28110/tracker/api/remarks").post(rbody).build();
            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ChequeReceived.this, "ERROR", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(ChequeReceived.this, "Transaction Success\nSuccess Code: " + value, Toast.LENGTH_SHORT).show();
                                    Toast.makeText(ChequeReceived.this, imagePath, Toast.LENGTH_SHORT).show();
                                    //sess_m.removeSession();
                                    scene_m.removeScene();
                                    acc_m.removeAcc();
                                    rem_m.removeRemark();
                                    loc_m.removeLocation();
                                    sign_m.removeSign();
                                    cs.removeCheck();
                                    Intent intent = new Intent(ChequeReceived.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    scene_m.removeScene();
                                    acc_m.removeAcc();
                                    rem_m.removeRemark();
                                    loc_m.removeLocation();
                                    sign_m.removeSign();
                                    cs.removeCheck();
                                    comp.setText(value);
                                    Toast.makeText(ChequeReceived.this, value, Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
            });
        }
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
                                           @NonNull String permissions[],@NonNull int[] grantResults) {
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
