package SessionPackage;

import android.content.Context;
import android.content.SharedPreferences;

public class chequeManagement {
    final SharedPreferences sp;
    final SharedPreferences.Editor editor;
    final String SHARED_PREF_NAME = "session";
    final String SESSION_CHEQUE = "session_check";

    public chequeManagement(Context context){
        sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void saveCheck(chequeSession cs){
        String cheque = cs.getCheque();

        editor.putString(SESSION_CHEQUE, cheque).commit();
    }
    public String getCheck(){ //return saved session
        return sp.getString(SESSION_CHEQUE, "none");
    }
    public void removeCheck(){ //Logout
        editor.putString(SESSION_CHEQUE, "none").commit();
    }
}
