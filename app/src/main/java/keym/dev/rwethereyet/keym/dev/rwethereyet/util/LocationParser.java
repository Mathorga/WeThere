package keym.dev.rwethereyet.keym.dev.rwethereyet.util;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by luka on 10/06/17.
 */

public class LocationParser {

    private static final String TAG = "LocationParser";

    private static final String LOCATIONS_FILE_NAME = "locations.json";

    private static final String ID_KEY = "id";
    private static final String LABEL_KEY = "label";
    private static final String RADIUS_KEY = "radius";
    private static final String LATITUDE_KEY = "lat";
    private static final String LONGITUDE_KEY = "lng";
    private static final String RINGTONE_KEY = "tone";
    private static final String ACTIVE_KEY = "active";

    private File file;
    private int nextIndex;

    public LocationParser(final Context context) {
        this.file = new File(context.getFilesDir(), LOCATIONS_FILE_NAME);
        try {
            this.nextIndex = this.parseFile().names() == null ? 0 : this.parseFile().names().length();
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
        Log.d(TAG, "" + this.nextIndex);
    }

    /**
     * Appends the given LocationItem in the default file in JSON format.
     * @param item
     * The LocationItem to save.
     * @throws JSONException
     * If an error occurs while creating a json object.
     */
    public void writeItem(final LocationItem item) throws JSONException {
        //Log.d(TAG, item.toString());

        // Obtain currently saved object.
        JSONObject fileContent = this.parseFile();

        try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(this.file))) {
            // Create the new JSON to append to the file.
            JSONObject newObject = new JSONObject();
            newObject.put(ID_KEY, this.nextIndex);
            newObject.put(LABEL_KEY, item.getLabel());
            newObject.put(RADIUS_KEY, item.getRadius());
            newObject.put(LATITUDE_KEY, item.getLocation().latitude);
            newObject.put(LONGITUDE_KEY, item.getLocation().longitude);
            // TODO Put ringtone.
            newObject.put(ACTIVE_KEY, item.isActive());


            // Add the new object to the file content.
            fileContent.put(String.valueOf(item.getId()), newObject);
            Log.d(TAG, "New File:\n" + fileContent.toString());
            out.write(fileContent.toString());
            this.nextIndex++;
            Log.d(TAG, "WRITE:\n" + this.nextIndex);
        } catch (JSONException | IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * TODO
     * @return
     */
//    public LocationItem readItem(final String id) {
//        String label = null;
//        int radius = 0;
//        double latitude = 0.0;
//        double longitude = 0.0;
//        // Ringtone.
//        boolean active = false;
//        try (JsonReader in = new JsonReader(new InputStreamReader(new FileInputStream(this.file)))) {
//            in.beginObject();
//            while (in.hasNext()) {
//                String name = in.nextName();
//                if (name.equals(ID_KEY)) {
//                    id = in.nextInt();
//                } else if (name.equals(LABEL_KEY)) {
//                    label = in.nextString();
//                } else if (name.equals(RADIUS_KEY)) {
//                    radius = in.nextInt();
//                } else if (name.equals(LATITUDE_KEY)) {
//                    latitude = in.nextDouble();
//                } else if (name.equals(LONGITUDE_KEY)) {
//                    longitude = in.nextDouble();
//                } else if (name.equals(RINGTONE_KEY)) {
//                    // TODO
//                } else if (name.equals(ACTIVE_KEY)) {
//                    active = in.nextBoolean();
//                } else {
//                    in.skipValue();
//                }
//            }
//            in.endObject();
//            return new LocationItem(label, radius, new LatLng(latitude, longitude), null, active);
//        } catch (IOException exception) {
//            exception.printStackTrace();
//            return null;
//        }
//    }

    /**
     * Reads data from file and returns all the saved locations (uses JSON structure internally).
     * @return
     * A list of all saved LocationItems, in the order they're saved.
     */
    public List<LocationItem> readAllItems() {

        List<LocationItem> items = new ArrayList<>();

        try {
            JSONObject fileContent = this.parseFile();
            Iterator<String> iterator = fileContent.keys();

            while (iterator.hasNext()) {
                String key = iterator.next();
                JSONObject item = (JSONObject) fileContent.get(key);

                items.add(new LocationItem(item.getInt(ID_KEY),
                                           item.getString(LABEL_KEY),
                                           item.getInt(RADIUS_KEY),
                                           new LatLng(item.getDouble(LATITUDE_KEY), item.getDouble(LONGITUDE_KEY)),
                                           null,
                                           item.getBoolean(ACTIVE_KEY)));
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
        }

        return items;
    }

    /**
     * TODO
     * @return
     */
    public boolean updateItem(final LocationItem item) {
        try {
            JSONObject fileContent = this.parseFile();
            Iterator<String> iterator = fileContent.keys();

            while (iterator.hasNext()) {
                String key = iterator.next();
                JSONObject object = (JSONObject) fileContent.get(key);
                if (item.getId().equals(object.getInt(ID_KEY))) {
                    // TODO
                }
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    private JSONObject parseFile() throws JSONException {
        // Parse existing JSON.
        String fileContentString = null;
        if (this.file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(this.file))) {
                Log.d(TAG, "Reading...");
                StringBuilder builder = new StringBuilder();
                String line = reader.readLine();
                while (line != null) {
                    Log.d(TAG, "Line:\n" + line);
                    builder.append(line);
                    builder.append("\n");
                    line = reader.readLine();
                }
                if (builder.length() == 0) {
                    // The file is empty.
                    Log.d(TAG, "The file is empty.");
                    fileContentString = "{}";
                } else {
                    // The file is not empty.
                    fileContentString = builder.toString();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            // The file does not exist.
            Log.d(TAG, "The file does not exist.");
            fileContentString = "{}";
        }
        Log.d(TAG, "Parsed:\n" + fileContentString);
        return new JSONObject(fileContentString);
    }

    public int getNextIndex() {
        return this.nextIndex;
    }
}