package SessionPackage;

import android.content.Context;
import android.content.SharedPreferences;

public class HistoryManagement {
    final SharedPreferences sp;
    final SharedPreferences.Editor editor;
    final String SHARED_PREF_NAME = "session";
    final String SESSION_TRANS = "session_trans";
    final String SESSION_COMP = "session_comp";
    final String SESSION_ADD = "session_add";
    final String SESSION_STAT = "session_stat";
    public HistoryManagement(Context context){
        sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void saveHistory(HistorySession his){
        String trans = his.getTrans();
        String comp = his.getComp();
        String add = his.getAdd();
        String stat = his.getStat();

        editor.putString(SESSION_TRANS, trans).commit();
        editor.putString(SESSION_COMP, comp).commit();
        editor.putString(SESSION_ADD, add).commit();
        editor.putString(SESSION_STAT, stat).commit();
    }

    public String getTrans(){ //return saved session
        return sp.getString(SESSION_TRANS, "none");
    }
    public String getComp(){ //return saved session
        return sp.getString(SESSION_COMP, "none");
    }
    public String getAdd(){ //return saved session
        return sp.getString(SESSION_ADD, "none");
    }
    public String getStat(){ //return saved session
        return sp.getString(SESSION_STAT, "none");
    }

    public void removeLocation(){ //Logout
        editor.putString(SESSION_TRANS, "none").commit();
        editor.putString(SESSION_COMP, "none").commit();
        editor.putString(SESSION_ADD, "none").commit();
        editor.putString(SESSION_STAT, "none").commit();
    }
}
