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

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import SessionPackage.LocationManagement;
import SessionPackage.LocationSession;
import SessionPackage.SessionManagement;
import SessionPackage.UserSession;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    private int ctr = 5;
    Context cont;
    Button go_button;
    ViewFlipper carousel;
    LinearLayout layout;
    TextView comp1,p1,ad1,cont1, next_arr, back_arr;
    OkHttpClient client;
    String responseData;
    ArrayList<String> compArr = new ArrayList<String>();
    ArrayList<String> personArr = new ArrayList<String>();
    ArrayList<String> contactArr = new ArrayList<String>();
    ArrayList<String> addressArr = new ArrayList<String>();
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
        cont = getContext();
        client = new OkHttpClient();
        SessionManagement sm = new SessionManagement(cont);
        String rider = sm.getSession();
        Toast.makeText(cont, "rider " + rider, Toast.LENGTH_SHORT).show();
        //post(rider);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        layout = view.findViewById(R.id.linearlayout);

        CardView customer_btn = (CardView) view.findViewById(R.id.customer_btn);

        TextView back_arrow, next_arrow;

        back_arrow = (TextView) view.findViewById(R.id.back_arrow);

        next_arrow = (TextView) view.findViewById(R.id.next_arrow);

        Typeface font = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/fontawesome-webfont.ttf");

        back_arrow.setTypeface(font);
        next_arrow.setTypeface(font);
        back_arrow.setText("\uf060");
        next_arrow.setText("\uf061");

        go_button = (Button) customer_btn.findViewById(R.id.go_button);

        next_arr = (TextView) customer_btn.findViewById(R.id.next_arrow);
        back_arr = (TextView) customer_btn.findViewById(R.id.back_arrow);

        carousel = (ViewFlipper) view.findViewById(R.id.carousel);
        //ViewGroup.MarginLayoutParams carousel_margins = (ViewGroup.MarginLayoutParams) carousel.getLayoutParams();
        //carousel_margins.setMarginStart(30);
        //carousel_margins.setMarginEnd(30);
        //carousel.setLayoutParams(carousel_margins);

        //DETAILS
        comp1 = (TextView) customer_btn.findViewById(R.id.companyname);
        p1 = (TextView) customer_btn.findViewById(R.id.companyperson);
        ad1 = (TextView) customer_btn.findViewById(R.id.companyadd);
        cont1 = (TextView) customer_btn.findViewById(R.id.companycontact);

        //COMPANY ARRAY
        /*String compArr[] = {"PARAMOUNT HOTELS & FACILITIES MANAGEMENT COMPANY, INC.","PREMIERE MEDICAL AND CARDIOVASCULAR LABORATORY, INC."
        ,"REJ DIAMOND PHARMACEUTICALS, INC.","AYALA LAND METRO NORTH, INC","MEGAWIDE CONSTRUCTION CORP.","PINAKAMASARAP CORPORATION"};
        //PERSON ARRAY
        String personArr[] = {"Ms. Norly Araneta","Avegail Ferasol","Charmaine De Jesus","Ranny A. Pimentel",
                "Rebecca Aycocho","Rowena Bahillo"};
        //ADDRESS ARRAY
        String addressArr[] = {"G/F Microtel Bldg. UP Ayala Land Technohub Commonwealth Ave., Diliman Quezon City",
                "G/F Bell-Kenz Tower 127 Malakas st. Diliman, Quezon City","#8 Feria Road Commonwealth Ave. Diliman QC",
                "249, U.P Town Center, 216 katipunan Ave., Diliman Quezon city", "# 20 N. Domingo St., Brgy. Valencia, Quezon City",
                "#23 P. Dela St. San Bartolome Novaliches Quezon City"};
        //CONTACT ARRAY
        String contactArr[] = {"9178434921","9176245003","9175876919","9178643759",
                "9178544177","9175694437"};*/

        String api = "http://203.177.49.26:28110/tracker/api/accounts";
        JsonObjectRe
        comp1.setText(compArr.get(0));
        p1.setText(personArr.get(0));
        ad1.setText(addressArr.get(0));
        cont1.setText("+63"+ contactArr.get(0));

        for(int i = 1; i < 5; i++){ //LOOP CARDS
            //BUTTONS
            TextView back = new TextView(cont);
            LinearLayout.LayoutParams backbtn = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            ViewGroup.MarginLayoutParams back_margins = (ViewGroup.MarginLayoutParams) back_arr.getLayoutParams();

            back_margins.topMargin = 20;
            float back_weight = 1f;
            //backbtn.setMarginStart(20);
            backbtn.weight = back_weight;
            back.setLayoutParams(back_margins);
            back.setLayoutParams(backbtn);
            back.setId(R.id.back_arrow+i);
            back.setTypeface(ResourcesCompat.getFont(cont,R.font.poppins_bold));
            back.setText("BACK");
            back.setTextSize(20);
            //back.setTextAlignment(TextView.TEXT_ALIGNMENT_TEXT_END);
            back.setTypeface(font);
            back.setText("\uf060");

            TextView skip = new TextView(cont);
            LinearLayout.LayoutParams skipbtn = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            ViewGroup.MarginLayoutParams skip_margins = (ViewGroup.MarginLayoutParams) next_arr.getLayoutParams();

            skip_margins.topMargin = 20;
            float skip_weight = 1f;
            skipbtn.setMarginStart(20);
            skipbtn.weight = skip_weight;
            skip.setLayoutParams(skip_margins);
            skip.setLayoutParams(skipbtn);
            skip.setId(R.id.next_arrow+i);
            skip.setTypeface(ResourcesCompat.getFont(cont,R.font.poppins_bold));
            skip.setText("NEXT");
            skip.setTextSize(20);
            skip.setTextAlignment(TextView.TEXT_ALIGNMENT_TEXT_END);
            skip.setTypeface(font);
            skip.setText("\uf061");
            //skip.setTypeface(null, Typeface.BOLD);

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
            go.setId(R.id.go_button+i);
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
            TextView contact = new TextView(cont);
            LinearLayout.LayoutParams conttext = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            contact.setLayoutParams(conttext);
            contact.setText("+63"+ contactArr.get(i));
            contact.setTextColor(Color.BLACK);
            contact.setTypeface(ResourcesCompat.getFont(cont,R.font.poppins));
            contact.setTextSize(15);

            TextView add = new TextView(cont);
            LinearLayout.LayoutParams addtext = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            add.setLayoutParams(addtext);
            add.setText(addressArr.get(i));
            add.setTextColor(Color.BLACK);
            add.setTypeface(ResourcesCompat.getFont(cont,R.font.poppins));
            add.setTextSize(15);

            TextView per = new TextView(cont);
            LinearLayout.LayoutParams pertext = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            per.setLayoutParams(pertext);
            per.setText(personArr.get(i));
            per.setTextColor(Color.BLACK);
            per.setTypeface(ResourcesCompat.getFont(cont,R.font.poppins));
            per.setTextSize(18);

            TextView comp = new TextView(cont);
            LinearLayout.LayoutParams comptext = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            comp.setLayoutParams(comptext);
            comp.setText(compArr.get(i));
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
            //LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) new_card.getLayoutParams();
            //lp.setMargins(0,10,0,0);

            //ViewGroup.MarginLayoutParams carousel_margins = (ViewGroup.MarginLayoutParams) carousel.getLayoutParams();
            //carousel_margins.setMarginStart(20);
            //carousel_margins.setMarginEnd(20);

            new_ll.addView(comp);
            new_ll.addView(per);
            new_ll.addView(add);
            new_ll.addView(contact);
            new_ll.addView(new_2ll);
            new_2ll.addView(back);
            new_2ll.addView(go);
            new_2ll.addView(skip);
            new_rl.addView(new_ll);
            new_card.addView(new_rl);
            carousel.addView(new_card);
            //carousel.setLayoutParams(carousel_margins);
            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    carousel.showNext();
                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    carousel.showPrevious();
                }
            });

            go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ChequePickUp.class);
                    LocationManagement lm = new LocationManagement(cont);
                    LocationSession ls = new LocationSession(String.valueOf(comp.getText()), String.valueOf(per.getText()),
                            String.valueOf(add.getText()),String.valueOf(contact.getText()));
                    lm.saveLocation(ls);
                /*intent.putExtra("company", comp1.getText().toString());
                intent.putExtra("person", p1.getText().toString());
                intent.putExtra("address", ad1.getText().toString());
                intent.putExtra("contact", cont1.getText().toString());*/
                    startActivity(intent);
                }
            });
        } //END OF LOOP CARDS
        go_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChequePickUp.class);
                LocationManagement lm = new LocationManagement(cont);
                LocationSession ls = new LocationSession(String.valueOf(comp1.getText()), String.valueOf(p1.getText()),
                        String.valueOf(ad1.getText()),String.valueOf(cont1.getText()));
                lm.saveLocation(ls);
                /*intent.putExtra("company", comp1.getText().toString());
                intent.putExtra("person", p1.getText().toString());
                intent.putExtra("address", ad1.getText().toString());
                intent.putExtra("contact", cont1.getText().toString());*/
                startActivity(intent);
            }
        });

        next_arr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carousel.showNext();
            }
        });
        back_arr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carousel.showPrevious();
            }
        });
        //carousel.setAutoStart(true);
        //carousel.setFlipInterval(1000);
        //carousel.startFlipping();
        //TEXTVIEWS
        //

        return view;
    }

    /*public void post(String rID){
        RequestBody rbody = new FormBody.Builder()
                .add("riderID", rID)
                .build();
        Request req = new Request.Builder().url("http://203.177.49.26:28110/tracker/api/accounts").post(rbody).build();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(cont, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                };
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            responseData = response.body().string();
                            JSONObject json = new JSONObject(responseData);
                            compArr.add(json.getString( "company_name"));
                            personArr.add(json.getString( "fullname"));
                            contactArr.add(json.getString( "contact_no"));
                            addressArr.add(json.getString( "address"));
                        } catch (IOException | JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
            }
        });
    }*/
}