package keym.dev.rwethereyet.keym.dev.rwethereyet.util;

import android.media.Ringtone;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Luka on 10/05/2017.
 */

public class LocationItem implements Parcelable {

    private String label;
    private Integer radius;
    private LatLng location;
    private Ringtone tone;
    private boolean active;

    public LocationItem(final String label,
                        final Integer radius,
                        final LatLng location,
                        final Ringtone tone) {
        new LocationItem(label, radius, location, tone, false);
    }

    public LocationItem(final String label,
                        final Integer radius,
                        final LatLng location,
                        final Ringtone tone,
                        final Boolean active) {
        this.label = label;
        this.radius = radius;
        this.location = location;
        this.tone = tone;
        this.active = active;
    }

    public LocationItem(final Parcel parcel) {
        // Read name.
        this.label = parcel.readString();
        // Read radius.
        this.radius = parcel.readInt();
        // Read position.
        String lat = parcel.readString();
        String lng = parcel.readString();
        this.location = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        // Read ringtone.
        this.tone = null;
        parcel.readString();
        // Read whether it's active or not.
        this.active = parcel.readByte() != 0;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(final String name) {
        this.label = name;
    }

    public Integer getRadius() {
        return this.radius;
    }

    public void setRadius(final Integer radius) {
        this.radius = radius;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        // Write name.
        parcel.writeString(this.label);
        // Write radius.
        parcel.writeInt(this.radius);
        // Write position.
        parcel.writeString(String.valueOf(this.location.latitude));
        parcel.writeString(String.valueOf(this.location.longitude));
        // Write ringtone.
        //parcel.writeString(this.tone.toString());
        // Write whether it's active or not.
        parcel.writeByte((byte) (this.active ? 1 : 0));
    }

    public static final Parcelable.Creator<LocationItem> CREATOR
            = new Parcelable.Creator<LocationItem>() {
        public LocationItem createFromParcel(Parcel parcel) {
            return new LocationItem(parcel);
        }

        public LocationItem[] newArray(int size) {
            return new LocationItem[size];
        }
    };

    @Override
    public String toString() {
        return "Label: " + this.label + "\n" +
               "Radius: " + this.radius + "\n" +
               "Location: " + this.location + "\n" +
               "Ringtone: " + this.tone + "\n" +
               "Active: " + this.active;
    }
}
