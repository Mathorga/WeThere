package keym.dev.rwethereyet.addlocation;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

/**
 * Created by luka on 18/05/17.
 */

public class ResearchLocation extends AsyncTask<Void, Void, Boolean> {

    private final static String TAG = "ResearchLocation";

    private String toSearch;
    private Address address;
    private AppCompatActivity caller;

    public ResearchLocation(final String toSearch, final AppCompatActivity caller) {
        this.toSearch = toSearch;
        this.caller = caller;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            Geocoder geocoder = new Geocoder(this.caller.getBaseContext(), Locale.ITALY);
            List<Address> results = geocoder.getFromLocationName(this.toSearch, 1);

            if (results.size() == 0) {
                return false;
            }

            this.address = results.get(0);

            // Now do something with this coords:
            LatLng coords = new LatLng((int) (this.address.getLatitude() * 1E6),
                                       (int) (this.address.getLongitude() * 1E6));
            Log.d(TAG, coords.toString());

        } catch (Exception e) {
            Log.e("", "Something went wrong: ", e);
            return false;
        }
        return true;
    }
}
