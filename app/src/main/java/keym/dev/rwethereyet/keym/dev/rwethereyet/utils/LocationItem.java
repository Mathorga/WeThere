package keym.dev.rwethereyet.keym.dev.rwethereyet.utils;

import android.content.Intent;
import android.location.Location;
import android.media.Ringtone;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Luka on 10/05/2017.
 */

public class LocationItem implements Serializable {

    private String name;
    private Integer radius;
    private LatLng location;;
    private Ringtone tone;
    private boolean active;

    public LocationItem(final String name,
                        final Integer radius,
                        final LatLng location,
                        final Ringtone tone) {
        this.name = name;
        this.radius = radius;
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

    public LatLng getLocation() {
        return this.location;
    }

    public void setLocation(final LatLng location) {
        this.location = location;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

}
