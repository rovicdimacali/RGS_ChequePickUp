package com.example.rgs_chequepickup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import SessionPackage.LocationManagement;
import SessionPackage.cancelManagement;
import SessionPackage.cancelSession;

public class CancelActivity extends AppCompatActivity {

    DatePickerDialog datePickerDialog;
    Button datepicker, submit, timepicker1, timepicker2;
    TextView back_button;
    LinearLayout datefield, proof, timefield;
    RadioButton absentRB, reschedRB, diffRB, longRB, othersRB, noChequeRB, collRB;
    EditText cancelText, point, nocheck;
    String cancelStatus;
    String reason;
    FusedLocationProviderClient fspc;
    Intent i;

    private final static int REQUEST_CODE = 100;

    double cur_lat, cur_long, des_lat, des_long;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);

        //LocationManagement lm = new LocationManagement(CancelActivity.this);

        fspc = LocationServices.getFusedLocationProviderClient(this);

        //LAYOUT FOR DATEFIELD
        datefield = findViewById(R.id.date_field);
        timefield = findViewById(R.id.time_field);
        //timefieldTo = (LinearLayout) findViewById(R.id.time_field_to);
        proof = findViewById(R.id.proof_field);
        point = findViewById(R.id.point);
        nocheck = findViewById(R.id.nocheck_text);

        //RADIO BUTTONS
        absentRB = findViewById(R.id.client_not_around);
        noChequeRB = findViewById(R.id.no_cheque);
        collRB = findViewById(R.id.collected);
        reschedRB = findViewById(R.id.reschedule);
        datepicker = findViewById(R.id.datePickerButton);
        timepicker1 = findViewById(R.id.timePickerButton1);
        timepicker2 = findViewById(R.id.timePickerButton2);
        diffRB = findViewById(R.id.wrong_add);
        longRB = findViewById(R.id.unvisited);
        //cutoffRB = (RadioButton) findViewById(R.id.cut_off);
        othersRB = findViewById(R.id.others);
        cancelText = findViewById(R.id.other_reason);

        //absentRB.setText(lm.getAdd());
        //BUTTONS
        back_button = findViewById(R.id.back_button);
        submit = findViewById(R.id.submit_btn);
        datepicker.setText(getTodayDate());

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        back_button.setTypeface(font);
        back_button.setText("\uf060");

        initDatePicker();

        //RADIO BUTTONS ONCLICKS
        CompoundButton.OnCheckedChangeListener cbl = (buttonView, isChecked) -> {
            if (absentRB.isChecked() || reschedRB.isChecked() || diffRB.isChecked() || longRB.isChecked() ||
                    noChequeRB.isChecked() || collRB.isChecked() || othersRB.isChecked()) {
                if (absentRB.isChecked() || collRB.isChecked()) {
                    nocheck.setVisibility(View.GONE);
                    nocheck.setHint("Enter additional details");
                    nocheck.setText("");
                    datefield.setVisibility(View.GONE);
                    timefield.setVisibility(View.GONE);
                    proof.setVisibility(View.VISIBLE);
                    cancelText.setEnabled(false);
                    cancelText.setHint(" ");
                    cancelText.setText("");
                }
                else if(noChequeRB.isChecked()){
                    nocheck.setVisibility(View.VISIBLE);
                    nocheck.setHint("Enter additional details");
                    proof.setVisibility(View.VISIBLE);
                    datefield.setVisibility(View.GONE);
                    timefield.setVisibility(View.GONE);
                    cancelText.setEnabled(false);
                    cancelText.setHint(" ");
                    cancelText.setText("");
                }
                else if (reschedRB.isChecked()) { // DISPLAY DATE PICKER WHEN SELECTED
                    nocheck.setVisibility(View.GONE);
                    nocheck.setHint("Enter additional details");
                    nocheck.setText("");
                    datefield.setVisibility(View.VISIBLE);
                    timefield.setVisibility(View.VISIBLE);
                    proof.setVisibility(View.GONE);
                    point.setText("");
                    cancelText.setEnabled(false);
                    cancelText.setHint(" ");
                    cancelText.setText("");
                    //cancelText.setHint("Enter Reason Here");
                } else if (othersRB.isChecked()) {
                    nocheck.setVisibility(View.GONE);
                    nocheck.setHint("Enter additional details");
                    nocheck.setText("");
                    datefield.setVisibility(View.GONE);
                    timefield.setVisibility(View.GONE);
                    proof.setVisibility(View.GONE);
                    point.setText("");
                    cancelText.setEnabled(true);
                    cancelText.setHint("Enter reason");
                } else { // HIDE DATE PICKER AND TEXT FIELD WHEN NOT SELECTED
                    nocheck.setVisibility(View.GONE);
                    nocheck.setHint("Enter additional details");
                    nocheck.setText("");
                    datefield.setVisibility(View.GONE);
                    timefield.setVisibility(View.GONE);
                    proof.setVisibility(View.GONE);
                    point.setText("");
                    cancelText.setEnabled(false);
                    cancelText.setHint(" ");
                    cancelText.setText("");
                    //cancelText.setHint("Enter Reason Here");
                }
                submit.setEnabled(true);
                submit.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.rgs_green));
            }
        };

        reschedRB.setOnCheckedChangeListener(cbl);
        othersRB.setOnCheckedChangeListener(cbl);
        absentRB.setOnCheckedChangeListener(cbl);
        diffRB.setOnCheckedChangeListener(cbl);
        longRB.setOnCheckedChangeListener(cbl);
        noChequeRB.setOnCheckedChangeListener(cbl);
        //cutoffRB.setOnCheckedChangeListener(cbl);
        collRB.setOnCheckedChangeListener(cbl);

        datepicker.setOnClickListener(v -> datePickerDialog.show());

        timepicker1.setOnClickListener(v -> openTime1());

        timepicker2.setOnClickListener(v -> openTime2());

        back_button.setOnClickListener(v -> {
            //lm.removeLocation();
            Intent intent = new Intent(CancelActivity.this, ChequePickUp.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        submit.setOnClickListener(v -> {
            if ((absentRB.isChecked() && !point.getText().toString().isEmpty()) || noChequeRB.isChecked()
            || collRB.isChecked() || diffRB.isChecked()) {
                getCurrentLocation();
            } else if (reschedRB.isChecked()) {
                Intent i = new Intent(CancelActivity.this, Failed.class);
                cancelManagement cm = new cancelManagement(CancelActivity.this);
                cancelSession cs = new cancelSession("Rescheduled By Client: " + datepicker.getText().toString() + ", " + timepicker1.getText().toString() + "-" +
                        timepicker2.getText().toString(), "none");
                cm.saveCancel(cs);
                //i.putExtra("cancel", "Rescheduled by client on " + datepicker.getText().toString() + ", "  + timepicker.getText().toString());
                startActivity(i);
                finish();
            } else if (longRB.isChecked()) {
                Intent i = new Intent(CancelActivity.this, Failed.class);
                cancelManagement cm = new cancelManagement(CancelActivity.this);
                cancelSession cs = new cancelSession("Unattended By Rider", "none");
                cm.saveCancel(cs);
                //i.putExtra("cancel", "Unattended - Prolonged Transaction");
                startActivity(i);
                finish();
            } else if (othersRB.isChecked() && !(cancelText.getText().toString().isEmpty())) {
                cancelStatus = cancelText.getText().toString();
                Intent i = new Intent(CancelActivity.this, Failed.class);
                cancelManagement cm = new cancelManagement(CancelActivity.this);
                cancelSession cs = new cancelSession(cancelStatus, "none");
                cm.saveCancel(cs);
                //i.putExtra("cancel", cancelStatus);
                startActivity(i);
                finish();
            } else if (!(othersRB.isChecked() || reschedRB.isChecked() || diffRB.isChecked() ||
                    longRB.isChecked() || noChequeRB.isChecked() || collRB.isChecked()) && (absentRB.isChecked() && point.getText().toString().isEmpty())) {
                Toast.makeText(CancelActivity.this, "Please fill up the field", Toast.LENGTH_SHORT).show();
            } else if (!(absentRB.isChecked() || reschedRB.isChecked() || diffRB.isChecked() ||
                    longRB.isChecked() || noChequeRB.isChecked() || collRB.isChecked()) && (othersRB.isChecked() && cancelText.getText().toString().isEmpty() || cancelText.getText().toString().equals(" "))) {
                Toast.makeText(CancelActivity.this, "Please fill up the field", Toast.LENGTH_SHORT).show();
            } else if (!(absentRB.isChecked() || reschedRB.isChecked() || diffRB.isChecked() ||
                    longRB.isChecked() || othersRB.isChecked() || noChequeRB.isChecked() || collRB.isChecked())) {
                Toast.makeText(CancelActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
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
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            datepicker.setText(date);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        Date fromDate = cal.getTime();

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month,day);
        datePickerDialog.getDatePicker().setMinDate(fromDate.getTime());
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
            return "JUN";
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

    public void openTime1() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                AlertDialog.THEME_HOLO_LIGHT,
                (view, hourOfDay, minute1) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute1);

                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    String selectedTime = sdf.format(calendar.getTime());
                    timepicker1.setText(selectedTime);
                }, hour, minute, false);

        timePickerDialog.show();
    }

    public void openTime2() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                AlertDialog.THEME_HOLO_LIGHT,
                (view, hourOfDay, minute1) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute1);

                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    String selectedTime = sdf.format(calendar.getTime());
                    timepicker2.setText(selectedTime);
                }, hour, minute, false);

        timePickerDialog.show();
    }

    private void getCurrentLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){
            fspc.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    Geocoder gc = new Geocoder(CancelActivity.this, Locale.getDefault());
                    List<Address> sadd;
                    List<Address> dadd;

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
                        if (distance < 6000) {
                            if(absentRB.isChecked()){
                                reason = "Person in Charge Not Available";
                                i = new Intent(CancelActivity.this, ESignature.class);
                            }
                            else if(noChequeRB.isChecked()){
                                reason = "Cheque Not Available: " + nocheck.getText().toString();
                                i = new Intent(CancelActivity.this, ESignature.class);
                            }
                            else if(collRB.isChecked()){
                                reason = "Cheque Already Collected";
                                i = new Intent(CancelActivity.this, ESignature.class);
                            }
                            else if(diffRB.isChecked()){
                                reason = "Wrong Collection Address";
                                point.setText("none");
                                i = new Intent(CancelActivity.this, Failed.class);
                            }

                            cancelManagement cm = new cancelManagement(CancelActivity.this);
                            cancelSession cs = new cancelSession(reason,point.getText().toString());
                            cm.saveCancel(cs);
                            startActivity(i);
                            finish();
                        } else {
                            //NotArrivedPopupWindow();
                            Toast.makeText(CancelActivity.this, "Must be at the destination first", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        NoSignalPopupWindow();
                        //throw new RuntimeException(e);
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

    private void NoSignalPopupWindow() {
        RelativeLayout layout = findViewById(R.id.layout);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.popup_unstable, null);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);
        layout.post(() -> popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0));

        Button retry = popUpView.findViewById(R.id.retry);
        retry.setOnClickListener(v -> {
            recreate();
            popupWindow.dismiss();
        });
    }

}