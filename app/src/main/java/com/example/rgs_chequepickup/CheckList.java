package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class CheckList extends AppCompatActivity {

    TextView back_button;
    Button submit;
    CheckBox payable, date, figures, months, sign, erase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        //CHECKBOXES
        payable = (CheckBox) findViewById(R.id.checkbox_payable);
        date = (CheckBox) findViewById(R.id.checkbox_date);
        figures = (CheckBox) findViewById(R.id.checkbox_figures);
        months = (CheckBox) findViewById(R.id.checkbox_6months);
        sign = (CheckBox) findViewById(R.id.checkbox_signature) ;
        erase = (CheckBox) findViewById(R.id.checkbox_erasures);

        back_button = (TextView) findViewById(R.id.back_button);
        submit = (Button) findViewById(R.id.submit);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        back_button.setTypeface(font);
        back_button.setText("\uf060");

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckList.this, ChequePickUp.class);
                startActivity(intent);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(payable.isChecked() && date.isChecked() && figures.isChecked()
                && months.isChecked() && sign.isChecked() && erase.isChecked()){
                    Toast.makeText(CheckList.this, "Cheque is not defective", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CheckList.this, CaptureCheque.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(CheckList.this, "Cheque has one or more defects", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}