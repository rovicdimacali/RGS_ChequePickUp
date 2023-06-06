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
    LinearLayout parent_rl;

    JSONArray jsonArray = new JSONArray();
    JSONObject jsonObject = new JSONObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        SessionManagement sm = new SessionManagement(HistoryActivity.this);
        fetchDataWithSpecificValue(sm.getSession());


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

        comp = (TextView) findViewById(R.id.company);
        code = (TextView) findViewById(R.id.code);
        address = (TextView) findViewById(R.id.address);
        number = (TextView) findViewById(R.id.number);
        transact = (TextView) findViewById(R.id.transact);
        result = (TextView) findViewById(R.id.remarks);

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
        String url = "http://203.177.49.26:28110/tracker/api/accounts";
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
                        code.setText("NO TASK");
                        address.setText("NO TASK");
                        number.setText("NO TASK");
                        transact.setText("NO TASK");
                        result.setText("NO TASK");
                    }
                    else{
                        JSONObject item2 = associativeArray.getJSONObject(0);

                        String og1 = item2.getString("company_name");
                        //String og2 = item2.getString("fullname");
                        String og3 = item2.getString("address");
                        String og4 = item2.getString("contact_no");
                        String og5 = item2.getString("company_code");

                        comp.setText(og1);
                        code.setText(og5);
                        address.setText(og3);
                        number.setText(og4);
                        //transact.setText("NO TASK");
                        result.setText("SUCCESS!");
                    }

                    // Iterate over the associative array
                    parent_rl = (LinearLayout) findViewById(R.id.linearlayout);
                    for (int i = 1; i < associativeArray.length(); i++) {
                        JSONObject item = associativeArray.getJSONObject(i);

                        String val1 = item.getString("company_name");
                        //String val2 = item2.getString("fullname");
                        String val3 = item.getString("address");
                        String val4 = item.getString("contact_no");
                        String val5 = item.getString("company_code");

                        //TEXTVIEWS
                        TextView icon1 = new TextView(HistoryActivity.this);
                        icon1.setId(R.id.icon_company);
                        icon1.setTextSize(18);
                        icon1.setTextColor(Color.BLACK);
                        RelativeLayout.LayoutParams icon1_params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                        );

                        icon1_params.rightMargin = 20;
                        icon1_params.addRule(RelativeLayout.CENTER_VERTICAL);
                        icon1.setLayoutParams(icon1_params);

                        TextView comp = new TextView(HistoryActivity.this);
                        comp.setTextSize(18);
                        comp.setTextColor(Color.BLACK);
                        comp.setText(val1);

                        RelativeLayout.LayoutParams comp_params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );

                        comp_params.addRule(RelativeLayout.CENTER_VERTICAL);
                        comp_params.addRule(RelativeLayout.RIGHT_OF, R.id.icon_company);
                        comp.setLayoutParams(comp_params);

                        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

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
                        layout1.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));

                        layout1.setPadding(60, 40, 60,40);
                        layout1.setOrientation(LinearLayout.VERTICAL);

                        //SCROLL VIEW
                        ScrollView sv = new ScrollView(HistoryActivity.this);
                        LinearLayout.LayoutParams scrollparams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                        sv.setLayoutParams(scrollparams);

                        //CARDVIEW
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
                        cv.setLayoutParams(cvparams);

                        //ADDVIEWS
                        rl_1.addView(comp);
                        rl_1.addView(icon1);
                        layout1.addView(rl_1);
                        cv.addView(layout1);
                        sv.addView(cv);
                        parent_rl.addView(sv);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}