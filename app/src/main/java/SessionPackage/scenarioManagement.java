package SessionPackage;

import android.content.Context;
import android.content.SharedPreferences;

public class scenarioManagement {
    final SharedPreferences sp;
    final SharedPreferences.Editor editor;
    final String SHARED_PREF_NAME = "session";
    final String SESSION_SCENARIO = "session_scenario";
    final String SESSION_STAT = "session_stat";

    public scenarioManagement(Context context){
        sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void saveScene(scenarioSession ss){
        String scene = ss.getScenario();
        String stat = ss.getStat();

        editor.putString(SESSION_SCENARIO, scene).commit();
        editor.putString(SESSION_STAT, stat).commit();
    }
    public String getScene(){ //return saved session
        return sp.getString(SESSION_SCENARIO, "none");
    }
    public String getStat(){ //return saved session
        return sp.getString(SESSION_STAT, "none");
    }
    public void removeScene(){ //Logout
        editor.putString(SESSION_STAT, "none").commit();
        editor.putString(SESSION_SCENARIO, "none").commit();
    }
}
