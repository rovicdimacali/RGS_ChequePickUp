package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import SessionPackage.LocationManagement;
import SessionPackage.LocationSession;
import SessionPackage.ReceiptManagement;
import SessionPackage.ReceiptSession;
import SessionPackage.scenarioManagement;

public class OfficialReceipt extends AppCompatActivity {

    TextView back_button;
    //FIRST INPUTS
    EditText cheq_num, cheq_amount, compname, compadd, tin, payee;
    //ADDITIONAL INPUTS
    EditText newCheqNum1, newCheqNum2, newCheqNum3, newCheqNum4;
    LinearLayout layNum1, layNum2, layNum3, layNum4;
    EditText newCheqAm1, newCheqAm2, newCheqAm3, newCheqAm4;
    LinearLayout layAm1, layAm2, layAm3, layAm4;
    //DELETE BUTTONS
    ImageView deleteNum1, deleteNum2, deleteNum3, deleteNum4;
    ImageView deleteAm1, deleteAm2, deleteAm3, deleteAm4;
    Button submit_btn, addBtn;
    String remark;
    LinearLayout Llayout_num, Llayout_am;

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
        setContentView(R.layout.activity_official_receipt);

        ReceiptManagement rm = new ReceiptManagement(OfficialReceipt.this);
        LocationManagement lm = new LocationManagement(OfficialReceipt.this);
        scenarioManagement sm = new scenarioManagement(OfficialReceipt.this);

        remark = sm.getScene();

        //ADDITIONAL CHECK AMOUNT
        layAm1 = (LinearLayout)  findViewById(R.id.layout_amount1);
        layAm2 = (LinearLayout)  findViewById(R.id.layout_amount2);
        layAm3 = (LinearLayout)  findViewById(R.id.layout_amount3);
        layAm4 = (LinearLayout)  findViewById(R.id.layout_amount4);

        newCheqAm1 = (EditText) findViewById(R.id.inputchequeamount1);
        newCheqAm2 = (EditText) findViewById(R.id.inputchequeamount2);
        newCheqAm3 = (EditText) findViewById(R.id.inputchequeamount3);
        newCheqAm4 = (EditText) findViewById(R.id.inputchequeamount4);

        deleteAm1 = (ImageView) findViewById(R.id.delete_amount1);
        deleteAm2 = (ImageView) findViewById(R.id.delete_amount2);
        deleteAm3 = (ImageView) findViewById(R.id.delete_amount3);
        deleteAm4 = (ImageView) findViewById(R.id.delete_amount4);

        //ADDITIONAL CHECK NUMBER
        layNum1 = (LinearLayout)  findViewById(R.id.layout_number1);
        layNum2 = (LinearLayout)  findViewById(R.id.layout_number2);
        layNum3 = (LinearLayout)  findViewById(R.id.layout_number3);
        layNum4 = (LinearLayout)  findViewById(R.id.layout_number4);

        newCheqNum1 = (EditText) findViewById(R.id.inputchequenumber1);
        newCheqNum2 = (EditText) findViewById(R.id.inputchequenumber2);
        newCheqNum3 = (EditText) findViewById(R.id.inputchequenumber3);
        newCheqNum4 = (EditText) findViewById(R.id.inputchequenumber4);

        deleteNum1 = (ImageView) findViewById(R.id.delete_number1);
        deleteNum2 = (ImageView) findViewById(R.id.delete_number2);
        deleteNum3 = (ImageView) findViewById(R.id.delete_number3);
        deleteNum4 = (ImageView) findViewById(R.id.delete_number4);

        addBtn = (Button) findViewById(R.id.addCheque_button);
        Llayout_num = (LinearLayout) findViewById(R.id.chknumText);
        Llayout_am = (LinearLayout) findViewById(R.id.chkamountText);

        compname = (EditText) findViewById(R.id.inputcompany);
        compadd = (EditText) findViewById(R.id.inputaddress);
        tin = (EditText) findViewById(R.id.inputtin);
        cheq_amount = (EditText) findViewById(R.id.inputchequeamount);
        cheq_num = (EditText) findViewById(R.id.inputchequenumber);
        payee = (EditText) findViewById(R.id.inputpayee);

        /*if(!(rm.getTin().isEmpty())){
            tin.setText(rm.getTin());
        }*/

        compname.setText(lm.getComp());
        compadd.setText(lm.getAdd());

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

        back_button.setTypeface(font);
        back_button.setText("\uf060");

        if(remark.equals("One Check, One Account")){
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

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(remark.equals("Multiple Accounts, Multiple Cheques") || remark.equals("One Account, Multiple Cheques")){
                    //STRING FOR CHEQUE NUM RESULTS
                    cheqResNum = "chk1_num:" + cheq_num.getText().toString() + ",chk2_num:" + newCheqNum1.getText().toString() + ",chk3_num:" + newCheqNum2.getText().toString() +
                            ",chk4_num:" + newCheqNum3.getText().toString() + ",chk5_num:" + newCheqNum4.getText().toString();
                    //STRING FOR CHEQUE AMOUNT RESULTS
                    cheqResAm = "chk1_amount:" + cheq_amount.getText().toString() + ",chk2_amount:" + newCheqAm1.getText().toString() + ",chk3_amount:" + newCheqAm2.getText().toString() +
                            ",chk4_amount:" + newCheqAm3.getText().toString() + ",chk5_amount:" + newCheqAm4.getText().toString();
                }
                else{
                    cheqResNum = cheq_num.getText().toString();
                    cheqResAm = cheq_amount.getText().toString();
                }
                ReceiptSession rs = new ReceiptSession(String.valueOf(tin.getText()), cheqResAm, cheqResNum, String.valueOf(payee.getText()));
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
                /*if(count < 4){ // IF COUNT IS LESS THAN 5, ADD CHECK
                    EditText etNum = new EditText(OfficialReceipt.this);

                    LinearLayout.LayoutParams ln = new LinearLayout.LayoutParams(1050,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

                    ln.setMargins(80, 30,0,0);
                    etNum.setBackgroundColor(Integer.parseInt(String.valueOf(Color.parseColor("#F2F2F2"))));
                    etNum.setLayoutParams(ln);
                    etNum.setHint("Cheque Number");
                    etNum.setTextSize(15);
                    etNum.setInputType(InputType.TYPE_CLASS_NUMBER);
                    etNum.setPadding(54,60,20,60);

                    Llayout_num.addView(etNum);

                    EditText etAmount = new EditText(OfficialReceipt.this);

                    LinearLayout.LayoutParams la = new LinearLayout.LayoutParams(1050,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

                    la.setMargins(80, 30,0,0);
                    etAmount.setBackgroundColor(Integer.parseInt(String.valueOf(Color.parseColor("#F2F2F2"))));
                    etAmount.setLayoutParams(la);
                    etAmount.setHint("Cheque Amount");
                    etAmount.setTextSize(15);
                    etAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
                    etAmount.setPadding(54,60,20,60);

                    Llayout_am.addView(etAmount);

                    chkNums.add(etNum);
                    chkAmounts.add(etNum);

                    count++;
                    if(count >= 4)
                    {
                        addBtn.setText("Reset");
                        addBtn.setActivated(false);
                        addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_gray1));
                    }
                }
                else if(count >= 3){ // ELSE, DISABLE BUTTON
                    //Toast.makeText(OfficialReceipt.this, "Maximum of 5 Cheques Only", Toast.LENGTH_SHORT).show();
                    recreate();
               }*/
                if(count < 5 && delete == 0){
                    if(count == 1){
                        layNum1.setVisibility(View.VISIBLE);
                        layAm1.setVisibility(View.VISIBLE);
                        chkNumList.add(newCheqNum1);
                        chkAmList.add(newCheqAm1);
                        addBtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_secondary));
                        count++;
                    }
                    else if(count == 2){
                        layNum2.setVisibility(View.VISIBLE);
                        layAm2.setVisibility(View.VISIBLE);
                        chkNumList.add(newCheqNum2);
                        chkAmList.add(newCheqAm2);
                        addBtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_secondary));
                        count++;
                    }
                    else if(count == 3){
                        layNum3.setVisibility(View.VISIBLE);
                        layAm3.setVisibility(View.VISIBLE);
                        chkNumList.add(newCheqNum3);
                        chkAmList.add(newCheqAm3);
                        addBtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_secondary));
                        count++;
                    }
                    else if(count == 4){
                        layNum4.setVisibility(View.VISIBLE);
                        layAm4.setVisibility(View.VISIBLE);
                        chkNumList.add(newCheqNum4);
                        chkAmList.add(newCheqAm4);
                        addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_gray1));
                        //count++;
                    }
                }
                else if(count < 5 && delete > 1){
                    if(!(chkNums.isEmpty() && chkAmounts.isEmpty())){
                        chkNums.get(0).setVisibility(View.VISIBLE);
                        chkAmounts.get(0).setVisibility(View.VISIBLE);
                        chkNums.remove(0);
                        chkAmounts.remove(0);
                        addBtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_secondary));
                        delete--;
                        count++;
                    }
                }
                else if(count < 5 && delete == 1){
                    if(!(chkNums.isEmpty() && chkAmounts.isEmpty())){
                        chkNums.get(0).setVisibility(View.VISIBLE);
                        chkAmounts.get(0).setVisibility(View.VISIBLE);
                        chkNums.remove(0);
                        chkAmounts.remove(0);
                        addBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_gray1));
                        delete--;
                        count++;
                    }
                }
            }
        });

        //DELETE BUTTONS
        deleteNum1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layNum1.setVisibility(View.GONE);
                layAm1.setVisibility(View.GONE);
                chkNums.add(layNum1);
                chkAmounts.add(layAm1);
                chkNumList.remove(newCheqNum1);
                chkAmList.remove(newCheqAm1);
                delete++;
                count--;
                addBtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_secondary));
            }
        });
        deleteAm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layNum1.setVisibility(View.GONE);
                layAm1.setVisibility(View.GONE);
                chkNums.add(layNum1);
                chkAmounts.add(layAm1);
                chkNumList.remove(newCheqNum1);
                chkAmList.remove(newCheqAm1);
                delete++;
                count--;
                addBtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_secondary));
            }
        });
        deleteNum2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layNum2.setVisibility(View.GONE);
                layAm2.setVisibility(View.GONE);
                chkNums.add(layNum2);
                chkAmounts.add(layAm2);
                chkNumList.remove(newCheqNum2);
                chkAmList.remove(newCheqAm2);
                delete++;
                count--;
                addBtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_secondary));
            }
        });
        deleteAm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layNum2.setVisibility(View.GONE);
                layAm2.setVisibility(View.GONE);
                chkNums.add(layNum2);
                chkAmounts.add(layAm2);
                chkNumList.remove(newCheqNum2);
                chkAmList.remove(newCheqAm2);
                delete++;
                count--;
                addBtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_secondary));
            }
        });
        deleteNum3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layNum3.setVisibility(View.GONE);
                layAm3.setVisibility(View.GONE);
                chkNums.add(layNum3);
                chkAmounts.add(layAm3);
                chkNumList.remove(newCheqNum3);
                chkAmList.remove(newCheqAm3);
                delete++;
                count--;
                addBtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_secondary));
            }
        });
        deleteAm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layNum3.setVisibility(View.GONE);
                layAm3.setVisibility(View.GONE);
                chkNums.add(layNum3);
                chkAmounts.add(layAm3);
                chkNumList.remove(newCheqNum3);
                chkAmList.remove(newCheqAm3);
                delete++;
                count--;
                addBtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_secondary));
            }
        });
        deleteNum4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layNum4.setVisibility(View.GONE);
                layAm4.setVisibility(View.GONE);
                chkNums.add(layNum4);
                chkAmounts.add(layAm4);
                chkNumList.remove(newCheqNum4);
                chkAmList.remove(newCheqAm4);
                delete++;
                count--;
                addBtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_secondary));
            }
        });
        deleteAm4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layNum4.setVisibility(View.GONE);
                layAm4.setVisibility(View.GONE);
                chkNums.add(layNum4);
                chkAmounts.add(layAm4);
                chkNumList.remove(newCheqNum4);
                chkAmList.remove(newCheqAm4);
                delete++;
                count--;
                addBtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_secondary));
            }
        });
    }

    public void openEsignature() {
        Intent intent = new Intent(this, ESignature.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}