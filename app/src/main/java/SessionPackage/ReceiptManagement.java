package SessionPackage;

import android.content.Context;
import android.content.SharedPreferences;

public class ReceiptManagement {
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_TIN = "session_tin";
    String SESSION_AMOUNT = "session_amount";
    String SESSION_NUMBER = "session_number";

    public ReceiptManagement(Context context){
        sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void saveReceipt(ReceiptSession rs){
        String tin = rs.getTin();
        String amount = rs.getAmount();
        String number = rs.getNumber();

        editor.putString(SESSION_TIN, tin).commit();
        editor.putString(SESSION_AMOUNT, amount).commit();
        editor.putString(SESSION_NUMBER, number).commit();
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

    public void removeReceipt(){
        editor.putString(SESSION_TIN, "none").commit();
        editor.putString(SESSION_AMOUNT, "none").commit();
        editor.putString(SESSION_NUMBER, "none").commit();
    }
}
