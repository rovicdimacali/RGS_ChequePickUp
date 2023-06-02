package SessionPackage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ReceiptManagement {
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_TIN = "session_tin";
    String SESSION_AMOUNT = "session_amount";
    String SESSION_NUMBER = "session_number";
    String SESSION_PAYEE = "session_payee";
    String SESSION_BCODE = "session_bcode";
    String SESSION_ORNUM = "session_or";
    String SESSION_DATE = "session_date";


    public ReceiptManagement(Context context){
        sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void saveReceipt(ReceiptSession rs){
        String tin = rs.getTin();
        String amount = rs.getAmount();
        String number = rs.getNumber();
        String payee = rs.getPayee();
        String bcode = rs.getBcode();
        String ornum = rs.getOrnum();
        String date = rs.getDate();

        //Gson gson = new Gson();

        editor.putString(SESSION_TIN, tin).commit();
        editor.putString(SESSION_AMOUNT, amount).commit();
        editor.putString(SESSION_NUMBER, number).commit();
        editor.putString(SESSION_PAYEE, payee).commit();
        editor.putString(SESSION_BCODE, bcode).commit();
        editor.putString(SESSION_ORNUM, ornum).commit();
        editor.putString(SESSION_DATE, date).commit();
    }

    public String getTin(){ //return saved session
        return sp.getString(SESSION_TIN, "none");
    }
    public String getAmount(){ //return saved session
        return sp.getString(SESSION_AMOUNT, "none");
    }
    public String getNumber(){ //return saved session
        return sp.getString(SESSION_NUMBER, "none");
    }
    public String getPayee(){ //return saved session
        return sp.getString(SESSION_PAYEE, "none");
    }
    public String getBcode(){ //return saved session
        return sp.getString(SESSION_BCODE, "none");
    }
    public String getOR(){ //return saved session
        return sp.getString(SESSION_ORNUM, "none");
    }
    public String getDate(){ //return saved session
        return sp.getString(SESSION_DATE, "none");
    }

    public void removeReceipt(){
        editor.putString(SESSION_TIN, "none").commit();
        editor.putString(SESSION_AMOUNT, "none").commit();
        editor.putString(SESSION_NUMBER, "none").commit();
        editor.putString(SESSION_PAYEE, "none").commit();
        editor.putString(SESSION_BCODE, "none").commit();
        editor.putString(SESSION_ORNUM, "none").commit();
        editor.putString(SESSION_DATE, "none").commit();
    }
}
