package com.example.rgs_chequepickup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import SessionPackage.LocationManagement;

public class CancelActivity extends AppCompatActivity {

    DatePickerDialog datePickerDialog;
    Button datepicker, submit;
    TextView back_button;
    LinearLayout datefield;
    RadioButton absentRB, reschedRB, diffRB, longRB, othersRB;
    EditText cancelText;
    String cancelStatus;

    FusedLocationProviderClient fspc;

    private final static int REQUEST_CODE = 100;

    double cur_lat, cur_long, des_lat, des_long;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);

        LocationManagement lm = new LocationManagement(CancelActivity.this);

        fspc = LocationServices.getFusedLocationProviderClient(this);

        //LAYOUT FOR DATEFIELD
        datefield = (LinearLayout) findViewById(R.id.date_field);

        //RADIO BUTTONS
        absentRB = (RadioButton) findViewById(R.id.client_not_around);
        reschedRB = (RadioButton) findViewById(R.id.reschedule);
        diffRB = (RadioButton) findViewById(R.id.rider_problem);
        longRB = (RadioButton) findViewById(R.id.unattended);
        othersRB = (RadioButton) findViewById(R.id.others);

        //absentRB.setText(lm.getAdd());
        //BUTTONS
        datepicker = (Button) findViewById(R.id.datePickerButton);
        back_button = (TextView) findViewById(R.id.back_button);
        submit = (Button) findViewById(R.id.submit_btn);
        datepicker.setText(getTodayDate());

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        back_button.setTypeface(font);
        back_button.setText("\uf060");
        cancelText = (EditText) findViewById(R.id.other_reason);
        initDatePicker();

        //RADIO BUTTONS ONCLICKS
        CompoundButton.OnCheckedChangeListener cbl = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(absentRB.isChecked() || reschedRB.isChecked() || diffRB.isChecked() || longRB.isChecked() || othersRB.isChecked()){
                    if(reschedRB.isChecked()){ // DISPLAY DATE PICKER WHEN SELECTED
                        datefield.setVisibility(View.VISIBLE);
                        cancelText.setEnabled(false);
                        cancelText.setHint(" ");
                        cancelText.setText(" ");
                        //cancelText.setHint("Enter Reason Here");
                    }
                    else if(othersRB.isChecked()){
                        datefield.setVisibility(View.GONE);
                        cancelText.setEnabled(true);
                        cancelText.setHint("Enter Reason Here");
                    }
                    else{ // HIDE DATE PICKER AND TEXT FIELD WHEN NOT SELECTED
                        datefield.setVisibility(View.GONE);
                        cancelText.setEnabled(false);
                        cancelText.setHint(" ");
                        cancelText.setText(" ");
                        //cancelText.setHint("Enter Reason Here");
                    }
                    submit.setEnabled(true);
                    submit.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_green));
                }
            }
        };

        reschedRB.setOnCheckedChangeListener(cbl);
        othersRB.setOnCheckedChangeListener(cbl);
        absentRB.setOnCheckedChangeListener(cbl);
        diffRB.setOnCheckedChangeListener(cbl);
        longRB.setOnCheckedChangeListener(cbl);

        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //lm.removeLocation();
                Intent intent = new Intent(CancelActivity.this, ChequePickUp.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(absentRB.isChecked()){
                    getCurrentLocation();
                }
                else if(reschedRB.isChecked()){
                    LocationManagement lm = new LocationManagement(CancelActivity.this);
                    Intent i = new Intent(CancelActivity.this, Failed.class);
                    i.putExtra("cancel", "Rescheduled by client on " + datepicker.getText().toString());
                    startActivity(i);
                }
                else if(diffRB.isChecked()){
                    LocationManagement lm = new LocationManagement(CancelActivity.this);
                    Intent i = new Intent(CancelActivity.this, Failed.class);
                    i.putExtra("cancel", "Rider Mechanical Difficulties");
                    startActivity(i);
                }
                else if(longRB.isChecked()){
                    LocationManagement lm = new LocationManagement(CancelActivity.this);
                    Intent i = new Intent(CancelActivity.this, Failed.class);
                    i.putExtra("cancel", "Unattended - Prolonged Transaction");
                    startActivity(i);
                }
                else if(othersRB.isChecked() && !(cancelText.getText().toString().isEmpty())){
                    cancelStatus = cancelText.getText().toString();
                    LocationManagement lm = new LocationManagement(CancelActivity.this);
                    Intent i = new Intent(CancelActivity.this, Failed.class);
                    i.putExtra("cancel", cancelStatus);
                    startActivity(i);
                }
                else if(!(absentRB.isChecked() || reschedRB.isChecked() || diffRB.isChecked() ||
                        longRB.isChecked()) && (othersRB.isChecked() && cancelText.getText().toString().isEmpty())){
                    Toast.makeText(CancelActivity.this, "Please fill up the field", Toast.LENGTH_SHORT).show();
                }
                else if(!(absentRB.isChecked() || reschedRB.isChecked() || diffRB.isChecked() ||
                        longRB.isChecked() || othersRB.isChecked())){
                    Toast.makeText(CancelActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
                }
                //else if()
                //Intent intent = new Intent(CancelActivity.this, Failed.class);
                //startActivity(intent);
            }
        });
    }

    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                datepicker.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month,day);
    }

    private String makeDateString(int day, int month, int year){
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1){
            return "JAN";
        }
        else if (month == 2){
            return "FEB";
        }
        else if (month == 3){
            return "MAR";
        }
        else if (month == 4){
            return "APR";
        }
        else if (month == 5){
            return "MAY";
        }
        else if (month == 6){
            return "JuN";
        }
        else if (month == 7){
            return "JUL";
        }
        else if (month == 8){
            return "AUG";
        }
        else if (month == 9){
            return "SEP";
        }
        else if (month == 10){
            return "OCT";
        }
        else if (month == 11){
            return "NOV";
        }
        else if (month == 12){
            return "DEC";
        }
        return "JAN";
    }

    private void getCurrentLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){
            fspc.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Geocoder gc = new Geocoder(CancelActivity.this, Locale.getDefault());
                        List<Address> sadd = null;
                        List<Address> dadd = null;

                        LocationManagement lm = new LocationManagement(CancelActivity.this);
                        try {
                            //CURRENT LOCATION
                            sadd = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            Location sp = new Location("currLoc");
                            cur_lat = sadd.get(0).getLatitude();
                            cur_long = sadd.get(0).getLongitude();
                            sp.setLatitude(cur_lat);
                            sp.setLongitude(cur_long);
                            //DESTINATION
                            dadd = gc.getFromLocationName(lm.getAdd(), 1);
                            Location ep = new Location("destination");
                            des_lat = dadd.get(0).getLatitude();
                            des_long = dadd.get(0).getLongitude();
                            ep.setLatitude(des_lat);
                            ep.setLongitude(des_long);

                            double distance = sp.distanceTo(ep);
                            //address.setText(String.valueOf(distance));
                            if (distance < 99999) {
                                //Toast.makeText(ChequePickUp.this, "You're 100m near at your destination", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(CancelActivity.this, Failed.class);
                                i.putExtra("cancel", "Client/Customer Not Around");
                                startActivity(i);
                            } else {
                                //NotArrivedPopupWindow();
                                Toast.makeText(CancelActivity.this, "Must be at the destination first", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }
        else{
            askPermission();
        }
    }
    private void askPermission(){
        ActivityCompat.requestPermissions(CancelActivity.this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
            else{
                Toast.makeText(CancelActivity.this,"Required Permission", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}