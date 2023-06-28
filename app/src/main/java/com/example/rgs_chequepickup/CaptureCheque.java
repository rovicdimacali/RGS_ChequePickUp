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
            ActivityCompat.requestPermissions(CaptureCheque.this, new String[]{Manifest.permission.CAMERA}, 101);
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
                    ActivityCompat.requestPermissions(CaptureCheque.this, new String[]{Manifest.permission.CAMERA}, 1);
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
                /*scenarioManagement sm = new scenarioManagement(CaptureCheque.this);
                String res = sm.getStat();
                if(res.equals("Defective")){
                    accountSession as = new accountSession("N/A", "N/A");
                    remarkSession rs = new remarkSession("Defective Cheque");

                    Intent intent = new Intent(CaptureCheque.this, ChecklistInvalidCheque.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(CaptureCheque.this, ChecklistInvalidCheque.class);
                    startActivity(intent);
                    finish();
                    //VerifyPopupWindow(remark);
                }*/

               /*AlertDialog.Builder builder = new AlertDialog.Builder(CaptureCheque.this);

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

                builder.show();*/
            }
        });

    }


    /*private void VerifyPopupWindow(String remark) {
        layout = (RelativeLayout) findViewById(R.id.layout);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.popup_invalid_cheque, null);
        services_list = popUpView.findViewById(R.id.cardView_spinner);

        Spinner spinner = (Spinner) popUpView.findViewById(R.id.spinner);
        checklist = (LinearLayout) popUpView.findViewById(R.id.services_checklist);

        services_list.setVisibility(View.VISIBLE);
        checklist.setVisibility(View.GONE);

        bayan = (CheckBox) checklist.findViewById(R.id.checkbox_bayan);
        innove = (CheckBox) checklist.findViewById(R.id.checkbox_innove);
        globe = (CheckBox) checklist.findViewById(R.id.checkbox_globe);

        EditText chequeNum = popUpView.findViewById(R.id.cheque_number);
        String[] services;
        if(remark.equals("One Cheque, Multiple Accounts")  || remark.equals("One Cheque, Multiple Entities") ||
                remark.equals("Multiple Accounts, Multiple Cheques")){
            chequeNum.setText(remark);
            chequeNum.setEnabled(false);

            String remarklist;

            if(remark.equals("One Cheque, Multiple Entities")){
                services_list.setVisibility(View.GONE);
                checklist.setVisibility(View.VISIBLE);
                remarklist = "Multiple Entities";
            }
            else{
                services_list.setVisibility(View.VISIBLE);
                checklist.setVisibility(View.GONE);
                remarklist = "Multiple Accounts";
            }
            services = new String[]{remarklist};
        }
        else{
            services = new String[]{"--SERVICES--", "Bayan", "Innove", "Globe Handyphone", "FA ID: Postpaid"};
        }

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
        RelativeLayout overlay = (RelativeLayout) popUpView.findViewById(R.id.overlay);

        layout.post(new Runnable() {
            @Override
            public void run() {
                overlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
        });

        //String[] services = {"--SERVICES--", "Bayan", "Innove", "Globe Handyphone", "FA ID: Postpaid", "Standard", "Multiple Accounts" , "Multiple Entities"};

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
                                        Intent intent = null;
                                        Toast.makeText(CaptureCheque.this, "Account Number is valid", Toast.LENGTH_SHORT).show();
                                        if(hasRetake == true){
                                            intent = new Intent(CaptureCheque.this, ChequeReceived.class);
                                        }
                                        else{
                                            intent = new Intent(CaptureCheque.this, RemarksActivity.class);
                                        }
                                        accountManagement am = new accountManagement(CaptureCheque.this);
                                        accountSession as = new accountSession(chequeNum.getText().toString(), service);
                                        am.saveAccount(as);
                                        startActivity(intent);
                                        finish();
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
                                        Intent intent;
                                        Toast.makeText(CaptureCheque.this, "Account Number is valid", Toast.LENGTH_SHORT).show();
                                        if(hasRetake == true){
                                            intent = new Intent(CaptureCheque.this, ChequeReceived.class);
                                        }
                                        else{
                                            intent = new Intent(CaptureCheque.this, RemarksActivity.class);
                                        }
                                        accountManagement am = new accountManagement(CaptureCheque.this);
                                        accountSession as = new accountSession(chequeNum.getText().toString(), service);
                                        am.saveAccount(as);
                                        startActivity(intent);
                                        finish();
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
                                        Intent intent;
                                        Toast.makeText(CaptureCheque.this, "Account Number is valid", Toast.LENGTH_SHORT).show();
                                        if(hasRetake == true){
                                            intent = new Intent(CaptureCheque.this, ChequeReceived.class);
                                        }
                                        else{
                                            intent = new Intent(CaptureCheque.this, RemarksActivity.class);
                                        }
                                        accountManagement am = new accountManagement(CaptureCheque.this);
                                        accountSession as = new accountSession(chequeNum.getText().toString(), service);
                                        am.saveAccount(as);
                                        startActivity(intent);
                                        finish();
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
                        //FA ID FOR POSTPAID
                        else if(service.equals("FA ID: Postpaid")){
                            if(chequeNum.getText().toString().length() == 9){
                                CheckerCheque cc = new CheckerCheque(); //CHECKER CLASS
                                int result = cc.FA_ID(chequeNum.getText().toString());
                                if(result == 1){
                                    Intent intent;
                                    Toast.makeText(CaptureCheque.this, "Account Number is valid", Toast.LENGTH_SHORT).show();
                                    if(hasRetake == true){
                                        intent = new Intent(CaptureCheque.this, ChequeReceived.class);
                                    }
                                    else{
                                        intent = new Intent(CaptureCheque.this, RemarksActivity.class);
                                    }
                                    accountManagement am = new accountManagement(CaptureCheque.this);
                                    accountSession as = new accountSession(chequeNum.getText().toString(), service);
                                    am.saveAccount(as);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(CaptureCheque.this, "Account Number is not valid", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(CaptureCheque.this, "Account Number must be 9 digits", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if(service.equals("Multiple Accounts")){
                            Intent intent;
                            Toast.makeText(CaptureCheque.this, "Account Number is valid", Toast.LENGTH_SHORT).show();
                            if(hasRetake == true){
                                intent = new Intent(CaptureCheque.this, ChequeReceived.class);
                            }
                            else{
                                intent = new Intent(CaptureCheque.this, RemarksActivity.class);
                            }
                            accountManagement am = new accountManagement(CaptureCheque.this);
                            accountSession as = new accountSession(chequeNum.getText().toString(), service);
                            am.saveAccount(as);
                            startActivity(intent);
                            finish();
                        }
                        else if(service.equals("Multiple Entities")){
                            Intent intent;
                            Toast.makeText(CaptureCheque.this, "Account Number is valid", Toast.LENGTH_SHORT).show();
                            if(hasRetake == true){
                                intent = new Intent(CaptureCheque.this, ChequeReceived.class);
                            }
                            else{
                                intent = new Intent(CaptureCheque.this, RemarksActivity.class);
                            }
                            accountManagement am = new accountManagement(CaptureCheque.this);
                            accountSession as = new accountSession(chequeNum.getText().toString(), service);
                            am.saveAccount(as);
                            startActivity(intent);
                            finish();
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
        verify.setOnClickListener(new View.OnClickListener() { // CHECBOX FOR MULTIPLE ENTITIES
            @Override
            public void onClick(View v) {
                String checked_services = String.valueOf(bayan.getText()) + ", " + String.valueOf(innove.getText()) +
                        ", " + String.valueOf(globe.getText());
                Toast.makeText(CaptureCheque.this, "Multiple Entities", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CaptureCheque.this, RemarksActivity.class);
                accountManagement am = new accountManagement(CaptureCheque.this);
                accountSession as = new accountSession(chequeNum.getText().toString(), checked_services);
                am.saveAccount(as);
                startActivity(intent);
                finish();
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
        else{
            //Toast.makeText(CaptureCheque.this,"2,"+cm.getCheck(),Toast.LENGTH_SHORT).show();
            cheques = String.valueOf(imageFile);
        }

        chequeSession cs = new chequeSession(cheques);
        cm.saveCheck(cs);

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
        /*LocationManagement lm = new LocationManagement(CaptureCheque.this);
        String comp = lm.getComp();

        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName = "IMG-Cheque_"+ comp + "_" + time + ".jpg";

        // GET DIR
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, imageName);

        chequeManagement cm = new chequeManagement(CaptureCheque.this);
        chequeSession cs = new chequeSession(String.valueOf(imageFile));
        cm.saveCheck(cs);

        // CREATE DIRECTORY IF NONE
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        // IMAGE SAVE
        // SAVED IMAGED NOTIF
        MediaScannerConnection.scanFile(this, new String[]{imageFile.getAbsolutePath()}, null, null);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        image = Uri.fromFile(imageFile);
        intent.setData(image);
        sendBroadcast(intent);*/
        try{
            FileOutputStream fos = new FileOutputStream(currentPhotoPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();
            Toast.makeText(this, "Saved to gallery", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "ERROR SAVING", Toast.LENGTH_SHORT).show();
            //throw new RuntimeException(e);
        }
        //Toast.makeText(this, "" + imageFile, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStop() {
        super.onStop();
        if(isSubmit == false){
            chequeManagement cm = new chequeManagement(CaptureCheque.this);
            cm.removeCheck();
        }
    }
}