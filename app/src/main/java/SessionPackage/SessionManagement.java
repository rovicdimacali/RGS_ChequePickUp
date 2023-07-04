package SessionPackage;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    final SharedPreferences sp;
    final SharedPreferences.Editor editor;
    final String SHARED_PREF_NAME = "session_login";
    final String SESSION_KEY = "session_user";
    public SessionManagement(Context context){
        sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void saveSession(UserSession user){ //save user's session when logged in
        String activeEmail = user.getEmail();
        editor.putString(SESSION_KEY, activeEmail).commit();
    }

    public String getSession(){ //return saved session

        return sp.getString(SESSION_KEY, "none");
    }

    public void removeSession(){ //Logout

        editor.putString(SESSION_KEY, "none").commit();
    }

}
