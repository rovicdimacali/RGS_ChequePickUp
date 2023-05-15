package com.example.rgs_chequepickup;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button go_button, go_button1 , go_button2 , go_button3 , go_button4 , go_button5 , skip_button, skip_button1, skip_button2, skip_button3, skip_button4, skip_button5;
    ViewFlipper carousel;
    TextView comp1, comp2, comp3, comp4, comp5, comp6, p1,p2,p3,p4,p5,p6, ad1,ad2,ad3,ad4,ad5,ad6, cont1,cont2,cont3,cont4,cont5,cont6;

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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        CardView customer_btn = (CardView) view.findViewById(R.id.customer_btn);
        CardView customer_btn1 = (CardView) view.findViewById(R.id.customer_btn1);
        CardView customer_btn2 = (CardView) view.findViewById(R.id.customer_btn2);
        CardView customer_btn3 = (CardView) view.findViewById(R.id.customer_btn3);
        CardView customer_btn4 = (CardView) view.findViewById(R.id.customer_btn4);
        CardView customer_btn5 = (CardView) view.findViewById(R.id.customer_btn5);
        go_button = (Button) customer_btn.findViewById(R.id.go_button);
        go_button1 = (Button) customer_btn1.findViewById(R.id.go_button1);
        go_button2 = (Button) customer_btn2.findViewById(R.id.go_button2);
        go_button3 = (Button) customer_btn3.findViewById(R.id.go_button3);
        go_button4 = (Button) customer_btn4.findViewById(R.id.go_button4);
        go_button5 = (Button) customer_btn5.findViewById(R.id.go_button5);
        skip_button = (Button) customer_btn.findViewById(R.id.skip_button);
        skip_button1 = (Button) customer_btn1.findViewById(R.id.skip_button1);
        skip_button2 = (Button) customer_btn2.findViewById(R.id.skip_button2);
        skip_button3 = (Button) customer_btn3.findViewById(R.id.skip_button3);
        skip_button4 = (Button) customer_btn4.findViewById(R.id.skip_button4);
        skip_button5 = (Button) customer_btn5.findViewById(R.id.skip_button5);
        carousel = (ViewFlipper) view.findViewById(R.id.carousel);

        //TEXTVIEWS
        comp1 = (TextView) customer_btn.findViewById(R.id.companyname1);
        comp2 = (TextView) customer_btn1.findViewById(R.id.companyname2);
        comp3 = (TextView) customer_btn2.findViewById(R.id.companyname3);
        comp4 = (TextView) customer_btn3.findViewById(R.id.companyname4);
        comp5 = (TextView) customer_btn4.findViewById(R.id.companyname5);
        comp6 = (TextView) customer_btn5.findViewById(R.id.companyname6);

        p1 = (TextView) customer_btn.findViewById(R.id.companyperson1);
        p2 = (TextView) customer_btn1.findViewById(R.id.companyperson2);
        p3 = (TextView) customer_btn2.findViewById(R.id.companyperson3);
        p4 = (TextView) customer_btn3.findViewById(R.id.companyperson4);
        p5 = (TextView) customer_btn4.findViewById(R.id.companyperson5);
        p6 = (TextView) customer_btn5.findViewById(R.id.companyperson6);

        ad1 = (TextView) customer_btn.findViewById(R.id.companyadd1);
        ad2 = (TextView) customer_btn1.findViewById(R.id.companyadd2);
        ad3 = (TextView) customer_btn2.findViewById(R.id.companyadd3);
        ad4 = (TextView) customer_btn3.findViewById(R.id.companyadd4);
        ad5 = (TextView) customer_btn4.findViewById(R.id.companyadd5);
        ad6 = (TextView) customer_btn5.findViewById(R.id.companyadd6);

        cont1 = (TextView) customer_btn.findViewById(R.id.companycontact1);
        cont2 = (TextView) customer_btn1.findViewById(R.id.companycontact2);
        cont3 = (TextView) customer_btn2.findViewById(R.id.companycontact3);
        cont4 = (TextView) customer_btn3.findViewById(R.id.companycontact4);
        cont5 = (TextView) customer_btn4.findViewById(R.id.companycontact5);
        cont6 = (TextView) customer_btn5.findViewById(R.id.companycontact6);
        //
        go_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChequePickUp.class);
                intent.putExtra("company", comp1.getText().toString());
                intent.putExtra("person", p1.getText().toString());
                intent.putExtra("address", ad1.getText().toString());
                intent.putExtra("contact", cont1.getText().toString());
                startActivity(intent);
            }
        });

        go_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChequePickUp.class);
                intent.putExtra("company", comp2.getText().toString());
                intent.putExtra("person", p2.getText().toString());
                intent.putExtra("address", ad2.getText().toString());
                intent.putExtra("contact", cont2.getText().toString());
                startActivity(intent);
            }
        });

        go_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChequePickUp.class);
                intent.putExtra("company", comp3.getText().toString());
                intent.putExtra("person", p3.getText().toString());
                intent.putExtra("address", ad3.getText().toString());
                intent.putExtra("contact", cont3.getText().toString());
                startActivity(intent);
            }
        });

        go_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChequePickUp.class);
                intent.putExtra("company", comp4.getText().toString());
                intent.putExtra("person", p4.getText().toString());
                intent.putExtra("address", ad4.getText().toString());
                intent.putExtra("contact", cont4.getText().toString());
                startActivity(intent);
            }
        });

        go_button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChequePickUp.class);
                intent.putExtra("company", comp5.getText().toString());
                intent.putExtra("person", p5.getText().toString());
                intent.putExtra("address", ad5.getText().toString());
                intent.putExtra("contact", cont5.getText().toString());
                startActivity(intent);
            }
        });

        go_button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChequePickUp.class);
                intent.putExtra("company", comp6.getText().toString());
                intent.putExtra("person", p6.getText().toString());
                intent.putExtra("address", ad6.getText().toString());
                intent.putExtra("contact", cont6.getText().toString());
                startActivity(intent);
            }
        });

        skip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carousel.showNext();
            }
        });

        skip_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carousel.showNext();
            }
        });

        skip_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carousel.showNext();
            }
        });

        skip_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carousel.showNext();
            }
        });

        skip_button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carousel.showNext();
            }
        });

        skip_button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carousel.showNext();
            }
        });
        return view;
    }
}