package com.example.rgs_chequepickup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Objects;

import SessionPackage.LocationManagement;
import SessionPackage.SessionManagement;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    String mParam1;
    String mParam2;
    TextView profile_name;
    Context cont;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

        cont = getActivity();
        SessionManagement sm = new SessionManagement(Objects.requireNonNull(cont));

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        //RelativeLayout changepass_btn = (RelativeLayout) view.findViewById(R.id.changepass_btn);
        RelativeLayout history_btn = view.findViewById(R.id.history_btn);
        TextView logout_btn = view.findViewById(R.id.logout_btn);

        profile_name = view.findViewById(R.id.profile_name);

        profile_name.setText(sm.getSession());
        //profile_number.setText("09167065890");

        logout_btn.setOnClickListener(v -> {
            cont = getActivity();
            SessionManagement sm1 = new SessionManagement(cont);
            LocationManagement loc_m = new LocationManagement(cont);
            sm1.removeSession();
            loc_m.removeLocation();

            Intent intent = new Intent(cont,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        });

       history_btn.setOnClickListener(v -> {
           Intent intent = new Intent(getActivity(), HistoryActivity.class);
           startActivity(intent);
           getActivity().finish();
           //Toast.makeText(getContext(), "Currently Under Development", Toast.LENGTH_SHORT).show();
       });


        return view;
    }
}