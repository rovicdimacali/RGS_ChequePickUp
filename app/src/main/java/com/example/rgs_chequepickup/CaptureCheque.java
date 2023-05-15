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
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import Database.CheckerCheque;
public class CaptureCheque extends AppCompatActivity {

    ImageView captured_image;
    Button camera_button, next_button;

    RelativeLayout layout;

    TextView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_cheque);

        captured_image = (ImageView) findViewById(R.id.captured_image);
        camera_button = (Button) findViewById(R.id.camera_button);
        next_button = (Button) findViewById(R.id.next_button);
        back_button = (TextView) findViewById(R.id.back_button);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        back_button.setTypeface(font);
        back_button.setText("\uf060");

        if(ContextCompat.checkSelfPermission(CaptureCheque.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CaptureCheque.this, new String[]{Manifest.permission.CAMERA}, 101);
        }
        else{
            //Toast.makeText(getApplicationContext(),"Camera Disabled/Not Found!", Toast.LENGTH_SHORT).show();
        }

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CaptureCheque.this, CheckList.class);
                startActivity(intent);
            }
        });

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
                        VerifyPopupWindow();
                    }
                });

                builder.show();
            }
        });

    }

    private void VerifyPopupWindow() {
        layout = (RelativeLayout) findViewById(R.id.layout);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.popup_verify_cheque, null);

        EditText chequeNum = popUpView.findViewById(R.id.cheque_number);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);
        layout.post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            }
        });

        Button verify = (Button) popUpView.findViewById(R.id.verify_button);

        String[] services = {"--SERVICES--", "Bayan", "Innove", "Globe Handyphone", "FA ID: Postpaid", "Standard"};
        Spinner spinner = (Spinner) popUpView.findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CaptureCheque.this, R.layout.simple_spinner_item, services);
        adapter.setDropDownViewResource(R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String service = parent.getItemAtPosition(position).toString();
                //Toast.makeText(CaptureCheque.this, service, Toast.LENGTH_SHORT).show();

                verify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Enter code here for cheque verification

                        //NO SERVICE SELECTED
                        if(service.equals("--SERVICES--")){
                            Toast.makeText(CaptureCheque.this,"Please select a service", Toast.LENGTH_SHORT).show();
                        }
                        //BAYAN SERVICES
                        else if(service.equals("Bayan")){
                            if(chequeNum.getText().toString().length() == 9){ //Check if Account Number has 9 digits
                                if(Long.parseLong(chequeNum.getText().toString()) >= 700000000 &&
                                        Long.parseLong(chequeNum.getText().toString()) <= 799999999){
                                    CheckerCheque cc = new CheckerCheque(); //CHECKER CLASS
                                    int result = cc.BayanChecker(Long.parseLong(chequeNum.getText().toString()));
                                    if(result == 1){
                                        Toast.makeText(CaptureCheque.this, "Account Number is valid", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(CaptureCheque.this, ESignature.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(CaptureCheque.this, "Account Number is not valid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(CaptureCheque.this,"Account range is not applicable to BAYAN service", Toast.LENGTH_SHORT).show();
                                }

                            }
                            else{
                                Toast.makeText(CaptureCheque.this, "Account Number for BAYAN service must be 9 digits", Toast.LENGTH_SHORT).show();
                            }
                        }
                        //INNOVE SERVICES
                        else if(service.equals("Innove")){
                            if(chequeNum.getText().toString().length() == 9){ //Check if Account Number has 9 digits
                                if(Long.parseLong(chequeNum.getText().toString()) >= 100000000 &&
                                        Long.parseLong(chequeNum.getText().toString()) <= 999999999){
                                    CheckerCheque cc = new CheckerCheque(); //CHECKER CLASS
                                    int result = cc.InnoveChecker(Long.parseLong(chequeNum.getText().toString()));
                                    if(result == 1){
                                        Toast.makeText(CaptureCheque.this, "Account Number is valid", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(CaptureCheque.this, ESignature.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(CaptureCheque.this, "Account Number is not valid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(CaptureCheque.this,"Account range is not applicable to INNOVE service", Toast.LENGTH_SHORT).show();
                                }

                            }
                            else{
                                Toast.makeText(CaptureCheque.this, "Account Number for INNOVE service must be 9 digits", Toast.LENGTH_SHORT).show();
                            }
                        }
                        //GLOBE HANDYPHONE SERVICES
                        else if(service.equals("Globe Handyphone")){
                            if(chequeNum.getText().toString().length() == 8){ //Check if Account Number has 9 digits
                                if(Long.parseLong(chequeNum.getText().toString()) >= 10000000 &&
                                        Long.parseLong(chequeNum.getText().toString()) <= 99999999){
                                    CheckerCheque cc = new CheckerCheque(); //CHECKER CLASS
                                    int result = cc.GlobeHandyphoneChecker(Long.parseLong(chequeNum.getText().toString()));
                                    if(result == 1){
                                        Toast.makeText(CaptureCheque.this, "Account Number is valid", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(CaptureCheque.this, ESignature.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(CaptureCheque.this, "Account Number is not valid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(CaptureCheque.this,"Account range is not applicable to GLOBE HP service", Toast.LENGTH_SHORT).show();
                                }

                            }
                            else{
                                Toast.makeText(CaptureCheque.this, "Account Number for GLOBE HP service must be 8 digits", Toast.LENGTH_SHORT).show();
                            }
                        }
                        //Intent intent = new Intent(CaptureCheque.this, ESignature.class);
                        //startActivity(intent);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Enter code here for cheque verification

                Intent intent = new Intent(CaptureCheque.this, ESignature.class);
                startActivity(intent);
            }
        });*/
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            captured_image.setImageBitmap(bitmap);
        }
    }
}