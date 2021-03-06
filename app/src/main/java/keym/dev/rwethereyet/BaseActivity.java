package keym.dev.rwethereyet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by luka on 26/05/17.
 * Implements the basic functions for the app activities.
 */

public class BaseActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "BaseActivity";

    protected static final String INDEX_PREFERENCES_NAME = "index";

    protected static final String PREF_THEME = "Theme";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

        // Set theme.
        String theme = PreferenceManager.getDefaultSharedPreferences(this).getString(PREF_THEME, String.valueOf(R.style.Fuscus));
        if (theme.equals(this.getResources().getString(R.string.theme_fuscus))) {
            this.setTheme(R.style.Fuscus);
        } else if (theme.equals(this.getResources().getString(R.string.theme_albus))) {
            this.setTheme(R.style.Albus);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(this.getString(R.string.preference_theme_key))) {
            // Update theme.
            String theme = sharedPreferences.getString(s, String.valueOf(R.style.Fuscus));
            if (theme.equals(this.getResources().getString(R.string.theme_fuscus))) {
                this.setTheme(R.style.Fuscus);
            } else if (theme.equals(this.getResources().getString(R.string.theme_albus))) {
                this.setTheme(R.style.Albus);
            }
//            this.recreate();
            this.restart();
        } else if (s.equals(this.getString(R.string.preference_distances_key)) /*&& this instanceof MainActivity*/) {
            Log.wtf(TAG, "recreate activity");
//            this.recreate();
            this.restart();

        }
    }

    /**
     * Restarts the activity.
     */
    protected void restart() {
        Intent intent = this.getIntent();
        this.overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.finish();
        this.overridePendingTransition(0, 0);
        this.startActivity(intent);
    }
}