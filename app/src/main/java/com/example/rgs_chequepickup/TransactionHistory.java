package com.example.rgs_chequepickup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

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
    String tin, accno, payee, ornum, amount, depStat;
    TextView back_button;
    Button signBtn;
    TextView tnum, company, address, status;
    LinearLayout parentLayout;
    String stat;
    int cheques = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        HistoryManagement hm = new HistoryManagement(TransactionHistory.this);
        stat = hm.getStat();


        SessionManagement sm = new SessionManagement(TransactionHistory.this);
        String rider = sm.getSession();
        fetchDataWithSpecificValue(rider);

        back_button = findViewById(R.id.back_button);

        signBtn = findViewById(R.id.sign_button);
        parentLayout = findViewById(R.id.parentLayout);
        tnum = findViewById(R.id.TNumber);
        company = findViewById(R.id.companyname);
        address = findViewById(R.id.address);
        status = findViewById(R.id.stat);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        back_button.setTypeface(font);
        back_button.setText("\uf060");

        if(!(stat.equals("SUCCESS - Valid Cheque/s"))){
           signBtn.setVisibility(View.GONE);
        }
        else{
            signBtn.setVisibility(View.VISIBLE);
        }
        signBtn.setOnClickListener(v -> {
            Intent intent = new Intent(TransactionHistory.this, DepositSign.class);
            intent.putExtra("cheques", String.valueOf(cheques));
            startActivity(intent);
            finish();
        });
        back_button.setOnClickListener(v -> {
            Intent intent = new Intent(TransactionHistory.this, HistoryActivity.class);
            startActivity(intent);
            finish();
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
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = Objects.requireNonNull(response.body()).string();
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
                    Toast.makeText(TransactionHistory.this, "Response Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void processAssociativeArray(JSONArray associativeArray){

        runOnUiThread(() -> {
            try {
                if(associativeArray.length() == 0){
                    tnum.setText("NO HISTORY");
                    company.setText("NO HISTORY");
                    address.setText("NO HISTORY");
                    status.setText("NO HISTORY");
                }
                else {
                    HistoryManagement hm = new HistoryManagement(TransactionHistory.this);

                    tnum.setText(hm.getTrans());
                    company.setText(hm.getComp());
                    address.setText(hm.getAdd());
                    status.setText(hm.getStat());

                    if (stat.equals("SUCCESS - Valid Cheque/s") || stat.equals("UNSUCCESSFUL - Invalid Cheque/s")) {
                        for (int i = 0; i < associativeArray.length(); i++) {
                            JSONObject item = associativeArray.getJSONObject(i);

                            if (item.getString("transaction_num").equals(hm.getTrans())) {
                                tin = item.getString("chk_tin");
                                accno = item.getString("account_no");
                                payee = item.getString("chk_payee");
                                ornum = item.getString("or_no");
                                amount = item.getString("chk_amount");
                                depStat = item.getString("chk_deposited_by");
                                break;
                            }
                        }

                        if(!depStat.equals("null")){
                            signBtn.setEnabled(false);
                            signBtn.setText("Deposited");
                            signBtn.setTextColor(Color.BLACK);
                            signBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_gray1));
                        }

                        String[] accArr = accno.split(",");
                        String[] payArr = payee.split(",");
                        String[] orArr = ornum.split(",");
                        String[] amArr = amount.split(",");

                        int size = payArr.length;

                        for (int i = 0; i < size; i++) {
                            cheques = cheques + 1;
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
                            amIn_params.setMargins(15, 0, 0, 0);
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
                            orIn_params.setMargins(15, 0, 0, 0);
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
                            payIn_params.setMargins(15, 0, 0, 0);
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
                            if (accArr.length == 1) {
                                if (accArr[0].equals("Multiple Accounts")) {
                                    accIn.setText("Multiple Accounts");
                                } else {
                                    accIn.setText("none");
                                }
                            } else if (accArr.length > 1) {
                                accIn.setText(accArr[i]);
                            }
                            accIn.setTextSize(15);
                            accIn.setInputType(InputType.TYPE_CLASS_NUMBER);
                            accIn.setTextColor(Color.BLACK);
                            RelativeLayout.LayoutParams accIn_params = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                            accIn_params.setMargins(15, 0, 0, 0);
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
                            tinIn_params.setMargins(15, 0, 0, 0);
                            tinIn.setLayoutParams(tinIn_params);
                            //TITLE
                            TextView title = new TextView(TransactionHistory.this);
                            if(orArr[i].equals("none")){
                                title.setText("Cheque " + (i + 1) + " (INVALID)");
                                cheques--;
                            }
                            else{
                                title.setText("Cheque " + (i + 1));
                            }
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
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
}