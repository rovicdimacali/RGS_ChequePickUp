package com.example.rgs_chequepickup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import SessionPackage.SessionManagement;
import SessionPackage.UserSession;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    TextView back_button, forgotpassword, signup, signin;
    EditText inputemail, inputpassword;
    String email, pass;
    Button login_button;
    OkHttpClient client;
    String responseData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //HTTP CLIENT
        client = new OkHttpClient();

        back_button = (TextView) findViewById(R.id.back_button);
        //signup = (TextView) findViewById(R.id.signup);
        forgotpassword = (TextView) findViewById(R.id.forgotpassword);
        signin = (TextView) findViewById(R.id.signin);

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
                /*
                SqlDatabase sql = new SqlDatabase(LoginActivity.this);
                Cursor res = sql.checkAccount("",String.valueOf(inputemail.getText()),String.valueOf(inputpassword.getText()));

                if(res.getCount() == 0){ //NO ACCOUNT
                    Toast.makeText(LoginActivity.this, "Account does not exists. Try again.", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Logging in and save session
                    SessionManagement sm = new SessionManagement(LoginActivity.this);
                    UserSession us = new UserSession(String.valueOf(inputemail.getText()),String.valueOf(inputpassword.getText()));
                    sm.saveSession(us);

                    openMain();
                }*/
                //String result = api.getData();
                post(String.valueOf(inputemail.getText()),String.valueOf(inputpassword.getText()));
                //Toast.makeText(LoginActivity.this, res, Toast.LENGTH_SHORT).show();
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

        /*signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignup();
            }
        });*/
    }
    protected void onStart() {
        super.onStart();

        //check if user is logged in
        SessionManagement sm = new SessionManagement(LoginActivity.this);
        String isLoggedIn = sm.getSession();

        if(!(isLoggedIn.equals("none"))){
            openMain();
        }
        else if(isLoggedIn.equals("none")){
            //Intent intent = new Intent(this, LoginActivity.class);
            //startActivity(intent);
        }
    }
    public void openStartAct() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    public void openForgotPass(){
        Intent intent = new Intent(this, ForgotPassword.class);
        intent.putExtra("type", "forgot");
        startActivity(intent);
    }

    public void openSignup(){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    public void openMain(){
        //Move to main activity
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("email", String.valueOf(inputemail.getText()));
        //intent.putExtra("pass", String.valueOf(inputpassword.getText()));
        startActivity(intent);
    }

    /*public void getData(){
        //REQUEST OBJECT WITH THE API URL
        Request request = new Request.Builder()
                .url("http://203.177.49.26:28110/tracker/api/login")
                .build();

        //SEND REQ ASYNCH
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, " " + e, Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            signin.setText("POST: " + response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }*/
    public void post(String un, String pass){
        if(un.isEmpty() || pass.isEmpty()){
            Toast.makeText(LoginActivity.this, "Fill up empty fields", Toast.LENGTH_SHORT).show();
        }
        else{
            RequestBody rbody = new FormBody.Builder()
                    .add("username", un)
                    .add("password", pass)
                    .build();
            Request req = new Request.Builder().url("http://203.177.49.26:28110/tracker/api/login").post(rbody).build();
            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                responseData = response.body().string();
                                String value = specificValue(responseData);
                                if(value.equals("1")){
                                    SessionManagement sm = new SessionManagement(LoginActivity.this);
                                    UserSession us = new UserSession(String.valueOf(inputemail.getText()),String.valueOf(inputpassword.getText()));
                                    sm.saveSession(us);
                                    openMain();
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "Account does not exist", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
            });
        }
    }

    private String specificValue(String responseData){
        try{
            JSONObject json = new JSONObject(responseData);
            String value = json.getString("success");
            return value;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}