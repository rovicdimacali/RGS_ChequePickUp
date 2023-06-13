package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import SessionPackage.accountManagement;
import SessionPackage.remarkManagement;
import SessionPackage.remarkSession;
import SessionPackage.scenarioManagement;

public class RemarksActivity extends AppCompatActivity {

    Button submit_btn;
    TextView back_btn;
    LinearLayout numField, otherField;
    RadioButton collectRB, otherRB;
    EditText notes, numCheq, other_rem;
    String remark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remarks);

        remarkManagement rm = new remarkManagement(RemarksActivity.this);
        scenarioManagement sm = new scenarioManagement(RemarksActivity.this);

        remark = sm.getScene();

        back_btn = (TextView) findViewById(R.id.back_button);
        submit_btn = (Button) findViewById(R.id.submit_btn);

        //CHEQUE FIELD
        collectRB = (RadioButton) findViewById(R.id.collected);
        numField = (LinearLayout) findViewById(R.id.collected_field);
        numCheq = (EditText) findViewById(R.id.numCheq);

        //OTHERS FIELD
        otherRB = (RadioButton) findViewById(R.id.other);
        otherField = (LinearLayout) findViewById(R.id.other_field);
        other_rem = (EditText) findViewById(R.id.other_remark);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        //notes = (EditText) findViewById(R.id.remarks_text);

        back_btn.setTypeface(font);
        back_btn.setText("\uf060");

        CompoundButton.OnCheckedChangeListener cbl = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(collectRB.isChecked() && !(otherRB.isChecked())){
                    if (remark.equals("One Check, One Account") || remark.equals("One Cheque, Multiple Accounts")) {
                        numCheq.setText("1");
                        numCheq.setEnabled(false);
                    }
                    else if (remark.equals("One Check, One Account") || remark.equals("One Cheque, Multiple Accounts")) {
                        numCheq.setText("");
                        numCheq.setEnabled(true);
                    }
                    numField.setVisibility(View.VISIBLE);
                    otherField.setVisibility(View.GONE);
                    other_rem.setText("");
                    submit_btn.setEnabled(true);
                    submit_btn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_green));
                }
                else if(!(collectRB.isChecked()) && otherRB.isChecked()){
                    numField.setVisibility(View.GONE);
                    otherField.setVisibility(View.VISIBLE);
                    numCheq.setText("");
                    submit_btn.setEnabled(true);
                    submit_btn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_green));
                }
            }
        };

        collectRB.setOnCheckedChangeListener(cbl);
        otherRB.setOnCheckedChangeListener(cbl);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountManagement am = new accountManagement(RemarksActivity.this);
                am.removeAcc();
                Intent intent = new Intent(RemarksActivity.this, CaptureCheque.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((collectRB.isChecked() && !(numCheq.getText().toString().isEmpty())) && !(otherRB.isChecked())){
                    //Toast.makeText(RemarksActivity.this, "op1", Toast.LENGTH_SHORT).show();
                    int num = Integer.valueOf(numCheq.getText().toString());

                    if(num > 5){
                        Toast.makeText(RemarksActivity.this, "Maximum of 5 Cheques Only", Toast.LENGTH_SHORT).show();
                    }
                    else if(num < 0){
                        Toast.makeText(RemarksActivity.this, "Invalid number of cheques", Toast.LENGTH_SHORT).show();
                    }
                    else if (num < 5 && num > 0){
                        remarkSession rs = new remarkSession("Collected " + numCheq.getText().toString() + " check/s");
                        rm.saveRemark(rs);
                        Intent intent = new Intent(RemarksActivity.this, ESignature.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else if(!(collectRB.isChecked()) && (otherRB.isChecked() && !(other_rem.getText().toString().isEmpty()))){
                    //Toast.makeText(RemarksActivity.this, "op2", Toast.LENGTH_SHORT).show();
                    remarkSession rs = new remarkSession(other_rem.getText().toString());
                    rm.saveRemark(rs);
                    Intent intent = new Intent(RemarksActivity.this, ESignature.class);
                    startActivity(intent);
                    finish();
                }
                else if((collectRB.isChecked() && numCheq.getText().toString().isEmpty()) && !(otherRB.isChecked())){
                    Toast.makeText(RemarksActivity.this, "Please enter number of check/s collected", Toast.LENGTH_SHORT).show();
                }
                else if(!(collectRB.isChecked()) && (otherRB.isChecked() && other_rem.getText().toString().isEmpty())){
                    Toast.makeText(RemarksActivity.this, "Please fill up the field", Toast.LENGTH_SHORT).show();
                }
                else if(!(collectRB.isChecked() && otherRB.isChecked())){
                    Toast.makeText(RemarksActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}