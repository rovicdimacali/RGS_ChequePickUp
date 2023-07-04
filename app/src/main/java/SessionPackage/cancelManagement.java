package SessionPackage;

import android.content.Context;
import android.content.SharedPreferences;

public class cancelManagement {
    final SharedPreferences sp;
    final SharedPreferences.Editor editor;
    final String SHARED_PREF_NAME = "session";
    final String SESSION_CANCEL = "session_cancel";
    final String SESSION_POINT = "session_point";

    public cancelManagement(Context context){
        sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void saveCancel(cancelSession cs){
        String cancel = cs.getCancel();
        String point = cs.getPointPerson();

        editor.putString(SESSION_CANCEL, cancel).commit();
        editor.putString(SESSION_POINT, point).commit();
    }
    public String getCancel(){ //return saved session
        return sp.getString(SESSION_CANCEL, "none");
    }
    public String getPoint(){ //return saved session
        return sp.getString(SESSION_POINT, "none");
    }
    public void removeCancel(){
        editor.putString(SESSION_CANCEL, "none").commit();
        editor.putString(SESSION_POINT, "none").commit();
    }
}
