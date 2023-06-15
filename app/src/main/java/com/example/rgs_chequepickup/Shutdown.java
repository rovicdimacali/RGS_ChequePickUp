package com.example.rgs_chequepickup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import SessionPackage.SessionManagement;

public class Shutdown extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SessionManagement sm = new SessionManagement(context.getApplicationContext());
        SharedPreferences sharedPreferences = context.getSharedPreferences(sm.getSession(), Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }
}
