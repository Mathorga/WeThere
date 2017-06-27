package keym.dev.rwethereyet;

import android.Manifest;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import keym.dev.rwethereyet.background.Triggerer;
import keym.dev.rwethereyet.keym.dev.rwethereyet.util.FragmentTabAdapter;
import keym.dev.rwethereyet.keym.dev.rwethereyet.util.LocationItem;
import keym.dev.rwethereyet.settings.SettingsActivity;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private ViewPager pager;
    private FragmentTabAdapter adapter;
    private TabLayout tabs;
    private ImageButton menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        Intent starter = this.getIntent();
        LocationItem location = starter.getParcelableExtra("location");
        // Stop alarm.
        Uri alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(this.getApplicationContext(), alarm);
        ringtone.stop();

        // Ask for locations permissions.
        ActivityCompat.requestPermissions(this,
                                          new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                          1);
        ActivityCompat.requestPermissions(this,
                                          new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                          1);

        Intent triggererIntent = new Intent(this, Triggerer.class);
        this.startService(triggererIntent);

        this.pager = (ViewPager) this.findViewById(R.id.mainPager);
        this.adapter = new FragmentTabAdapter(this.getSupportFragmentManager());
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
                PopupMenu popup = new PopupMenu(MainActivity.this, menu);
                popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            // Settings.
                            case R.id.mainSettings:
                                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                                startActivity(intent);
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
}