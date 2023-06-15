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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import SessionPackage.LocationManagement;
import SessionPackage.ReceiptManagement;
import SessionPackage.SessionManagement;
import SessionPackage.SignatureManagement;
import SessionPackage.SignatureSession;
import SessionPackage.UserSession;
import SessionPackage.accountManagement;
import SessionPackage.chequeManagement;
import SessionPackage.remarkManagement;
import SessionPackage.scenarioManagement;
import Database.sqlPickUp;
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
    String imagePath;
    TextView comp;
    Drawable defaultPic;
    File imageFile;
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
        File chequeFile = new File(chequePath);

        /*if(!(chequeFile.exists())){
            Toast.makeText(ChequeReceived.this, "Cheque Image Missing", Toast.LENGTH_LONG).show();
            Intent i = new Intent(ChequeReceived.this, CaptureCheque.class);
            i.putExtra("retake", 1);
            startActivity(i);
        }*/

        RequestBody rbody;
        MediaType mediaType = MediaType.parse("image/jpeg");

        if(scene_m.getStat().equals("Defective")){
            Resources res = getResources();
            defaultPic = res.getDrawable(R.drawable.cancel);

            Random ran = new Random();
            int transNum = ran.nextInt(900000) + 100000;
            Date currDate = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = sdf.format(currDate);

            //String transaction = "PU" + String.valueOf(transNum) + "_" + dateString;
            String transaction = "PU" + String.valueOf(transNum);

            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            builder.addFormDataPart("chk_rider", sess_m.getSession());
            builder.addFormDataPart("chk_status", scene_m.getStat());
            builder.addFormDataPart("cancel_name", "none");
            builder.addFormDataPart("chk_sign", "", RequestBody.create(MediaType.parse("image/jpeg"),defaultPic.toString()));
            for(int i = 0; i < explodePaths.length; i++){
                if(explodePaths[i].contains("INVALID-Cheque")){
                    File file = new File(explodePaths[i]);
                    builder.addFormDataPart("chk_pic", cs.getCheck(), RequestBody.create(mediaType, file));
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
            /*rbody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("chk_rider", sess_m.getSession())
                    .addFormDataPart("chk_status", scene_m.getStat())
                    .addFormDataPart("cancel_name", "none")
                    .addFormDataPart("chk_sign", "", RequestBody.create(MediaType.parse("image/jpeg"),defaultPic.toString()))
                    //.addFormDataPart("chk_pic", cs.getCheck(), RequestBody.create(MediaType.parse("image/jpeg"), chequeFile))
                    .addFormDataPart("chk_accno", "none")
                    .addFormDataPart("chk_payee", "none")
                    .addFormDataPart("chk_entity", "none")
                    .addFormDataPart("chk_remark", "none")
                    .addFormDataPart("chk_address", loc_m.getAdd())
                    .addFormDataPart("chk_company", loc_m.getComp())
                    .addFormDataPart("chk_code", loc_m.getCode())
                    .addFormDataPart("chk_tin", "none")
                    .addFormDataPart("chk_or", "none")
                    .addFormDataPart("chk_date", "none")
                    .addFormDataPart("chk_bcode", "none")
                    .addFormDataPart("transaction_num", transaction)
                    .addFormDataPart("chk_amount", "none")
                    .addFormDataPart("chk_number", "none")
                    .addFormDataPart("latitude", latitude)
                    .addFormDataPart("longitude", longitude)
                    .build();*/
        }
        //sign_m.getSign(), RequestBody.create(MEDIA_TYPE_JPEG, new File(imagePath))
        /*.addFormDataPart("chk_tin", rm.getTin())
                    .addFormDataPart("chk_amount", rm.getAmount())
                    .addFormDataPart("chk_number", rm.getNumber())*/
        else{
            Resources res = getResources();
            defaultPic = res.getDrawable(R.drawable.cancel);

            Random ran = new Random();
            int transNum = ran.nextInt(900000) + 100000;
            Date currDate = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = sdf.format(currDate);

            //String transaction = "PU" + String.valueOf(transNum) + "_" + dateString;
            String transaction = "PU" + String.valueOf(transNum);

            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            builder.addFormDataPart("chk_rider", sess_m.getSession());
            builder.addFormDataPart("chk_status", scene_m.getStat());
            builder.addFormDataPart("cancel_name", "none");
            builder.addFormDataPart("chk_sign", "", RequestBody.create(MediaType.parse("image/jpeg"),defaultPic.toString()));
            for(int i = 0; i < explodePaths.length; i++){
                if(!(explodePaths[i].contains("IMG-Cheque0"))){
                    File file = new File(explodePaths[i]);
                    builder.addFormDataPart("chk_pic", cs.getCheck(), RequestBody.create(mediaType, file));
                }
            }
            builder.addFormDataPart("chk_accno", acc_m.getAccno());
            builder.addFormDataPart("chk_payee", rm.getPayee());
            builder.addFormDataPart("chk_entity", "none");
            builder.addFormDataPart("chk_remark", "Invalid Cheques: " + rem_m.getRemark());
            builder.addFormDataPart("chk_address", loc_m.getAdd());
            builder.addFormDataPart("chk_company", loc_m.getComp());
            builder.addFormDataPart("chk_code", loc_m.getCode());
            builder.addFormDataPart("chk_tin", rm.getTin());
            builder.addFormDataPart("chk_or", rm.getOR());
            builder.addFormDataPart("chk_date", "none");
            builder.addFormDataPart("chk_bcode", "none");
            builder.addFormDataPart("transaction_num", transaction);
            builder.addFormDataPart("chk_amount", rm.getAmount());
            builder.addFormDataPart("chk_number", "none");
            builder.addFormDataPart("latitude", latitude);
            builder.addFormDataPart("longitude", longitude);

            rbody = builder.build();

            /*rbody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("chk_rider", sess_m.getSession())
                    .addFormDataPart("chk_status", scene_m.getStat())
                    .addFormDataPart("cancel_name", "none")
                    .addFormDataPart("chk_sign", "", RequestBody.create(MediaType.parse("image/jpeg"), defaultPic.toString()))
                    .addFormDataPart("chk_pic", cs.getCheck(), RequestBody.create(MediaType.parse("image/jpeg"), chequeFile))
                    .addFormDataPart("chk_accno", acc_m.getAccno())
                    .addFormDataPart("chk_payee", rm.getPayee())
                    .addFormDataPart("chk_entity", "none")
                    .addFormDataPart("chk_remark", rem_m.getRemark())
                    .addFormDataPart("chk_address", loc_m.getAdd())
                    .addFormDataPart("chk_company", loc_m.getComp())
                    .addFormDataPart("chk_code", loc_m.getCode())
                    .addFormDataPart("chk_tin", rm.getTin())
                    .addFormDataPart("chk_or", rm.getOR())
                    .addFormDataPart("chk_date", "none")
                    .addFormDataPart("chk_bcode", "none")
                    .addFormDataPart("transaction_num", "none")
                    .addFormDataPart("chk_amount", rm.getAmount())
                    .addFormDataPart("chk_number", "none")
                    .addFormDataPart("latitude", latitude)
                    .addFormDataPart("longitude", longitude)
                    .build();*/
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
                            responseData = response.body().
                                    string();
                            String value = specificValue(responseData);
                            //value.replace("<br />", "");
                            if (value.equals("1")) { //DATA SENT BACK TO API SUCCESSFULLY
                                //sqlPickUp spu = new sqlPickUp(ChequeReceived.this);
                                //int res = spu.addHistory(loc_m.getComp(), loc_m.getPer(), loc_m.getAdd(), loc_m.getCont(), loc_m.getCode());
                                //if(res == 1){
                                    Toast.makeText(ChequeReceived.this, "Transaction Success", Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(ChequeReceived.this, "Transaction added to Pick Up history", Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(ChequeReceived.this, imagePath, Toast.LENGTH_SHORT).show();
                                    //sess_m.removeSession();
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
                                //}
                                //else{
                                    //Toast.makeText(ChequeReceived.this, "Error in transaction", Toast.LENGTH_SHORT).show();
                                //}
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
