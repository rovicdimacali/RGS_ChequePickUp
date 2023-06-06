package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class HistoryActivity extends AppCompatActivity {

    TextView icon_company, icon_code, icon_location, icon_number, icon_remarks, icon_transact, back_button;
    JSONArray jsonArray = new JSONArray();
    JSONObject jsonObject = new JSONObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        icon_transact = (TextView) findViewById(R.id.icon_transact);
        icon_company = (TextView) findViewById(R.id.icon_company);
        icon_code = (TextView) findViewById(R.id.icon_code);
        icon_location = (TextView) findViewById(R.id.icon_location);
        icon_number = (TextView) findViewById(R.id.icon_number);
        icon_remarks= (TextView) findViewById(R.id.icon_remarks);
        back_button = (TextView) findViewById(R.id.back_button);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        icon_transact.setTypeface(font);
        icon_company.setTypeface(font);
        icon_code.setTypeface(font);
        icon_location.setTypeface(font);
        icon_number.setTypeface(font);
        icon_remarks.setTypeface(font);
        back_button.setTypeface(font);

        back_button.setText("\uf060");
        icon_transact.setText("\uf2c2");
        icon_company.setText("\uf1ad");
        icon_code.setText("\uf25d");
        icon_location.setText("\uf015");
        icon_number.setText("\uf2a0");
        icon_remarks.setText("\uf075");

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}