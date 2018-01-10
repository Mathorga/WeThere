package keym.dev.rwethereyet.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;

import keym.dev.rwethereyet.BaseActivity;
import keym.dev.rwethereyet.R;

/**
 * Created by luka on 26/05/17.
 */

public class SettingsActivity extends BaseActivity {

    private Toolbar toolbar;
    private FloatingActionButton save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_settings);

        this.toolbar = (Toolbar) this.findViewById(R.id.settingsToolbar);
        this.setSupportActionBar(this.toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.getFragmentManager().beginTransaction()
                                 .replace(R.id.settingsFragmentContainer, new SettingsFragment())
                                 .commit();

        this.save = (FloatingActionButton) this.findViewById(R.id.settingsSave);
        this.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.onBackPressed();
        return true;
    }
}
