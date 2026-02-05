package config_swipe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

@SuppressLint("CommitPrefEdits") 
public class PrefManager {

	private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    
    // shared pref mode
    private int PRIVATE_MODE = 0;
 
    // Shared preferences file name
    private static final String PREF_NAME = "Selamat Datang";
 
    private static final String a = "IsFirstTimeLaunch";
    
    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    
    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(a, isFirstTime);
        editor.commit();
    }
 
    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(a, true);
    }
}
