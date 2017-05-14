package keym.dev.rwethereyet.locations;

import android.location.Location;
import android.media.Ringtone;

/**
 * Created by Luka on 10/05/2017.
 */

class SavedLocation {

    private String name;
    private Location location;;
    private Ringtone tone;
    private boolean active;

    public SavedLocation(final String name, final Location location, final Ringtone tone) {
        this.name = name;
        this.location = location;
        this.tone = tone;
        this.active = false;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(final Location location) {
        this.location = location;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

}
