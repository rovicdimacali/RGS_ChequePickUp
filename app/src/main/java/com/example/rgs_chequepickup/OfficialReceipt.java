package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import SessionPackage.LocationManagement;
import SessionPackage.LocationSession;
import SessionPackage.ReceiptManagement;
import SessionPackage.ReceiptSession;
import SessionPackage.scenarioManagement;

public class OfficialReceipt extends AppCompatActivity {

    TextView back_button;
    EditText cheq_num, cheq_amount, compname, compadd, tin;
    Button submit_btn;
    String remark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_receipt);

        ReceiptManagement rm = new ReceiptManagement(OfficialReceipt.this);
        LocationManagement lm = new LocationManagement(OfficialReceipt.this);
        scenarioManagement sm = new scenarioManagement(OfficialReceipt.this);
        remark = sm.getScene();


        compname = (EditText) findViewById(R.id.inputcompany);
        compadd = (EditText) findViewById(R.id.inputaddress);
        tin = (EditText) findViewById(R.id.inputtin);
        cheq_amount = (EditText) findViewById(R.id.inputchequeamount);
        cheq_num = (EditText) findViewById(R.id.inputchequenumber);

        /*if(!(rm.getTin().isEmpty())){
            tin.setText(rm.getTin());
        }*/

        compname.setText(lm.getComp());
        compadd.setText(lm.getAdd());

        if(remark.equals("One Account, Multiple Cheques") || remark.equals("Multiple Accounts, Multiple Cheques"))
        {
            cheq_num.setActivated(false);
            cheq_amount.setActivated(false);

            cheq_num.setText("Multiple Cheques");
            cheq_amount.setText("Multiple Cheques");
        }
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
                ReceiptSession rs = new ReceiptSession(String.valueOf(tin.getText()), String.valueOf(cheq_amount.getText()), String.valueOf(cheq_num.getText()));
                rm.saveReceipt(rs);
                //Toast.makeText(OfficialReceipt.this, "Transaction Completed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OfficialReceipt.this, ChequeReceived.class);
                startActivity(intent);
            }
        });
    }
    public void openEsignature() {
        Intent intent = new Intent(this, ESignature.class);
        startActivity(intent);
    }
}