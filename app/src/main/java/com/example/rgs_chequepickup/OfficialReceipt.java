package com.example.rgs_chequepickup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import SessionPackage.LocationManagement;
import SessionPackage.LocationSession;
import SessionPackage.ReceiptManagement;
import SessionPackage.ReceiptSession;
import SessionPackage.accountManagement;
import SessionPackage.accountSession;
import SessionPackage.chequeManagement;
import SessionPackage.chequeSession;
import SessionPackage.scenarioManagement;

public class OfficialReceipt extends AppCompatActivity {
    private String currentPhotoPath;
    int cameraTurn;
    int cameraDel;
    //INPUTS
    EditText comp1, comp2, comp3, comp4, comp5, comp6;
    EditText tin1, tin2, tin3, tin4, tin5, tin6;
    EditText acc1, acc2, acc3, acc4, acc5, acc6;
    Spinner pay1, pay2, pay3, pay4, pay5, pay6;
    EditText or1, or2, or3, or4, or5, or6;
    EditText am1, am2, am3, am4, am5, am6;
    Button capt1, capt2, capt3, capt4, capt5, capt6;
    Button del2, del3, del4, del5, del6;
    LinearLayout l1, l2, l3, l4, l5, l6;
    CardView card1, card2, card3, card4, card5, card6;
    ImageView chk1, chk2, chk3, chk4, chk5, chk6;
    TextView accT1, accT2, accT3, accT4, accT5, accT6;
    TextView chktitle2, chktitle3, chktitle4, chktitle5, chktitle6;
    CheckBox multAcc;
    //--
    DatePickerDialog datePickerDialog;

    TextView back_button, checkTitle;
    Button addBtn, datepicker, date, confirm_btn;
    String remark;
    String payeeList[];
    String imageArr;
    String payeeArr = "", orArr = "", amArr = "", accnoArr = "";
    int count = 1;
    int delete = 0;
    ArrayList<LinearLayout> deleted = new ArrayList<>();
    ArrayList<ImageView> iv = new ArrayList<>();
    ArrayList<TextView> tv = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_receipt);

        ReceiptManagement rm = new ReceiptManagement(OfficialReceipt.this);
        LocationManagement lm = new LocationManagement(OfficialReceipt.this);
        scenarioManagement sm = new scenarioManagement(OfficialReceipt.this);
        accountManagement am = new accountManagement(OfficialReceipt.this);

        remark = sm.getScene();

        addBtn = (Button) findViewById(R.id.addCheque_button);
        multAcc = (CheckBox) findViewById(R.id.checkbox_accnum);

        //INPUT 1
        l1 = (LinearLayout) findViewById(R.id.form1);
        card1 = (CardView) findViewById(R.id.cardView_spinner);
        capt1 = (Button) findViewById(R.id.capture_button1);
        chk1 = (ImageView) findViewById(R.id.cheqepic1);
        comp1 = (EditText) findViewById(R.id.inputcompany);
        tin1 = (EditText) findViewById(R.id.inputtin);
        or1 = (EditText) findViewById(R.id.inputOR);
        acc1 = (EditText) findViewById(R.id.inputacc);
        am1 = (EditText) findViewById(R.id.inputChequeAmount);
        pay1 = (Spinner) findViewById(R.id.spinner);
        accT1 = (TextView) findViewById(R.id.accNumber);

        //INPUT 2
        l2 = (LinearLayout) findViewById(R.id.form2);
        card2 = (CardView) findViewById(R.id.cardView_spinner2);
        capt2 = (Button) findViewById(R.id.capture_button2);
        chk2 = (ImageView) findViewById(R.id.cheqepic2);
        comp2 = (EditText) findViewById(R.id.inputcompany2);
        tin2 = (EditText) findViewById(R.id.inputtin2);
        or2 = (EditText) findViewById(R.id.inputOR2);
        acc2 = (EditText) findViewById(R.id.inputacc2);
        am2 = (EditText) findViewById(R.id.inputChequeAmount2);
        pay2 = (Spinner) findViewById(R.id.spinner2);
        accT2 = (TextView) findViewById(R.id.accNumber2);
        del2 = (Button) findViewById(R.id.delete_button2);

        //INPUT 3
        l3 = (LinearLayout) findViewById(R.id.form3);
        card3 = (CardView) findViewById(R.id.cardView_spinner3);
        capt3 = (Button) findViewById(R.id.capture_button3);
        chk3 = (ImageView) findViewById(R.id.cheqepic3);
        comp3 = (EditText) findViewById(R.id.inputcompany3);
        tin3 = (EditText) findViewById(R.id.inputtin3);
        or3 = (EditText) findViewById(R.id.inputOR3);
        acc3 = (EditText) findViewById(R.id.inputacc3);
        am3 = (EditText) findViewById(R.id.inputChequeAmount3);
        pay3 = (Spinner) findViewById(R.id.spinner3);
        accT3 = (TextView) findViewById(R.id.accNumber3);
        del3 = (Button) findViewById(R.id.delete_button3) ;

        //INPUT 4
        l4 = (LinearLayout) findViewById(R.id.form4);
        card4 = (CardView) findViewById(R.id.cardView_spinner4);
        capt4= (Button) findViewById(R.id.capture_button4);
        chk4 = (ImageView) findViewById(R.id.cheqepic4);
        comp4 = (EditText) findViewById(R.id.inputcompany4);
        tin4 = (EditText) findViewById(R.id.inputtin4);
        or4 = (EditText) findViewById(R.id.inputOR4);
        acc4 = (EditText) findViewById(R.id.inputacc4);
        am4 = (EditText) findViewById(R.id.inputChequeAmount4);
        pay4 = (Spinner) findViewById(R.id.spinner4);
        accT4 = (TextView) findViewById(R.id.accNumber4);
        del4 = (Button) findViewById(R.id.delete_button4) ;

        //INPUT 5
        l5 = (LinearLayout) findViewById(R.id.form5);
        card5 = (CardView) findViewById(R.id.cardView_spinner5);
        capt5= (Button) findViewById(R.id.capture_button5);
        chk5 = (ImageView) findViewById(R.id.cheqepic5);
        comp5 = (EditText) findViewById(R.id.inputcompany5);
        tin5 = (EditText) findViewById(R.id.inputtin5);
        or5 = (EditText) findViewById(R.id.inputOR5);
        acc5 = (EditText) findViewById(R.id.inputacc5);
        am5 = (EditText) findViewById(R.id.inputChequeAmount5);
        pay5 = (Spinner) findViewById(R.id.spinner5);
        accT5 = (TextView) findViewById(R.id.accNumber5);
        del5 = (Button) findViewById(R.id.delete_button5) ;

        //INPUT 6
        l6 = (LinearLayout) findViewById(R.id.form6);
        card6 = (CardView) findViewById(R.id.cardView_spinner6);
        capt6= (Button) findViewById(R.id.capture_button6);
        chk6 = (ImageView) findViewById(R.id.cheqepic6);
        comp6 = (EditText) findViewById(R.id.inputcompany6);
        tin6 = (EditText) findViewById(R.id.inputtin6);
        or6 = (EditText) findViewById(R.id.inputOR6);
        acc6 = (EditText) findViewById(R.id.inputacc6);
        am6 = (EditText) findViewById(R.id.inputChequeAmount6);
        pay6 = (Spinner) findViewById(R.id.spinner6);
        accT6 = (TextView) findViewById(R.id.accNumber6);
        del6 = (Button) findViewById(R.id.delete_button6) ;

        comp1.setText(lm.getComp());
        comp2.setText(lm.getComp());
        comp3.setText(lm.getComp());
        comp4.setText(lm.getComp());
        comp5.setText(lm.getComp());
        comp6.setText(lm.getComp());

        payeeList = new String[]{"---PAYEE---","Globe Telecom Inc.", "Innove Communications Inc.", "Bayan Communications Inc."}; //PAYEE LIST
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(OfficialReceipt.this, R.layout.simple_spinner_item, payeeList);
        adapter.setDropDownViewResource(R.layout.simple_spinner_item);
        pay1.setAdapter(adapter);
        pay2.setAdapter(adapter);
        pay3.setAdapter(adapter);
        pay4.setAdapter(adapter);
        pay5.setAdapter(adapter);
        pay6.setAdapter(adapter);

        back_button = (TextView) findViewById(R.id.back_button);
        confirm_btn = (Button) findViewById(R.id.confirm_button);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        back_button.setTypeface(font);
        back_button.setText("\uf060");

        if (remark.equals("One Check, One Account") || remark.equals("One Cheque, Multiple Accounts")) {
            addBtn.setVisibility(View.GONE);
            addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_gray1));
        }

        CompoundButton.OnCheckedChangeListener cbl = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(multAcc.isChecked()){
                    acc1.setVisibility(View.GONE);
                    acc2.setVisibility(View.GONE);
                    acc3.setVisibility(View.GONE);
                    acc4.setVisibility(View.GONE);
                    acc5.setVisibility(View.GONE);
                    acc6.setVisibility(View.GONE);

                    acc1.setText("");
                    acc2.setText("");
                    acc3.setText("");
                    acc4.setText("");
                    acc5.setText("");;
                    acc6.setText("");

                    accT1.setVisibility(View.GONE);
                    accT2.setVisibility(View.GONE);
                    accT3.setVisibility(View.GONE);
                    accT4.setVisibility(View.GONE);
                    accT5.setVisibility(View.GONE);
                    accT6.setVisibility(View.GONE);
                }
                else if(!(multAcc.isChecked())){
                    acc1.setVisibility(View.VISIBLE);
                    acc2.setVisibility(View.VISIBLE);
                    acc3.setVisibility(View.VISIBLE);
                    acc4.setVisibility(View.VISIBLE);
                    acc5.setVisibility(View.VISIBLE);
                    acc6.setVisibility(View.VISIBLE);

                    accT1.setVisibility(View.VISIBLE);
                    accT2.setVisibility(View.VISIBLE);
                    accT3.setVisibility(View.VISIBLE);
                    accT4.setVisibility(View.VISIBLE);
                    accT5.setVisibility(View.VISIBLE);
                    accT6.setVisibility(View.VISIBLE);
                }
            }
        };

        multAcc.setOnCheckedChangeListener(cbl);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rm.removeReceipt();
                openChecklist();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deleted.size() == 0){
                    if(count == 1){
                        l2.setVisibility(View.VISIBLE);
                        tin2.setText(tin1.getText().toString());
                        if(!(multAcc.isChecked())){
                            acc2.setText(acc1.getText().toString());
                        }
                        //del2.setVisibility(View.VISIBLE);
                        count++;
                    }
                    else if(count == 2){
                        l3.setVisibility(View.VISIBLE);
                        tin3.setText(tin1.getText().toString());
                        if(!(multAcc.isChecked())){
                            acc3.setText(acc1.getText().toString());
                        }
                        //del3.setVisibility(View.VISIBLE);
                        count++;
                    }
                    else if(count == 3){
                        l4.setVisibility(View.VISIBLE);
                        tin4.setText(tin1.getText().toString());
                        if(!(multAcc.isChecked())){
                            acc4.setText(acc1.getText().toString());
                        }
                        //del4.setVisibility(View.VISIBLE);
                        count++;
                    }
                    else if(count == 4){
                        l5.setVisibility(View.VISIBLE);
                        tin5.setText(tin1.getText().toString());
                        if(!(multAcc.isChecked())){
                            acc5.setText(acc1.getText().toString());
                        }
                        //del5.setVisibility(View.VISIBLE);
                        count++;
                    }
                    else if(count == 5){
                        l6.setVisibility(View.VISIBLE);
                        tin6.setText(tin1.getText().toString());
                        if(!(multAcc.isChecked())){
                            acc6.setText(acc1.getText().toString());
                        }
                        //del6.setVisibility(View.VISIBLE);
                        addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_gray1));
                        addBtn.setActivated(false);
                        //count++;
                    }
                }
                else if(deleted.size() > 0){
                    deleted.get(0).setVisibility(View.VISIBLE);
                    tv.get(0).setText(tin1.getText().toString());
                    deleted.remove(0);
                    tv.remove(0);
                    count++;
                    if(count == 5){
                        addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_gray1));
                        addBtn.setEnabled(false);
                    }
                }
            }
        });

        del2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l2.setVisibility(View.GONE);
                pay2.setSelection(0);
                tin2.setText("");
                acc2.setText("");
                or2.setText("");
                am2.setText("");
                deleted.add(l2);
                tv.add(tin2);
                chk2.setImageDrawable(null);
                cameraDel = 2;
                if(count == 5){
                    addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_green));
                    addBtn.setEnabled(true);
                }

                String name = "IMG-Cheque"+cameraDel;
                String[] explode = imageArr.split(",");
                Arrays.sort(explode);
                for(int i = 0; i < explode.length; i++){
                    if(!(explode[i].contains(name))){
                        imageArr += explode[i] + ",";
                    }
                }
                imageArr = imageArr.substring(0, imageArr.length() - 1);
                chequeManagement cm = new chequeManagement(OfficialReceipt.this);
                chequeSession cs = new chequeSession(imageArr);
                cm.saveCheck(cs);
                count--;
            }
        });
        del3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l3.setVisibility(View.GONE);
                pay3.setSelection(0);
                tin3.setText("");
                acc3.setText("");
                or3.setText("");
                am3.setText("");
                deleted.add(l3);
                tv.add(tin3);
                chk3.setImageDrawable(null);
                cameraDel = 3;
                if(count == 5){
                    addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_green));
                    addBtn.setEnabled(true);
                }

                String name = "IMG-Cheque"+cameraDel;
                String[] explode = imageArr.split(",");
                Arrays.sort(explode);
                for(int i = 0; i < explode.length; i++){
                    if(!(explode[i].contains(name))){
                        imageArr += explode[i] + ",";
                    }
                }
                imageArr = imageArr.substring(0, imageArr.length() - 1);
                chequeManagement cm = new chequeManagement(OfficialReceipt.this);
                chequeSession cs = new chequeSession(imageArr);
                cm.saveCheck(cs);
                count--;
            }
        });
        del4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l4.setVisibility(View.GONE);
                pay4.setSelection(0);
                tin4.setText("");
                acc4.setText("");
                or4.setText("");
                am4.setText("");
                deleted.add(l4);
                tv.add(tin4);
                chk4.setImageDrawable(null);
                cameraDel = 4;
                if(count == 5){
                    addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_green));
                    addBtn.setEnabled(true);
                }

                String name = "IMG-Cheque"+cameraDel;
                String[] explode = imageArr.split(",");
                Arrays.sort(explode);
                for(int i = 0; i < explode.length; i++){
                    if(!(explode[i].contains(name))){
                        imageArr += explode[i] + ",";
                    }
                }
                imageArr = imageArr.substring(0, imageArr.length() - 1);
                chequeManagement cm = new chequeManagement(OfficialReceipt.this);
                chequeSession cs = new chequeSession(imageArr);
                cm.saveCheck(cs);
                count--;
            }
        });
        del5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l5.setVisibility(View.GONE);
                pay5.setSelection(0);
                tin5.setText("");
                or5.setText("");
                acc5.setText("");
                am5.setText("");
                deleted.add(l5);
                tv.add(tin5);
                chk5.setImageDrawable(null);
                cameraDel = 5;
                if(count == 5){
                    addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_green));
                    addBtn.setEnabled(true);
                }

                String name = "IMG-Cheque"+cameraDel;
                String[] explode = imageArr.split(",");
                Arrays.sort(explode);
                for(int i = 0; i < explode.length; i++){
                    if(!(explode[i].contains(name))){
                        imageArr += explode[i] + ",";
                    }
                }
                imageArr = imageArr.substring(0, imageArr.length() - 1);
                chequeManagement cm = new chequeManagement(OfficialReceipt.this);
                chequeSession cs = new chequeSession(imageArr);
                cm.saveCheck(cs);
                count--;
            }
        });
        del6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l6.setVisibility(View.GONE);
                pay6.setSelection(0);
                tin6.setText("");
                acc6.setText("");
                or6.setText("");
                am6.setText("");
                deleted.add(l6);
                tv.add(tin6);
                chk6.setImageDrawable(null);
                cameraDel = 6;
                if(count == 5){
                    addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_green));
                    addBtn.setEnabled(true);
                }

                String name = "IMG-Cheque"+cameraDel;
                String[] explode = imageArr.split(",");
                Arrays.sort(explode);
                for(int i = 0; i < explode.length; i++){
                    if(!(explode[i].contains(name))){
                        imageArr += explode[i] + ",";
                    }
                }
                imageArr = imageArr.substring(0, imageArr.length() - 1);
                chequeManagement cm = new chequeManagement(OfficialReceipt.this);
                chequeSession cs = new chequeSession(imageArr);
                cm.saveCheck(cs);
                count--;
            }
        });

        capt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                cameraTurn = 1;
            }
        });

        capt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                cameraTurn = 2;
            }
        });
        capt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                cameraTurn = 3;
            }
        });

        capt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                cameraTurn = 4;
            }
        });
        capt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                cameraTurn = 5;
            }
        });

        capt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                cameraTurn = 6;
            }
        });
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(multAcc.isChecked()){
                    accnoArr = "Multiple Accounts";
                }
                else{
                    String[] accArr = {acc1.getText().toString(), acc2.getText().toString(),acc3.getText().toString(),
                            acc4.getText().toString(),acc5.getText().toString(),acc6.getText().toString()};
                    for(int i = 0; i < 6; i++){
                        if(!(accArr[i].equals("") || accArr[i].equals(" ") || accArr[i].isEmpty())){
                            accnoArr += accArr[i] + ",";
                        }
                    }
                    accnoArr = accnoArr.substring(0, accnoArr.length() - 1);
                }

                String[] pay = {pay1.getSelectedItem().toString(), pay2.getSelectedItem().toString(),
                        pay3.getSelectedItem().toString(), pay4.getSelectedItem().toString(),
                        pay5.getSelectedItem().toString(),pay6.getSelectedItem().toString()};
                for(int i = 0; i < 6; i++){
                    if(!(pay[i].equals("") || pay[i].equals(" ") || pay[i].equals("---PAYEE---")|| pay[i].isEmpty())){
                        payeeArr += pay[i] + ",";
                    }
                }

                String[] ornum = {or1.getText().toString(), or2.getText().toString(), or3.getText().toString(),
                        or4.getText().toString(), or5.getText().toString(), or6.getText().toString()};
                for(int i = 0; i < 6; i++){
                    if(!(ornum[i].equals("") || ornum[i].equals(" ") || ornum[i].isEmpty())){
                        orArr += ornum[i] + ",";
                    }
                }

                String[] amount = {am1.getText().toString(), am2.getText().toString(), am3.getText().toString(),
                        am4.getText().toString(), am5.getText().toString(), am6.getText().toString()};
                for(int i = 0; i < 6; i++){
                    if(!(amount[i].equals("") || amount[i].equals(" ") || amount[i].isEmpty())){
                        amArr += amount[i] + ",";
                    }
                }

                payeeArr = payeeArr.substring(0, payeeArr.length() - 1);
                orArr = orArr.substring(0, orArr.length() - 1);
                amArr = amArr.substring(0, amArr.length() - 1);

                ReceiptSession rs = new ReceiptSession(String.valueOf(tin1.getText()).toString(), amArr, "", payeeArr, "",
                        orArr, "");
                rm.saveReceipt(rs);
                accountSession as = new accountSession(accnoArr, "");
                am.saveAccount(as);

                Intent intent = new Intent(OfficialReceipt.this, ChequeReceived.class);
                startActivity(intent);
            }
        });
    }

    public void openChecklist() {
        Intent intent = new Intent(this, CheckList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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
            Uri photoURI = FileProvider.getUriForFile(OfficialReceipt.this,
                    "com.example.rgs_chequepickup.fileprovider",
                    photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, 101);
        }
    }
    private File createImageFile() throws IOException{
        LocationManagement lm = new LocationManagement(OfficialReceipt.this);
        String comp = lm.getComp();

        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //String imageName = "IMG-Cheque_"+ comp + "_" + time + ".jpg";
        String imageName = "IMG-Cheque" + cameraTurn + "_"+ comp + "_" + time;

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //File imageFile = new File(storageDir, imageName);
        File imageFile = File.createTempFile(imageName,".jpg",storageDir);

        imageArr += String.valueOf(imageFile) + ",";
        String[] explode = imageArr.split(",");
        Arrays.sort(explode);
        for(int i = 0; i<explode.length; i++){
            imageArr += explode[i] + ",";
        }
        imageArr = imageArr.substring(0, imageArr.length() - 1);
        chequeManagement cm = new chequeManagement(OfficialReceipt.this);
        chequeSession cs = new chequeSession(imageArr);
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
            if(cameraTurn == 1){
                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                chk1.setImageBitmap(bitmap);

                saveImageToGallery(bitmap);
            }

            else if(cameraTurn == 2){
                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                chk2.setImageBitmap(bitmap);

                saveImageToGallery(bitmap);
            }
            else if(cameraTurn == 3){
                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                chk3.setImageBitmap(bitmap);

                saveImageToGallery(bitmap);
            }

            else if(cameraTurn == 4){
                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                chk4.setImageBitmap(bitmap);

                saveImageToGallery(bitmap);
            }
            else if(cameraTurn == 5){
                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                chk5.setImageBitmap(bitmap);

                saveImageToGallery(bitmap);
            }

            else if(cameraTurn == 6){
                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                chk6.setImageBitmap(bitmap);

                saveImageToGallery(bitmap);
            }
        }
        else{
            Toast.makeText(this, "Image capture cancelled", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Error Saving", Toast.LENGTH_SHORT).show();
            //throw new RuntimeException(e);
        }
        //Toast.makeText(this, "" + imageFile, Toast.LENGTH_SHORT).show();
    }
}