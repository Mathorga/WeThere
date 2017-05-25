package keym.dev.rwethereyet;

import android.Manifest;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ViewPager pager;
    private FragmentTabAdapter adapter;
    private TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        //this.toolbar = (Toolbar) this.findViewById(R.id.mainToolbar);
        //this.setSupportActionBar(toolbar);

        // Ask for locations permissions.
        ActivityCompat.requestPermissions(this,
                                          new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                          1);
        ActivityCompat.requestPermissions(this,
                                          new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                          1);

        this.pager = (ViewPager) this.findViewById(R.id.mainPager);
        this.adapter = new FragmentTabAdapter(this.getSupportFragmentManager());
        this.pager.setAdapter(adapter);

        this.tabs = (TabLayout) this.findViewById(R.id.mainTabs);
        this.tabs.setupWithViewPager(this.pager);
        for (int i = 0; i < this.tabs.getTabCount(); i++) {
            this.tabs.getTabAt(i).setIcon(this.adapter.getTabs().get(i).getIconResId());
            this.tabs.getTabAt(i).setText(this.adapter.getTabs().get(i).getTitleResId());
        }
    }
}