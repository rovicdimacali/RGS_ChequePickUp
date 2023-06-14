package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import SessionPackage.LocationManagement;
import SessionPackage.LocationSession;
import SessionPackage.ReceiptManagement;
import SessionPackage.ReceiptSession;
import SessionPackage.scenarioManagement;

public class OfficialReceipt extends AppCompatActivity {

    DatePickerDialog datePickerDialog;
    CardView card_spinner;
    Spinner spinner;
    TextView back_button, checkTitle;
    //FIRST INPUTS
    EditText cheq_num, cheq_amount, compname, compadd, tin, payee, bcode, ornum;
    //ADDITIONAL INPUTS
    Button submit_btn, addBtn, datepicker, date, confirm_btn;
    String remark;
    LinearLayout Llayout_num, Llayout_am, layout;
    LinearLayout datefield;
    String cheqResNum, cheqResAm;
    String payeeList[];
    int count = 1;
    int delete = 0;
    ArrayList<LinearLayout> chkNums = new ArrayList<>();
    ArrayList<LinearLayout> chkAmounts = new ArrayList<>();
    ArrayList<EditText> chkNumList = new ArrayList<>();
    ArrayList<EditText> chkAmList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_receipt);

        ReceiptManagement rm = new ReceiptManagement(OfficialReceipt.this);
        LocationManagement lm = new LocationManagement(OfficialReceipt.this);
        scenarioManagement sm = new scenarioManagement(OfficialReceipt.this);

        remark = sm.getScene();

        card_spinner = (CardView) findViewById(R.id.cardView_spinner);
        spinner = (Spinner) findViewById(R.id.spinner);
        addBtn = (Button) findViewById(R.id.addCheque_button);
        //Llayout_num = (LinearLayout) findViewById(R.id.chknumText);
        //Llayout_am = (LinearLayout) findViewById(R.id.chkamountText);
        checkTitle = (TextView) findViewById(R.id.checkTitle);

        compname = (EditText) findViewById(R.id.inputcompany);
        //compadd = (EditText) findViewById(R.id.inputaddress);
        tin = (EditText) findViewById(R.id.inputtin);
        //bcode = (EditText) findViewById(R.id.inputBC);
        ornum = (EditText) findViewById(R.id.inputOR);
        //date = (Button) findViewById(R.id.datePickerButton);
        cheq_amount = (EditText) findViewById(R.id.inputChequeAmount);
        //cheq_num = (EditText) findViewById(R.id.inputchequenumber);
        //payee = (EditText) findViewById(R.id.inputpayee);

        /*if(!(rm.getTin().isEmpty())){
            tin.setText(rm.getTin());
        }*/

        compname.setText(lm.getComp());
        compadd.setText(lm.getAdd());

        payeeList = new String[]{"---PAYEE---","Globe Telecom Inc.", "Innove Communications Inc.", "Bayan Communications Inc."}; //PAYEE LIST
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(OfficialReceipt.this, R.layout.simple_spinner_item, payeeList);
        adapter.setDropDownViewResource(R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        if(remark.equals("One Account, Multiple Cheques") || remark.equals("Multiple Accounts, Multiple Cheques"))
        {
           checkTitle.setText("Cheque 1");
        }
        else{
            checkTitle.setText("Cheque");
        }
        back_button = (TextView) findViewById(R.id.back_button);
        confirm_btn = (Button) findViewById(R.id.confirm_button);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        datefield = (LinearLayout) findViewById(R.id.date_field);
        datepicker = (Button) findViewById(R.id.datePickerButton);
        datepicker.setText(getTodayDate());
        initDatePicker();

        back_button.setTypeface(font);
        back_button.setText("\uf060");

        if (remark.equals("One Check, One Account") || remark.equals("One Cheque, Multiple Accounts")) {
            addBtn.setVisibility(View.GONE);
            addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_gray1));
        }

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rm.removeReceipt();
                openEsignature();
            }
        });

        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String service = parent.getItemAtPosition(position).toString();
                //submit_btn = (Button) findViewById(R.id.submit_button);

                submit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    /*if (remark.equals("Multiple Accounts, Multiple Cheques") || remark.equals("One Account, Multiple Cheques")) {
                        //STRING FOR CHEQUE NUM RESULTS
                        /cheqResNum = "chk1_num:" + cheq_num.getText().toString() + ",chk2_num:" + newCheqNum1.getText().toString() + ",chk3_num:" + newCheqNum2.getText().toString() +
                                ",chk4_num:" + newCheqNum3.getText().toString() + ",chk5_num:" + newCheqNum4.getText().toString();
                        //STRING FOR CHEQUE AMOUNT RESULTS
                        cheqResAm = "chk1_amount:" + cheq_amount.getText().toString() + ",chk2_amount:" + newCheqAm1.getText().toString() + ",chk3_amount:" + newCheqAm2.getText().toString() +
                                ",chk4_amount:" + newCheqAm3.getText().toString() + ",chk5_amount:" + newCheqAm4.getText().toString();
                    } else {
                        cheqResNum = cheq_num.getText().toString();
                        cheqResAm = cheq_amount.getText().toString();
                    }*/
                       ReceiptSession rs = new ReceiptSession(String.valueOf(tin.getText()), cheq_amount.getText().toString().replace(",", ""), cheq_num.getText().toString().replace(",", ""), service, bcode.getText().toString(),
                                ornum.getText().toString(), date.getText().toString());
                       rm.saveReceipt(rs);
                        //Toast.makeText(OfficialReceipt.this, "Transaction Completed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OfficialReceipt.this, ChequeReceived.class);
                        startActivity(intent);
                        finish();
                    }
                });

                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ReceiptSession rs = new ReceiptSession(String.valueOf(tin.getText()), cheq_amount.getText().toString().replace(",", ""), cheq_num.getText().toString().replace(",", ""), service, bcode.getText().toString(),
                                ornum.getText().toString(), date.getText().toString());
                        rm.saveReceipt(rs);

                        Intent intent = new Intent(OfficialReceipt.this, OfficialReceipt2.class);

                        startActivity(intent);
                        finish();
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void openEsignature() {
        Intent intent = new Intent(this, ESignature.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }



    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day, month, year);
    }
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                datepicker.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month,day);
    }
    private String makeDateString(int day, int month, int year){
        return getMonthFormat(month) + " " + day + " " + year;
    }
    private String getMonthFormat(int month) {
        if (month == 1){
            return "JAN";
        }
        else if (month == 2){
            return "FEB";
        }
        else if (month == 3){
            return "MAR";
        }
        else if (month == 4){
            return "APR";
        }
        else if (month == 5){
            return "MAY";
        }
        else if (month == 6){
            return "JUN";
        }
        else if (month == 7){
            return "JUL";
        }
        else if (month == 8){
            return "AUG";
        }
        else if (month == 9){
            return "SEP";
        }
        else if (month == 10){
            return "OCT";
        }
        else if (month == 11){
            return "NOV";
        }
        else if (month == 12){
            return "DEC";
        }
        return "JAN";
    }
}