package keym.dev.rwethereyet.keym.dev.rwethereyet.util;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by luka on 10/06/17.
 */

public class LocationParser {

    private static final String TAG = "LocationParser";

    private static final String LABEL_KEY = "label";
    private static final String RADIUS_KEY = "radius";
    private static final String LATITUDE_KEY = "lat";
    private static final String LONGITUDE_KEY = "lng";
    private static final String RINGTONE_KEY = "tone";
    private static final String ACTIVE_KEY = "active";

    private File file;
    private JSONObject wrapper;

    public LocationParser(final File file) {
        this.file = file;
        this.wrapper = new JSONObject();
    }

    /**
     * Writes the given LocationItem in a file in JSON format.
     * @param item
     * The LocationItem to save.
     * @throws JSONException
     * If an error occurs while creating a json object.
     */
    public void writeItem(final LocationItem item) throws JSONException {
        this.wrapper.put(LABEL_KEY, item.getLabel());
        this.wrapper.put(RADIUS_KEY, item.getRadius());
        this.wrapper.put(LATITUDE_KEY, item.getLocation().latitude);
        this.wrapper.put(LONGITUDE_KEY, item.getLocation().longitude);
        // Put ringtone.
        this.wrapper.put(ACTIVE_KEY, item.isActive());

        try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(this.file))) {
            out.write(this.wrapper.toString());
            Log.d(TAG, this.wrapper.toString());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        Log.d(TAG, "Write: \n" + item.toString());
    }

    public LocationItem readItem() {
        String label = null;
        int radius = 0;
        double latitude = 0.0;
        double longitude = 0.0;
        // Ringtone.
        boolean active = false;
        try (JsonReader in = new JsonReader(new InputStreamReader(new FileInputStream(this.file)))) {
            in.beginObject();
            while (in.hasNext()) {
                String name = in.nextName();
                if (name.equals(LABEL_KEY)) {
                    label = in.nextString();
                } else if (name.equals(RADIUS_KEY)) {
                    radius = in.nextInt();
                } else if (name.equals(LATITUDE_KEY)) {
                    latitude = in.nextDouble();
                } else if (name.equals(LONGITUDE_KEY)) {
                    longitude = in.nextDouble();
                } else if (name.equals(RINGTONE_KEY)) {
                    // TODO
                } else if (name.equals(ACTIVE_KEY)) {
                    active = in.nextBoolean();
                } else {
                    in.skipValue();
                }
            }
            in.endObject();
            LocationItem item = new LocationItem(label, radius, new LatLng(latitude, longitude), null, active);
            Log.d(TAG, "Read: \n" + item.toString());
            return item;
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
