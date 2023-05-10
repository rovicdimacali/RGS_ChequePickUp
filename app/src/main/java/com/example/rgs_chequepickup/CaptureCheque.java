package com.example.rgs_chequepickup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CaptureCheque extends AppCompatActivity {

    ImageView captured_image;
    Button camera_button, next_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_cheque);

        captured_image = (ImageView) findViewById(R.id.captured_image);
        camera_button = (Button) findViewById(R.id.camera_button);
        next_button = (Button) findViewById(R.id.next_button);

        if(ContextCompat.checkSelfPermission(CaptureCheque.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CaptureCheque.this, new String[]{Manifest.permission.CAMERA}, 101);
        }
        else{
            //Toast.makeText(getApplicationContext(),"Camera Disabled/Not Found!", Toast.LENGTH_SHORT).show();
        }

        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 101);

            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CaptureCheque.this);

                builder.setCancelable(true);
                builder.setTitle("Defective Cheque");
                builder.setMessage("Is the Cheque Defective?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CaptureCheque.this, ChequeReceived.class);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(CaptureCheque.this, ESignature.class);
                        startActivity(intent);
                    }
                });

                builder.show();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            captured_image.setImageBitmap(bitmap);
        }
    }
}