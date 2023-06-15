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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import SessionPackage.scenarioManagement;
import SessionPackage.scenarioSession;

public class CheckList extends AppCompatActivity {

    TextView back_button, date_btn;
    Button submit, oneAcc, multAcc, multEnt, oneEnt, allMult;
    CheckBox payable, date, figures, months, sign, erase;
    LinearLayout layout;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);


        //CHECKBOXES
        payable = (CheckBox)findViewById(R.id.checkbox_payable);
        date = (CheckBox) findViewById(R.id.checkbox_date);
        figures = (CheckBox) findViewById(R.id.checkbox_figures);
        months = (CheckBox) findViewById(R.id.checkbox_6months);
        sign = (CheckBox) findViewById(R.id.checkbox_signature) ;
        erase = (CheckBox) findViewById(R.id.checkbox_erasures);
        date_btn = (TextView) findViewById(R.id.date_btn);

        back_button = (TextView) findViewById(R.id.back_button);
        submit = (Button) findViewById(R.id.submit);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        back_button.setTypeface(font);
        back_button.setText("\uf060");

        CompoundButton.OnCheckedChangeListener cbl = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
            }
        };

        payable.setOnCheckedChangeListener(cbl);
        date.setOnCheckedChangeListener(cbl);
        figures.setOnCheckedChangeListener(cbl);
        months.setOnCheckedChangeListener(cbl);
        sign.setOnCheckedChangeListener(cbl);
        erase.setOnCheckedChangeListener(cbl);

        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopUpDate();
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckList.this, ChequePickUp.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(payable.isChecked() && date.isChecked() && figures.isChecked()
                && months.isChecked() && sign.isChecked() && !(erase.isChecked())){
                    //Toast.makeText(CheckList.this, "Cheque is not defective", Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(CheckList.this, CaptureCheque.class);
                    //intent.putExtra("cheque", 1);
                    //startActivity(intent);
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

            }
        });
    }

    private void PopUpDate(){
        layout = (LinearLayout) findViewById(R.id.linear);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.popup_dates, null);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);

        layout.post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
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

    /*private void PopUpScenarios(){
        layout = (LinearLayout) findViewById(R.id.linear);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.popup_scenarios, null);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        boolean focusable = true;

        oneAcc = (Button) popUpView.findViewById(R.id.oneConeA);
        multAcc = (Button) popUpView.findViewById(R.id.oneCmultA);
        multEnt = (Button) popUpView.findViewById(R.id.oneCmultE);
        //oneEnt = (Button) popUpView.findViewById(R.id.multConeA);
        allMult = (Button) popUpView.findViewById(R.id.multCmultE);

        PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);

        oneAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(CheckList.this, CaptureCheque.class);
                scenarioManagement sm = new scenarioManagement(CheckList.this);
                scenarioSession ss = new scenarioSession("One Check, One Account", "Not Defective");
                sm.saveScene(ss);
                startActivity(i);
                finish();
            }
        });
        multAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(CheckList.this, CaptureCheque.class);
                scenarioManagement sm = new scenarioManagement(CheckList.this);
                scenarioSession ss = new scenarioSession("One Account, Multiple Cheques", "Not Defective");
                sm.saveScene(ss);
                startActivity(i);
                finish();
            }
        });
       multEnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(CheckList.this, CaptureCheque.class);
                scenarioManagement sm = new scenarioManagement(CheckList.this);
                scenarioSession ss = new scenarioSession("One Cheque, Multiple Accounts", "Not Defective");
                sm.saveScene(ss);
                startActivity(i);
                finish();
            }
        });
       oneEnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(CheckList.this, CaptureCheque.class);
                scenarioManagement sm = new scenarioManagement(CheckList.this);
                scenarioSession ss = new scenarioSession("One Cheque, Multiple Entities", "Not Defective");
                sm.saveScene(ss);
                startActivity(i);
            }
        });
       allMult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(CheckList.this, CaptureCheque.class);
                scenarioManagement sm = new scenarioManagement(CheckList.this);
                scenarioSession ss = new scenarioSession("Multiple Accounts, Multiple Cheques", "Not Defective");
                sm.saveScene(ss);
                startActivity(i);
                finish();
            }
        });
        layout.post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            }
        });

        RelativeLayout overlay = (RelativeLayout) popUpView.findViewById(R.id.overlay);

        layout.post(new Runnable() {
            @Override
            public void run() {
                overlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
        });
    }*/
}