package com.example.rgs_chequepickup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import SessionPackage.LocationManagement;
import SessionPackage.LocationSession;
import SessionPackage.SessionManagement;
import okhttp3.OkHttpClient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    //final float density = getContext().getResources().getDisplayMetrics().density;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int ctr = 0;
    Context cont;
    Button go_button;
    ViewFlipper carousel;
    LinearLayout parentL;
    CardView customer_btn;
    View view;
    LinearLayout layout;
    TextView comp1,p1,ad1,cont1,code1, next_arr, back_arr, noTask;
    OkHttpClient client;
    ScrollView scrollpick;
    String responseData;

    //COMPANY ARRAY
    String compArr[] = new String[100];
    //PERSON ARRAY
    String personArr[] = new String[5];
    //ADDRESS ARRAY
    String addressArr[] = new String[5];
    //CONTACT ARRAY
    String contactArr[] = new String[5];

    public HomeFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        client = new OkHttpClient();
        cont = getContext();
        SessionManagement sm = new SessionManagement(cont);
        String rider = sm.getSession();

        fetchDataWithSpecificValue(rider);
        //Toast.makeText(cont, "rider " + rider, Toast.LENGTH_SHORT).show();

        view = inflater.inflate(R.layout.fragment_home, container, false);
        layout = view.findViewById(R.id.linearlayout);

        noTask = (TextView) view.findViewById(R.id.notask);
        scrollpick = (ScrollView) view.findViewById(R.id.scroll_pick);

        customer_btn = (CardView) view.findViewById(R.id.customer_btn);

        TextView back_arrow, next_arrow, company_icon, person_icon, address_icon, number_icon, code_icon;
        /*company_icon = (TextView) view.findViewById(R.id.companyicon);
        person_icon = (TextView) view.findViewById(R.id.personicon);
        address_icon = (TextView) view.findViewById(R.id.addressicon);
        number_icon = (TextView) view.findViewById(R.id.numbericon);
        code_icon = (TextView) view.findViewById(R.id.codeicon);*/

        Typeface font = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/fontawesome-webfont.ttf");

       //back_arrow.setTypeface(font);
        //next_arrow.setTypeface(font);
        /*company_icon.setTypeface(font);
        person_icon.setTypeface(font);
        address_icon.setTypeface(font);
        number_icon.setTypeface(font);
        code_icon.setTypeface(font);*/

        /*company_icon.setText("\uf1ad");
        person_icon.setText("\uf007");
        address_icon.setText("\uf015");
        number_icon.setText("\uf2a0");
        code_icon.setText("\uf25d");*/

        go_button = (Button) customer_btn.findViewById(R.id.go_button);

        //carousel = (ViewFlipper) view.findViewById(R.id.carousel);
        parentL = (LinearLayout) view.findViewById(R.id.parentLayout);
        //ViewGroup.MarginLayoutParams carousel_margins = (ViewGroup.MarginLayoutParams) carousel.getLayoutParams();
        //carousel_margins.setMarginStart(30);
        //carousel_margins.setMarginEnd(30);
        //carousel.setLayoutParams(carousel_margins);

        //DETAILS
        comp1 = (TextView) customer_btn.findViewById(R.id.companyname);
        p1 = (TextView) customer_btn.findViewById(R.id.companyperson);
        ad1 = (TextView) customer_btn.findViewById(R.id.companyadd);
        cont1 = (TextView) customer_btn.findViewById(R.id.companycontact);
        code1 = (TextView) customer_btn.findViewById(R.id.companycode);

        //post(rider);

        /*comp1.setText("start: " + compArr[0]);
        p1.setText(personArr[0]);
        ad1.setText(addressArr[0]);
        cont1.setText("+63"+ contactArr[0]);*/


        go_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChequePickUp.class);
                LocationManagement lm = new LocationManagement(cont);
                LocationSession ls = new LocationSession(String.valueOf(comp1.getText()), String.valueOf(p1.getText()),
                        String.valueOf(ad1.getText()),String.valueOf(cont1.getText()), String.valueOf(code1.getText()));
                lm.saveLocation(ls);
                /*intent.putExtra("company", comp1.getText().toString());
                intent.putExtra("person", p1.getText().toString());
                intent.putExtra("address", ad1.getText().toString());
                intent.putExtra("contact", cont1.getText().toString());*/
                startActivity(intent);
                //getActivity().finish();
            }
        });
        //carousel.setAutoStart(true);
        //carousel.setFlipInterval(1000);
        //carousel.startFlipping();
        //TEXTVIEWS
        //
        return view;
    }

    private void fetchDataWithSpecificValue(String riderID) {
        OkHttpClient client = new OkHttpClient();

        // Construct the URL with the specific value
        String url = "http://203.177.49.26:28110/tracker/api/accounts";
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
                    //String responseData = "";
                    //responseData = responseData.replace("<br", "");
                    try {
                        // Parse the response JSON
                        //JSONObject jsonObject = new JSONObject(responseData);
                        JSONArray jsonArray = new JSONArray(responseData);

                        // Access the multidimensional associative array within the JSON
                        //JSONArray associativeArray = jsonObject.getJSONArray("riderID");

                        // Process and display the associative array data
                        processAssociativeArray(jsonArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle unsuccessful response
                }
            }
        });
    }
    public void processAssociativeArray(JSONArray associativeArray) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(associativeArray.length() == 0){
                        noTask.setVisibility(View.VISIBLE);
                        scrollpick.setVisibility(View.GONE);
                        comp1.setText("NO TASK");
                        p1.setText("NO TASK");
                        ad1.setText("NO TASK");
                        cont1.setText("NO TASK");
                        code1.setText("NO TASK");
                    }
                    else{
                        noTask.setVisibility(View.GONE);
                        scrollpick.setVisibility(View.VISIBLE);
                        JSONObject item2 = associativeArray.getJSONObject(0);
                        String og1 = item2.getString("company_name");
                        String og2 = item2.getString("fullname");
                        String og3 = item2.getString("address");
                        String og4 = item2.getString("contact_no");
                        String og5 = item2.getString("company_code");

                        comp1.setText(og1);
                        p1.setText(og2);
                        ad1.setText(og3);
                        cont1.setText(og4);
                        code1.setText(og5);
                    }

                    // Iterate over the associative array
                    for (int i = 1; i < associativeArray.length(); i++) {
                        JSONObject item = associativeArray.getJSONObject(i);

                        // Access specific values within the associative array
                        String value1 = item.getString("company_name");
                        String value2 = item.getString("fullname");
                        String value3 = item.getString("address");
                        String value4 = item.getString("contact_no");
                        String value5 = item.getString("company_code");
                        // Display the values or perform further processing
                        /*
                        Log.d("AssociativeArray", "Value 1: " + value1);
                        Log.d("AssociativeArray", "Value 2: " + value2);
                        Log.d("AssociativeArray", "Value 3: " + value3);
                        Log.d("AssociativeArray", "Value 4: " + value4);
                        comp1.setText(value1);
                        p1.setText(value2);
                        ad1.setText(value3);
                        cont1.setText(value4);*/

                        //BUTTONS
                        Button go = new Button(cont);
                        LinearLayout.LayoutParams gobtn = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        ViewGroup.MarginLayoutParams go_margins = (ViewGroup.MarginLayoutParams) go_button.getLayoutParams();

                        go_margins.topMargin = 20;
                        float go_weight = 1f;
                        gobtn.setMarginEnd(20);
                        gobtn.weight = go_weight;
                        go.setLayoutParams(go_margins);
                        go.setLayoutParams(gobtn);
                        go.setId(R.id.go_button);
                        go.setBackgroundColor(ContextCompat.getColor(cont, R.color.rgs_green));
                        go.setTypeface(ResourcesCompat.getFont(cont,R.font.poppins_bold));
                        go.setText("LET'S GO");
                        //go.setTypeface(null, Typeface.BOLD);

                        //2ND LINEAR LAYOUT
                        LinearLayout.LayoutParams new_2ll_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        LinearLayout new_2ll = new LinearLayout(cont);

                        new_2ll.setLayoutParams(new_2ll_params);
                        new_2ll.setOrientation(LinearLayout.HORIZONTAL);

                        //TEXT VIEWS
                        TextView code = new TextView(cont);
                        LinearLayout.LayoutParams codetext = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        code.setLayoutParams(codetext);
                        code.setText(value5);
                        code.setVisibility(View.GONE);
                        code.setTextColor(Color.BLACK);
                        code.setTypeface(ResourcesCompat.getFont(cont,R.font.poppins));
                        code.setTextSize(15);

                        TextView contact = new TextView(cont);
                        LinearLayout.LayoutParams conttext = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        contact.setLayoutParams(conttext);
                        contact.setText(value4);
                        contact.setVisibility(View.GONE);
                        contact.setTextColor(Color.BLACK);
                        contact.setTypeface(ResourcesCompat.getFont(cont,R.font.poppins));
                        contact.setTextSize(15);

                        TextView add = new TextView(cont);
                        LinearLayout.LayoutParams addtext = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        add.setLayoutParams(addtext);
                        add.setText(value3);
                        add.setTextColor(Color.BLACK);
                        add.setTypeface(ResourcesCompat.getFont(cont,R.font.poppins));
                        add.setTextSize(15);

                        TextView per = new TextView(cont);
                        LinearLayout.LayoutParams pertext = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        per.setLayoutParams(pertext);
                        per.setText(value2);
                        per.setVisibility(View.GONE);
                        per.setTextColor(Color.BLACK);
                        per.setTypeface(ResourcesCompat.getFont(cont,R.font.poppins));
                        per.setTextSize(18);

                        TextView comp = new TextView(cont);
                        LinearLayout.LayoutParams comptext = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        comp.setLayoutParams(comptext);
                        comp.setText(value1);
                        comp.setTextColor(Color.BLACK);
                        comp.setTypeface(ResourcesCompat.getFont(cont,R.font.poppins));
                        comp.setTextSize(18);
                        //NEW LLAYOUT
                        LinearLayout.LayoutParams new_ll_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);

                        new_ll_params.setMarginEnd(20);
                        new_ll_params.setMarginStart(20);
                        new_ll_params.gravity = Gravity.CENTER_VERTICAL;

                        LinearLayout new_ll = new LinearLayout(cont);
                        new_ll.setLayoutParams(new_ll_params);
                        new_ll.setOrientation(LinearLayout.VERTICAL);
                        new_ll.setId(R.id.linearlayout+i);
                        new_ll.setPadding(70, 50, 40, 50);

                        //NEW RLAYOUT
                        RelativeLayout new_rl = new RelativeLayout(cont);
                        new_rl.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.MATCH_PARENT));
                        new_rl.setId(R.id.relativeLayout+i);

                        //NEW CARD
                        CardView.LayoutParams new_card_params = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT,
                                CardView.LayoutParams.WRAP_CONTENT);
                        CardView new_card = new CardView(cont);
                        ViewGroup.MarginLayoutParams new_card_margins = (ViewGroup.MarginLayoutParams) customer_btn.getLayoutParams();

                        new_card_margins.topMargin = -40;
                        new_card.setLayoutParams(new_card_margins);
                        new_card.setLayoutParams(new_card_params);
                        new_card.setId(R.id.customer_btn+i);
                        new_card.setRadius(60);
                        new_card.setElevation(40);
                        new_card.setPreventCornerOverlap(true);
                        new_card.setUseCompatPadding(true);

                        new_ll.addView(comp);
                        new_ll.addView(per);
                        new_ll.addView(add);
                        new_ll.addView(contact);
                        new_ll.addView(code);
                        new_ll.addView(new_2ll);
                        new_2ll.addView(go);
                        new_rl.addView(new_ll);
                        new_card.addView(new_rl);
                        parentL.addView(new_card);

                        go.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(cont, ChequePickUp.class);
                                LocationManagement lm = new LocationManagement(cont);
                                LocationSession ls = new LocationSession(String.valueOf(comp.getText()), String.valueOf(per.getText()),
                                        String.valueOf(add.getText()),String.valueOf(contact.getText()), String.valueOf(code.getText()));
                                lm.saveLocation(ls);
                                startActivity(intent);
                                //getActivity().finish();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}