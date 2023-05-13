package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Email.JavaMailAPI;

public class OneTimePassChangePass extends AppCompatActivity {

    TextView back_button, resend_btn;
    EditText input_otp;
    Button submit_btn;
    Intent intent;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_time_pass_change_pass);

        intent = getIntent();
        //INTENTS
        email = intent.getStringExtra("email");
        long otp = intent.getLongExtra("otp",0);
        String final_otp = Long.toString(otp);

        //BUTTONS AND EDITTEXTS
        submit_btn = (Button) findViewById(R.id.submit_button);
        resend_btn = (TextView) findViewById(R.id.resend);
        back_button = (TextView) findViewById(R.id.back_button);
        //TextView test = (TextView) findViewById(R.id.otp_forgot);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        back_button.setTypeface(font);
        back_button.setText("\uf060");

        input_otp = (EditText) findViewById(R.id.inputotp_change);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input_otp.getText().toString().equals(final_otp)){
                    Intent i = new Intent(OneTimePassChangePass.this, EnterNewPassword.class);
                    i.putExtra("email", email);
                    startActivity(i);
                }
                else{
                    Toast.makeText(OneTimePassChangePass.this, "Invalid entered OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
        resend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(final_otp);
                Toast.makeText(OneTimePassChangePass.this, "OTP was sent to your email", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void sendEmail(String otp){
        String mail = email;
        String subject = "RGS Express: One-Time Pin for Change Password";
        String message = "<h1 align=center>Good Day!</h1><br><p align = center>Here is your OTP to reset your password.\n" +
                "Do not share it to anyone.</p><h1 align = center>"+ otp +"</h1><br>\n" +
                "<h6><center>If you\'re not the one who requested for the password change, ignore the this email and change your password to prevent an account breach.<br>Only the person with access\n" +
                "to your email can see the OTP for the password reset.</h6></center>";
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,mail,subject,message);
        javaMailAPI.execute();
    }
}