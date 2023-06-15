package com.example.rgs_chequepickup;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Iterator;

import SessionPackage.remarkManagement;
import SessionPackage.remarkSession;

public class ChecklistInvalidCheque extends AppCompatActivity {
    TextView back_button;
    Button submit;
    CheckBox wrongSpellBox, erasuresBox, noSignBox, dateDisBox, othersBox;
    EditText othersText;
    ArrayList<CheckBox> checkboxes = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_invalid_cheque);

        submit = (Button) findViewById(R.id.submit_button);
        submit.setEnabled(false);
        submit.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_gray1));

        othersText = (EditText)findViewById(R.id.others_content);
        back_button = (TextView) findViewById(R.id.back_button);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        back_button.setTypeface(font);
        back_button.setText("\uf060");

        //CHECKBOXES
        wrongSpellBox = (CheckBox) findViewById(R.id.checkbox_wrongSpelling);
        erasuresBox = (CheckBox) findViewById(R.id.checkbox_erasures);
        noSignBox = (CheckBox) findViewById(R.id.checkbox_noSignature);
        dateDisBox = (CheckBox) findViewById(R.id.checkbox_discrepancy);
        othersBox = (CheckBox) findViewById(R.id.checkbox_others);

        checkboxes.add(wrongSpellBox);
        checkboxes.add(erasuresBox);
        checkboxes.add(noSignBox );
        checkboxes.add(dateDisBox);
        checkboxes.add(othersBox);

        CompoundButton.OnCheckedChangeListener cbl = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(wrongSpellBox.isChecked() || erasuresBox.isChecked() || noSignBox.isChecked() || dateDisBox.isChecked()
                || othersBox.isChecked()){
                    //WRONG SPELLING INSERT
                    submit.setEnabled(true);
                    submit.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_green));
                }
                else if(!(wrongSpellBox.isChecked() && erasuresBox.isChecked() && noSignBox.isChecked() && dateDisBox.isChecked()
                        && othersBox.isChecked())){
                    submit.setEnabled(false);
                    submit.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_gray));
                }
            }
        };

        wrongSpellBox.setOnCheckedChangeListener(cbl);
        erasuresBox.setOnCheckedChangeListener(cbl);
        noSignBox.setOnCheckedChangeListener(cbl);
        dateDisBox.setOnCheckedChangeListener(cbl);
        othersBox.setOnCheckedChangeListener(cbl);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((wrongSpellBox.isChecked() || erasuresBox.isChecked() || noSignBox.isChecked() || dateDisBox.isChecked())
                        && (othersBox.isChecked() && (othersText.getText().toString().isEmpty() || othersText.getText().toString().equals(" ")))){
                    Toast.makeText(ChecklistInvalidCheque.this, "Please enter a reason", Toast.LENGTH_SHORT).show();
                }
                else if(!(wrongSpellBox.isChecked() || erasuresBox.isChecked() || noSignBox.isChecked() || dateDisBox.isChecked()
                        || othersBox.isChecked())){
                    Toast.makeText(ChecklistInvalidCheque.this, "Please select an option", Toast.LENGTH_SHORT).show();
                }
                else if(!(wrongSpellBox.isChecked() || erasuresBox.isChecked() || noSignBox.isChecked() || dateDisBox.isChecked())
                        && (othersBox.isChecked() && (othersText.getText().toString().isEmpty() || othersText.getText().toString().equals(" ")))){
                    Toast.makeText(ChecklistInvalidCheque.this, "Please enter a reason", Toast.LENGTH_SHORT).show();
                }
                else if(wrongSpellBox.isChecked() || erasuresBox.isChecked() || noSignBox.isChecked() || dateDisBox.isChecked()
                        || (othersBox.isChecked() && !(othersText.getText().toString().isEmpty() || othersText.getText().toString().equals(" ")))){

                    /*String[] arrayRem = {wrongSpellBox.getText().toString(), erasuresBox.getText().toString(), noSignBox.getText().toString()
                    , dateDisBox.getText().toString(), othersText.getText().toString()};*/

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

                    Intent i = new Intent(ChecklistInvalidCheque.this, ChequeReceived.class);
                    startActivity(i);
                }
            }
        });
    }
}
