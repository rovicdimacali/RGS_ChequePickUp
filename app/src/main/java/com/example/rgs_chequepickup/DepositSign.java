package com.example.rgs_chequepickup;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import SessionPackage.HistoryManagement;
import SessionPackage.LocationManagement;
import SessionPackage.SessionManagement;
import SessionPackage.chequeManagement;
import SessionPackage.chequeSession;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DepositSign extends AppCompatActivity {

    Button save_image, capture;
    TextView back, caption;
    EditText name, cheques;
    ImageView image;
    String numCheq, responseData;
    Intent intent;

    private String currentPhotoPath, photoPath, signaturePath, signatureName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depositsign);

        Intent i = getIntent();

        name = (EditText) findViewById(R.id.inputname);
        numCheq = i.getStringExtra("cheques");
        cheques = (EditText) findViewById(R.id.inputnumberofcheques);
        caption = (TextView) findViewById(R.id.caption);
        capture = (Button) findViewById(R.id.capture_button1);
        save_image = (Button) findViewById(R.id.save_image);
        back = (TextView) findViewById(R.id.back_button);

        cheques.setText(numCheq);
        cheques.setEnabled(false);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        back.setTypeface(font);
        back.setText("\uf060");

        if(ContextCompat.checkSelfPermission(DepositSign.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(DepositSign.this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        else{
            //Toast.makeText(getApplicationContext(),"Camera Disabled/Not Found!", Toast.LENGTH_SHORT).show();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                intent = new Intent(DepositSign.this, TransactionHistory.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(DepositSign.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    // Request CAMERA permission
                    ActivityCompat.requestPermissions(DepositSign.this, new String[]{Manifest.permission.CAMERA}, 101);
                }
            }
        });

        save_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty()){
                    Toast.makeText(DepositSign.this, "Enter a name", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent i = new Intent(DepositSign.this, DSignature.class);
                    i.putExtra("cheques", cheques.getText().toString());
                    i.putExtra("name", name.getText().toString());
                    i.putExtra("img", photoPath);
                    startActivity(i);
                }

                //postResults();
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted
                openCamera();
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
            Uri photoURI = FileProvider.getUriForFile(DepositSign.this,
                    "com.example.rgs_chequepickup.fileprovider",
                    photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, 101);
        }
    }
    private File createImageFile() throws IOException{
        HistoryManagement hm = new HistoryManagement(DepositSign.this);
        String comp = hm.getComp();

        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //String imageName = "IMG-Cheque_"+ comp + "_" + time + ".jpg";
        String imageName = "DepositPicture_"+ comp + "_" + time;

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //File imageFile = new File(storageDir, imageName);
        File imageFile = File.createTempFile(imageName,".jpg",storageDir);

        //Toast.makeText(CaptureCheque.this,"2,"+cm.getCheck(),Toast.LENGTH_SHORT).show();
        currentPhotoPath = imageFile.getAbsolutePath();
        photoPath = String.valueOf(imageFile);
        return imageFile;
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101 && resultCode == RESULT_OK) {
            image = (ImageView) findViewById(R.id.cheque_img);
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            image.setImageBitmap(bitmap);

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
            //Toast.makeText(this, "Saved to gallery", Toast.LENGTH_SHORT).show();
            //Toast.makeText(CaptureCheque.this,"2,"+cm.getCheck(),Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "ERROR SAVING", Toast.LENGTH_SHORT).show();
            //throw new RuntimeException(e);
        }
        //Toast.makeText(this, "" + imageFile, Toast.LENGTH_SHORT).show();
    }
}
