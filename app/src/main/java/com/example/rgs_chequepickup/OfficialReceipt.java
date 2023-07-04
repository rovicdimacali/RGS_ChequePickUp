package com.example.rgs_chequepickup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

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
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import SessionPackage.LocationManagement;
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
    int imageIndex = 0;
    boolean isUpdate = false;
    boolean isEdit = false;
    boolean isSubmit = false;
    boolean isFilled = true;
    boolean isSet1 = false;
    boolean isSet2 = false;
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
    String company, tin, accno, payee, or, amount, img;
    String[] accArr, payArr, ornumArr, amoArr, imgArr;
    CheckBox cbox1, cbox2, cbox3, cbox4, cbox5, cbox6;
    final ArrayList<String> pics = new ArrayList<>();
    final ArrayList<TextView> chktitle = new ArrayList<>();
    //--
    LinearLayout parentL;

    TextView back_button, checkTitle;
    Button addBtn, confirm_btn;
    String remark;
    String[] payeeList;
    String imageArr = "";
    String payeeArr = "", orArr = "", amArr = "", accnoArr = "", images = "";
    int count = 1;
    int delete = 0;
    final ArrayList<LinearLayout> deleted = new ArrayList<>();
    ArrayList<ImageView> iv = new ArrayList<>();
    final ArrayList<EditText> tv = new ArrayList<>();
    final ArrayList<EditText> acc = new ArrayList<>();

    //EDITTEXTS
    final ArrayList<EditText> companyInput = new ArrayList<>();
    final ArrayList<EditText> tinInput = new ArrayList<>();
    final ArrayList<EditText> accInput = new ArrayList<>();
    final ArrayList<Spinner> payeeInput = new ArrayList<>();
    final ArrayList<EditText> orInput = new ArrayList<>();
    final ArrayList<EditText> amInput = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_receipt);

        ReceiptManagement rm = new ReceiptManagement(OfficialReceipt.this);
        LocationManagement lm = new LocationManagement(OfficialReceipt.this);
        scenarioManagement sm = new scenarioManagement(OfficialReceipt.this);
        accountManagement am = new accountManagement(OfficialReceipt.this);
        chequeManagement cm = new chequeManagement(OfficialReceipt.this);

        remark = sm.getScene();

        addBtn = findViewById(R.id.addCheque_button);

        parentL = findViewById(R.id.Llayout);
        checkTitle = findViewById(R.id.checkTitle);

        //INPUT 1
        l1 = findViewById(R.id.form1);
        card1 = findViewById(R.id.cardView_spinner);
        capt1 = findViewById(R.id.capture_button1);
        chk1 = findViewById(R.id.cheqepic1);
        comp1 = findViewById(R.id.inputcompany);
        tin1 = findViewById(R.id.inputtin);
        or1 =  findViewById(R.id.inputOR);
        acc1 = findViewById(R.id.inputacc);
        am1 = findViewById(R.id.inputChequeAmount);
        pay1 = findViewById(R.id.spinner);
        accT1 = findViewById(R.id.accNumber);
        cbox1 = findViewById(R.id.checkboxAcc1);

        //INPUT 2
        chktitle2 = findViewById(R.id.chktitle2);
        l2 = findViewById(R.id.form2);
        card2 = findViewById(R.id.cardView_spinner2);
        capt2 = findViewById(R.id.capture_button2);
        chk2 = findViewById(R.id.cheqepic2);
        comp2 = findViewById(R.id.inputcompany2);
        tin2 = findViewById(R.id.inputtin2);
        or2 = findViewById(R.id.inputOR2);
        or2.setInputType(InputType.TYPE_CLASS_NUMBER);
        acc2 = findViewById(R.id.inputacc2);
        acc2.setInputType(InputType.TYPE_CLASS_NUMBER);
        am2 = findViewById(R.id.inputChequeAmount2);
        am2.setInputType(InputType.TYPE_CLASS_NUMBER);
        pay2 = findViewById(R.id.spinner2);
        accT2 = findViewById(R.id.accNumber2);
        del2 = findViewById(R.id.delete_button2);
        cbox2 = findViewById(R.id.checkboxAcc2);

        //INPUT 3
        chktitle3 = findViewById(R.id.chktitle3);
        l3 = findViewById(R.id.form3);
        card3 = findViewById(R.id.cardView_spinner3);
        capt3 = findViewById(R.id.capture_button3);
        chk3 = findViewById(R.id.cheqepic3);
        comp3 = findViewById(R.id.inputcompany3);
        tin3 = findViewById(R.id.inputtin3);
        or3 = findViewById(R.id.inputOR3);
        or3.setInputType(InputType.TYPE_CLASS_NUMBER);
        acc3 = findViewById(R.id.inputacc3);
        acc3.setInputType(InputType.TYPE_CLASS_NUMBER);
        am3 = findViewById(R.id.inputChequeAmount3);
        am3.setInputType(InputType.TYPE_CLASS_NUMBER);
        pay3 = findViewById(R.id.spinner3);
        accT3 = findViewById(R.id.accNumber3);
        del3 = findViewById(R.id.delete_button3);
        cbox3 = findViewById(R.id.checkboxAcc3);

        //INPUT 4
        chktitle4 = findViewById(R.id.chktitle4);
        l4 = findViewById(R.id.form4);
        card4 = findViewById(R.id.cardView_spinner4);
        capt4= findViewById(R.id.capture_button4);
        chk4 = findViewById(R.id.cheqepic4);
        comp4 = findViewById(R.id.inputcompany4);
        tin4 = findViewById(R.id.inputtin4);
        or4 = findViewById(R.id.inputOR4);
        or4.setInputType(InputType.TYPE_CLASS_NUMBER);
        acc4 = findViewById(R.id.inputacc4);
        acc4.setInputType(InputType.TYPE_CLASS_NUMBER);
        am4 = findViewById(R.id.inputChequeAmount4);
        am4.setInputType(InputType.TYPE_CLASS_NUMBER);
        pay4 = findViewById(R.id.spinner4);
        accT4 = findViewById(R.id.accNumber4);
        del4 = findViewById(R.id.delete_button4);
        cbox4 = findViewById(R.id.checkboxAcc4);

        //INPUT 5
        chktitle5 = findViewById(R.id.chktitle5);
        l5 = findViewById(R.id.form5);
        card5 = findViewById(R.id.cardView_spinner5);
        capt5= findViewById(R.id.capture_button5);
        chk5 = findViewById(R.id.cheqepic5);
        comp5 = findViewById(R.id.inputcompany5);
        tin5 = findViewById(R.id.inputtin5);
        or5 = findViewById(R.id.inputOR5);
        or5.setInputType(InputType.TYPE_CLASS_NUMBER);
        acc5 = findViewById(R.id.inputacc5);
        acc5.setInputType(InputType.TYPE_CLASS_NUMBER);
        am5 = findViewById(R.id.inputChequeAmount5);
        am5.setInputType(InputType.TYPE_CLASS_NUMBER);
        pay5 = findViewById(R.id.spinner5);
        accT5 = findViewById(R.id.accNumber5);
        del5 = findViewById(R.id.delete_button5);
        cbox5 = findViewById(R.id.checkboxAcc5);

        //INPUT 6
        chktitle6 = findViewById(R.id.chktitle6);
        l6 = findViewById(R.id.form6);
        card6 = findViewById(R.id.cardView_spinner6);
        capt6= findViewById(R.id.capture_button6);
        chk6 = findViewById(R.id.cheqepic6);
        comp6 = findViewById(R.id.inputcompany6);
        tin6 = findViewById(R.id.inputtin6);
        or6 = findViewById(R.id.inputOR6);
        or6.setInputType(InputType.TYPE_CLASS_NUMBER);
        acc6 = findViewById(R.id.inputacc6);
        acc6.setInputType(InputType.TYPE_CLASS_NUMBER);
        am6 = findViewById(R.id.inputChequeAmount6);
        am6.setInputType(InputType.TYPE_CLASS_NUMBER);
        pay6 = findViewById(R.id.spinner6);
        accT6 = findViewById(R.id.accNumber6);
        del6 = findViewById(R.id.delete_button6);
        cbox6 = findViewById(R.id.checkboxAcc6);

        comp1.setText(lm.getComp());
        comp2.setText(lm.getComp());
        comp3.setText(lm.getComp());
        comp4.setText(lm.getComp());
        comp5.setText(lm.getComp());
        comp6.setText(lm.getComp());

        payeeList = new String[]{"---PAYEE---","Globe Telecom Inc.", "Innove Communications Inc.", "Bayan Communications Inc."}; //PAYEE LIST
        ArrayAdapter<String> adapter = new ArrayAdapter<>(OfficialReceipt.this, R.layout.simple_spinner_item, payeeList);
        adapter.setDropDownViewResource(R.layout.simple_spinner_item);
        pay1.setAdapter(adapter);
        pay2.setAdapter(adapter);
        pay3.setAdapter(adapter);
        pay4.setAdapter(adapter);
        pay5.setAdapter(adapter);
        pay6.setAdapter(adapter);

        if (remark.equals("One Check, One Account") || remark.equals("One Cheque, Multiple Accounts")) {
            addBtn.setVisibility(View.GONE);
            addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_gray1));
        }

        addLists();

        back_button = findViewById(R.id.back_button);
        confirm_btn = findViewById(R.id.confirm_button);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        back_button.setTypeface(font);
        back_button.setText("\uf060");

        if(!(rm.getOR().isEmpty() || rm.getOR().equals("") || rm.getOR().equals("none"))) {
            isEdit = true;
            isSubmit = false;
            company = lm.getComp();
            tin = rm.getTin();
            accno = am.getAccno();
            payee = rm.getPayee();
            or = rm.getOR();
            amount = rm.getAmount();
            img = cm.getCheck();

            accArr = accno.split(",");
            payArr = payee.split(",");
            ornumArr = or.split(",");
            amoArr = amount.split(",");
            imgArr = img.split(",");

            for (int i = 0; i < payArr.length; i++) {
                try {
                    if (i == 0) {
                        int pos = adapter.getPosition(payArr[i]);
                        comp1.setText(lm.getComp());
                        tin1.setText(rm.getTin());
                        if (accArr[i].equals("Multiple Accounts")){
                            acc1.setText("Multiple Accounts");
                            cbox1.setChecked(true);
                            acc1.setEnabled(false);
                        } else {
                            acc1.setText(accArr[i]);
                            cbox1.setChecked(false);
                            acc1.setEnabled(true);
                        }
                        if (payArr[i].isEmpty()) {
                            pay1.setSelection(0);
                        } else {
                            pay1.setSelection(pos);
                        }
                        or1.setText(ornumArr[i]);
                        am1.setText(amoArr[i]);
                        File img = new File(imgArr[i]);
                        if (img.exists()) {
                            Uri imageUri = Uri.fromFile(img);

                            chk1.setImageURI(imageUri);
                            pics.add(imgArr[i]);
                        }
                        //count = count + 1;
                    } else if (i == 1) {
                        int pos = adapter.getPosition(payArr[i]);
                        l2.setVisibility(View.VISIBLE);
                        comp2.setText(lm.getComp());
                        tin2.setText(rm.getTin());
                        if (accArr[i].equals("Multiple Accounts")){
                            acc2.setText("Multiple Accounts");
                            cbox2.setChecked(true);
                            acc2.setEnabled(false);
                        } else {
                            acc2.setText(accArr[i]);
                            cbox2.setChecked(false);
                            acc2.setEnabled(true);
                        }
                        if (payArr[i].isEmpty()) {
                            pay2.setSelection(0);
                        } else {
                            pay2.setSelection(pos);
                        }
                        or2.setText(ornumArr[i]);
                        am2.setText(amoArr[i]);
                        File img = new File(imgArr[i]);
                        if (img.exists()) {
                            Uri imageUri = Uri.fromFile(img);

                            chk2.setImageURI(imageUri);
                            pics.add(imgArr[i]);
                        }
                        count = count + 1;
                        //chktitle2.setText("Cheque " + count);
                    } else if (i == 2) {
                        int pos = adapter.getPosition(payArr[i]);
                        l3.setVisibility(View.VISIBLE);
                        comp3.setText(lm.getComp());
                        tin3.setText(rm.getTin());
                        if (accArr[i].equals("Multiple Accounts")){
                            acc3.setText("Multiple Accounts");
                            cbox3.setChecked(true);
                            acc3.setEnabled(false);
                        } else {
                            acc3.setText(accArr[i]);
                            cbox3.setChecked(false);
                            acc3.setEnabled(true);
                        }
                        if (payArr[i].isEmpty()) {
                            pay3.setSelection(0);
                        } else {
                            pay3.setSelection(pos);
                        }
                        or3.setText(ornumArr[i]);
                        am3.setText(amoArr[i]);
                        File img = new File(imgArr[i]);
                        if (img.exists()) {
                            Uri imageUri = Uri.fromFile(img);

                            chk3.setImageURI(imageUri);
                            pics.add(imgArr[i]);
                        }
                        count = count + 1;
                        //chktitle3.setText("Cheque " + count);
                    } else if (i == 3) {
                        int pos = adapter.getPosition(payArr[i]);
                        l4.setVisibility(View.VISIBLE);
                        comp4.setText(lm.getComp());
                        tin4.setText(rm.getTin());
                        if (accArr[i].equals("Multiple Accounts")){
                            acc4.setText("Multiple Accounts");
                            cbox4.setChecked(true);
                            acc4.setEnabled(false);
                        } else {
                            acc4.setText(accArr[i]);
                            cbox4.setChecked(false);
                            acc4.setEnabled(true);
                        }
                        if (payArr[i].isEmpty()) {
                            pay4.setSelection(0);
                        } else {
                            pay4.setSelection(pos);
                        }
                        or4.setText(ornumArr[i]);
                        am4.setText(amoArr[i]);
                        File img = new File(imgArr[i]);
                        if (img.exists()) {
                            Uri imageUri = Uri.fromFile(img);

                            chk4.setImageURI(imageUri);
                            pics.add(imgArr[i]);
                        }
                        count = count + 1;
                        //chktitle4.setText("Cheque " + count);
                    } else if (i == 4) {
                        int pos = adapter.getPosition(payArr[i]);
                        l5.setVisibility(View.VISIBLE);
                        comp5.setText(lm.getComp());
                        tin5.setText(rm.getTin());
                        if (accArr[i].equals("Multiple Accounts")){
                            acc5.setText("Multiple Accounts");
                            cbox5.setChecked(true);
                            acc5.setEnabled(false);
                        } else {
                            acc5.setText(accArr[i]);
                            cbox5.setChecked(false);
                            acc5.setEnabled(true);
                        }
                        if (payArr[i].isEmpty()) {
                            pay5.setSelection(0);
                        } else {
                            pay5.setSelection(pos);
                        }
                        or5.setText(ornumArr[i]);
                        am5.setText(amoArr[i]);
                        File img = new File(imgArr[i]);
                        if (img.exists()) {
                            Uri imageUri = Uri.fromFile(img);

                            chk5.setImageURI(imageUri);
                            pics.add(imgArr[i]);
                        }
                        count = count + 1;
                        //chktitle5.setText("Cheque " + count);
                        addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_gray1));
                        addBtn.setActivated(false);
                    } else if (i == 5) {
                        int pos = adapter.getPosition(payArr[i]);
                        l6.setVisibility(View.VISIBLE);
                        comp6.setText(lm.getComp());
                        tin6.setText(rm.getTin());
                        if (accArr[i].equals("Multiple Accounts")){
                            acc6.setText("Multiple Accounts");
                            cbox6.setChecked(true);
                            acc6.setEnabled(false);
                        } else {
                            acc6.setText(accArr[i]);
                            cbox6.setChecked(false);
                            acc6.setEnabled(true);
                        }
                        if (payArr[i].isEmpty()) {
                            pay6.setSelection(0);
                        } else {
                            pay6.setSelection(pos);
                        }
                        or6.setText(ornumArr[i]);
                        am6.setText(amoArr[i]);
                        File img = new File(imgArr[i]);
                        if (img.exists()) {
                            Uri imageUri = Uri.fromFile(img);

                            chk6.setImageURI(imageUri);
                            pics.add(imgArr[i]);
                        }
                        count = count + 1;
                    }
                    //count++;
                }
                catch (IndexOutOfBoundsException e) {
                    //Toast.makeText(OfficialReceipt.this, "i - " + i + " s - " + payArr.length, Toast.LENGTH_SHORT).show();
                    for(int x = 0; i < payArr.length; i++){
                        Log.d("Result", "Result" + x + ": " + payArr[x]);
                    }
                }
            }

        }


        CompoundButton.OnCheckedChangeListener cbl = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        };

        //multAcc.setOnCheckedChangeListener(cbl);
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
                        //del2.setVisibility(View.VISIBLE);
                        count++;
                        //chktitle2.setText("Cheque " + count);
                    }
                    else if(count == 2){
                        l3.setVisibility(View.VISIBLE);
                        tin3.setText(tin1.getText().toString());
                        //del3.setVisibility(View.VISIBLE);
                        count++;
                        //chktitle3.setText("Cheque " + count);
                    }
                    else if(count == 3){
                        l4.setVisibility(View.VISIBLE);
                        tin4.setText(tin1.getText().toString());
                        //del4.setVisibility(View.VISIBLE);
                        count++;
                        //chktitle4.setText("Cheque " + count);
                    }
                    else if(count == 4){
                        l5.setVisibility(View.VISIBLE);
                        tin5.setText(tin1.getText().toString());
                        //del5.setVisibility(View.VISIBLE);
                        count++;
                        //chktitle5.setText("Cheque " + count);
                    }
                    else if(count == 5){
                        l6.setVisibility(View.VISIBLE);
                        tin6.setText(tin1.getText().toString());
                        //del6.setVisibility(View.VISIBLE);
                        addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_gray1));
                        addBtn.setActivated(false);
                        count++;
                        //chktitle6.setText("Cheque " + count);
                    }
                }
                else {
                    deleted.size();
                    if(deleted.size() == 1){
                        deleted.get(0).setVisibility(View.VISIBLE);
                        tv.get(0).setText(tin1.getText().toString());
                        deleted.remove(0);
                        tv.remove(0);
                        acc.remove(0);
                        //chktitle.remove(0);
                    }
                    else{
                        deleted.get(deleted.size() - 1).setVisibility(View.VISIBLE);
                        tv.get(tv.size() - 1).setText(tin1.getText().toString());
                        deleted.remove(deleted.size() - 1);
                        tv.remove(tv.size() - 1);
                        acc.remove(acc.size() - 1);
                        //chktitle.remove(chktitle.size() - 1);

                    }
                    count++;
                    if(count == 6){
                        addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_gray1));
                        addBtn.setEnabled(false);
                    }
                }

            }
        });

        CompoundButton.OnCheckedChangeListener check1 = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //CBOX1
                if(cbox1.isChecked()){
                    acc1.setText("Multiple Accounts");
                    acc1.setEnabled(false);
                }
                else if(!(cbox1.isChecked())){
                    acc1.setText("");
                    acc1.setEnabled(true);
                }
            }
        };
        CompoundButton.OnCheckedChangeListener check2 = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //CBOX2
                if(cbox2.isChecked()){
                    acc2.setText("Multiple Accounts");
                    acc2.setEnabled(false);
                }
                else if(!(cbox2.isChecked())){
                    acc2.setText("");
                    acc2.setEnabled(true);
                }
            }
        };
        CompoundButton.OnCheckedChangeListener check3 = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //CBOX3
                if(cbox3.isChecked()){
                    acc3.setText("Multiple Accounts");
                    acc3.setEnabled(false);
                }
                else if(!(cbox3.isChecked())){
                    acc3.setText("");
                    acc3.setEnabled(true);
                }
            }
        };
        CompoundButton.OnCheckedChangeListener check4 = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //CBOX4
                if(cbox4.isChecked()){
                    acc4.setText("Multiple Accounts");
                    acc4.setEnabled(false);
                }
                else if(!(cbox4.isChecked())){
                    acc4.setText("");
                    acc4.setEnabled(true);
                }
            }
        };
        CompoundButton.OnCheckedChangeListener check5 = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //CBOX5
                if(cbox5.isChecked()){
                    acc5.setText("Multiple Accounts");
                    acc5.setEnabled(false);
                }
                else if(!(cbox5.isChecked())){
                    acc5.setText("");
                    acc5.setEnabled(true);
                }
            }
        };
        CompoundButton.OnCheckedChangeListener check6 = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //CBOX6
                if(cbox6.isChecked()){
                    acc6.setText("Multiple Accounts");
                    acc6.setEnabled(false);
                }
                else if(!(cbox6.isChecked())){
                    acc6.setText("");
                    acc6.setEnabled(true);
                }
            }
        };
        cbox1.setOnCheckedChangeListener(check1);
        cbox2.setOnCheckedChangeListener(check2);
        cbox3.setOnCheckedChangeListener(check3);
        cbox4.setOnCheckedChangeListener(check4);
        cbox5.setOnCheckedChangeListener(check5);
        cbox6.setOnCheckedChangeListener(check6);
        View.OnClickListener bcl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int buttonID = v.getId();

                if(buttonID == R.id.delete_button2){
                    l2.setVisibility(View.GONE);
                    pay2.setSelection(0);
                    tin2.setText("");
                    acc2.setText("");
                    or2.setText("");
                    am2.setText("");
                    deleted.add(l2);
                    tv.add(tin2);
                    acc.add(acc2);
                    //chktitle.add(chktitle2);
                    chk2.setImageDrawable(null);
                    cameraDel = 2;
                    if(count >= 5){
                        addBtn.setBackground(getResources().getDrawable(R.drawable.btn_secondary));
                        addBtn.setEnabled(true);
                    }
                    String name = "IMG-Cheque"+cameraDel;

                    Iterator<String> iterator = pics.iterator();
                    while(iterator.hasNext()){
                        String elem = iterator.next();
                        if(elem.contains(name)){
                            iterator.remove();
                        }
                    }
                    count--;

                }
                else if(buttonID == R.id.delete_button3){
                    l3.setVisibility(View.GONE);
                    pay3.setSelection(0);
                    tin3.setText("");
                    acc3.setText("");
                    or3.setText("");
                    am3.setText("");
                    deleted.add(l3);
                    tv.add(tin3);
                    acc.add(acc3);
                    //chktitle.add(chktitle3);
                    chk3.setImageDrawable(null);
                    cameraDel = 3;
                    if(count >= 5){
                        addBtn.setBackground(getResources().getDrawable(R.drawable.btn_secondary));
                        addBtn.setEnabled(true);
                    }

                    String name = "IMG-Cheque"+cameraDel;

                    Iterator<String> iterator = pics.iterator();
                    while(iterator.hasNext()){
                        String elem = iterator.next();
                        if(elem.contains(name)){
                            iterator.remove();
                        }
                    }
                    count--;

                }
                else if(buttonID == R.id.delete_button4){
                    l4.setVisibility(View.GONE);
                    pay4.setSelection(0);
                    tin4.setText("");
                    acc4.setText("");
                    or4.setText("");
                    am4.setText("");
                    deleted.add(l4);
                    tv.add(tin4);
                    acc.add(acc4);
                    //chktitle.add(chktitle4);
                    chk4.setImageDrawable(null);
                    cameraDel = 4;
                    if(count >= 5){
                        addBtn.setBackground(getResources().getDrawable(R.drawable.btn_secondary));
                        addBtn.setEnabled(true);
                    }

                    String name = "IMG-Cheque"+cameraDel;

                    Iterator<String> iterator = pics.iterator();
                    while(iterator.hasNext()){
                        String elem = iterator.next();
                        if(elem.contains(name)){
                            iterator.remove();
                        }
                    }
                    count--;

                }
                else if(buttonID == R.id.delete_button5){
                    l5.setVisibility(View.GONE);
                    pay5.setSelection(0);
                    tin5.setText("");
                    or5.setText("");
                    acc5.setText("");
                    am5.setText("");
                    deleted.add(l5);
                    tv.add(tin5);
                    acc.add(acc5);
                    //chktitle.add(chktitle5);
                    chk5.setImageDrawable(null);
                    cameraDel = 5;
                    if(count >= 5){
                        addBtn.setBackground(getResources().getDrawable(R.drawable.btn_secondary));
                        addBtn.setEnabled(true);
                    }

                    String name = "IMG-Cheque"+cameraDel;

                    Iterator<String> iterator = pics.iterator();
                    while(iterator.hasNext()){
                        String elem = iterator.next();
                        if(elem.contains(name)){
                            iterator.remove();
                        }
                    }
                    count--;
                }
                else if(buttonID == R.id.delete_button6){
                    l6.setVisibility(View.GONE);
                    pay6.setSelection(0);
                    tin6.setText("");
                    acc6.setText("");
                    or6.setText("");
                    am6.setText("");
                    deleted.add(l6);
                    tv.add(tin6);
                    acc.add(acc6);
                    chktitle.add(chktitle6);
                    //chk6.setImageDrawable(null);
                    cameraDel = 6;
                    if(count >= 5){
                        addBtn.setBackground(getResources().getDrawable(R.drawable.btn_secondary));
                        addBtn.setEnabled(true);
                    }

                    String name = "IMG-Cheque"+cameraDel;

                    Iterator<String> iterator = pics.iterator();
                    while(iterator.hasNext()){
                        String elem = iterator.next();
                        if(elem.contains(name)){
                            iterator.remove();
                        }
                    }
                    count--;
                }
            }
        };

        del2.setOnClickListener(bcl);
        del3.setOnClickListener(bcl);
        del4.setOnClickListener(bcl);
        del5.setOnClickListener(bcl);
        del6.setOnClickListener(bcl);

        capt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(OfficialReceipt.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    cameraTurn = 1;
                    openCamera();
                } else {
                    // Request CAMERA permission
                    cameraTurn = 1;
                    ActivityCompat.requestPermissions(OfficialReceipt.this, new String[]{Manifest.permission.CAMERA}, 1);
                }

            }
        });

        capt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(OfficialReceipt.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    cameraTurn = 2;
                    openCamera();
                } else {
                    // Request CAMERA permission
                    cameraTurn = 2;
                    ActivityCompat.requestPermissions(OfficialReceipt.this, new String[]{Manifest.permission.CAMERA}, 1);
                }
            }
        });
        capt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(OfficialReceipt.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    cameraTurn = 3;
                    openCamera();
                } else {
                    // Request CAMERA permission
                    cameraTurn = 3;
                    ActivityCompat.requestPermissions(OfficialReceipt.this, new String[]{Manifest.permission.CAMERA}, 1);
                }
            }
        });

        capt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(OfficialReceipt.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    cameraTurn = 4;
                    openCamera();
                } else {
                    // Request CAMERA permission
                    cameraTurn = 4;
                    ActivityCompat.requestPermissions(OfficialReceipt.this, new String[]{Manifest.permission.CAMERA}, 1);
                }
            }
        });
        capt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(OfficialReceipt.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    cameraTurn = 5;
                    openCamera();
                } else {
                    // Request CAMERA permission
                    cameraTurn = 5;
                    ActivityCompat.requestPermissions(OfficialReceipt.this, new String[]{Manifest.permission.CAMERA}, 1);
                }
            }
        });

        capt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(OfficialReceipt.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    cameraTurn = 6;
                    openCamera();
                } else {
                    // Request CAMERA permission
                    ActivityCompat.requestPermissions(OfficialReceipt.this, new String[]{Manifest.permission.CAMERA}, 1);
                }
            }
        });
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pics.size() == 0 || pics.size() != count) {
                    Toast.makeText(OfficialReceipt.this, "Cheque Pictures Missing", Toast.LENGTH_SHORT).show();
                }
                else {
                    pics.size();
                    try {
                        for (int i = 0; i < count; i++) {
                            if(companyInput.get(i).getText().toString().isEmpty() || tinInput.get(i).getText().toString().isEmpty()
                                    || accInput.get(i).getText().toString().isEmpty() || payeeInput.get(i).getSelectedItem().toString().equals("---PAYEE---")
                                    || orInput.get(i).getText().toString().isEmpty() || amInput.get(i).getText().toString().isEmpty()) {

                                Toast.makeText(OfficialReceipt.this, "Please fill up all the fields", Toast.LENGTH_SHORT).show();
                                isFilled = false;
                                break;
                            }
                        }
                        if (isFilled == false) {
                            isFilled = true;
                            //Toast.makeText(OfficialReceipt.this, "Please fill up all the fields", Toast.LENGTH_SHORT).show();
                        } else if (isFilled == true) {
                            String[] accArr = {acc1.getText().toString(), acc2.getText().toString(), acc3.getText().toString(),
                                    acc4.getText().toString(), acc5.getText().toString(), acc6.getText().toString()};
                            for (int i = 0; i < 6; i++) {
                                if (!(accArr[i].equals("") || accArr[i].equals(" ") || accArr[i].equals("none") || accArr[i].isEmpty())) {
                                    accnoArr += accArr[i] + ",";
                                }
                            }
                            accnoArr = accnoArr.substring(0, accnoArr.length() - 1);

                            String[] pay = {pay1.getSelectedItem().toString(), pay2.getSelectedItem().toString(),
                                    pay3.getSelectedItem().toString(), pay4.getSelectedItem().toString(),
                                    pay5.getSelectedItem().toString(), pay6.getSelectedItem().toString()};

                            for (String s : pay) {
                                if (!(s.equals("") || s.equals(" ") || s.equals("---PAYEE---") || s.equals("none") || s.isEmpty())) {
                                    payeeArr += s + ",";
                                }
                            }

                            String[] ornum = {or1.getText().toString(), or2.getText().toString(), or3.getText().toString(),
                                    or4.getText().toString(), or5.getText().toString(), or6.getText().toString()};
                            for (int i = 0; i < 6; i++) {
                                if (!(ornum[i].equals("") || ornum[i].equals(" ") || ornum[i].equals("none") || ornum[i].isEmpty())) {
                                    orArr += ornum[i] + ",";
                                }
                            }

                            String[] amount = {am1.getText().toString(), am2.getText().toString(), am3.getText().toString(),
                                    am4.getText().toString(), am5.getText().toString(), am6.getText().toString()};
                            for (int i = 0; i < 6; i++) {
                                if (!(amount[i].equals("") || amount[i].equals(" ") || amount[i].equals("none") || amount[i].isEmpty())) {
                                    amArr += amount[i] + ",";
                                }
                            }

                            //Toast.makeText(OfficialReceipt.this, "Size " + pics.size(), Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < pics.size(); i++) {
                                images += pics.get(i) + ",";
                                //pics.remove(0);
                            }

                            payeeArr = payeeArr.substring(0, payeeArr.length() - 1);
                            orArr = orArr.substring(0, orArr.length() - 1);
                            amArr = amArr.substring(0, amArr.length() - 1);
                            images = images.substring(0, images.length() - 1);

                            ReceiptSession rs = new ReceiptSession(String.valueOf(tin1.getText()), amArr, "", payeeArr, "",
                                    orArr, "");
                            rm.saveReceipt(rs);
                            accountSession as = new accountSession(accnoArr, "");
                            am.saveAccount(as);

                            chequeManagement cm = new chequeManagement(OfficialReceipt.this);
                            chequeSession cs = new chequeSession(images);
                            cm.saveCheck(cs);


                            isEdit = false;
                            isSubmit = true;
                            isUpdate = false;
                            //Toast.makeText(OfficialReceipt.this, "Payee " + pay.length, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(OfficialReceipt.this, VerifyOfficialReceipt.class);
                            startActivity(intent);
                        }
                    }catch(NullPointerException e){
                        Log.d("Result", "Error: " + e.getMessage());
                    }
                }
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
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
        if(intent.resolveActivity(getPackageManager()) != null){
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
    }
    private File createImageFile() throws IOException{
        LocationManagement lm = new LocationManagement(OfficialReceipt.this);
        String comp = lm.getComp();

        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageCheck = "IMG-Cheque" + cameraTurn;
        String imageName = "IMG-Cheque" + cameraTurn + "_"+ comp + "_" + time;

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //File imageFile = new File(storageDir, imageName);
        File imageFile = File.createTempFile(imageName,".jpg",storageDir);

        imageArr = String.valueOf(imageFile);
        // && isEdit == true
        if(pics.size() > 0){
            for (String elem : pics) {
                if (elem.contains(imageCheck)) {
                    imageIndex = pics.indexOf(elem);
                    if (imageIndex != -1) {
                        pics.set(imageIndex, imageArr);
                        isUpdate = true;
                        break;
                    } else {
                        Toast.makeText(OfficialReceipt.this, "Error Updating Image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            if(isUpdate == true){
                isUpdate = false;
            }
            else{
                for(int i = 0; i < pics.size(); i++){
                    String item = pics.get(i);
                    if(item.equals(imageArr)){
                        int indexUpdate = pics.indexOf(item);
                        pics.set(indexUpdate, imageArr);
                        isSet1 = true;
                        break;
                    }
                }

                if(isSet1 == false){
                    pics.add(imageArr);
                }
                else{
                    isSet1 = false;
                }
            }
        }
        else{
            for(int i = 0; i < pics.size(); i++){
                String item = pics.get(i);
                if(item.equals(imageArr)){
                    int indexUpdate = pics.indexOf(item);
                    pics.set(indexUpdate, imageArr);
                    isSet2 = true;
                    break;
                }
            }

            if(isSet2 == false){
                pics.add(imageArr);
            }
            else{
                isSet2 = false;
            }
        }

        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101 && resultCode == RESULT_OK) {
            if(cameraTurn == 1){
                //Toast.makeText(this, "" + currentPhotoPath, Toast.LENGTH_SHORT).show();
                Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(currentPhotoPath), 700, 900, false);

                chk1.setImageBitmap(bitmap);

                saveImageToGallery(bitmap);
            }

            else if(cameraTurn == 2){
                //Toast.makeText(this, "" + currentPhotoPath, Toast.LENGTH_SHORT).show();
                Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(currentPhotoPath), 700, 900, false);
                chk2.setImageBitmap(bitmap);

                saveImageToGallery(bitmap);
            }
            else if(cameraTurn == 3){
                //Toast.makeText(this, "" + currentPhotoPath, Toast.LENGTH_SHORT).show();
                Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(currentPhotoPath), 700, 900, false);
                chk3.setImageBitmap(bitmap);

                saveImageToGallery(bitmap);
            }

            else if(cameraTurn == 4){
                //Toast.makeText(this, "" + currentPhotoPath, Toast.LENGTH_SHORT).show();
                Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(currentPhotoPath), 700, 900, false);
                chk4.setImageBitmap(bitmap);

                saveImageToGallery(bitmap);
            }
            else if(cameraTurn == 5){
                //Toast.makeText(this, "" + currentPhotoPath, Toast.LENGTH_SHORT).show();
                Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(currentPhotoPath), 700, 900, false);
                chk5.setImageBitmap(bitmap);

                saveImageToGallery(bitmap);
            }

            else if(cameraTurn == 6){
                //Toast.makeText(this, "" + currentPhotoPath, Toast.LENGTH_SHORT).show();
                Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(currentPhotoPath), 700, 900, false);
                chk6.setImageBitmap(bitmap);

                saveImageToGallery(bitmap);
            }
        }
        else{
            Toast.makeText(this, "Image capture cancelled", Toast.LENGTH_SHORT).show();
        }
    }
    private void saveImageToGallery(Bitmap bitmap) {
        try{
            FileOutputStream fos = new FileOutputStream(currentPhotoPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
            fos.flush();
            fos.close();
            //Toast.makeText(this, "Saved to gallery" , Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error Saving", Toast.LENGTH_SHORT).show();
            //throw new RuntimeException(e);
        }
        //Toast.makeText(this, "" + imageFile, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onStop() {
        super.onStop();
        if(isSubmit == false){
            isEdit = false;
            isUpdate = false;
            ReceiptManagement rm = new ReceiptManagement(OfficialReceipt.this);
            rm.removeReceipt();
        }
    }

    public void addLists(){
        //Company
        companyInput.add(comp1);
        companyInput.add(comp2);
        companyInput.add(comp3);
        companyInput.add(comp4);
        companyInput.add(comp5);
        companyInput.add(comp6);
        //TIN
        tinInput.add(tin1);
        tinInput.add(tin2);
        tinInput.add(tin3);
        tinInput.add(tin4);
        tinInput.add(tin5);
        tinInput.add(tin6);
        //Account Number
        accInput.add(acc1);
        accInput.add(acc2);
        accInput.add(acc3);
        accInput.add(acc4);
        accInput.add(acc5);
        accInput.add(acc6);
        //Payee
        payeeInput.add(pay1);
        payeeInput.add(pay2);
        payeeInput.add(pay3);
        payeeInput.add(pay4);
        payeeInput.add(pay5);
        payeeInput.add(pay6);
        //OR Number
        orInput.add(or1);
        orInput.add(or2);
        orInput.add(or3);
        orInput.add(or4);
        orInput.add(or5);
        orInput.add(or6);
        //Amount
        amInput.add(am1);
        amInput.add(am2);
        amInput.add(am3);
        amInput.add(am4);
        amInput.add(am5);
        amInput.add(am6);
    }
}