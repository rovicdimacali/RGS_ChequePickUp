package com.example.rgs_chequepickup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import SessionPackage.LocationManagement;
import SessionPackage.SignatureManagement;
import SessionPackage.SignatureSession;
import SessionPackage.cancelManagement;

public class ESignature extends AppCompatActivity {
    int hasCancel = 0;

    //String pointPerson, cancel;
    private Button clear_img, save_image;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SignaturePad signature_pad;

    TextView back_button;
    String cancelStatus;
    EditText name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_esignature);

        cancelManagement cm = new cancelManagement(ESignature.this);
        cancelStatus = cm.getCancel();

        name = findViewById(R.id.name);

        clear_img = findViewById(R.id.clear_img);

        save_image = findViewById(R.id.save_image);

        back_button = findViewById(R.id.back_button);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        back_button.setTypeface(font);
        back_button.setText("\uf060");

        if(!cancelStatus.equals("none")){
            hasCancel = 1;
            name.setText(cm.getPoint());
        }

        back_button.setOnClickListener(v -> {
            Intent intent;
            if(hasCancel == 1){
                cancelManagement cm1 = new cancelManagement(ESignature.this);
                cm1.removeCancel();
                intent = new Intent(ESignature.this, CancelActivity.class);
            }
            else{
                intent = new Intent(ESignature.this, RemarksActivity.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        signature_pad = findViewById(R.id.signature_pad);
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
            }
        });

        clear_img.setOnClickListener(v -> signature_pad.clear());

        save_image.setOnClickListener(view -> {
            Bitmap signatureBitmap = signature_pad.getSignatureBitmap();
            if (addJpgSignatureToGallery(signatureBitmap)) {
                Intent intent;
                if(hasCancel == 1){
                    intent = new Intent(ESignature.this, Failed.class);
                }
                else{
                    intent = new Intent(ESignature.this, OfficialReceipt.class);
                }
                startActivity(intent);
                finish();
                //Toast.makeText(ESignature.this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(ESignature.this, "Empty Signature/Unable to store the signature", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
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

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            stream = Files.newOutputStream(photo.toPath());
        }
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        Objects.requireNonNull(stream).close();
    }

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        Date currentDate = new Date();
        SimpleDateFormat timeForm = new SimpleDateFormat("EEE, MMM d, ''yy");
        String currentTime = timeForm.format(currentDate);

        LocationManagement lm = new LocationManagement(ESignature.this);
        cancelManagement cm = new cancelManagement(ESignature.this);
        String comp = lm.getComp().replace(" ", "-").replace(",","_");;
        String fileName;

        if(hasCancel == 1){
            fileName = comp+"_"+currentTime+".jpg";
        }
        else{
            fileName = cm.getPoint()+"_"+currentTime+".jpg";
        }

        SignatureManagement sm = new SignatureManagement(ESignature.this);
        SignatureSession ss = new SignatureSession(fileName);
        sm.saveSign(ss);

        try {
            File photo = new File(getAlbumStorageDir("RGS_Express Signs"), String.format(fileName, System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        ESignature.this.sendBroadcast(mediaScanIntent);
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
