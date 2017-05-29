package keym.dev.rwethereyet.keym.dev.rwethereyet.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import keym.dev.rwethereyet.R;
import keym.dev.rwethereyet.locations.LocationsFragment;

/**
 * Created by luka on 23/05/17.
 */

public class FragmentTabAdapter extends FragmentStatePagerAdapter {

    private List<Tab> tabs;

    public FragmentTabAdapter(final FragmentManager manager) {
        super(manager);
        this.tabs = new ArrayList<>();
        this.tabs.add(new Tab(new LocationsFragment(),
                              R.drawable.ic_grade_custom_24dp,
                              R.string.locations_fragment_name));
        this.tabs.add(new Tab(new Fragment(),
                              R.drawable.ic_map_custom_24dp,
                              R.string.distance_fragment_name));
    }

    @Override
    public Fragment getItem(final int position) {
        return this.tabs.get(position).getPage();
    }

    @Override
    public int getCount() {
        return this.tabs.size();
    }

    public List<Tab> getTabs() {
        return this.tabs;
    }
}
