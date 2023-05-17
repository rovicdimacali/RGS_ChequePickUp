package com.example.rgs_chequepickup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

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
import android.widget.ViewFlipper;

import org.w3c.dom.Text;

import SessionPackage.LocationManagement;
import SessionPackage.LocationSession;

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
    Button go_button, skip_button;
    ViewFlipper carousel;
    LinearLayout layout;
    TextView comp1,p1,ad1,cont1;

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
        String args[] = {"One", "Two", "Three"};

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        layout = view.findViewById(R.id.linearlayout);

        CardView customer_btn = (CardView) view.findViewById(R.id.customer_btn);

        go_button = (Button) customer_btn.findViewById(R.id.go_button);

        skip_button = (Button) customer_btn.findViewById(R.id.skip_button);

        carousel = (ViewFlipper) view.findViewById(R.id.carousel);

        //DETAILS
        comp1 = (TextView) customer_btn.findViewById(R.id.companyname);
        p1 = (TextView) customer_btn.findViewById(R.id.companyperson);
        ad1 = (TextView) customer_btn.findViewById(R.id.companyadd);
        cont1 = (TextView) customer_btn.findViewById(R.id.companycontact);

        /*comp1.setText("Company 1" );
        p1.setText("Person 1");
        ad1.setText("Address 1");
        cont1.setText("Contact 1");*/

        for(int i = 0; i < 4; i++){ //LOOP CARDS
            //BUTTONS
            Button skip = new Button(cont);
            LinearLayout.LayoutParams skipbtn = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            ViewGroup.MarginLayoutParams skip_margins = (ViewGroup.MarginLayoutParams) skip_button.getLayoutParams();

            skip_margins.topMargin = -5;
            float skip_weight = 0.5f;
            skipbtn.setMarginEnd(10);
            skipbtn.weight = skip_weight;
            skip.setLayoutParams(skip_margins);
            skip.setLayoutParams(skipbtn);
            skip.setId(R.id.skip_button+i);
            skip.setBackgroundColor(ContextCompat.getColor(cont, R.color.rgs_gray1));
            skip.setTypeface(ResourcesCompat.getFont(cont,R.font.poppins_bold));
            skip.setText("SKIP");
            //skip.setTypeface(null, Typeface.BOLD);

            Button go = new Button(cont);
            LinearLayout.LayoutParams gobtn = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            ViewGroup.MarginLayoutParams go_margins = (ViewGroup.MarginLayoutParams) go_button.getLayoutParams();

            go_margins.topMargin = -5;
            float go_weight = 0.5f;
            gobtn.setMarginEnd(10);
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
            contact.setText("09123454556 " + i);
            contact.setTextColor(Color.BLACK);
            contact.setTypeface(ResourcesCompat.getFont(cont,R.font.poppins));
            contact.setTextSize(15);

            TextView add = new TextView(cont);
            LinearLayout.LayoutParams addtext = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            add.setLayoutParams(addtext);
            add.setText("Address City of Village Subdivision Hello World " + i);
            add.setTextColor(Color.BLACK);
            add.setTypeface(ResourcesCompat.getFont(cont,R.font.poppins));
            add.setTextSize(15);

            TextView per = new TextView(cont);
            LinearLayout.LayoutParams pertext = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            per.setLayoutParams(pertext);
            per.setText("Mr. Person People " + i);
            per.setTextColor(Color.BLACK);
            per.setTypeface(ResourcesCompat.getFont(cont,R.font.poppins));
            per.setTextSize(18);

            TextView comp = new TextView(cont);
            LinearLayout.LayoutParams comptext = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            comp.setLayoutParams(comptext);
            comp.setText("PARAMOUNT HOTELS " + i);
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
            new_ll.setPadding(70, 50, 70, 50);

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

            new_card_margins.topMargin = -20;
            new_card.setLayoutParams(new_card_margins);
            new_card.setLayoutParams(new_card_params);
            new_card.setId(R.id.customer_btn+i);
            new_card.setRadius(50);
            new_card.setElevation(40);
            new_card.setPreventCornerOverlap(true);
            new_card.setUseCompatPadding(true);
            //LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) new_card.getLayoutParams();
            //lp.setMargins(0,10,0,0);

            new_ll.addView(comp);
            new_ll.addView(per);
            new_ll.addView(add);
            new_ll.addView(contact);
            new_ll.addView(new_2ll);
            new_2ll.addView(go);
            new_2ll.addView(skip);
            new_rl.addView(new_ll);
            new_card.addView(new_rl);
            carousel.addView(new_card);

            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    carousel.showNext();
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

        skip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carousel.showNext();
            }
        });
        //carousel.setAutoStart(true);
        //carousel.setFlipInterval(1000);
        //carousel.startFlipping();
        //TEXTVIEWS
        //

        return view;
    }
}