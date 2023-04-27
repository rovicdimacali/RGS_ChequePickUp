package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.gcacace.signaturepad.views.SignaturePad;

public class ESignature extends AppCompatActivity {

    Button clear_img, load_image;

    SignaturePad signature_pad;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esignature);

        clear_img = (Button) findViewById(R.id.clear_img);

        load_image = (Button) findViewById(R.id.load_image);

        //imageView = (ImageView) findViewById(R.id.imageView);

        signature_pad = (SignaturePad) findViewById(R.id.signature_pad);

        clear_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signature_pad.clear();
            }
        });

        load_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bitmap bitmap = signature_pad.getSignatureBitmap();
                //imageView.setImageBitmap(bitmap);
                Intent intent = new Intent(ESignature.this, ChequeReceived.class);
                startActivity(intent);

                //openChequereceived();
                }
        });
    }
    /*public void openChequereceived(){
        Intent intent = new Intent(this, ChequeReceived.class);
        startActivity(intent);
    }*/
}
