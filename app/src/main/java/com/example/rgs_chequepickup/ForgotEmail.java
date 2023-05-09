package com.example.rgs_chequepickup;

import android.app.Activity;
import android.content.Intent;
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
    Intent intent;
    Button submit_button;
    int otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_email);

        Random r = new Random();
        otp = r.nextInt(9999-1000) + 1000;

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
                    sendEmail();
                    openOTP(otp);
                }
            }
        });
    }
    public void openStartAct() {
        Intent intent = new Intent(ForgotEmail.this, LoginActivity.class);
        startActivity(intent);
    }

    public void openOTP(long otp) {
        Toast.makeText(ForgotEmail.this, "A One-Time Pin was sent to your email.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, OneTimePass.class);
        intent.putExtra("otpstat", "forgot");
        intent.putExtra("forgotEmail",input_email.getText().toString().trim());
        intent.putExtra("otp", otp);
        startActivity(intent);
    }

    public void sendEmail(){
        String mail = String.valueOf(input_email.getText());
        String subject = "RGS Express: One-Time Pin for Forgot Password";
        String message = "<h1 align=center>Hello. </h1><br><p align = center>Here is your OTP for the forgot password.\n" +
                "Do not share it to anyone.</p><h1 align = center>"+ Long.toString(otp) +"</h1><br>\n" +
                "<h6><center>If you\'re not the one who created the account, ignore the this email and change your password to prevent an account breach.<br>Only the person with access\n" +
                "to your email can see the OTP for the account creation.</h6></center>";
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,mail,subject,message);
        javaMailAPI.execute();
    }
}
