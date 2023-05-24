package SessionPackage;

import android.content.Context;
import android.content.SharedPreferences;

public class LocationManagement {
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_COMP = "session_loc";
    String SESSION_PER = "session_per";
    String SESSION_ADD = "session_add";
    String SESSION_CONT = "session_cont";
    String SESSION_CODE = "session_code";
    public LocationManagement(Context context){
        sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void saveLocation(LocationSession loc){
        String comp = loc.getCompany();
        String per = loc.getPerson();
        String add = loc.getAddress();
        String cont = loc.getContact();
        String code = loc.getCode();

        editor.putString(SESSION_COMP, comp).commit();
        editor.putString(SESSION_PER, per).commit();
        editor.putString(SESSION_ADD, add).commit();
        editor.putString(SESSION_CONT, cont).commit();
        editor.putString(SESSION_CODE, code).commit();
    }

    public String getComp(){ //return saved session
        return sp.getString(SESSION_COMP, "none");
    }
    public String getPer(){ //return saved session
        return sp.getString(SESSION_PER, "none");
    }
    public String getAdd(){ //return saved session
        return sp.getString(SESSION_ADD, "none");
    }
    public String getCont(){ //return saved session
        return sp.getString(SESSION_CONT, "none");
    }
    public String getCode(){ //return saved session
        return sp.getString(SESSION_CODE, "none");
    }

    public void removeLocation(){ //Logout
        editor.putString(SESSION_COMP, "none").commit();
        editor.putString(SESSION_PER, "none").commit();
        editor.putString(SESSION_ADD, "none").commit();
        editor.putString(SESSION_CONT, "none").commit();
        editor.putString(SESSION_CODE, "none").commit();
    }
}
