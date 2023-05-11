package com.example.rgs_chequepickup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import Database.SqlDatabase;
import Email.JavaMailAPI;

public class OneTimePass extends AppCompatActivity {

    TextView back_button, otptext, resend, switchverif;
    String verify_id;
    PhoneAuthProvider.ForceResendingToken verify_token;
    EditText input_otp;
    Intent intent;
    Button submit_button;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final int PERMISSION_SEND_SMS = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_time_pass);

        //ActivityCompat.requestPermissions(OneTimePass.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        back_button = (TextView) findViewById(R.id.back_button);
        submit_button = (Button) findViewById(R.id.submit_button);
        resend = (TextView) findViewById(R.id.resend);
        switchverif = (TextView) findViewById(R.id.switchverif);
        mAuth = FirebaseAuth.getInstance();

        input_otp = (EditText) findViewById(R.id.inputotp);
        otptext = (TextView) findViewById(R.id.otp);
        intent = getIntent();

        //USER INPUT FOR OTP
        //String user_otp = input_otp.getText().toString();

        //INTENT VALUES
        String otpstat = intent.getStringExtra("otpstat");
        String name = intent.getStringExtra("signupName");
        String email = intent.getStringExtra("signupEmail");
        String pass = intent.getStringExtra("signupPass");
        String phone = intent.getStringExtra("signupPhone");

        long otp = intent.getLongExtra("otp", 0);

        String final_otp = Long.toString(otp);
        //otptext.setText(final_otp);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        back_button.setTypeface(font);
        back_button.setText("\uf060");
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input_otp.getText().toString().equals(final_otp)) {
                    SqlDatabase sql = new SqlDatabase(OneTimePass.this);
                    int result = sql.addUser(name, email, pass, phone, otp);
                    // If previous activity was SignUp

                    if(result == 1){
                        Toast.makeText(OneTimePass.this, "Account Created!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OneTimePass.this, Congratulations.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(OneTimePass.this, "Error Creating Account!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(OneTimePass.this, "Incorrect OTP. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        switchverif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(Build.VERSION.SDK_INT >= 23){
                    if(ContextCompat.checkSelfPermission(OneTimePass.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(OneTimePass.this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_SEND_SMS);
                    }
                    else{
                        sendSMS();
                    }
                }
                else{
                    sendSMS();
                }*/
               sendOTP(phone.substring(1), false);
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
                Toast.makeText(OneTimePass.this, "The OTP was sent to your email", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void openLogin() {
        Intent intent = new Intent(OneTimePass.this, LoginActivity.class);
        startActivity(intent);
    }

    public void openForgotEmailAct() {
        Intent intent = new Intent(OneTimePass.this, ForgotEmail.class);
        startActivity(intent);
    }

    public void sendEmail(){
        String mail = intent.getStringExtra("signupEmail");
        String name = intent.getStringExtra("signupName");
        String otp = Long.toString(intent.getLongExtra("otp", 0));

        String subject = "RGS Express: One-Time Pin for Account Creation";
        String message = "<h1 align=center>Hi, Mr./Ms. " + name + "</h1><br><p align = center>Here is your OTP for account creation.\n" +
                "Do not share it to anyone.</p><h1 align = center>"+ otp +"</h1><br>\n" +
                "<h6><center>If you\'re not the one who created the account, ignore the this email and change your password to prevent an account breach.<br>Only the person with access\n" +
                "to your email can see the OTP for the account creation.</h6></center>";
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,mail,subject,message);
        javaMailAPI.execute();
    }

    void sendOTP(String number, boolean isResend){
        PhoneAuthOptions.Builder build =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+63"+number)
                        .setTimeout(60L,TimeUnit.SECONDS)
                        .setActivity(OneTimePass.this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                                signIn(credential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(OneTimePass.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                super.onCodeSent(s,token);
                                verify_id = s;
                                verify_token = token;
                                Toast.makeText(OneTimePass.this,"OTP sent successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(build.setForceResendingToken(verify_token).build());
        }
        else{
            PhoneAuthProvider.verifyPhoneNumber(build.build());
        }
    }
    void signIn(PhoneAuthCredential cred){
        startActivity(new Intent(OneTimePass.this, Congratulations.class));
    }
    /*private void sendSMS(){
        //String mail = intent.getStringExtra("signupEmail");
        String name = intent.getStringExtra("signupName");
        String otp = Long.toString(intent.getLongExtra("otp", 0));

        String message = "Good Day, Mr./Ms. " + name + "\nHere is your One-Time Password: " + otp + "\nEnter it to complete " +
                "" +"your account creation. DON'T SHARE IT TO ANYONE.";
        //String message = "is your OTP.";
        String number = intent.getStringExtra("signupPhone");

        SmsManager sms_man = SmsManager.getDefault();
        ArrayList<String> parts = sms_man.divideMessage(message);
        sms_man.sendMultipartTextMessage(number,null,parts,null,null);
        Toast.makeText(this, "OTP was sent to your phone number", Toast.LENGTH_SHORT).show();
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == PERMISSION_SEND_SMS){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                sendSMS();
            }
            else{
                Toast.makeText(this," Permission DENIED " + grantResults[0], Toast.LENGTH_SHORT).show();
            }
        }

    }*/
}

