package keym.dev.rwethereyet;

import android.Manifest;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import keym.dev.rwethereyet.util.FragmentTabAdapter;
import keym.dev.rwethereyet.util.LocationItem;
import keym.dev.rwethereyet.settings.SettingsActivity;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private static final int SETTINGS_REQUEST = 0;

    private ViewPager pager;
    private FragmentTabAdapter adapter;
    private TabLayout tabs;
    private ImageButton menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        Intent starter = this.getIntent();
        if (starter != null) {
            LocationItem location = starter.getParcelableExtra("location");

            if (location != null) {
                // Stop alarm.
                Uri alarm = location.getTone();
                Ringtone ringtone = RingtoneManager.getRingtone(this.getApplicationContext(), alarm);
                ringtone.stop();

                Log.wtf(TAG, "Stopped Alarm!!");
            }
        }

        // Ask for locations permissions.
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                1);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);

        this.pager = (ViewPager) this.findViewById(R.id.mainPager);
        this.adapter = new FragmentTabAdapter(this.getSupportFragmentManager(), this.getApplicationContext(), this);
        this.pager.setAdapter(adapter);

        this.tabs = (TabLayout) this.findViewById(R.id.mainTabs);
        this.tabs.setupWithViewPager(this.pager);
        for (int i = 0; i < this.tabs.getTabCount(); i++) {
            this.tabs.getTabAt(i).setIcon(this.adapter.getTabs().get(i).getIconResId());
            this.tabs.getTabAt(i).setText(this.adapter.getTabs().get(i).getTitleResId());
        }

        this.menu = (ImageButton) this.findViewById(R.id.mainMenu);
        this.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Settings");
                PopupMenu popup = new PopupMenu(MainActivity.this, menu);
                popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            // Settings.
                            case R.id.mainSettings:
                                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                                startActivityForResult(intent, SETTINGS_REQUEST);
                                return true;
                            // TODO Credits.
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }


    public void refreshDistances() {
        Fragment fragment = this.adapter.getItem(1);
        this.getSupportFragmentManager()
            .beginTransaction()
            .detach(fragment)
            .attach(fragment)
            .commit();
    }
}