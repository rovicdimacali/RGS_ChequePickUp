package com.example.rgs_chequepickup;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import SessionPackage.ReceiptManagement;
import SessionPackage.ReceiptSession;
import SessionPackage.chequeManagement;
import SessionPackage.remarkManagement;
import SessionPackage.remarkSession;

public class ChecklistInvalidCheque extends AppCompatActivity {
    TextView back_button;
    Button submit;
    CheckBox wrongSpellBox, erasuresBox, noSignBox, dateDisBox, othersBox;
    EditText othersText, totalAmount;
    final ArrayList<CheckBox> checkboxes = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_invalid_cheque);

        submit = findViewById(R.id.submit_button);
        submit.setEnabled(false);
        submit.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_gray1));

        othersText = findViewById(R.id.others_content);
        back_button = findViewById(R.id.back_button);
        totalAmount = findViewById(R.id.total_amount);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        back_button.setTypeface(font);
        back_button.setText("\uf060");

        //CHECKBOXES
        wrongSpellBox = findViewById(R.id.checkbox_wrongSpelling);
        erasuresBox = findViewById(R.id.checkbox_erasures);
        noSignBox = findViewById(R.id.checkbox_noSignature);
        dateDisBox = findViewById(R.id.checkbox_discrepancy);
        othersBox = findViewById(R.id.checkbox_others);

        checkboxes.add(wrongSpellBox);
        checkboxes.add(erasuresBox);
        checkboxes.add(noSignBox );
        checkboxes.add(dateDisBox);
        checkboxes.add(othersBox);

        othersText.setEnabled(false);
        CompoundButton.OnCheckedChangeListener cbl = (buttonView, isChecked) -> {
            if(wrongSpellBox.isChecked() || erasuresBox.isChecked() || noSignBox.isChecked() || dateDisBox.isChecked()
            || othersBox.isChecked()){
                if(othersBox.isChecked()){
                    othersText.setEnabled(true);
                }
                else if(!(othersBox.isChecked())){
                    othersText.setEnabled(false);
                }
                //WRONG SPELLING INSERT
                submit.setEnabled(true);
                submit.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_green));
            }
            else if(!(wrongSpellBox.isChecked() && erasuresBox.isChecked() && noSignBox.isChecked() && dateDisBox.isChecked()
                    && othersBox.isChecked())){
                submit.setEnabled(false);
                submit.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_gray));
            }
        };

        wrongSpellBox.setOnCheckedChangeListener(cbl);
        erasuresBox.setOnCheckedChangeListener(cbl);
        noSignBox.setOnCheckedChangeListener(cbl);
        dateDisBox.setOnCheckedChangeListener(cbl);
        othersBox.setOnCheckedChangeListener(cbl);

        back_button.setOnClickListener(v -> {
            chequeManagement cm = new chequeManagement(ChecklistInvalidCheque.this);
            cm.removeCheck();
            Intent i = new Intent(ChecklistInvalidCheque.this, CaptureCheque.class);
            startActivity(i);
        });
        submit.setOnClickListener(v -> {
            if((wrongSpellBox.isChecked() || erasuresBox.isChecked() || noSignBox.isChecked() || dateDisBox.isChecked())
                    && (othersBox.isChecked() && (othersText.getText().toString().isEmpty() || othersText.getText().toString().equals("")))){
                Toast.makeText(ChecklistInvalidCheque.this, "Please enter a reason", Toast.LENGTH_SHORT).show();
            }
            else if(!(wrongSpellBox.isChecked() || erasuresBox.isChecked() || noSignBox.isChecked() || dateDisBox.isChecked()
                    || othersBox.isChecked())){
                Toast.makeText(ChecklistInvalidCheque.this, "Please select an option", Toast.LENGTH_SHORT).show();
            }
            else if(!(wrongSpellBox.isChecked() || erasuresBox.isChecked() || noSignBox.isChecked() || dateDisBox.isChecked())
                    && (othersBox.isChecked() && (othersText.getText().toString().isEmpty() || othersText.getText().toString().equals("")))){
                Toast.makeText(ChecklistInvalidCheque.this, "Please enter a reason", Toast.LENGTH_SHORT).show();
            }
            else if(wrongSpellBox.isChecked() || erasuresBox.isChecked() || noSignBox.isChecked() || dateDisBox.isChecked()
                    || (othersBox.isChecked() && !(othersText.getText().toString().isEmpty() || othersText.getText().toString().equals("")))){

                String remarks = "";

                ArrayList<String> checked = new ArrayList<>();
                for(CheckBox ck: checkboxes){
                    if(ck.isChecked()){
                        if(ck.getText().toString().equals("Others:")){
                            checked.add(othersText.getText().toString());
                        }
                        else{
                            checked.add(ck.getText().toString());
                        }
                    }
                }

                for(String word: checked){
                    remarks += word + ",";
                }
                //Toast.makeText(ChecklistInvalidCheque.this, "Size: " + checked.size(), Toast.LENGTH_SHORT).show();
                remarks = remarks.substring(0, remarks.length() - 1);

                remarkManagement rm = new remarkManagement(ChecklistInvalidCheque.this);
                remarkSession rs = new remarkSession(remarks);
                rm.saveRemark(rs);

                chequeManagement cm = new chequeManagement(ChecklistInvalidCheque.this);
                ReceiptManagement rec_m = new ReceiptManagement(ChecklistInvalidCheque.this);

                String tin = "", am = "", or = "", pay = "";
                String[] check = cm.getCheck().split(",");
                if(check[0].contains("INVALID-Cheque")){
                    am = totalAmount.getText().toString();
                    if(am.isEmpty()){
                        am = "0";
                    }
                    ReceiptSession rec_s = new ReceiptSession("", am, "", "", "",
                            "", "");
                    rec_m.saveReceipt(rec_s);
                }
                else{
                    am = rec_m.getAmount() + "," + totalAmount.getText().toString();
                    if(totalAmount.getText().toString().isEmpty()){
                        am = rec_m.getAmount() + ",0";
                    }
                    tin = rec_m.getTin();
                    or = rec_m.getOR();
                    pay = rec_m.getPayee();

                    ReceiptSession rec_s = new ReceiptSession(tin, am, "", pay, "",
                            or, "");
                    rec_m.saveReceipt(rec_s);
                }

                Intent i = new Intent(ChecklistInvalidCheque.this, ChequeReceived.class);
                startActivity(i);
            }
        });
    }
}
