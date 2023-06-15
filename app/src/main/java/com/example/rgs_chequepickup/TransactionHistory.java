package com.example.rgs_chequepickup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import SessionPackage.HistoryManagement;
import SessionPackage.SessionManagement;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TransactionHistory extends AppCompatActivity {
    String tin, accno, payee, ornum, amount;
    TextView back_button;
    TextView tnum, company, address, status;
    LinearLayout parentLayout;
    String trans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        HistoryManagement hm = new HistoryManagement(TransactionHistory.this);
        trans = hm.getTrans();

        SessionManagement sm = new SessionManagement(TransactionHistory.this);
        String rider = sm.getSession();
        fetchDataWithSpecificValue(rider);

        back_button = (TextView) findViewById(R.id.back_button);

        parentLayout = (LinearLayout) findViewById(R.id.parentLayout);
        tnum = (TextView) findViewById(R.id.TNumber);
        company = (TextView) findViewById(R.id.companyname);
        address = (TextView) findViewById(R.id.address);
        status = (TextView) findViewById(R.id.stat);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        back_button.setTypeface(font);
        back_button.setText("\uf060");

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionHistory.this, HistoryActivity.class);
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

    public void processAssociativeArray(JSONArray associativeArray) throws JSONException {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(associativeArray.length() == 0){
                        tnum.setText("NO HISTORY");
                        company.setText("NO HISTORY");
                        address.setText("NO HISTORY");
                        status.setText("NO HISTORY");
                    }
                    else{
                        JSONObject item2 = associativeArray.getJSONObject(0);
                        HistoryManagement hm = new HistoryManagement(TransactionHistory.this);

                        String og1 = item2.getString("transaction_num");
                        String og2 = item2.getString("company");
                        String og3 = item2.getString("address");
                        String og4  = item2.getString("status");

                        tnum.setText(hm.getTrans());
                        company.setText(hm.getComp());
                        address.setText(hm.getAdd());
                        status.setText(hm.getStat());

                        for(int i = 0; i < associativeArray.length(); i++){
                            JSONObject item = associativeArray.getJSONObject(i);

                            if(item.getString("transaction_num").equals(hm.getTrans())){
                                tin = item.getString("chk_tin");
                                accno = item.getString("account_no");
                                payee = item.getString("chk_payee");
                                ornum = item.getString("or_no");
                                amount = item.getString("chk_amount");
                                break;
                            }
                        }

                        //String[] tinArr = tin.split(",");
                        String[] accArr = accno.split(",");
                        String[] payArr = payee.split(",");
                        String[] orArr = ornum.split(",");
                        String[] amArr = amount.split(",");

                        Log.d("Result", "Result: " + payee);

                        int size = payArr.length;

                        for(int i = 0; i < size; i++){
                            View line = new View(TransactionHistory.this);
                            line.setBackgroundColor(Color.BLACK);

                            RelativeLayout.LayoutParams line_params = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    6
                            );

                            line_params.bottomMargin = 5;
                            line_params.topMargin = 25;
                            line.setLayoutParams(line_params);
                            //AMOUNT
                            TextView tv_am = new TextView(TransactionHistory.this);
                            tv_am.setText("Cheque Amount");
                            tv_am.setTypeface(null, Typeface.BOLD);
                            tv_am.setTextSize(15);
                            tv_am.setTextColor(Color.BLACK);
                            RelativeLayout.LayoutParams am_params = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                            tv_am.setLayoutParams(am_params);

                            TextView amIn = new TextView(TransactionHistory.this);
                            amIn.setText(amArr[i]);
                            amIn.setTextSize(15);
                            amIn.setInputType(InputType.TYPE_CLASS_NUMBER);
                            amIn.setTextColor(Color.BLACK);
                            RelativeLayout.LayoutParams amIn_params = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                            amIn_params.setMargins(15, 0,0,0);
                            amIn.setLayoutParams(amIn_params);
                            //ORNUM
                            TextView tv_or = new TextView(TransactionHistory.this);
                            tv_or.setText("OR Number");
                            tv_or.setTypeface(null, Typeface.BOLD);
                            tv_or.setTextSize(15);
                            tv_or.setTextColor(Color.BLACK);
                            RelativeLayout.LayoutParams or_params = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                            tv_or.setLayoutParams(or_params);

                            TextView orIn = new TextView(TransactionHistory.this);
                            orIn.setText(orArr[i]);
                            orIn.setTextSize(15);
                            orIn.setInputType(InputType.TYPE_CLASS_NUMBER);
                            orIn.setTextColor(Color.BLACK);
                            RelativeLayout.LayoutParams orIn_params = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                            orIn_params.setMargins(15, 0,0,0);
                            orIn.setLayoutParams(orIn_params);

                            //PAYEE
                            TextView tv_payee = new TextView(TransactionHistory.this);
                            tv_payee.setText("Payee");
                            tv_payee.setTypeface(null, Typeface.BOLD);
                            tv_payee.setTextSize(15);
                            tv_payee.setTextColor(Color.BLACK);
                            RelativeLayout.LayoutParams pay_params = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                            tv_payee.setLayoutParams(pay_params);

                            TextView payIn = new TextView(TransactionHistory.this);
                            payIn.setText(payArr[i]);
                            payIn.setTextSize(15);
                            payIn.setTextColor(Color.BLACK);
                            RelativeLayout.LayoutParams payIn_params = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                            payIn_params.setMargins(15, 0,0,0);
                            payIn.setLayoutParams(payIn_params);
                            //ACCNO
                            TextView tv_acc = new TextView(TransactionHistory.this);
                            tv_acc.setText("Account Number");
                            tv_acc.setTypeface(null, Typeface.BOLD);
                            tv_acc.setTextSize(15);
                            tv_acc.setTextColor(Color.BLACK);
                            RelativeLayout.LayoutParams acc_params = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                            tv_acc.setLayoutParams(acc_params);

                            TextView accIn = new TextView(TransactionHistory.this);
                            if(accArr.length == 1){
                                if(accArr[0].equals("Multiple Accounts")){
                                    accIn.setText("Multiple Accounts");
                                }
                                else{
                                    accIn.setText("none");
                                }
                            }
                            else if(accArr.length > 1){
                                accIn.setText(accArr[i]);
                            }
                            accIn.setTextSize(15);
                            accIn.setInputType(InputType.TYPE_CLASS_NUMBER);
                            accIn.setTextColor(Color.BLACK);
                            RelativeLayout.LayoutParams accIn_params = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                            accIn_params.setMargins(15, 0,0,0);
                            accIn.setLayoutParams(accIn_params);
                            //TIN
                            TextView tv_tin = new TextView(TransactionHistory.this);
                            tv_tin.setText("TIN Number");
                            tv_tin.setTypeface(null, Typeface.BOLD);
                            tv_tin.setTextSize(15);
                            tv_tin.setTextColor(Color.BLACK);
                            RelativeLayout.LayoutParams tin_params = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                            tv_tin.setLayoutParams(tin_params);

                            TextView tinIn = new TextView(TransactionHistory.this);
                            tinIn.setText(tin);
                            tinIn.setTextSize(15);
                            tinIn.setTextColor(Color.BLACK);
                            RelativeLayout.LayoutParams tinIn_params = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                            tinIn_params.setMargins(15, 0,0,0);
                            tinIn.setLayoutParams(tinIn_params);
                            //TITLE
                            TextView title = new TextView(TransactionHistory.this);
                            title.setText("Cheque " + (i + 1));
                            title.setId(View.generateViewId());
                            title.setTypeface(null, Typeface.BOLD);
                            title.setTextSize(15);
                            title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            title.setTextColor(Color.BLACK);
                            RelativeLayout.LayoutParams title_params = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                            title_params.topMargin = 30;
                            title.setLayoutParams(title_params);

                            parentLayout.addView(title);
                            parentLayout.addView(tv_tin);
                            parentLayout.addView(tinIn);
                            parentLayout.addView(tv_acc);
                            parentLayout.addView(accIn);
                            parentLayout.addView(tv_payee);
                            parentLayout.addView(payIn);
                            parentLayout.addView(tv_or);
                            parentLayout.addView(orIn);
                            parentLayout.addView(tv_am);
                            parentLayout.addView(amIn);
                            parentLayout.addView(line);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}