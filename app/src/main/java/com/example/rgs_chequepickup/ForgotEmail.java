package com.example.rgs_chequepickup;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import Database.SqlDatabase;
import Email.JavaMailAPI;

public class ForgotEmail extends Activity {
    TextView back_button, emailtext;
    EditText input_email;
    Intent intent, next_intent;
    String subject;
    Button submit_button;
    int otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_email);

        intent = getIntent();
        Random r = new Random();
        otp = r.nextInt(999999-100000) + 100000;

        back_button = (TextView) findViewById(R.id.back_button);
        submit_button = (Button) findViewById(R.id.submit_button);
        input_email = (EditText) findViewById(R.id.inputemail);
        emailtext = (TextView) findViewById(R.id.email);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        back_button.setTypeface(font);
        back_button.setText("\uf060");
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStartAct();
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If inputemail Edit View is empty
                if(input_email.getText().toString().trim().isEmpty()) {
                    Toast.makeText(ForgotEmail.this,"Please enter an email.", Toast.LENGTH_SHORT).show();
                }
                // If entered input is not a valid email format
                else if (!(input_email.getText().toString().trim().contains("@"))) {
                    Toast.makeText(ForgotEmail.this,"Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                } else{
                    SqlDatabase sql = new SqlDatabase(ForgotEmail.this);
                    Cursor res = sql.checkAccount("",input_email.getText().toString().trim(), "");
                    if(res.getCount() == 0){ //NO ACCOUNT
                        Toast.makeText(ForgotEmail.this, "Email is not associated with a registered account.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String type = intent.getStringExtra("type");
                        sendEmail(type);
                        openOTP(otp, type);
                    }

                }
            }
        });
    }
    public void openStartAct() {
        Intent intent = new Intent(ForgotEmail.this, ForgotPassword.class);
        startActivity(intent);
    }

    public void openOTP(long otp, String type) {
        Toast.makeText(ForgotEmail.this, "A One-Time Pin was sent to your email.", Toast.LENGTH_SHORT).show();
        if(type.equals("change")){
            next_intent = new Intent(this, OneTimePassChangePass.class);
        }
        else if(type.equals("forgot")){
            next_intent = new Intent(this, OneTimePassForgotPass.class);
        }
        next_intent.putExtra("email",input_email.getText().toString().trim());
        next_intent.putExtra("otp", otp);
        startActivity(next_intent);
        //intent.putExtra("otpstat", "forgot");

    }

    public void sendEmail(String type){
        String mail = String.valueOf(input_email.getText());

        if(type.equals("change")){
            subject = "RGS Express: One-Time Pin for Change Password";
        }
        else if(type.equals("forgot")){
            subject = "RGS Express: One-Time Pin for Forgot Password";
        }
        String message = "<h1 align=center>Good Day! </h1><br><p align = center>Here is your OTP to reset your password.\n" +
                "Do not share it to anyone.</p><h1 align = center>"+ Long.toString(otp) +"</h1><br>\n" +
                "<h6><center>If you\'re not the one who requested for the password change, ignore the this email and change your password to prevent an account breach.<br>Only the person with access\n" +
                "to your email can see the OTP for the password change.</h6></center>";
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,mail,subject,message);
        javaMailAPI.execute();
    }
}
