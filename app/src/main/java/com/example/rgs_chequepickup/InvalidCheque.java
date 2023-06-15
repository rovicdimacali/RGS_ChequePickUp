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

import org.w3c.dom.Text;

import SessionPackage.remarkManagement;
import SessionPackage.remarkSession;

public class InvalidCheque extends AppCompatActivity {
    TextView back_button;
    Button submit;
    CheckBox wrongSpellBox, erasuresBox, noSignBox, dateDisBox, othersBox;
    EditText othersText;
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

        CompoundButton.OnCheckedChangeListener cbl = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(wrongSpellBox.isChecked() || erasuresBox.isChecked() || noSignBox.isChecked() || dateDisBox.isChecked()
                || othersBox.isChecked()){
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
                if(wrongSpellBox.isChecked() || erasuresBox.isChecked() || noSignBox.isChecked() || dateDisBox.isChecked()
                        || othersBox.isChecked()){

                    String[] arrayRem = {wrongSpellBox.getText().toString(), erasuresBox.getText().toString(), noSignBox.getText().toString()
                    , dateDisBox.getText().toString(), othersText.getText().toString()};

                    String remarks = "";

                    for(int i = 0; i < arrayRem.length; i++){
                        if(!(arrayRem[i].equals("") || arrayRem[i].equals(" ") || arrayRem[i].isEmpty())){
                            remarks += arrayRem[i] + ",";
                        }
                    }

                    remarks = remarks.substring(0, remarks.length() - 1);

                    remarkManagement rm = new remarkManagement(InvalidCheque.this);
                    remarkSession rs = new remarkSession(remarks);
                    rm.saveRemark(rs);

                    Intent i = new Intent(InvalidCheque.this, ChequeReceived.class);
                    startActivity(i);
                }
                else if(!(wrongSpellBox.isChecked() || erasuresBox.isChecked() || noSignBox.isChecked() || dateDisBox.isChecked()
                        || othersBox.isChecked())){
                    Toast.makeText(InvalidCheque.this, "Please select an option", Toast.LENGTH_SHORT).show();
                }
                else if(!(wrongSpellBox.isChecked() || erasuresBox.isChecked() || noSignBox.isChecked() || dateDisBox.isChecked())
                        && (othersBox.isChecked() && (othersText.getText().toString().isEmpty() || othersText.getText().toString().equals(" ")))){
                    Toast.makeText(InvalidCheque.this, "Please enter a reason", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
