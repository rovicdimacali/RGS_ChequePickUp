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

import SessionPackage.scenarioManagement;

public class OfficialReceipt extends AppCompatActivity {

    TextView back_button;
    EditText cheq_num, cheq_amount;
    Button submit_btn;
    String remark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_receipt);

        scenarioManagement sm = new scenarioManagement(OfficialReceipt.this);
        remark = sm.getScene();

        cheq_amount = (EditText) findViewById(R.id.inputchequeamount);
        cheq_num = (EditText)  findViewById(R.id.inputchequenumber);

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
                openEsignature();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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