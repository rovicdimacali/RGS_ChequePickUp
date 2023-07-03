package com.example.rgs_chequepickup;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.github.gcacace.signaturepad.views.SignaturePad;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import SessionPackage.HistoryManagement;
import SessionPackage.LocationManagement;
import SessionPackage.SessionManagement;
import SessionPackage.SignatureManagement;
import SessionPackage.SignatureSession;
import SessionPackage.cancelManagement;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DSignature extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String responseData, name, cheques, img, signPath;
    Button clear, submit;
    TextView back, caption;;
    OkHttpClient client;
    private SignaturePad signature_pad;
    Intent i;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_dsignature);
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(DSignature.this);

        client = new OkHttpClient();
        i = getIntent();

        name = i.getStringExtra("name");
        cheques = i.getStringExtra("cheques");
        img = i.getStringExtra("img");

        clear = (Button) findViewById(R.id.clear_img);
        submit = (Button) findViewById(R.id.save_image);

        back = (TextView) findViewById(R.id.back_button);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        back.setTypeface(font);
        back.setText("\uf060");

        caption = (TextView) findViewById(R.id.caption);
        //caption.setText(img);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                intent = new Intent(DSignature.this, TransactionHistory.class);
                i.putExtra("cheques", cheques);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        signature_pad = (SignaturePad) findViewById(R.id.signature_pad);
        signature_pad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                //Toast.makeText(ESignature.this, "Start Signing", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSigned() {
                submit.setEnabled(true);
                clear.setEnabled(true);
            }

            @Override
            public void onClear() {
                submit.setEnabled(false);
                clear.setEnabled(false);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signature_pad.clear();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap signatureBitmap = signature_pad.getSignatureBitmap();
                if (addJpgSignatureToGallery(signatureBitmap) == true) {
                    postResults();
                    //Toast.makeText(ESignature.this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(DSignature.this, "Empty Signature/Unable to store the signature", Toast.LENGTH_LONG).show();
                }
                /*if (addSvgSignatureToGallery(signature_pad.getSignatureSvg())) {
                    Toast.makeText(ESignature.this, "SVG Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ESignature.this, "Unable to store the SVG signature", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
    }

    public void postResults(){
        SessionManagement sess_m = new SessionManagement(DSignature.this);
        HistoryManagement his_m = new HistoryManagement(DSignature.this);

        RequestBody rbody;
        MediaType mediaType = MediaType.parse("image/jpeg");

        File imgFile = new File(img);
        File signFile = new File(signPath);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("chk_rider", sess_m.getSession());
        builder.addFormDataPart("transaction_num", his_m.getTrans());
        builder.addFormDataPart("chk_deposit_sign", "DepositSign_"+name, RequestBody.create(mediaType,signFile));
        builder.addFormDataPart("chk_deposit_img", "DepositImage_"+name, RequestBody.create(mediaType, imgFile));
        builder.addFormDataPart("chk_deposited_by", name);
        builder.addFormDataPart("chk_cheques", cheques);

        rbody = builder.build();

        Request req = new Request.Builder().url("http://203.177.49.26:28110/tracker/api/deposit").post(rbody).build();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DSignature.this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                            if (value.equals("1")) { //DATA SENT BACK TO API SUCCESSFULLY
                                Toast.makeText(DSignature.this, "Deposit Sign Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DSignature.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                his_m.removeLocation();
                                startActivity(intent);
                                finish();

                            } else {
                                caption.setText(signPath);
                                Toast.makeText(DSignature.this, "Error: Data not sent to API", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            //comp.setText(e.getMessage());
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
            Log.e("DepositSigns", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        stream.close();
    }

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        Date currentDate = new Date();
        SimpleDateFormat timeForm = new SimpleDateFormat("EEE, MMM d, ''yy");
        String currentTime = timeForm.format(currentDate);

        HistoryManagement hm = new HistoryManagement(DSignature.this);
        String comp = hm.getComp();
        String fileName;

        fileName = "DepositSign_"+ comp + "_"+currentTime+".jpg";

        try {
            if(signature == null){
                result = false;
            }
            else{
                File photo = new File(getAlbumStorageDir("DepositSigns"), String.format(fileName, System.currentTimeMillis()));
                saveBitmapToJPG(signature, photo);
                scanMediaFile(photo);
                signPath = String.valueOf(photo);
                result = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        DSignature.this.sendBroadcast(mediaScanIntent);
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
