package keym.dev.rwethereyet.locations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import keym.dev.rwethereyet.R;
import keym.dev.rwethereyet.keym.dev.rwethereyet.util.LocationItem;

/**
 * Created by Luka on 09/05/2017.
 */
public class LocationListAdapter extends ArrayAdapter<LocationItem> {

    private int layoutResource;
    
    public LocationListAdapter(final Context context, final int layoutResource, List<LocationItem> data) {
        super(context, layoutResource, data);
        this.layoutResource = layoutResource;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(this.getContext());
            view = inflater.inflate(this.layoutResource, null);
        }

        final LocationItem item = this.getItem(position);

        if (item != null) {
            TextView name = (TextView) view.findViewById(R.id.locationName);
            TextView coordinates = (TextView) view.findViewById(R.id.locationCoord);
            Switch active = (Switch) view.findViewById(R.id.locationActive);

            if (name != null) {
                name.setText(item.getName());
            }

            if (coordinates != null) {
                coordinates.setText(item.getLocation().latitude + " - " + item.getLocation().longitude);
            }

            if (active != null) {
                active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        item.setActive(b);
                    }
                });
                active.setChecked(item.isActive());
            }
        }
        return view;
    }
}
