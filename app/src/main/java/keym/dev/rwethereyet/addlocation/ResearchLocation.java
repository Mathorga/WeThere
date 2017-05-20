package keym.dev.rwethereyet.addlocation;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

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
    private AddLocationActivity caller;
    private LatLng position;

    public ResearchLocation(final String toSearch, final AddLocationActivity caller) {
        this.toSearch = toSearch;
        this.caller = caller;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            // Create a geocoder for the default system locale.
            Geocoder geocoder = new Geocoder(this.caller.getBaseContext());
            List<Address> results = geocoder.getFromLocationName(this.toSearch, 1);

            if (results.size() == 0) {
                return false;
            }

            this.address = results.get(0);

            // Get the coordinates of the address.
            LatLng coords = new LatLng(this.address.getLatitude(), this.address.getLongitude());
            this.position = coords;
        } catch (Exception e) {
            Log.e("", "Something went wrong: ", e);
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean result) {
        if (result) {
            this.caller.moveMapTo(this.position);
        } else {
            Toast.makeText(this.caller, "No match found", Toast.LENGTH_SHORT).show();
        }
    }
}
