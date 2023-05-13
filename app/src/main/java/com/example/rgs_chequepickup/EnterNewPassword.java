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

public class EnterNewPassword extends AppCompatActivity {

    TextView back_button;
    EditText oldpass, newpass, confpass;
    Button submitBtn;
    String email;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_new_password);

        intent = getIntent();

        String email = intent.getStringExtra("email");

        //INPUTS
        oldpass = (EditText) findViewById(R.id.inputoldpass);
        newpass = (EditText) findViewById(R.id.inputnewpassword);
        confpass = (EditText) findViewById(R.id.inputconfirmpass);

        //BUTTONS
        submitBtn = (Button) findViewById(R.id.submit_button);
        back_button = (TextView) findViewById(R.id.back_button);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        back_button.setTypeface(font);
        back_button.setText("\uf060");

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openStartAct();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(oldpass.getText().toString().isEmpty() && newpass.getText().toString().isEmpty() && confpass.getText().toString().isEmpty())){
                    if(newpass.getText().toString().length() < 6 || confpass.getText().toString().length() < 6){
                        Toast.makeText(EnterNewPassword.this,"Lengths must be greater than 6 characters", Toast.LENGTH_SHORT).show();
                    }
                    else if(newpass.getText().toString().equals(confpass.getText().toString())) {
                        SqlDatabase sql = new SqlDatabase(EnterNewPassword.this);
                        Cursor oldres = sql.checkAccount("",email, oldpass.getText().toString());
                        if(oldres.getCount() > 0) //OLD PASSWORD IN DATABASE
                        {
                            Cursor res = sql.checkAccount("",email,confpass.getText().toString());
                            if(res.getCount() >= 1){ //OLD PASSWORD
                                Toast.makeText(EnterNewPassword.this, "Password entered is same as the old password", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                int result = sql.forgotPassword(email, confpass.getText().toString());
                                if(result == -1){
                                    Toast.makeText(EnterNewPassword.this, "Error changing password", Toast.LENGTH_SHORT).show();
                                }
                                else if(result == 1){
                                    Toast.makeText(EnterNewPassword.this, "Password changed", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(EnterNewPassword.this, LoginActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                }
                            }
                        }
                        else{
                            Toast.makeText(EnterNewPassword.this,"Invalid old password. Not in the database.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(newpass.getText().toString().equals(oldpass.getText().toString()) ||
                            confpass.getText().toString().equals(oldpass.getText().toString())){
                        Toast.makeText(EnterNewPassword.this,"New password must not be same with the old password", Toast.LENGTH_SHORT).show();
                    }
                    else if(!(newpass.getText().toString().equals(confpass.getText().toString()))){
                        Toast.makeText(EnterNewPassword.this,"New password inputs are not the same", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(oldpass.getText().toString().isEmpty() || newpass.getText().toString().isEmpty() || confpass.getText().toString().isEmpty()){
                    Toast.makeText(EnterNewPassword.this,"Fill up all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void openStartAct() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}