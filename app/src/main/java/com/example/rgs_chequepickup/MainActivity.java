package com.example.rgs_chequepickup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;

import Database.SqlDatabase;
import SessionPackage.SessionManagement;
import SessionPackage.UserSession;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView home,  tv, profile, icon_home, text_home, icon_profile, text_profile, text_priority;
    private TextView icon_priority;
    LinearLayout home_btn, profile_btn, priority_btn;
    Intent intent;
    //OkHttpClient client;
    String responseData;
    RelativeLayout layout;
    int prio_num;
    int prio_res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (RelativeLayout) findViewById(R.id.relative);
        tv = (TextView) findViewById(R.id.text_home);
        intent = getIntent();

        SessionManagement sm = new SessionManagement(MainActivity.this);
        String rider = sm.getSession();

        fetchDataWithSpecificValue(rider);

        /* ------------ START: Get textview to replace text with font awesome ------------ */
        String inputemail = intent.getStringExtra("email");
        //tv.setText(inputemail);
        //String inputpass = intent.getStringExtra("pass");


        home = (TextView) findViewById(R.id.icon_home);
        profile = (TextView) findViewById(R.id.icon_profile);
        icon_priority = (TextView) findViewById(R.id.icon_priority);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        home.setTypeface(font);
        profile.setTypeface(font);
        icon_priority.setTypeface(font);

        home.setText("\uf015");
        profile.setText("\uf007");
        icon_priority.setText("\uf06a");


        /* ------------ END: Get textview to replace text with font awesome ------------ */

        /* ------------ START: Change Fragment and Change Color of Nav Buttons On Click Navbar Buttons ------------ */

        home_btn = (LinearLayout) findViewById(R.id.home_button);
        profile_btn= (LinearLayout) findViewById(R.id.profile_button);
        priority_btn= (LinearLayout) findViewById(R.id.priority_button);
        icon_home = (TextView) findViewById(R.id.icon_home);
        text_home = (TextView) findViewById(R.id.text_home);
        icon_profile = (TextView) findViewById(R.id.icon_profile);
        text_profile = (TextView) findViewById(R.id.text_profile);
        text_priority = (TextView) findViewById(R.id.text_priority);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, HomeFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();

                text_home.setTextColor(Color.parseColor("#b0e32e"));
                icon_home.setTextColor(Color.parseColor("#b0e32e"));
                text_priority.setTextColor(Color.parseColor("#808080"));
                if(prio_num == 1){
                    icon_priority.setTextColor(Color.RED);
                }
                else{
                    icon_priority.setTextColor(Color.parseColor("#808080"));
                }
                text_profile.setTextColor(Color.parseColor("#808080"));
                icon_profile.setTextColor(Color.parseColor("#808080"));
            }
        });

        priority_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, PriorityFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();

                text_home.setTextColor(Color.parseColor("#808080"));
                icon_home.setTextColor(Color.parseColor("#808080"));
                text_priority.setTextColor(Color.parseColor("#b0e32e"));
                if(prio_num == 1){
                    icon_priority.setTextColor(Color.RED);
                }
                else{
                    icon_priority.setTextColor(Color.parseColor("#b0e32e"));
                }
                text_profile.setTextColor(Color.parseColor("#808080"));
                icon_profile.setTextColor(Color.parseColor("#808080"));
            }
        });

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, ProfileFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
                text_home.setTextColor(Color.parseColor("#808080"));
                icon_home.setTextColor(Color.parseColor("#808080"));
                text_priority.setTextColor(Color.parseColor("#808080"));
                if(prio_num == 1){
                    icon_priority.setTextColor(Color.RED);
                }
                else{
                    icon_priority.setTextColor(Color.parseColor("#808080"));
                }
                text_profile.setTextColor(Color.parseColor("#b0e32e"));
                icon_profile.setTextColor(Color.parseColor("#b0e32e"));

            }
        });

        /* ------------ END: Change Fragment and Change Color of Nav Buttons On Click Navbar Buttons ------------ */

        CreatePopUpWindow();
    }

    private void CreatePopUpWindow() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.notification_popup, null);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);
        layout.post(new Runnable() {
            @Override
            public void run() {
                /*home_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
                    }
                });*/
            }
        });

        Button go_button = popUpView.findViewById(R.id.go_button);
        RelativeLayout overlay = popUpView.findViewById(R.id.overlay);

        layout.post(new Runnable() {
            @Override
            public void run() {
                overlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
        });
        go_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChequePickUp.class);
                startActivity(intent);
                finish();
            }
        });
    }

    protected void onStart() {
        super.onStart();
        SessionManagement sm = new SessionManagement(MainActivity.this);
        String isLoggedIn = sm.getSession();

        fetchDataWithSpecificValue(sm.getSession());

        if(!(isLoggedIn.equals("none"))){
            //Intent intent = new Intent(this, LoginActivity.class);
            //startActivity(intent);
        }
        else if(isLoggedIn.equals("none")){
            openLogin();
        }
        //check if user is logged in
        /*SqlDatabase sql = new SqlDatabase(MainActivity.this);
        SessionManagement sm = new SessionManagement(MainActivity.this);
        String isLoggedIn = sm.getSession();

        if(!(isLoggedIn.equals("none"))){
            Cursor res = sql.checkAccount("", isLoggedIn, "");
            if(res.getCount() == 0){ //NO ACCOUNT
                sm.removeSession();
                Toast.makeText(MainActivity.this, "You have been logged out", Toast.LENGTH_SHORT).show();
                openLogin();
            }
            else{
                //Toast.makeText(MainActivity.this,"You are now logged in", Toast.LENGTH_SHORT).show();
            }
        }
        else if(isLoggedIn.equals("none")){
            Toast.makeText(MainActivity.this, "Error saving your session", Toast.LENGTH_SHORT).show();
            openLogin();
        }*/
    }
    public void openLogin(){
        //Move to main activity
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void fetchDataWithSpecificValue(String riderID) {
        OkHttpClient client = new OkHttpClient();

        // Construct the URL with the specific value
        String url = "http://203.177.49.26:28110/tracker/api/priority";
        RequestBody rbody = new FormBody.Builder()
                .add("riderID", riderID)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(rbody)
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    //String responseData = " ";
                    //responseData = responseData.replace("<br", "");
                    try {
                        JSONArray jsonArray = new JSONArray(responseData);
                        //JSONArray jsonArray = new JSONArray(resp);
                        if(jsonArray.length() > 0){
                            //icon_priority.setTextColor(Color.RED);
                            icon_priority.setTextColor(Color.RED);
                            prio_num = 1;
                        }
                        else{
                            //icon_priority.setTextColor(Color.parseColor("#b0e32e"));
                            icon_priority.setTextColor(Color.parseColor("#808080"));
                            prio_num = -1;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle unsuccessful response
                }
            }
        });
    }
}