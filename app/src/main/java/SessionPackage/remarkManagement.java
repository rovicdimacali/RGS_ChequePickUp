package SessionPackage;

import android.content.Context;
import android.content.SharedPreferences;

public class remarkManagement {
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_remark";
    String email;
    public remarkManagement(Context context){
        sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void saveRemark(remarkSession rs){ //save user's session when logged in
        String remark = rs.getRemark();
        editor.putString(SESSION_KEY, remark).commit();
    }

    public String getRemark(){ //return saved session
        return sp.getString(SESSION_KEY, "none");
    }

    public void removeRemark(){ //Logout
        editor.putString(SESSION_KEY, "none").commit();
    }
}
