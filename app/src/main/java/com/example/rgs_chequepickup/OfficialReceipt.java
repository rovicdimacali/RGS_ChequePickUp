package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import SessionPackage.LocationManagement;
import SessionPackage.LocationSession;
import SessionPackage.ReceiptManagement;
import SessionPackage.ReceiptSession;
import SessionPackage.scenarioManagement;

public class OfficialReceipt extends AppCompatActivity {

    TextView back_button;
    EditText cheq_num, cheq_amount, compname, compadd, tin, payee;
    Button submit_btn, addBtn;
    String remark;
    LinearLayout Llayout_num, Llayout_am;
    int count = 0;
    ArrayList<EditText> chkNums = new ArrayList<>();
    ArrayList<EditText> chkAmounts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_receipt);

        ReceiptManagement rm = new ReceiptManagement(OfficialReceipt.this);
        LocationManagement lm = new LocationManagement(OfficialReceipt.this);
        scenarioManagement sm = new scenarioManagement(OfficialReceipt.this);
        remark = sm.getScene();

        addBtn = (Button) findViewById(R.id.addCheque_button);
        Llayout_num = (LinearLayout) findViewById(R.id.chknumText);
        Llayout_am = (LinearLayout) findViewById(R.id.chkamountText);

        compname = (EditText) findViewById(R.id.inputcompany);
        compadd = (EditText) findViewById(R.id.inputaddress);
        tin = (EditText) findViewById(R.id.inputtin);
        cheq_amount = (EditText) findViewById(R.id.inputchequeamount);
        cheq_num = (EditText) findViewById(R.id.inputchequenumber);
        payee = (EditText) findViewById(R.id.inputpayee);

        /*if(!(rm.getTin().isEmpty())){
            tin.setText(rm.getTin());
        }*/

        compname.setText(lm.getComp());
        compadd.setText(lm.getAdd());

        /*if(remark.equals("One Account, Multiple Cheques") || remark.equals("Multiple Accounts, Multiple Cheques"))
        {
            cheq_num.setActivated(false);
            cheq_amount.setActivated(false);

            cheq_num.setText("Multiple Cheques");
            cheq_amount.setText("Multiple Cheques");
        }*/
        back_button = (TextView) findViewById(R.id.back_button);
        submit_btn = (Button) findViewById(R.id.submit_button);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        back_button.setTypeface(font);
        back_button.setText("\uf060");

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rm.removeReceipt();
                openEsignature();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReceiptSession rs = new ReceiptSession(String.valueOf(tin.getText()), String.valueOf(cheq_amount.getText()), String.valueOf(cheq_num.getText()), String.valueOf(payee.getText().toString()));
                rm.saveReceipt(rs);
                //Toast.makeText(OfficialReceipt.this, "Transaction Completed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OfficialReceipt.this, ChequeReceived.class);
                startActivity(intent);
            }
        });

        /*addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count < 4){ // IF COUNT IS LESS THAN 5, ADD CHECK
                    EditText etNum = new EditText(OfficialReceipt.this);

                    LinearLayout.LayoutParams ln = new LinearLayout.LayoutParams(1050,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

                    ln.setMargins(80, 30,0,0);
                    etNum.setBackgroundColor(Integer.parseInt(String.valueOf(Color.parseColor("#F2F2F2"))));
                    etNum.setLayoutParams(ln);
                    etNum.setHint("Cheque Number");
                    etNum.setTextSize(15);
                    etNum.setInputType(InputType.TYPE_CLASS_NUMBER);
                    etNum.setPadding(54,60,20,60);

                    Llayout_num.addView(etNum);

                    EditText etAmount = new EditText(OfficialReceipt.this);

                    LinearLayout.LayoutParams la = new LinearLayout.LayoutParams(1050,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

                    la.setMargins(80, 30,0,0);
                    etAmount.setBackgroundColor(Integer.parseInt(String.valueOf(Color.parseColor("#F2F2F2"))));
                    etAmount.setLayoutParams(la);
                    etAmount.setHint("Cheque Amount");
                    etAmount.setTextSize(15);
                    etAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
                    etAmount.setPadding(54,60,20,60);

                    Llayout_am.addView(etAmount);

                    chkNums.add(etNum);
                    chkAmounts.add(etNum);

                    count++;
                    if(count >= 4)
                    {
                        addBtn.setText("Reset");
                        addBtn.setActivated(false);
                        addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_gray1));
                    }
                }
                else if(count >= 3){ // ELSE, DISABLE BUTTON
                    //Toast.makeText(OfficialReceipt.this, "Maximum of 5 Cheques Only", Toast.LENGTH_SHORT).show();
                    recreate();
               }
            }
        });*/
    }
    public void openEsignature() {
        Intent intent = new Intent(this, ESignature.class);
        startActivity(intent);
    }
}