package keym.dev.rwethereyet.settings;

import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import keym.dev.rwethereyet.BaseActivity;
import keym.dev.rwethereyet.R;

/**
 * Created by luka on 26/05/17.
 */

public class SettingsActivity extends BaseActivity {

    private static final String FUSCUS = "Fuscus";
    private static final String ALBUS = "Albus";

    private SharedPreferences.Editor editor;

    private Toolbar toolbar;
    private Switch themeSwitch;
    private FloatingActionButton save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_settings);

        this.editor = this.getSharedPreferences(PREFERENCES, 0).edit();

        this.toolbar = (Toolbar) this.findViewById(R.id.settingsToolbar);
        this.setSupportActionBar(this.toolbar);

        this.themeSwitch = (Switch) this.findViewById(R.id.settingsSwitch);
        this.themeSwitch.setChecked(this.getSharedPreferences(PREFERENCES, 0).getInt(PREF_THEME, R.style.Fuscus) == R.style.Fuscus);

        this.themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    editor.putInt(PREF_THEME, R.style.Fuscus);
                } else {
                    editor.putInt(PREF_THEME, R.style.Albus);
                }
                editor.commit();
            }
        });

        this.save = (FloatingActionButton) this.findViewById(R.id.settingsSave);
        this.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.commit();
                finish();
            }
        });
    }
}
