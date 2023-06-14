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
import android.widget.CheckBox;
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

    //INPUTS
    EditText comp1, comp2, comp3, comp4, comp5, comp6;
    EditText tin1, tin2, tin3, tin4, tin5, tin6;
    EditText acc1, acc2, acc3, acc4, acc5, acc6;
    Spinner pay1, pay2, pay3, pay4, pay5, pay6;
    EditText or1, or2, or3, or4, or5, or6;
    EditText am1, am2, am3, am4, am5, am6;
    Button capt1, capt2, capt3, capt4, capt5, capt6;
    Button del2, del3, del4, del5, del6;
    LinearLayout l1, l2, l3, l4, l5, l6;
    CardView card1, card2, card3, card4, card5, card6;
    ImageView chk1, chk2, chk3, chk4, chk5, chk6;
    TextView accT1, accT2, accT3, accT4, accT5, accT6;
    TextView chktitle2, chktitle3, chktitle4, chktitle5, chktitle6;
    CheckBox multAcc;
    //--
    DatePickerDialog datePickerDialog;

    TextView back_button, checkTitle;
    Button addBtn, datepicker, date, confirm_btn;
    String remark;
    String payeeList[];
    int count = 1;
    int delete = 0;
    ArrayList<LinearLayout> deleted = new ArrayList<>();
    ArrayList<ImageView> iv = new ArrayList<>();
    ArrayList<TextView> tv = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_receipt);

        ReceiptManagement rm = new ReceiptManagement(OfficialReceipt.this);
        LocationManagement lm = new LocationManagement(OfficialReceipt.this);
        scenarioManagement sm = new scenarioManagement(OfficialReceipt.this);

        remark = sm.getScene();

        addBtn = (Button) findViewById(R.id.addCheque_button);
        multAcc = (CheckBox) findViewById(R.id.checkbox_accnum);

        //INPUT 1
        l1 = (LinearLayout) findViewById(R.id.form1);
        card1 = (CardView) findViewById(R.id.cardView_spinner);
        capt1 = (Button) findViewById(R.id.capture_button1);
        chk1 = (ImageView) findViewById(R.id.cheqepic1);
        comp1 = (EditText) findViewById(R.id.inputcompany);
        tin1 = (EditText) findViewById(R.id.inputtin);
        or1 = (EditText) findViewById(R.id.inputOR);
        acc1 = (EditText) findViewById(R.id.inputacc);
        am1 = (EditText) findViewById(R.id.inputChequeAmount);
        pay1 = (Spinner) findViewById(R.id.spinner);
        accT1 = (TextView) findViewById(R.id.accNumber);

        //INPUT 2
        l2 = (LinearLayout) findViewById(R.id.form2);
        card2 = (CardView) findViewById(R.id.cardView_spinner2);
        capt2 = (Button) findViewById(R.id.capture_button2);
        chk2 = (ImageView) findViewById(R.id.cheqepic2);
        comp2 = (EditText) findViewById(R.id.inputcompany2);
        tin2 = (EditText) findViewById(R.id.inputtin2);
        or2 = (EditText) findViewById(R.id.inputOR2);
        acc2 = (EditText) findViewById(R.id.inputacc2);
        am2 = (EditText) findViewById(R.id.inputChequeAmount2);
        pay2 = (Spinner) findViewById(R.id.spinner2);
        accT2 = (TextView) findViewById(R.id.accNumber2);
        del2 = (Button) findViewById(R.id.delete_button2);

        //INPUT 3
        l3 = (LinearLayout) findViewById(R.id.form3);
        card3 = (CardView) findViewById(R.id.cardView_spinner3);
        capt3 = (Button) findViewById(R.id.capture_button3);
        chk3 = (ImageView) findViewById(R.id.cheqepic3);
        comp3 = (EditText) findViewById(R.id.inputcompany3);
        tin3 = (EditText) findViewById(R.id.inputtin3);
        or3 = (EditText) findViewById(R.id.inputOR3);
        acc3 = (EditText) findViewById(R.id.inputacc3);
        am3 = (EditText) findViewById(R.id.inputChequeAmount3);
        pay3 = (Spinner) findViewById(R.id.spinner3);
        accT3 = (TextView) findViewById(R.id.accNumber3);
        del3 = (Button) findViewById(R.id.delete_button3) ;

        //INPUT 4
        l4 = (LinearLayout) findViewById(R.id.form4);
        card4 = (CardView) findViewById(R.id.cardView_spinner4);
        capt4= (Button) findViewById(R.id.capture_button4);
        chk4 = (ImageView) findViewById(R.id.cheqepic4);
        comp4 = (EditText) findViewById(R.id.inputcompany4);
        tin4 = (EditText) findViewById(R.id.inputtin4);
        or4 = (EditText) findViewById(R.id.inputOR4);
        acc4 = (EditText) findViewById(R.id.inputacc4);
        am4 = (EditText) findViewById(R.id.inputChequeAmount4);
        pay4 = (Spinner) findViewById(R.id.spinner4);
        accT4 = (TextView) findViewById(R.id.accNumber4);
        del4 = (Button) findViewById(R.id.delete_button4) ;

        //INPUT 5
        l5 = (LinearLayout) findViewById(R.id.form5);
        card5 = (CardView) findViewById(R.id.cardView_spinner5);
        capt5= (Button) findViewById(R.id.capture_button5);
        chk5 = (ImageView) findViewById(R.id.cheqepic5);
        comp5 = (EditText) findViewById(R.id.inputcompany5);
        tin5 = (EditText) findViewById(R.id.inputtin5);
        or5 = (EditText) findViewById(R.id.inputOR5);
        acc5 = (EditText) findViewById(R.id.inputacc5);
        am5 = (EditText) findViewById(R.id.inputChequeAmount5);
        pay5 = (Spinner) findViewById(R.id.spinner5);
        accT5 = (TextView) findViewById(R.id.accNumber5);
        del5 = (Button) findViewById(R.id.delete_button5) ;

        //INPUT 6
        l6 = (LinearLayout) findViewById(R.id.form6);
        card6 = (CardView) findViewById(R.id.cardView_spinner6);
        capt6= (Button) findViewById(R.id.capture_button6);
        chk6 = (ImageView) findViewById(R.id.cheqepic6);
        comp6 = (EditText) findViewById(R.id.inputcompany6);
        tin6 = (EditText) findViewById(R.id.inputtin6);
        or6 = (EditText) findViewById(R.id.inputOR6);
        acc6 = (EditText) findViewById(R.id.inputacc6);
        am6 = (EditText) findViewById(R.id.inputChequeAmount6);
        pay6 = (Spinner) findViewById(R.id.spinner6);
        accT6 = (TextView) findViewById(R.id.accNumber6);
        del6 = (Button) findViewById(R.id.delete_button6) ;

        comp1.setText(lm.getComp());
        comp2.setText(lm.getComp());
        comp3.setText(lm.getComp());
        comp4.setText(lm.getComp());
        comp5.setText(lm.getComp());
        comp6.setText(lm.getComp());

        payeeList = new String[]{"---PAYEE---","Globe Telecom Inc.", "Innove Communications Inc.", "Bayan Communications Inc."}; //PAYEE LIST
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(OfficialReceipt.this, R.layout.simple_spinner_item, payeeList);
        adapter.setDropDownViewResource(R.layout.simple_spinner_item);
        pay1.setAdapter(adapter);
        pay2.setAdapter(adapter);
        pay3.setAdapter(adapter);
        pay4.setAdapter(adapter);
        pay5.setAdapter(adapter);
        pay6.setAdapter(adapter);

        back_button = (TextView) findViewById(R.id.back_button);
        confirm_btn = (Button) findViewById(R.id.confirm_button);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        back_button.setTypeface(font);
        back_button.setText("\uf060");

        if (remark.equals("One Check, One Account") || remark.equals("One Cheque, Multiple Accounts")) {
            addBtn.setVisibility(View.GONE);
            addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_gray1));
        }

        CompoundButton.OnCheckedChangeListener cbl = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(multAcc.isChecked()){
                    acc1.setVisibility(View.GONE);
                    acc2.setVisibility(View.GONE);
                    acc3.setVisibility(View.GONE);
                    acc4.setVisibility(View.GONE);
                    acc5.setVisibility(View.GONE);
                    acc6.setVisibility(View.GONE);

                    acc1.setText("");
                    acc2.setText("");
                    acc3.setText("");
                    acc4.setText("");
                    acc5.setText("");;
                    acc6.setText("");

                    accT1.setVisibility(View.GONE);
                    accT2.setVisibility(View.GONE);
                    accT3.setVisibility(View.GONE);
                    accT4.setVisibility(View.GONE);
                    accT5.setVisibility(View.GONE);
                    accT6.setVisibility(View.GONE);
                }
                else if(!(multAcc.isChecked())){
                    acc1.setVisibility(View.VISIBLE);
                    acc2.setVisibility(View.VISIBLE);
                    acc3.setVisibility(View.VISIBLE);
                    acc4.setVisibility(View.VISIBLE);
                    acc5.setVisibility(View.VISIBLE);
                    acc6.setVisibility(View.VISIBLE);

                    accT1.setVisibility(View.VISIBLE);
                    accT2.setVisibility(View.VISIBLE);
                    accT3.setVisibility(View.VISIBLE);
                    accT4.setVisibility(View.VISIBLE);
                    accT5.setVisibility(View.VISIBLE);
                    accT6.setVisibility(View.VISIBLE);
                }
            }
        };

        multAcc.setOnCheckedChangeListener(cbl);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rm.removeReceipt();
                openChecklist();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deleted.size() == 0){
                    if(count == 1){
                        l2.setVisibility(View.VISIBLE);
                        tin2.setText(tin1.getText().toString());
                        //del2.setVisibility(View.VISIBLE);
                        count++;
                    }
                    else if(count == 2){
                        l3.setVisibility(View.VISIBLE);
                        tin3.setText(tin1.getText().toString());
                        //del3.setVisibility(View.VISIBLE);
                        count++;
                    }
                    else if(count == 3){
                        l4.setVisibility(View.VISIBLE);
                        tin4.setText(tin1.getText().toString());
                        //del4.setVisibility(View.VISIBLE);
                        count++;
                    }
                    else if(count == 4){
                        l5.setVisibility(View.VISIBLE);
                        tin5.setText(tin1.getText().toString());
                        //del5.setVisibility(View.VISIBLE);
                        count++;
                    }
                    else if(count == 5){
                        l6.setVisibility(View.VISIBLE);
                        tin6.setText(tin1.getText().toString());
                        //del6.setVisibility(View.VISIBLE);
                        addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_gray1));
                        addBtn.setActivated(false);
                        //count++;
                    }
                }
                else if(deleted.size() > 0){
                    deleted.get(0).setVisibility(View.VISIBLE);
                    tv.get(0).setText(tin1.getText().toString());
                    deleted.remove(0);
                    tv.remove(0);
                    count++;
                    if(count == 5){
                        addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_gray1));
                        addBtn.setEnabled(false);
                    }
                }
            }
        });

        del2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l2.setVisibility(View.GONE);
                pay2.setSelection(0);
                tin2.setText("");
                acc2.setText("");
                or2.setText("");
                am2.setText("");
                deleted.add(l2);
                tv.add(tin2);
                if(count == 5){
                    addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_green));
                    addBtn.setEnabled(true);
                }
                count--;
            }
        });
        del3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l3.setVisibility(View.GONE);
                pay3.setSelection(0);
                tin3.setText("");
                acc3.setText("");
                or3.setText("");
                am3.setText("");
                deleted.add(l3);
                tv.add(tin3);
                if(count == 5){
                    addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_green));
                    addBtn.setEnabled(true);
                }
                count--;
            }
        });
        del4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l4.setVisibility(View.GONE);
                pay4.setSelection(0);
                tin4.setText("");
                acc4.setText("");
                or4.setText("");
                am4.setText("");
                deleted.add(l4);
                tv.add(tin4);
                if(count == 5){
                    addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_green));
                    addBtn.setEnabled(true);
                }
                count--;
            }
        });
        del5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l5.setVisibility(View.GONE);
                pay5.setSelection(0);
                tin5.setText("");
                or5.setText("");
                acc5.setText("");
                am5.setText("");
                deleted.add(l5);
                tv.add(tin5);
                if(count == 5){
                    addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_green));
                    addBtn.setEnabled(true);
                }
                count--;
            }
        });
        del6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l6.setVisibility(View.GONE);
                pay6.setSelection(0);
                tin6.setText("");
                acc6.setText("");
                or6.setText("");
                am6.setText("");
                deleted.add(l6);
                tv.add(tin6);
                if(count == 5){
                    addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_green));
                    addBtn.setEnabled(true);
                }
                count--;
            }
        });


        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] accno = {acc1.getText().toString(), acc2.getText().toString(),acc3.getText().toString(),
                        acc4.getText().toString(),acc5.getText().toString(),acc6.getText().toString()};
                String[] pay = {pay1.getSelectedItem().toString(), pay2.getSelectedItem().toString(),
                        pay3.getSelectedItem().toString(), pay4.getSelectedItem().toString(),
                        pay5.getSelectedItem().toString(),pay6.getSelectedItem().toString()};
                String[] ornum = {or1.getText().toString(), or2.getText().toString(), or3.getText().toString(),
                        or4.getText().toString(), or5.getText().toString(), or6.getText().toString()};
                String[] amount = {am1.getText().toString(), am2.getText().toString(), am3.getText().toString(),
                        am4.getText().toString(), am5.getText().toString(), am6.getText().toString()};
            }
        });
    }

    public void openChecklist() {
        Intent intent = new Intent(this, CheckList.class);
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