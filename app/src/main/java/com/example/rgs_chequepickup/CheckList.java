package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import SessionPackage.scenarioManagement;
import SessionPackage.scenarioSession;

public class CheckList extends AppCompatActivity {

    TextView back_button, date_btn;
    Button submit;
    CheckBox payable, date, figures, months, sign, erase;
    LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);


        //CHECKBOXES
        payable = findViewById(R.id.checkbox_payable);
        date = findViewById(R.id.checkbox_date);
        figures = findViewById(R.id.checkbox_figures);
        months = findViewById(R.id.checkbox_6months);
        sign = findViewById(R.id.checkbox_signature);
        erase = findViewById(R.id.checkbox_erasures);
        date_btn = findViewById(R.id.date_btn);

        back_button = findViewById(R.id.back_button);
        submit = findViewById(R.id.submit);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        back_button.setTypeface(font);
        back_button.setText("\uf060");

        CompoundButton.OnCheckedChangeListener cbl = (buttonView, isChecked) -> {
            if ((payable.isChecked() && date.isChecked() && figures.isChecked()
                    && months.isChecked() && sign.isChecked()) && !(erase.isChecked())) {
                submit.setClickable(true);
                submit.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_green));
                Toast.makeText(CheckList.this, "Cheque is valid", Toast.LENGTH_SHORT).show();
            }
            else if((payable.isChecked() || date.isChecked() || figures.isChecked()
                    || months.isChecked() || sign.isChecked()) && (erase.isChecked())) {
                submit.setClickable(false);
                submit.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_gray1));
            }
            else if(!(payable.isChecked() && date.isChecked() && figures.isChecked()
                    && months.isChecked() && sign.isChecked()) && erase.isChecked()){
                submit.setClickable(true);
                submit.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_green));
                Toast.makeText(CheckList.this, "Cheque is invalid", Toast.LENGTH_SHORT).show();
            }
            else if(!(payable.isChecked() && date.isChecked() && figures.isChecked()
                    && months.isChecked() && sign.isChecked() && erase.isChecked())){
                submit.setClickable(false);
                submit.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_gray1));
            }
            else if(payable.isChecked() && date.isChecked() && figures.isChecked()
                    && months.isChecked() && sign.isChecked() && erase.isChecked()){
                submit.setClickable(false);
                Toast.makeText(CheckList.this, "Invalid checklist.", Toast.LENGTH_SHORT).show();
                submit.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_gray1));
            }
        };

        payable.setOnCheckedChangeListener(cbl);
        date.setOnCheckedChangeListener(cbl);
        figures.setOnCheckedChangeListener(cbl);
        months.setOnCheckedChangeListener(cbl);
        sign.setOnCheckedChangeListener(cbl);
        erase.setOnCheckedChangeListener(cbl);

        date_btn.setOnClickListener(v -> PopUpDate());
        back_button.setOnClickListener(v -> {
            Intent intent = new Intent(CheckList.this, ChequePickUp.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        submit.setOnClickListener(v -> {
            if(payable.isChecked() && date.isChecked() && figures.isChecked()
            && months.isChecked() && sign.isChecked() && !(erase.isChecked())){
                Intent intent = new Intent(CheckList.this, OfficialReceipt.class);
                scenarioManagement sm = new scenarioManagement(CheckList.this);
                scenarioSession ss = new scenarioSession("Valid Cheque", "Not Defective");
                sm.saveScene(ss);
                //intent.putExtra("cheque", -1);
                startActivity(intent);
                finish();
            }
            else if(!(payable.isChecked() && date.isChecked() && figures.isChecked()
                    && months.isChecked() && sign.isChecked()) && erase.isChecked()){
                //Toast.makeText(CheckList.this, "Cheque is defective", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CheckList.this, CaptureCheque.class);
                scenarioManagement sm = new scenarioManagement(CheckList.this);
                scenarioSession ss = new scenarioSession("Invalid Cheque", "Defective");
                sm.saveScene(ss);
                //intent.putExtra("cheque", -1);
                startActivity(intent);
                finish();
            }

        });
    }

    private void PopUpDate(){
        layout = findViewById(R.id.linear);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.popup_dates, null);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);

        layout.post(() -> popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0));

        Button dismiss = popUpView.findViewById(R.id.dismiss_button);
        layout.post(() -> dismiss.setOnClickListener(v -> popupWindow.dismiss()));
    }
}