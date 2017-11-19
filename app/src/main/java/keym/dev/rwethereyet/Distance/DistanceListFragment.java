package keym.dev.rwethereyet.Distance;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import keym.dev.rwethereyet.R;
import keym.dev.rwethereyet.keym.dev.rwethereyet.util.LocationItem;
import keym.dev.rwethereyet.keym.dev.rwethereyet.util.LocationParser;
import keym.dev.rwethereyet.locations.LocationListAdapter;

/**
 * Created by luka on 17/11/17.
 */

public class DistanceListFragment extends Fragment {

    private static final String TAG = "DistanceListFragment";

    private View rootView;
    private List<LocationItem> locations;
    private ListView distancesView;

    private DistanceListAdapter adapter;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_distance_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.rootView = view;

        this.locations = new ArrayList<>();
        this.distancesView = (ListView) this.rootView.findViewById(R.id.distanceList);

        List<LocationItem> tmpLocations = new LocationParser(this.getContext()).readAllItems();

        for (LocationItem item : tmpLocations) {
            if (item.isActive()) {
                this.locations.add(item);
            }
        }

        this.adapter = new DistanceListAdapter(this.getActivity(), R.layout.item_distance, this.locations);
        this.distancesView.setAdapter(this.adapter);
    }
}
