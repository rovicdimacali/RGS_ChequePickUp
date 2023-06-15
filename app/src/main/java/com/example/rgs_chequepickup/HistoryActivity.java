package com.example.rgs_chequepickup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import SessionPackage.HistoryManagement;
import SessionPackage.HistorySession;
import SessionPackage.LocationManagement;
import SessionPackage.LocationSession;
import SessionPackage.SessionManagement;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HistoryActivity extends AppCompatActivity {

    TextView icon_company, icon_code, icon_location, icon_number, icon_remarks, icon_transact, back_button;
    TextView comp, code, address, number, transact, result;
    LinearLayout parent_rl, linearContents;
    ScrollView parent_sv;
    Button viewBtn;

    JSONArray jsonArray = new JSONArray();
    JSONObject jsonObject = new JSONObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        SessionManagement sm = new SessionManagement(HistoryActivity.this);
        fetchDataWithSpecificValue(sm.getSession());

        viewBtn = (Button) findViewById(R.id.view_button);

        icon_transact = (TextView) findViewById(R.id.icon_transact);
        icon_company = (TextView) findViewById(R.id.icon_company);
        //icon_code = (TextView) findViewById(R.id.icon_code);
        icon_location = (TextView) findViewById(R.id.icon_location);
        //icon_number = (TextView) findViewById(R.id.icon_number);
        icon_remarks= (TextView) findViewById(R.id.icon_remarks);
        back_button = (TextView) findViewById(R.id.back_button);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        icon_transact.setTypeface(font);
        icon_company.setTypeface(font);
        //icon_code.setTypeface(font);
        icon_location.setTypeface(font);
        //icon_number.setTypeface(font);
        icon_remarks.setTypeface(font);
        back_button.setTypeface(font);

        back_button.setText("\uf060");
        icon_transact.setText("\uf2c2");
        icon_company.setText("\uf1ad");
        //icon_code.setText("\uf25d");
        icon_location.setText("\uf015");
        //icon_number.setText("\uf2a0");
        icon_remarks.setText("\uf075");

        comp = (TextView) findViewById(R.id.company);
        //code = (TextView) findViewById(R.id.code);
        address = (TextView) findViewById(R.id.address);
       // number = (TextView) findViewById(R.id.number);
        transact = (TextView) findViewById(R.id.transact);
        result = (TextView) findViewById(R.id.remarks);

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HistoryActivity.this, TransactionHistory.class);
                HistoryManagement hm = new HistoryManagement(HistoryActivity.this);
                HistorySession hs = new HistorySession(transact.getText().toString(), comp.getText().toString()
                ,address.getText().toString(),result.getText().toString());
                hm.saveHistory(hs);
                startActivity(i);
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void fetchDataWithSpecificValue(String riderID) {
        OkHttpClient client = new OkHttpClient();

        // Construct the URL with the specific value
        String url = "http://203.177.49.26:28110/tracker/api/history";
        RequestBody rbody = new FormBody.Builder()
                .add("riderID", riderID)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(rbody)
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    //String responseData = "";
                    //responseData = responseData.replace("<br", "");
                    try {
                        // Parse the response JSON
                        //JSONObject jsonObject = new JSONObject(responseData);
                        JSONArray jsonArray = new JSONArray(responseData);

                        // Access the multidimensional associative array within the JSON
                        //JSONArray associativeArray = jsonObject.getJSONArray("riderID");

                        // Process and display the associative array data
                        processAssociativeArray(jsonArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle unsuccessful response
                }
            }
        });
    }

    public void processAssociativeArray(JSONArray associativeArray) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(associativeArray.length() == 0){
                        comp.setText("NO TASK");
                        //code.setText("NO TASK");
                        address.setText("NO TASK");
                        //number.setText("NO TASK");
                        transact.setText("NO TASK");
                        result.setText("NO TASK");
                    }
                    else{
                        JSONObject item2 = associativeArray.getJSONObject(0);

                        String og1 = item2.getString("company");
                        //String og2 = item2.getString("fullname");
                        String og3 = item2.getString("address");
                        String og4 = item2.getString("transaction_num");
                        String og5 = item2.getString("company_code");
                        String status = item2.getString("status");

                        comp.setText(og1);
                        //code.setText(og5);
                        address.setText(og3);
                        //number.setText("09167065890");
                        transact.setText(og4);
                        if(!(status.equals("Not Defective") || status.equals("Defective"))){
                            result.setText("CANCELLED - " + status);
                            result.setTextColor(Color.RED);
                        }
                        else{
                            result.setText("SUCCESS - " + status);
                        }

                    }

                    // Iterate over the associative array
                    Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
                    parent_rl = (LinearLayout) findViewById(R.id.linearlayout);
                    parent_sv = (ScrollView) findViewById(R.id.scrollViewer);
                    linearContents = (LinearLayout) findViewById(R.id.linearContents);

                    for (int i = 1; i < associativeArray.length(); i++) {
                        JSONObject item = associativeArray.getJSONObject(i);

                        String val1 = item.getString("company");
                        //String val2 = item2.getString("fullname");
                        String val3 = item.getString("address");
                        //String val4 = item.getString("contact_no");
                        String val5 = item.getString("company_code");
                        String val6 = item.getString("transaction_num");
                        String status = item.getString("status");

                        View line = new View(HistoryActivity.this);
                        line.setBackgroundColor(Color.BLACK);

                        RelativeLayout.LayoutParams line_params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                6
                        );

                        line_params.bottomMargin = 40;
                        line.setLayoutParams(line_params);

                        Button edit = new Button(HistoryActivity.this);
                        edit.setText("View Transaction");
                        edit.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_green));
                        RelativeLayout.LayoutParams edit_params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                        );

                        edit_params.topMargin = 10;
                        edit_params.bottomMargin = 60;
                        //edit_params.setMarginStart(160);
                        edit.setLayoutParams(edit_params);

                        //TEXTVIEWS
                        TextView icon6 = new TextView(HistoryActivity.this);
                        icon6.setId(R.id.icon_remarks);
                        icon6.setTextSize(23);
                        icon6.setTextColor(Color.BLACK);
                        RelativeLayout.LayoutParams icon6_params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                        );

                        icon6_params.rightMargin = 20;
                        icon6_params.addRule(RelativeLayout.CENTER_VERTICAL);
                        icon6_params.setMarginStart(50);
                        icon6.setLayoutParams(icon6_params);

                        TextView res = new TextView(HistoryActivity.this);
                        res.setTextSize(20);

                        if(!(status.equals("Not Defective") || status.equals("Defective"))){
                            res.setText("CANCELLED - " + status);
                            res.setTextColor(Color.RED);
                        }
                        else if(status.equals("Defective")){
                            res.setText("UNSUCCESSFUL - Invalid Cheque/s");
                            res.setTextColor(Color.RED);
                        }
                        else if(status.equals("Not Defective")){
                            res.setText("SUCCESS - Valid Cheque/s");
                            res.setTextColor(Integer.parseInt(String.valueOf(Color.parseColor("#4CAF50"))));
                        }


                        RelativeLayout.LayoutParams res_params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                        res_params.addRule(RelativeLayout.CENTER_VERTICAL);
                        res_params.addRule(RelativeLayout.RIGHT_OF, R.id.icon_remarks);
                        res_params.setMarginStart(40);
                        res.setLayoutParams(res_params);

                        icon6.setTypeface(font);
                        icon6.setText("\uf075");

                        //5TH RELATIVE LAYOUT
                        RelativeLayout rl_6 = new RelativeLayout(HistoryActivity.this);
                        RelativeLayout.LayoutParams rl6Params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                        );

                        rl6Params.bottomMargin = 10;
                        rl6Params.topMargin = 10;
                        rl6Params.addRule(RelativeLayout.BELOW, R.id.address_container);
                        rl_6.setLayoutParams(rl6Params);

                        //TEXTVIEWS
                        TextView icon5 = new TextView(HistoryActivity.this);
                        icon5.setId(R.id.icon_transact);
                        icon5.setTextSize(23);
                        icon5.setTextColor(Color.BLACK);
                        RelativeLayout.LayoutParams icon5_params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                        );

                        icon5_params.rightMargin = 20;
                        icon5_params.addRule(RelativeLayout.CENTER_VERTICAL);
                        icon5_params.setMarginStart(50);
                        icon5.setLayoutParams(icon5_params);

                        TextView tran = new TextView(HistoryActivity.this);
                        tran.setTextSize(20);
                        tran.setTextColor(Color.BLACK);
                        tran.setText(val6);

                        RelativeLayout.LayoutParams tran_params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                        tran_params.addRule(RelativeLayout.CENTER_VERTICAL);
                        tran_params.addRule(RelativeLayout.RIGHT_OF, R.id.icon_transact);
                        tran_params.setMarginStart(40);
                        tran.setLayoutParams(tran_params);

                        icon5.setTypeface(font);
                        icon5.setText("\uf2c2");

                        //5TH RELATIVE LAYOUT
                        RelativeLayout rl_5 = new RelativeLayout(HistoryActivity.this);
                        RelativeLayout.LayoutParams rl5Params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                        );

                        rl5Params.bottomMargin = 10;
                        rl5Params.topMargin = 10;
                        rl5Params.addRule(RelativeLayout.BELOW, R.id.address_header);
                        rl_5.setLayoutParams(rl5Params);

                        //TEXTVIEWS
                        /*TextView icon4 = new TextView(HistoryActivity.this);
                        icon4.setId(R.id.icon_number);
                        icon4.setTextSize(23);
                        icon4.setTextColor(Color.BLACK);
                        RelativeLayout.LayoutParams icon4_params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                        );

                        icon4_params.rightMargin = 20;
                        icon4_params.addRule(RelativeLayout.CENTER_VERTICAL);
                        icon4_params.setMarginStart(50);
                        icon4.setLayoutParams(icon4_params);

                        TextView num = new TextView(HistoryActivity.this);
                        num.setTextSize(20);
                        num.setTextColor(Color.BLACK);
                        num.setText("09167065890");

                        RelativeLayout.LayoutParams num_params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                        num_params.addRule(RelativeLayout.CENTER_VERTICAL);
                        num_params.addRule(RelativeLayout.RIGHT_OF, R.id.icon_number);
                        num_params.setMarginStart(40);
                        num.setLayoutParams(num_params);

                        icon4.setTypeface(font);
                        icon4.setText("\uf2a0");

                        //4TH RELATIVE LAYOUT
                        RelativeLayout rl_4 = new RelativeLayout(HistoryActivity.this);
                        RelativeLayout.LayoutParams rl4Params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                        );

                        rl4Params.bottomMargin = 10;
                        rl4Params.topMargin = 10;
                        rl4Params.addRule(RelativeLayout.BELOW, R.id.address_header);
                        rl_4.setLayoutParams(rl4Params);
                        */
                        //TEXTVIEWS
                        TextView icon3 = new TextView(HistoryActivity.this);
                        icon3.setId(R.id.icon_location);
                        icon3.setTextSize(23);
                        icon3.setTextColor(Color.BLACK);
                        RelativeLayout.LayoutParams icon3_params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                        );

                        icon3_params.rightMargin = 20;
                        icon3_params.addRule(RelativeLayout.CENTER_VERTICAL);
                        icon3_params.setMarginStart(50);
                        icon3.setLayoutParams(icon3_params);

                        TextView add = new TextView(HistoryActivity.this);
                        add.setTextSize(20);
                        add.setTextColor(Color.BLACK);
                        add.setText(val3);

                        RelativeLayout.LayoutParams add_params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                        add_params.addRule(RelativeLayout.CENTER_VERTICAL);
                        add_params.addRule(RelativeLayout.RIGHT_OF, R.id.icon_location);
                        add_params.setMarginStart(40);
                        add.setLayoutParams(add_params);

                        icon3.setTypeface(font);
                        icon3.setText("\uf015");

                        //3RD RELATIVE LAYOUT
                        RelativeLayout rl_3 = new RelativeLayout(HistoryActivity.this);
                        RelativeLayout.LayoutParams rl3Params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                        );

                        rl3Params.bottomMargin = 10;
                        rl3Params.topMargin = 10;
                        rl3Params.addRule(RelativeLayout.BELOW, R.id.address_header);
                        rl_3.setLayoutParams(rl3Params);

                        //TEXTVIEWS
                        /*
                        TextView icon2 = new TextView(HistoryActivity.this);
                        icon2.setId(R.id.icon_code);
                        icon2.setTextSize(23);
                        icon2.setTextColor(Color.BLACK);
                        RelativeLayout.LayoutParams icon2_params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                        );

                        icon2_params.rightMargin = 20;
                        icon2_params.addRule(RelativeLayout.CENTER_VERTICAL);
                        icon2_params.setMarginStart(50);
                        icon2.setLayoutParams(icon2_params);

                        TextView code = new TextView(HistoryActivity.this);
                        code.setTextSize(20);
                        code.setTextColor(Color.BLACK);
                        code.setText(val5);

                        RelativeLayout.LayoutParams code_params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                        code_params.addRule(RelativeLayout.CENTER_VERTICAL);
                        code_params.addRule(RelativeLayout.RIGHT_OF, R.id.icon_code);
                        code_params.setMarginStart(40);
                        code.setLayoutParams(code_params);

                        icon2.setTypeface(font);
                        icon2.setText("\uf25d");

                        //2ND RELATIVE LAYOUT
                        RelativeLayout rl_2 = new RelativeLayout(HistoryActivity.this);
                        RelativeLayout.LayoutParams rl2Params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                        );

                        rl2Params.bottomMargin = 10;
                        rl2Params.topMargin = 10;
                        rl2Params.addRule(RelativeLayout.BELOW, R.id.address_header);
                        rl_2.setLayoutParams(rl2Params);

                        */
                        //TEXTVIEWS
                        TextView icon1 = new TextView(HistoryActivity.this);
                        icon1.setId(R.id.icon_company);
                        icon1.setTextSize(23);
                        icon1.setTextColor(Color.BLACK);
                        RelativeLayout.LayoutParams icon1_params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                        );

                        icon1_params.rightMargin = 20;
                        icon1_params.setMarginStart(50);
                        icon1_params.addRule(RelativeLayout.CENTER_VERTICAL);
                        icon1.setLayoutParams(icon1_params);

                        TextView comp = new TextView(HistoryActivity.this);
                        comp.setTextSize(20);
                        comp.setTextColor(Color.BLACK);
                        comp.setText(val1);

                        RelativeLayout.LayoutParams comp_params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                        comp_params.addRule(RelativeLayout.CENTER_VERTICAL);
                        comp_params.addRule(RelativeLayout.RIGHT_OF, R.id.icon_company);
                        comp_params.setMarginStart(40);
                        comp.setLayoutParams(comp_params);

                        icon1.setTypeface(font);
                        icon1.setText("\uf1ad");

                        //1ST RELATIVE LAYOUT
                        RelativeLayout rl_1 = new RelativeLayout(HistoryActivity.this);
                        RelativeLayout.LayoutParams rl1Params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                        );

                        rl1Params.bottomMargin = 10;
                        rl1Params.topMargin = 10;
                        rl1Params.addRule(RelativeLayout.BELOW, R.id.address_header);
                        rl_1.setLayoutParams(rl1Params);

                        //LINEARLAYOUT
                        LinearLayout layout1 = new LinearLayout(HistoryActivity.this);
                        layout1.setBackgroundColor(Color.WHITE);
                        layout1.setPadding(60, 40, 60,40);
                        layout1.setOrientation(LinearLayout.VERTICAL);

                        LinearLayout.LayoutParams l1_params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        l1_params.topMargin = 20;
                        layout1.setLayoutParams(l1_params);

                        /*SCROLL VIEW
                        ScrollView sv = new ScrollView(HistoryActivity.this);
                        LinearLayout.LayoutParams scrollparams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                        sv.setLayoutParams(scrollparams);*/

                        /*CARDVIEW
                        CardView cv = new CardView(HistoryActivity.this);
                        cv.setRadius(40);
                        cv.setElevation(20);
                        cv.setBackgroundColor(Color.WHITE);
                        cv.setPreventCornerOverlap(true);
                        cv.setUseCompatPadding(true);

                        RelativeLayout.LayoutParams cvparams = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                        );
                        cvparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        cv.setLayoutParams(cvparams);*/

                        //ADDVIEWS
                        rl_6.addView(res);
                        rl_6.addView(icon6);
                        rl_3.addView(add);
                        rl_3.addView(icon3);
                        rl_1.addView(comp);
                        rl_1.addView(icon1);
                        rl_5.addView(tran);
                        rl_5.addView(icon5);
                        linearContents.addView(rl_5);
                        linearContents.addView(rl_1);
                        linearContents.addView(rl_3);
                        linearContents.addView(rl_6);
                        linearContents.addView(edit);
                        linearContents.addView(line);
                        /*layout1.addView(rl_1);
                        layout1.addView(rl_2);
                        layout1.addView(rl_3);
                        layout1.addView(rl_4);
                        layout1.addView(rl_5);
                        layout1.addView(rl_6);
                        layout1.addView(edit);*/
                        //cv.addView(layout1);
                        //linearContents.addView(layout1);
                        //parent_rl.addView(parent_sv);
                        edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(HistoryActivity.this, TransactionHistory.class);
                                HistoryManagement hm = new HistoryManagement(HistoryActivity.this);
                                HistorySession hs = new HistorySession(tran.getText().toString(), comp.getText().toString()
                                        ,add.getText().toString(),res.getText().toString());
                                hm.saveHistory(hs);
                                startActivity(i);
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}