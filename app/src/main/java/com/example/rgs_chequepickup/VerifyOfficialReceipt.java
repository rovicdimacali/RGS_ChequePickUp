package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import SessionPackage.LocationManagement;
import SessionPackage.ReceiptManagement;
import SessionPackage.accountManagement;
import SessionPackage.chequeManagement;
import SessionPackage.scenarioManagement;

public class VerifyOfficialReceipt extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 150;
    LinearLayout parentLayout;
    TextView compname, tinname, accname, payeename, orname, amname;
    TextView compin, tinin, accin, payeein, orin, amin;
    ImageView chequeImg;
    LinearLayout layout;
    String company, tin, accno, payee, or, amount, img;
    String[] accArr, payArr, orArr, amArr, imgArr;
    TextView back_button;
    int size;
    Button submit, editBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_official_receipt);


        ReceiptManagement rm = new ReceiptManagement(VerifyOfficialReceipt.this);
        LocationManagement lm = new LocationManagement(VerifyOfficialReceipt.this);
        scenarioManagement sm = new scenarioManagement(VerifyOfficialReceipt.this);
        accountManagement am = new accountManagement(VerifyOfficialReceipt.this);
        chequeManagement cm = new chequeManagement(VerifyOfficialReceipt.this);

        company = lm.getComp();
        tin = rm.getTin();
        accno = am.getAccno();
        payee = rm.getPayee();
        or = rm.getOR();
        amount = rm.getAmount();
        img = cm.getCheck();

        accArr = accno.split(",");
        payArr = payee.split(",");
        orArr = or.split(",");
        amArr = amount.split(",");
        imgArr = img.split(",");

        ArrayList<String> list = new ArrayList<>(Arrays.asList(payArr));
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String element = iterator.next();
            if (element.isEmpty()) {
                iterator.remove();
            }
        }
        payArr = list.toArray(new String[0]);
        size = payArr.length;

        parentLayout = (LinearLayout) findViewById(R.id.parentLayout);
        editBtn = (Button) findViewById(R.id.edit_button);
        chequeImg = (ImageView) findViewById(R.id.cheque_img);
        submit = (Button) findViewById(R.id.submit_button);
        back_button = (TextView) findViewById(R.id.back_button);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        back_button.setTypeface(font);
        back_button.setText("\uf060");

        compname = (TextView) findViewById(R.id.label_companyName);
        tinname = (TextView) findViewById(R.id.label_tinNumber);
        accname = (TextView) findViewById(R.id.label_accountNumber);
        payeename = (TextView) findViewById(R.id.label_payee);
        orname = (TextView) findViewById(R.id.label_orNumber);
        amname = (TextView) findViewById(R.id.label_chequeAmount);

        compin = (TextView) findViewById(R.id.companyname);
        tinin = (TextView) findViewById(R.id.tinNumber);
        accin = (TextView) findViewById(R.id.accountNumber);
        payeein = (TextView) findViewById(R.id.payee);
        orin = (TextView) findViewById(R.id.orNumber);
        amin = (TextView) findViewById(R.id.chequeAmount);

        compin.setText(company);
        tinin.setText(tin);
        accin.setText(accArr[0]);
        payeein.setText(payArr[0]);
        orin.setText(orArr[0]);
        amin.setText(amArr[0]);
        File img = new File(imgArr[0]);
        //Toast.makeText(VerifyOfficialReceipt.this, "Size" + size, Toast.LENGTH_LONG).show();
        if(img.exists()){
            Uri imageUri = Uri.fromFile(img);

            chequeImg.setImageURI(imageUri);
        }
        else{
            Toast.makeText(this, "Image file not found", Toast.LENGTH_SHORT).show();
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InvalidChequePopupWindow();
            }
        });

        for(int i = 1; i < size; i++){
            //LINE
            try{
            View line = new View(VerifyOfficialReceipt.this);
            line.setBackgroundColor(Color.BLACK);

            RelativeLayout.LayoutParams line_params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    6
            );

            line_params.bottomMargin = 80;
            line.setLayoutParams(line_params);
            //IMAGE VIEW
            File img1 = new File(imgArr[i]);

            ImageView imgView = new ImageView(VerifyOfficialReceipt.this);

            LinearLayout.LayoutParams img_params = new LinearLayout.LayoutParams(
                    600,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            //img_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            img_params.gravity = Gravity.CENTER_HORIZONTAL;
            img_params.bottomMargin = 90;
            imgView.setLayoutParams(img_params);

            if(img1.exists()){
                Uri imageUri = Uri.fromFile(img1);

                imgView.setImageURI(imageUri);
            }
            else{
                Toast.makeText(this, "Image file not found", Toast.LENGTH_SHORT).show();
            }

            //AMOUNT
            TextView tv_am = new TextView(VerifyOfficialReceipt.this);
            tv_am.setText("Cheque Amount");
            tv_am.setTypeface(null, Typeface.BOLD);
            tv_am.setTextSize(15);
            tv_am.setTextColor(Color.BLACK);
            RelativeLayout.LayoutParams am_params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            tv_am.setLayoutParams(am_params);

            TextView amIn = new TextView(VerifyOfficialReceipt.this);
            amIn.setText(amArr[i]);
            amIn.setTextSize(15);
            amIn.setInputType(InputType.TYPE_CLASS_NUMBER);
            amIn.setTextColor(Color.BLACK);
            RelativeLayout.LayoutParams amIn_params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            amIn_params.setMargins(15, 0,0,0);
            amIn.setLayoutParams(amIn_params);

            //ORNUM
            TextView tv_or = new TextView(VerifyOfficialReceipt.this);
            tv_or.setText("OR Number");
            tv_or.setTypeface(null, Typeface.BOLD);
            tv_or.setTextSize(15);
            tv_or.setTextColor(Color.BLACK);
            RelativeLayout.LayoutParams or_params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            tv_or.setLayoutParams(or_params);

            TextView orIn = new TextView(VerifyOfficialReceipt.this);
            orIn.setText(orArr[i]);
            orIn.setTextSize(15);
            orIn.setInputType(InputType.TYPE_CLASS_NUMBER);
            orIn.setTextColor(Color.BLACK);
            RelativeLayout.LayoutParams orIn_params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            orIn_params.setMargins(15, 0,0,0);
            orIn.setLayoutParams(orIn_params);

            //PAYEE
            TextView tv_payee = new TextView(VerifyOfficialReceipt.this);
            tv_payee.setText("Payee");
            tv_payee.setTypeface(null, Typeface.BOLD);
            tv_payee.setTextSize(15);
            tv_payee.setTextColor(Color.BLACK);
            RelativeLayout.LayoutParams pay_params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            tv_payee.setLayoutParams(pay_params);

            TextView payIn = new TextView(VerifyOfficialReceipt.this);
            payIn.setText(payArr[i]);
            payIn.setTextSize(15);
            payIn.setTextColor(Color.BLACK);
            RelativeLayout.LayoutParams payIn_params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            payIn_params.setMargins(15, 0,0,0);
            payIn.setLayoutParams(payIn_params);

            //ACCNO
            TextView tv_acc = new TextView(VerifyOfficialReceipt.this);
            tv_acc.setText("Account Number");
            tv_acc.setTypeface(null, Typeface.BOLD);
            tv_acc.setTextSize(15);
            tv_acc.setTextColor(Color.BLACK);
            RelativeLayout.LayoutParams acc_params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            tv_acc.setLayoutParams(acc_params);

            TextView accIn = new TextView(VerifyOfficialReceipt.this);
            if(accArr.length == 1){
                accIn.setText("Multiple Accounts");
            }
            else if(accArr.length > 1){
                accIn.setText(accArr[i]);
            }
            accIn.setTextSize(15);
            accIn.setInputType(InputType.TYPE_CLASS_NUMBER);
            accIn.setTextColor(Color.BLACK);
            RelativeLayout.LayoutParams accIn_params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            accIn_params.setMargins(15, 0,0,0);
            accIn.setLayoutParams(accIn_params);

            //TIN
            TextView tv_tin = new TextView(VerifyOfficialReceipt.this);
            tv_tin.setText("TIN Number");
            tv_tin.setTypeface(null, Typeface.BOLD);
            tv_tin.setTextSize(15);
            tv_tin.setTextColor(Color.BLACK);
            RelativeLayout.LayoutParams tin_params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            tv_tin.setLayoutParams(tin_params);

            TextView tinIn = new TextView(VerifyOfficialReceipt.this);
            tinIn.setText(tin);
            tinIn.setTextSize(15);
            tinIn.setTextColor(Color.BLACK);
            RelativeLayout.LayoutParams tinIn_params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            tinIn_params.setMargins(15, 0,0,0);
            tinIn.setLayoutParams(tinIn_params);

            //COMPANY
            TextView tv_comp = new TextView(VerifyOfficialReceipt.this);
            tv_comp.setText("Company Name");
            tv_comp.setTypeface(null, Typeface.BOLD);
            tv_comp.setTextSize(15);
            tv_comp.setTextColor(Color.BLACK);
            RelativeLayout.LayoutParams comp_params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            tv_comp.setLayoutParams(comp_params);

            TextView compIn = new TextView(VerifyOfficialReceipt.this);
            compIn.setText(company);
            compIn.setTextSize(15);
            compIn.setTextColor(Color.BLACK);
            RelativeLayout.LayoutParams compIn_params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            compIn_params.setMargins(15, 0,0,0);
            compIn.setLayoutParams(compIn_params);

            //TITLE
            TextView title = new TextView(VerifyOfficialReceipt.this);
            title.setText("Cheque " + (i + 1));
            title.setId(View.generateViewId());
            title.setTypeface(null, Typeface.BOLD);
            title.setTextSize(15);
            title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            title.setTextColor(Color.BLACK);
            RelativeLayout.LayoutParams title_params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            title.setLayoutParams(title_params);

            //EDIT
            /*ImageView editBtn = new ImageView(VerifyOfficialReceipt.this);
            editBtn.setImageDrawable(getResources().getDrawable(R.drawable.edit));

            RelativeLayout.LayoutParams edit_params = new RelativeLayout.LayoutParams(
                    28,
                    44
            );
            edit_params.addRule(RelativeLayout.END_OF, title.getId());
            edit_params.addRule(RelativeLayout.ALIGN_TOP, title.getId());
            edit_params.addRule(RelativeLayout.ALIGN_BOTTOM, title.getId());
            edit_params.setMargins(dpToPx(8),0,0,0);
            /*edit_params.topMargin = -60;
            edit_params.rightMargin = -120;

            editBtn.setLayoutParams(edit_params);*/

            parentLayout.addView(title);
            //parentLayout.addView(editBtn);
            parentLayout.addView(tv_comp);
            parentLayout.addView(compIn);
            parentLayout.addView(tv_tin);
            parentLayout.addView(tinIn);
            parentLayout.addView(tv_acc);
            parentLayout.addView(accIn);
            parentLayout.addView(tv_payee);
            parentLayout.addView(payIn);
            parentLayout.addView(tv_or);
            parentLayout.addView(orIn);
            parentLayout.addView(tv_am);
            parentLayout.addView(amIn);
            parentLayout.addView(imgView);
            parentLayout.addView(line);
            }
            catch (IndexOutOfBoundsException e){
                //Toast.makeText(VerifyOfficialReceipt.this, "i - " + i + " s - " + size, Toast.LENGTH_SHORT).show();
                for(int x = 0; i < size; i++){
                    Log.d("Result", "Verify" + x + ": " + payArr[x]);
                }
            }
        }

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerifyOfficialReceipt.this, OfficialReceipt.class);
                startActivity(intent);
                finish();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerifyOfficialReceipt.this, OfficialReceipt.class);
                startActivity(intent);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InvalidChequePopupWindow();
            }
        });
    }
    private int dpToPx(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private void InvalidChequePopupWindow() {
        layout = (LinearLayout) findViewById(R.id.layout);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.popup_invalid_cheque, null);

        Button yesbtn = (Button) popUpView.findViewById(R.id.yes_button);
        Button nobtn = (Button) popUpView.findViewById(R.id.no_button);

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

        yesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VerifyOfficialReceipt.this, CaptureCheque.class);
                startActivity(i);
            }
        });

        nobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VerifyOfficialReceipt.this, ChequeReceived.class);
                startActivity(i);
            }
        });
    }
}