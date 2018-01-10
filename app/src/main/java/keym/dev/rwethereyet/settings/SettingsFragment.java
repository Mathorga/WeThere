package keym.dev.rwethereyet.settings;

import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.util.Log;
import android.widget.Toast;

import keym.dev.rwethereyet.R;

/**
 * Created by luka on 29/05/17.
 */

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = "SettingsFragment";

    private ListPreference themePreference;
    private RingtonePreference tonePreference;
    private SwitchPreference mapPreference;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.addPreferencesFromResource(R.xml.preferences);

        // Register this as listener over SharedPreferences change event.
        PreferenceManager.getDefaultSharedPreferences(this.getActivity().getApplicationContext()).registerOnSharedPreferenceChangeListener(this);

        // Update preference summary according to the user selection.
        String themeKey = this.getResources().getString(R.string.preference_theme_key);
        this.themePreference = (ListPreference) this.findPreference(themeKey);
        this.themePreference.setSummary(PreferenceManager.getDefaultSharedPreferences(this.getActivity()).getString(themeKey, this.getResources().getString(R.string.theme_fuscus)));

        String toneKey = this.getResources().getString(R.string.preference_ringtone_key);
        Uri defaultRintoneUri = RingtoneManager.getActualDefaultRingtoneUri(this.getActivity().getApplicationContext(), RingtoneManager.TYPE_ALARM);

        String actualUriString = PreferenceManager.getDefaultSharedPreferences(this.getActivity()).getString("Ringtone", defaultRintoneUri.toString());
        Ringtone actualRingtone = RingtoneManager.getRingtone(this.getActivity(), Uri.parse(actualUriString));
        String actualRingtoneTitle = actualRingtone.getTitle(this.getActivity());

        this.tonePreference = (RingtonePreference) this.findPreference(toneKey);
        this.tonePreference.setSummary(actualRingtoneTitle);

        String mapKey = this.getResources().getString(R.string.preferences_key);
        this.mapPreference = (SwitchPreference) this.findPreference(mapKey);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(this.getActivity()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(this.getResources().getString(R.string.preference_theme_key))) {
            // Update theme preference summary according to the user selection.
            String themeKey = this.getResources().getString(R.string.preference_theme_key);
            this.themePreference = (ListPreference) this.findPreference(themeKey);
            this.themePreference.setSummary(sharedPreferences.getString(themeKey, this.getResources().getString(R.string.theme_fuscus)));
        } else if (s.equals(this.getResources().getString(R.string.preference_ringtone_key))) {
            // Update default ringtone preference summary according to the user selection.
            String toneKey = this.getResources().getString(R.string.preference_ringtone_key);
            Uri defaultRintoneUri = RingtoneManager.getActualDefaultRingtoneUri(this.getActivity().getApplicationContext(), RingtoneManager.TYPE_ALARM);

            String actualUriString = sharedPreferences.getString("Ringtone", defaultRintoneUri.toString());
            Ringtone actualRingtone = RingtoneManager.getRingtone(this.getActivity(), Uri.parse(actualUriString));
            String actualRingtoneTitle = actualRingtone.getTitle(this.getActivity());

            this.tonePreference = (RingtonePreference) this.findPreference(toneKey);
            this.tonePreference.setSummary(actualRingtoneTitle);
        } else if (s.equals(this.getResources().getString(R.string.preference_distances_key))) {
//            Toast.makeText(this.getActivity(), "distances changed", Toast.LENGTH_SHORT).show();
        }
    }
}
