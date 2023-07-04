package com.example.rgs_chequepickup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

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

    TextView back_button;
    EditText inputemail, inputpassword;
    Button login_button;
    OkHttpClient client;
    String responseData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //HTTP CLIENT
        client = new OkHttpClient();

        back_button = findViewById(R.id.back_button);

        login_button = findViewById(R.id.login_button);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        //INPUTS
        inputemail = findViewById(R.id.inputemail);
        //email = inputemail.getText().toString();

        inputpassword = findViewById(R.id.inputpassword);
        ///////
        back_button.setTypeface(font);
        back_button.setText("\uf060");

        login_button.setOnClickListener(v -> {
            post(String.valueOf(inputemail.getText()),String.valueOf(inputpassword.getText()));
            //Toast.makeText(LoginActivity.this, res, Toast.LENGTH_SHORT).show();
        });

        back_button.setOnClickListener(v -> openStartAct());
    }
    protected void onStart() {
        super.onStart();

        //check if user is logged in
        SessionManagement sm = new SessionManagement(LoginActivity.this);
        String isLoggedIn = sm.getSession();

        if(!(isLoggedIn.equals("none"))){
            openMain();
        }
    }
    public void openStartAct() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
    }
    public void openMain(){
        //Move to main activity
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("email", String.valueOf(inputemail.getText()));
        //intent.putExtra("pass", String.valueOf(inputpassword.getText()));
        startActivity(intent);
        finish();
    }

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
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "ERROR", Toast.LENGTH_SHORT).show());
                }
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    runOnUiThread(() -> {
                        try {
                            responseData = Objects.requireNonNull(response.body()).string();
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
                    });
                }
            });
        }
    }

    private String specificValue(String responseData){
        try{
            JSONObject json = new JSONObject(responseData);
            return json.getString("success");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}