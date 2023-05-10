package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import Database.SqlDatabase;
import Email.JavaMailAPI;
import Email.Utils;

public class SignUp extends AppCompatActivity {

    Button signup_button;
    EditText name, email, password,phone;
    //String un, em, passwd;
    TextView signing;
    int otp, result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //BUTTONS
        signing = (TextView) findViewById(R.id.signing);
        signup_button = (Button) findViewById(R.id.signup_button);

        //INPUTS
        name = (EditText) findViewById(R.id.inputname);
        //un = username.getText().toString().trim();

        email = (EditText) findViewById(R.id.inputemail1);
        //em = email.getText().toString().trim();

        password = (EditText) findViewById(R.id.inputpassword1);
        //passwd = password.getText().toString().trim();
        phone = (EditText) findViewById(R.id.inputphonenumber);

        signing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SqlDatabase sql = new SqlDatabase(SignUp.this);
                Random r = new Random();
                otp = r.nextInt(9999-1000) + 1000;

                if(!(name.getText().toString().trim().isEmpty() || email.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty() || phone.getText().toString().trim().isEmpty())){
                    if(email.getText().toString().trim().contains("@") && phone.getText().toString().trim().length() == 11 && password.getText().toString().trim().length() >= 6 && !(name.getText().toString().trim().matches(".*[0-9].*"))){
                        //result = sql.addUser(name.getText().toString().trim(), email.getText().toString().trim(), password.getText().toString().trim(), otp);
                        //openCongrats();
                        //if(result == 1){
                            //Toast.makeText(SignUp.this, "Account Created!", Toast.LENGTH_SHORT).show();
                            //openCongrats();
                        //}
                        //else if(result == -1){
                            //Toast.makeText(SignUp.this, "Error Creating Account!", Toast.LENGTH_SHORT).show();
                        //}

                        //CHECK IF AN ACCOUNT ALREADY EXISTS
                        Cursor res = sql.checkAccount(name.getText().toString().trim(), email.getText().toString().trim(), "");
                        if(res.getCount() == 0){ //NO ACCOUNT
                            sendEmail();
                            openOTP(otp);
                        }
                        else{
                            Toast.makeText(SignUp.this, "Email is already associated with an another account.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(name.getText().toString().trim().matches(".*[0-9].*")){
                        Toast.makeText(SignUp.this,"Name must not contain numbers.", Toast.LENGTH_SHORT).show();
                    }
                    else if(password.getText().toString().trim().length() < 6){
                        Toast.makeText(SignUp.this,"Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    }
                    else if(!(email.getText().toString().trim().contains("@"))){
                        Toast.makeText(SignUp.this,"Invalid Email Address", Toast.LENGTH_SHORT).show();
                    }
                    else if(!(phone.getText().toString().trim().length() == 11)){
                        Toast.makeText(SignUp.this,"Phone Number must be 11 digits", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(SignUp.this,"Fill up all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    public void openLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openCongrats(){
        Intent intent = new Intent(this, Congratulations.class);
        startActivity(intent);
    }

    public void openOTP(long otp_signup){
        Toast.makeText(SignUp.this, "A One-Time Pin was sent to your email.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, OneTimePass.class);
        intent.putExtra("otpstat", "signup");
        intent.putExtra("signupName", name.getText().toString().trim());
        intent.putExtra("signupEmail",email.getText().toString().trim());
        intent.putExtra("signupPass", password.getText().toString().trim());
        intent.putExtra("signupPhone", phone.getText().toString().trim());
        intent.putExtra("otp", otp_signup);
        startActivity(intent);
    }


    public void sendEmail(){
        String mail = String.valueOf(email.getText());
        String subject = "RGS Express: One-Time Pin for Account Creation";
        String message = "<h1 align=center>Hi, Mr./Ms. " + String.valueOf(name.getText()) + "</h1><br><p align = center>Here is your OTP for account creation.\n" +
                "Do not share it to anyone.</p><h1 align = center>"+ Long.toString(otp) +"</h1><br>\n" +
                "<h6><center>If you\'re not the one who created the account, ignore the this email and change your password to prevent an account breach.<br>Only the person with access\n" +
                "to your email can see the OTP for the account creation.</h6></center>";
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,mail,subject,message);
        javaMailAPI.execute();
    }
}