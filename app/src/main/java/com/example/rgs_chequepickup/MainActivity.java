package com.example.rgs_chequepickup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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

import java.io.IOException;
import java.util.Objects;

import SessionPackage.SessionManagement;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView home, profile, icon_home, text_home, icon_profile, text_profile, text_priority;
    private TextView icon_priority;
    LinearLayout home_btn, profile_btn, priority_btn;
    //OkHttpClient client;
    RelativeLayout layout;
    int prio_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.relative);

        SessionManagement sm = new SessionManagement(MainActivity.this);
        String rider = sm.getSession();

        fetchDataWithSpecificValue(rider);

        /* ------------ START: Get textview to replace text with font awesome ------------ */
        home = findViewById(R.id.icon_home);
        profile = findViewById(R.id.icon_profile);
        icon_priority = findViewById(R.id.icon_priority);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        home.setTypeface(font);
        profile.setTypeface(font);
        icon_priority.setTypeface(font);

        home.setText("\uf015");
        profile.setText("\uf007");
        icon_priority.setText("\uf06a");


        /* ------------ END: Get textview to replace text with font awesome ------------ */

        /* ------------ START: Change Fragment and Change Color of Nav Buttons On Click Navbar Buttons ------------ */

        home_btn = findViewById(R.id.home_button);
        profile_btn= findViewById(R.id.profile_button);
        priority_btn= findViewById(R.id.priority_button);
        icon_home = findViewById(R.id.icon_home);
        text_home = findViewById(R.id.text_home);
        icon_profile = findViewById(R.id.icon_profile);
        text_profile = findViewById(R.id.text_profile);
        text_priority = findViewById(R.id.text_priority);
        home_btn.setOnClickListener(view -> {
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
        });

        priority_btn.setOnClickListener(view -> {
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
        });

        profile_btn.setOnClickListener(view -> {
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
            //Toast.makeText(MainActivity.this, "Currently Under Development", Toast.LENGTH_SHORT).show();

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
        layout.post(() -> {
        });

        Button go_button = popUpView.findViewById(R.id.go_button);
        RelativeLayout overlay = popUpView.findViewById(R.id.overlay);

        layout.post(() -> overlay.setOnClickListener(v -> popupWindow.dismiss()));
        go_button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ChequePickUp.class);
            startActivity(intent);
            finish();
        });
    }

    protected void onStart() {
        super.onStart();
        SessionManagement sm = new SessionManagement(MainActivity.this);
        String isLoggedIn = sm.getSession();

        fetchDataWithSpecificValue(sm.getSession());

        if(isLoggedIn.equals("none")){
            openLogin();
        }
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
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = Objects.requireNonNull(response.body()).string();
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
                    Toast.makeText(MainActivity.this, "Response Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}