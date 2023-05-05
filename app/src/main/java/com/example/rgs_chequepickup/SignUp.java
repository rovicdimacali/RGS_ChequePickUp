package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import Database.SqlDatabase;

public class SignUp extends AppCompatActivity {

    Button signup_button;
    EditText name, email, password;
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

                if(!(name.getText().toString().trim().isEmpty() || email.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty())){
                    if(email.getText().toString().trim().contains("@") && password.getText().toString().trim().length() >= 6 && !(name.getText().toString().trim().matches(".*[0-9].*"))){
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
                        Cursor res = sql.checkAccount(name.getText().toString().trim(), email.getText().toString().trim());
                        if(res.getCount() == 0){ //NO ACCOUNT
                            //sendEmail(otp);
                            openOTP(otp);
                        }
                        else{
                            Toast.makeText(SignUp.this, "Account already exists.", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(this, OneTimePass.class);
        intent.putExtra("otpstat", "signup");
        intent.putExtra("signupName", name.getText().toString().trim());
        intent.putExtra("signupEmail",email.getText().toString().trim());
        intent.putExtra("signupPass", password.getText().toString().trim());
        intent.putExtra("otp", otp_signup);
        startActivity(intent);
    }

    public void sendEmail(long otp_signup){

    }
}