package com.example.rgs_chequepickup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUp extends AppCompatActivity {

    private Button signup_button;
    private EditText username, email, password;
    private String un, em, passwd;
    TextView signing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //BUTTONS
        signing = (TextView) findViewById(R.id.signing);
        signup_button = (Button) findViewById(R.id.signup_button);

        //INPUTS
        username = (EditText) findViewById(R.id.inputname);
        un = username.getText().toString();

        email = (EditText) findViewById(R.id.inputemail1);
        em = email.getText().toString();

        password = (EditText) findViewById(R.id.inputpassword1);
        passwd = password.getText().toString();

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
                sql.addUser(un, em, passwd);
                openCongrats();
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
}