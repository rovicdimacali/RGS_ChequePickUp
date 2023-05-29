package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import SessionPackage.accountManagement;
import SessionPackage.remarkManagement;
import SessionPackage.remarkSession;

public class RemarksActivity extends AppCompatActivity {

    Button submit_btn;
    TextView back_btn;
    EditText notes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remarks);

        remarkManagement rm = new remarkManagement(RemarksActivity.this);
        back_btn = (TextView) findViewById(R.id.back_button);
        submit_btn = (Button) findViewById(R.id.submit_btn);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        notes = (EditText) findViewById(R.id.remarks_text);

        if(!(rm.getRemark().equals("none"))){
            notes.setText(rm.getRemark());
        }

        back_btn.setTypeface(font);
        back_btn.setText("\uf060");

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
                remarkSession rs = new remarkSession(notes.getText().toString());
                rm.saveRemark(rs);
                Intent intent = new Intent(RemarksActivity.this, ESignature.class);
                startActivity(intent);
                finish();
            }
        });
    }
}