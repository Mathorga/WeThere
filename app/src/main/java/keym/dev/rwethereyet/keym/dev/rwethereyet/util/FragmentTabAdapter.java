package keym.dev.rwethereyet.keym.dev.rwethereyet.util;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import keym.dev.rwethereyet.BaseActivity;
import keym.dev.rwethereyet.Distance.DistanceListFragment;
import keym.dev.rwethereyet.Distance.DistanceMapFragment;
import keym.dev.rwethereyet.R;
import keym.dev.rwethereyet.locations.LocationsFragment;

/**
 * Created by luka on 23/05/17.
 */

public class FragmentTabAdapter extends FragmentPagerAdapter {

    private List<Tab> tabs;

    public FragmentTabAdapter(final FragmentManager manager, final Context context, final BaseActivity creator) {
        super(manager);
        this.tabs = new ArrayList<>();
        this.tabs.add(new Tab(new LocationsFragment(),
                              R.drawable.ic_grade_custom_24dp,
                              R.string.locations_fragment_name));

        // Check what fragment to show based on the distance map preference.
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.preference_distances_key), false)) {
            this.tabs.add(new Tab(new DistanceMapFragment(),
                                  R.drawable.ic_map_custom_24dp,
                                  R.string.distance_fragment_name));
        } else {
            this.tabs.add(new Tab(new DistanceListFragment(),
                                  R.drawable.ic_map_custom_24dp,
                                  R.string.distance_fragment_name));
        }
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
