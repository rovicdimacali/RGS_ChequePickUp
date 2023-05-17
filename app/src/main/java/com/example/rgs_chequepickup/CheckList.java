package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
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

        CompoundButton.OnCheckedChangeListener cbl = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (payable.isChecked() && date.isChecked() && figures.isChecked()
                        && months.isChecked() && sign.isChecked() && !(erase.isChecked())) {
                    submit.setClickable(true);
                    submit.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_green));
                    Toast.makeText(CheckList.this, "Cheque is not defective", Toast.LENGTH_SHORT).show();
                }
                else if(!(payable.isChecked() && date.isChecked() && figures.isChecked()
                        && months.isChecked() && sign.isChecked()) && erase.isChecked()){
                    submit.setClickable(true);
                    submit.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_green));
                    Toast.makeText(CheckList.this, "Cheque is defective", Toast.LENGTH_SHORT).show();
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
            }
        };

        payable.setOnCheckedChangeListener(cbl);
        date.setOnCheckedChangeListener(cbl);
        figures.setOnCheckedChangeListener(cbl);
        months.setOnCheckedChangeListener(cbl);
        sign.setOnCheckedChangeListener(cbl);
        erase.setOnCheckedChangeListener(cbl);

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
                && months.isChecked() && sign.isChecked() && !(erase.isChecked())){
                    //Toast.makeText(CheckList.this, "Cheque is not defective", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CheckList.this, CaptureCheque.class);
                    intent.putExtra("cheque", 1);
                    startActivity(intent);
                }
                else if(!(payable.isChecked() && date.isChecked() && figures.isChecked()
                        && months.isChecked() && sign.isChecked()) && erase.isChecked()){
                    Toast.makeText(CheckList.this, "Cheque is defective", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CheckList.this, CaptureCheque.class);
                    intent.putExtra("cheque", -1);
                    startActivity(intent);
                }

            }
        });
    }

    private void PopUpDate(){
        layout = (LinearLayout) findViewById(R.id.linear);
        date_btn = (TextView) findViewById(R.id.date_btn);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.popup_dates, null);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);
        layout.post(new Runnable() {
            @Override
            public void run() {
                date_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
                    }
                });
            }
        });

        Button dismiss = (Button) popUpView.findViewById(R.id.dismiss_button);
        layout.post(new Runnable() {
            @Override
            public void run() {
                dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
        });
    }
}