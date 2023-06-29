package com.example.rgs_chequepickup;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import SessionPackage.LocationManagement;
import SessionPackage.ReceiptManagement;
import SessionPackage.accountSession;
import SessionPackage.chequeManagement;
import SessionPackage.chequeSession;
import SessionPackage.remarkSession;
import SessionPackage.scenarioManagement;

public class CaptureCheque extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String currentPhotoPath;
    ImageView captured_image;
    Button camera_button, next_button;
    LinearLayout checklist;
    Intent i;
    String remark, cheq_stat;
    RelativeLayout layout;
    CardView services_list;
    CheckBox bayan, innove, globe;
    String cheques = "";
    TextView back_button;
    Uri image;
    int pic = 0;
    boolean hasRetake = false;
    boolean isSubmit = false;
    Intent retake;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_cheque);

        retake = getIntent();


        if(retake != null && retake.hasExtra("retake")){
            hasRetake = true;
        }
        else{
            hasRetake = false;
        }

        //INTENT
        scenarioManagement sm = new scenarioManagement(CaptureCheque.this);
        remark = sm.getScene();
        cheq_stat = sm.getStat();

        camera_button = (Button) findViewById(R.id.camera_button);
        next_button = (Button) findViewById(R.id.next_button);
        back_button = (TextView) findViewById(R.id.back_button);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        back_button.setTypeface(font);
        back_button.setText("\uf060");


        next_button.setEnabled(false);
        if(ContextCompat.checkSelfPermission(CaptureCheque.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CaptureCheque.this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        else{
            //Toast.makeText(getApplicationContext(),"Camera Disabled/Not Found!", Toast.LENGTH_SHORT).show();
        }

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sm.removeScene();
                Intent intent = new Intent(CaptureCheque.this, CheckList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CaptureCheque.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                    next_button.setEnabled(true);
                } else {
                    // Request CAMERA permission
                    ActivityCompat.requestPermissions(CaptureCheque.this, new String[]{Manifest.permission.CAMERA}, 101);
                }
                //Toast.makeText(CaptureCheque.this, "Counter " + pic, Toast.LENGTH_SHORT).show();
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CaptureCheque.this, ChecklistInvalidCheque.class);
                isSubmit = true;
                startActivity(intent);
                finish();
            }
        });

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted
                openCamera();
                next_button.setEnabled(true);
            } else {
                // Camera permission denied
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
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
            Uri photoURI = FileProvider.getUriForFile(CaptureCheque.this,
                    "com.example.rgs_chequepickup.fileprovider",
                    photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, 101);
        }
    }
    private File createImageFile() throws IOException{
        LocationManagement lm = new LocationManagement(CaptureCheque.this);
        String comp = lm.getComp();

        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //String imageName = "IMG-Cheque_"+ comp + "_" + time + ".jpg";
        String imageName = "INVALID-Cheque_"+ comp + "_" + time;

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //File imageFile = new File(storageDir, imageName);
        File imageFile = File.createTempFile(imageName,".jpg",storageDir);

        chequeManagement cm = new chequeManagement(CaptureCheque.this);
        if(!(cm.getCheck().isEmpty() || cm.getCheck().equals("") || cm.getCheck().equals(" ") || cm.getCheck().equals("none"))){
            //Toast.makeText(CaptureCheque.this,"1,"+cm.getCheck(),Toast.LENGTH_SHORT).show();
            cheques = cm.getCheck() + "," + String.valueOf(imageFile);
        }
        else if(cm.getCheck().isEmpty() || cm.getCheck().equals("") || cm.getCheck().equals(" ") || cm.getCheck().equals("none")){
            cheques = String.valueOf(imageFile);
            //Toast.makeText(CaptureCheque.this,"2,"+cheques,Toast.LENGTH_SHORT).show();
        }
        chequeSession cs = new chequeSession(cheques);
        cm.saveCheck(cs);
        //Toast.makeText(CaptureCheque.this,"2,"+cm.getCheck(),Toast.LENGTH_SHORT).show();
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101 && resultCode == RESULT_OK) {
                /*captured_image = (ImageView) findViewById(R.id.captured_image);
                Bitmap bitmapDisplay = (Bitmap) data.getExtras().get("data");
                captured_image.setImageBitmap(bitmapDisplay);

                saveImageToGallery(bitmapDisplay);*/
            captured_image = (ImageView) findViewById(R.id.captured_image);
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            captured_image.setImageBitmap(bitmap);

            saveImageToGallery(bitmap);
        }
        else{
            Toast.makeText(this, "Image capture failed", Toast.LENGTH_SHORT).show();
        }
    }
    private void saveImageToGallery(Bitmap bitmap) {
        try{
            FileOutputStream fos = new FileOutputStream(currentPhotoPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
            fos.flush();
            fos.close();
            chequeManagement cm = new chequeManagement(CaptureCheque.this);
            //Toast.makeText(this, "Saved to gallery", Toast.LENGTH_SHORT).show();
            //Toast.makeText(CaptureCheque.this,"2,"+cm.getCheck(),Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "ERROR SAVING", Toast.LENGTH_SHORT).show();
            //throw new RuntimeException(e);
        }
        //Toast.makeText(this, "" + imageFile, Toast.LENGTH_SHORT).show();
    }
/*
    @Override
    public void onStop() {
        super.onStop();
        if(isSubmit == false){
            chequeManagement cm = new chequeManagement(CaptureCheque.this);
            cm.removeCheck();
        }
    }
  */
}