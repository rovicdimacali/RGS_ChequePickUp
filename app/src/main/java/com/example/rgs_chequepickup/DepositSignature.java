package com.example.rgs_chequepickup;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.github.gcacace.signaturepad.views.SignaturePad;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import SessionPackage.HistoryManagement;
import SessionPackage.LocationManagement;
import SessionPackage.ReceiptManagement;
import SessionPackage.SessionManagement;
import SessionPackage.SignatureManagement;
import SessionPackage.SignatureSession;
import SessionPackage.accountManagement;
import SessionPackage.cancelManagement;
import SessionPackage.chequeManagement;
import SessionPackage.chequeSession;
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

public class DepositSignature extends AppCompatActivity {
    Intent cancel;
    int hasCancel = 0;

    //String pointPerson, cancel;
    private Button clear_img, save_image, capture;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SignaturePad signature_pad;

    private ImageView imageView;
    OkHttpClient client;
    String currentPhotoPath, photoName, signaturePath, signatureName;
    private TextView back_button;
    EditText numberCheq, inputName;
    String numCheq, responseData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_depositsign);

        Intent i = getIntent();
        client = new OkHttpClient();

        numCheq = i.getStringExtra("cheques");

        clear_img = (Button) findViewById(R.id.clear_img);
        capture = (Button) findViewById(R.id.capture_button1);
        save_image = (Button) findViewById(R.id.save_image);

        back_button = (TextView) findViewById(R.id.back_button);
        inputName = (EditText) findViewById(R.id.inputname);
        numberCheq = (EditText) findViewById(R.id.inputnumberofcheques);

        clear_img.setEnabled(false);
        save_image.setEnabled(false);

        numberCheq.setText(numCheq);
        numberCheq.setEnabled(false);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        back_button.setTypeface(font);
        back_button.setText("\uf060");

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                intent = new Intent(DepositSignature.this, TransactionHistory.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
                save_image.setEnabled(true);
                clear_img.setEnabled(true);
            }

            @Override
            public void onClear() {
                save_image.setEnabled(false);
                clear_img.setEnabled(false);
            }
        });

        clear_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signature_pad.clear();
            }
        });

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(DepositSignature.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    // Request CAMERA permission
                    ActivityCompat.requestPermissions(DepositSignature.this, new String[]{Manifest.permission.CAMERA}, 1);
                }
            }
        });
        save_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap signatureBitmap = signature_pad.getSignatureBitmap();
                if (addJpgSignatureToGallery(signatureBitmap) == true) {
                    //Toast.makeText(ESignature.this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(DepositSignature.this, "Empty Signature/Unable to store the signature", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(DepositSignature.this,
                    "com.example.rgs_chequepickup.fileprovider",
                    photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, 101);
        }
    }
    private File createImageFile() throws IOException{
        LocationManagement lm = new LocationManagement(DepositSignature.this);
        String comp = lm.getComp();

        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //String imageName = "IMG-Cheque_"+ comp + "_" + time + ".jpg";
        photoName = "Deposit_"+ inputName.getText().toString() + "_" + time;

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //File imageFile = new File(storageDir, imageName);
        File imageFile = File.createTempFile(photoName,".jpg",storageDir);

        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
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
            Log.e("Gallery", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        Date currentDate = new Date();
        SimpleDateFormat timeForm = new SimpleDateFormat("EEE, MMM d, ''yy");
        String currentTime = timeForm.format(currentDate);

        signatureName = "DepisitSign_"+inputName.getText().toString()+"_"+currentTime+".jpg";

        try {
            if(signature == null){
                result = false;
            }
            else{
                File photo = new File(getAlbumStorageDir("Deposits"), String.format(signatureName, System.currentTimeMillis()));
                saveBitmapToJPG(signature, photo);
                scanMediaFile(photo);
                signaturePath = String.valueOf(photo);
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
        DepositSignature.this.sendBroadcast(mediaScanIntent);
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

    public void postResults(String longitude, String latitude){
        MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");

        SessionManagement sess_m = new SessionManagement(DepositSignature.this);
        HistoryManagement his_m = new HistoryManagement(DepositSignature.this);

        RequestBody rbody;
        MediaType mediaType = MediaType.parse("image/jpeg");

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("chk_rider", sess_m.getSession());
        builder.addFormDataPart("transaction_num", his_m.getTrans());
        builder.addFormDataPart("depo_sign", "DepositSign_"+signatureName, RequestBody.create(MediaType.parse("image/jpeg"),signaturePath));
        builder.addFormDataPart("depo_pic", photoName, RequestBody.create(mediaType, currentPhotoPath));
        builder.addFormDataPart("depo_name", inputName.getText().toString());
        builder.addFormDataPart("depo_cheques", numberCheq.getText().toString());

        rbody = builder.build();

        Request req = new Request.Builder().url("http://203.177.49.26:28110/tracker/api/history").post(rbody).build();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DepositSignature.this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                                Toast.makeText(DepositSignature.this, "Deposit Sign Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DepositSignature.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                                //}
                                //else{
                                //Toast.makeText(ChequeReceived.this, "Error in transaction", Toast.LENGTH_SHORT).show();
                                //}
                            } else {
                                Toast.makeText(DepositSignature.this, "Error: Data not sent to API", Toast.LENGTH_SHORT).show();
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

}
