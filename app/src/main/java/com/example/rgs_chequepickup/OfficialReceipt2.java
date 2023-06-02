package com.example.rgs_chequepickup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import SessionPackage.LocationManagement;
import SessionPackage.ReceiptManagement;
import SessionPackage.ReceiptSession;
import SessionPackage.scenarioManagement;

public class OfficialReceipt2 extends AppCompatActivity {

    DatePickerDialog datePickerDialog;
    CardView card_spinner;
    String payeeList[];
    Spinner spinner;
    TextView back_button;
    //FIRST INPUTS
    EditText cheq_num, cheq_amount, compname, compadd, tin, payee, bcode, ornum;
    //ADDITIONAL INPUTS
    Button submit_btn, addBtn, datepicker, date;
    String remark;
    String numList, amountList, bcodeList, serviceList, ornumList, dateList;
    String intentNum, intentAm, intentBcode, intentServe, intentOR, intentDate;
    LinearLayout Llayout_num, Llayout_am;
    LinearLayout datefield;
    String cheqResNum, cheqResAm;

    int count = 1;
    int delete = 0;
    ArrayList<LinearLayout> chkNums = new ArrayList<>();
    ArrayList<LinearLayout> chkAmounts = new ArrayList<>();
    ArrayList<EditText> chkNumList = new ArrayList<>();
    ArrayList<EditText> chkAmList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_receipt2);

        ReceiptManagement rm = new ReceiptManagement(OfficialReceipt2.this);
        LocationManagement lm = new LocationManagement(OfficialReceipt2.this);
        scenarioManagement sm = new scenarioManagement(OfficialReceipt2.this);

        remark = sm.getScene();

        card_spinner = (CardView) findViewById(R.id.cardView_spinner);
        spinner = (Spinner) findViewById(R.id.spinner);
        addBtn = (Button) findViewById(R.id.addCheque_button);
        Llayout_num = (LinearLayout) findViewById(R.id.chknumText);
        Llayout_am = (LinearLayout) findViewById(R.id.chkamountText);

        compname = (EditText) findViewById(R.id.inputcompany);
        compadd = (EditText) findViewById(R.id.inputaddress);
        tin = (EditText) findViewById(R.id.inputtin);
        bcode = (EditText) findViewById(R.id.inputBC);
        ornum = (EditText) findViewById(R.id.inputOR);
        date = (Button) findViewById(R.id.datePickerButton);
        cheq_amount = (EditText) findViewById(R.id.inputchequeamount);
        cheq_num = (EditText) findViewById(R.id.inputchequenumber);
        //payee = (EditText) findViewById(R.id.inputpayee);

        tin.setText(rm.getTin());
        //bcode.setText(rm.getBcode());
        //payee.setText(rm.getPayee());
        /*if(!(rm.getTin().isEmpty())){
            tin.setText(rm.getTin());
        }*/

        compname.setText(lm.getComp());
        compadd.setText(lm.getAdd());

        payeeList = new String[]{"Globe Telecom Services", "Innove Services", "Bayan Services"}; //PAYEE LIST
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(OfficialReceipt2.this, R.layout.simple_spinner_item, payeeList);
        adapter.setDropDownViewResource(R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        /*if(remark.equals("One Account, Multiple Cheques") || remark.equals("Multiple Accounts, Multiple Cheques"))
        {
            cheq_num.setActivated(false);
            cheq_amount.setActivated(false);

            cheq_num.setText("Multiple Cheques");
            cheq_amount.setText("Multiple Cheques");
        }*/
        back_button = (TextView) findViewById(R.id.back_button);
        submit_btn = (Button) findViewById(R.id.submit_button);

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
                ReceiptManagement rm = new ReceiptManagement(OfficialReceipt2.this);
                String bamount = rm.getAmount();
                String bnumber = rm.getNumber();
                String bpayee = rm.getPayee();
                String bbcode = rm.getBcode();
                String bornum = rm.getOR();
                String bdate = rm.getDate();

                Log.d("PrevAmountExplode", "Amount: " + bamount);
                Log.d("PrevNumExplode", "Number: " + bnumber);
                Log.d("PrevPayExplode", "Payee: " + bpayee);
                Log.d("PrevBCExplode", "Bcode: " + bbcode);
                Log.d("PrevORExplode", "OR: " + bornum);
                Log.d("PrevDateExplode", "Date: " + bdate);

                String[] explodeAmount = bamount.split(",");
                String[] explodeNumber = bnumber.split(",");
                String[] explodePayee = bpayee.split(",");
                String[] explodeBcode = bbcode.split(",");
                String[] explodeOrnum = bornum.split(",");
                String[] explodeDate = bdate.split(",");

                bamount = "";
                bnumber = "";
                bpayee = "";
                bbcode = "";
                bornum = "";
                bdate = "";

                int size = explodeAmount.length - 1;
                for(int i = 0; i < size; i++){
                    bamount += explodeAmount[i] + ",";
                    bnumber += explodeNumber[i] + ",";
                    bpayee += explodePayee[i] + ",";
                    bbcode += explodeBcode[i] + ",";
                    bornum += explodeOrnum[i] + ",";
                    bdate += explodeDate[i] + ",";
                }
                bamount = bamount.substring(0, bamount.length() - 1);
                bnumber = bnumber.substring(0, bnumber.length() - 1);
                bpayee = bpayee.substring(0, bpayee.length() - 1);
                bbcode = bbcode.substring(0, bbcode.length() - 1);
                bornum = bornum.substring(0, bornum.length() - 1);
                bdate = bdate.substring(0, bdate.length() - 1);

                ReceiptSession rs = new ReceiptSession(String.valueOf(tin.getText()), bamount, bnumber, bpayee, bbcode,
                        bornum, bdate);
                rm.saveReceipt(rs);

                Log.d("sampleAmountExplode", "Exploded Amount: " + bamount);
                Log.d("sampleNumExplode", "Exploded Number: " + bnumber);
                Log.d("samplePayExplode", "Exploded Payee: " + bpayee);
                Log.d("sampleBCExplode", "Exploded Bcode: " + bbcode);
                Log.d("sampleORExplode", "Exploded OR: " + bornum);
                Log.d("sampleDateExplode", "Exploded Date: " + bdate);
                Intent intent = new Intent(OfficialReceipt2.this, OfficialReceipt.class);
                startActivity(intent);
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
                Intent or = getIntent();
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
                        ReceiptManagement rm = new ReceiptManagement(OfficialReceipt2.this);
                        amountList = rm.getAmount() + "," + cheq_amount.getText().toString().replace(",","");
                        numList = rm.getNumber() + "," + cheq_num.getText().toString().replace(",","");;
                        serviceList = rm.getPayee() + "," + service;
                        bcodeList = rm.getBcode() + "," + bcode.getText().toString();
                        ornumList = rm.getOR() + "," + ornum.getText().toString();
                        dateList = rm.getDate() + "," + date.getText().toString();

                        ReceiptSession rs = new ReceiptSession(String.valueOf(tin.getText()), amountList, numList, serviceList, bcodeList,
                                ornumList, dateList);
                        rm.saveReceipt(rs);
                        //Toast.makeText(OfficialReceipt.this, "Transaction Completed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OfficialReceipt2.this, ChequeReceived.class);
                        startActivity(intent);
                        finish();
                    }
                });

                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ReceiptManagement rm = new ReceiptManagement(OfficialReceipt2.this);
                        amountList = rm.getAmount() + "," + cheq_amount.getText().toString().replace(",","");
                        numList = rm.getNumber() + "," + cheq_num.getText().toString().replace(",","");;
                        serviceList = rm.getPayee() + "," + service;
                        bcodeList = rm.getBcode() + "," + bcode.getText().toString();
                        ornumList = rm.getOR() + "," + ornum.getText().toString();
                        dateList = rm.getDate() + "," + date.getText().toString();

                        ReceiptSession rs = new ReceiptSession(String.valueOf(tin.getText()), amountList, numList, serviceList, bcodeList,
                                ornumList, dateList);
                        rm.saveReceipt(rs);

                        Intent intent = new Intent(OfficialReceipt2.this, OfficialReceipt3.class);

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

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1) {
            return "JAN";
        } else if (month == 2) {
            return "FEB";
        } else if (month == 3) {
            return "MAR";
        } else if (month == 4) {
            return "APR";
        } else if (month == 5) {
            return "MAY";
        } else if (month == 6) {
            return "JUN";
        } else if (month == 7) {
            return "JUL";
        } else if (month == 8) {
            return "AUG";
        } else if (month == 9) {
            return "SEP";
        } else if (month == 10) {
            return "OCT";
        } else if (month == 11) {
            return "NOV";
        } else if (month == 12) {
            return "DEC";
        }
        return "JAN";
    }
}