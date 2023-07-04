package SessionPackage;

import android.content.Context;
import android.content.SharedPreferences;

public class SignatureManagement {
    final SharedPreferences sp;
    final SharedPreferences.Editor editor;
    final String SHARED_PREF_NAME = "session";
    final String SESSION_SIGN = "session_sign";

    public SignatureManagement(Context context){
        sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void saveSign(SignatureSession ss){
        String sign = ss.getSign();

        editor.putString(SESSION_SIGN, sign).commit();
    }
    public String getSign(){ //return saved session
        return sp.getString(SESSION_SIGN, "none");
    }
    public void removeSign(){ //Logout
        editor.putString(SESSION_SIGN, "none").commit();
    }
}
