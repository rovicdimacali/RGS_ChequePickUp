package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import SessionPackage.LocationManagement;
import SessionPackage.ReceiptManagement;
import SessionPackage.accountManagement;
import SessionPackage.chequeManagement;
import SessionPackage.scenarioManagement;

public class VerifyOfficialReceipt extends AppCompatActivity {
    LinearLayout parentLayout;
    TextView compname, tinname, accname, payeename, orname, amname;
    TextView compin, tinin, accin, payeein, orin, amin;
    LinearLayout layout;
    String company, tin, accno, payee, or, amount;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_official_receipt);

        ReceiptManagement rm = new ReceiptManagement(VerifyOfficialReceipt.this);
        LocationManagement lm = new LocationManagement(VerifyOfficialReceipt.this);
        scenarioManagement sm = new scenarioManagement(VerifyOfficialReceipt.this);
        accountManagement am = new accountManagement(VerifyOfficialReceipt.this);
        chequeManagement cm = new chequeManagement(VerifyOfficialReceipt.this);


        submit = (Button) findViewById(R.id.submit_button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InvalidChequePopupWindow();
            }
        });
    }

    private void InvalidChequePopupWindow() {
        layout = (LinearLayout) findViewById(R.id.layout);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.popup_invalid_cheque, null);

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
    }
}