package SessionPackage;

import android.content.Context;
import android.content.SharedPreferences;

public class accountManagement {
    final SharedPreferences sp;
    final SharedPreferences.Editor editor;
    final String SHARED_PREF_NAME = "session";
    final String SESSION_ACCNO = "session_accno";
    final String SESSION_ENTITY = "session_entity";

    public accountManagement(Context context){
        sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void saveAccount(accountSession as){
        String accno = as.getAccno();
        String entity = as.getEntity();

        editor.putString(SESSION_ACCNO, accno).commit();
        editor.putString(SESSION_ENTITY, entity).commit();
    }
    public String getAccno(){ //return saved session
        return sp.getString(SESSION_ACCNO, "none");
    }
    public void removeAcc(){ //Logout
        editor.putString(SESSION_ACCNO, "none").commit();
        editor.putString(SESSION_ENTITY, "none").commit();
    }
}
