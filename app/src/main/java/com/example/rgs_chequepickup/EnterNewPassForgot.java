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

public class EnterNewPassForgot extends AppCompatActivity {
    EditText newpass, confpass;
    Button submit;
    TextView back_button;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_new_pass_forgot);

        intent = getIntent();

        String email = intent.getStringExtra("email");

        //TEXTFIELDS
        newpass = (EditText) findViewById(R.id.inputnewpassword);
        confpass = (EditText) findViewById(R.id.inputconfirmpass);

        //BUTTONS
        submit = (Button) findViewById(R.id.submit_button);
        back_button = (TextView) findViewById(R.id.back_button);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        back_button.setTypeface(font);
        back_button.setText("\uf060");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(newpass.getText().toString().isEmpty() && confpass.getText().toString().isEmpty())){
                    if(newpass.getText().toString().isEmpty() || confpass.getText().toString().isEmpty()){
                        Toast.makeText(EnterNewPassForgot.this,"Fill up all the fields", Toast.LENGTH_SHORT).show();
                    }
                    else if(newpass.getText().toString().length() < 6 || confpass.getText().toString().length() < 6){
                        Toast.makeText(EnterNewPassForgot.this,"Lengths must be greater than 6 characters", Toast.LENGTH_SHORT).show();
                    }
                    else if(newpass.getText().toString().equals(confpass.getText().toString())) {
                        SqlDatabase sql = new SqlDatabase(EnterNewPassForgot.this);
                        Cursor res = sql.checkAccount("",email,confpass.getText().toString());
                        if(res.getCount() >= 1){ //OLD PASSWORD
                            Toast.makeText(EnterNewPassForgot.this, "Password entered is same as the old password", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            int result = sql.forgotPassword(email, confpass.getText().toString());
                            if(result == -1){
                                Toast.makeText(EnterNewPassForgot.this, "Error changing password", Toast.LENGTH_SHORT).show();
                            }
                            else if(result == 1){
                                Toast.makeText(EnterNewPassForgot.this, "Password changed", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(EnterNewPassForgot.this, LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }
                        }
                    }
                    else{
                        Toast.makeText(EnterNewPassForgot.this,"Passwords are not the same: " + newpass.getText().toString()
                                + " " + confpass.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}