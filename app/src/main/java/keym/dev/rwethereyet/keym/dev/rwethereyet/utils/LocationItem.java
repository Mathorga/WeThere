package keym.dev.rwethereyet.keym.dev.rwethereyet.utils;

import android.content.Intent;
import android.location.Location;
import android.media.Ringtone;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Luka on 10/05/2017.
 */

public class LocationItem implements Parcelable {

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

    public LocationItem(final Parcel parcel) {
        this.name = parcel.readString();
        this.radius = parcel.readInt();
        String coords = parcel.readString();
        this.location = new LatLng(Double.parseDouble(coords.substring(0, 3)),
                                   Double.parseDouble(coords.substring(4)));
        this.tone = null;
        parcel.readString();
        this.active = parcel.readByte() != 0;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
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
        parcel.writeString(this.name);
        parcel.writeInt(this.radius);
        parcel.writeString(this.location.latitude + "" + this.location.longitude);
        parcel.writeString(this.tone.toString());
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
}
