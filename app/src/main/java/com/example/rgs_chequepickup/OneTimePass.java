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

import Database.SqlDatabase;

public class OneTimePass extends AppCompatActivity {

    TextView back_button, otptext;
    EditText input_otp;
    Intent intent;
    Button submit_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_time_pass);

        back_button = (TextView) findViewById(R.id.back_button);
        submit_button = (Button) findViewById(R.id.submit_button);
        input_otp = (EditText) findViewById(R.id.inputotp);
        otptext = (TextView) findViewById(R.id.otp);
        intent = getIntent();

        //USER INPUT FOR OTP
        //String user_otp = input_otp.getText().toString();

        //INTENT VALUES
        String name = intent.getStringExtra("signupName");
        String email = intent.getStringExtra("signupEmail");
        String pass = intent.getStringExtra("signupPass");
        long otp = intent.getLongExtra("otp", 0);

        String final_otp = Long.toString(otp);
        //otptext.setText(final_otp);

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
                if (input_otp.getText().toString().equals(final_otp)) {
                    SqlDatabase sql = new SqlDatabase(OneTimePass.this);
                    int result = sql.addUser(name, email, pass, otp);

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
    }
    public void openStartAct() {
        Intent intent = new Intent(OneTimePass.this, LoginActivity.class);
        startActivity(intent);
    }
}
