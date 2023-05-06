package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Database.SqlDatabase;

public class LoginActivity extends AppCompatActivity {

    TextView back_button, forgotpassword, signup;
    EditText inputemail, inputpassword;
    String email, pass;
    Button login_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        back_button = (TextView) findViewById(R.id.back_button);
        signup = (TextView) findViewById(R.id.signup);
        forgotpassword = (TextView) findViewById(R.id.forgotpassword);

        login_button = (Button) findViewById(R.id.login_button);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        //INPUTS
        inputemail = (EditText) findViewById(R.id.inputemail);
        //email = inputemail.getText().toString();

        inputpassword = (EditText) findViewById(R.id.inputpassword);
        //pass = inputpassword.getText().toString();

        ///////
        back_button.setTypeface(font);
        back_button.setText("\uf060");

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SqlDatabase sql = new SqlDatabase(LoginActivity.this);
                Cursor res = sql.checkAccount("",String.valueOf(inputemail.getText()),String.valueOf(inputpassword.getText()));

                if(res.getCount() == 0){ //NO ACCOUNT
                    Toast.makeText(LoginActivity.this, "Account does not exists. Try again.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openStartAct();
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openForgotPass();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignup();
            }
        });
    }
    public void openStartAct() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    public void openForgotPass(){
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
    }

    public void openSignup(){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
}