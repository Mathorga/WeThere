package keym.dev.rwethereyet;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by luka on 26/05/17.
 */

public class BaseActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "BaseActivity";

    protected static final String PREFERENCES = "RWTYPreferences";
    protected static final String PREF_THEME = "Theme";

    protected SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        this.setTheme(this.getSharedPreferences(PREFERENCES, 0).getInt(PREF_THEME, R.style.Fuscus));
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
//        this.setTheme(this.getSharedPreferences(PREFERENCES, 0).getInt(PREF_THEME, R.style.Fuscus));
        super.onResume();
        this.getSharedPreferences(PREFERENCES, 0).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        this.getSharedPreferences(PREFERENCES, 0).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(PREF_THEME)) {
            // Update theme.
            this.setTheme(sharedPreferences.getInt(s, R.style.Fuscus));
            this.recreate();
        }
    }
}