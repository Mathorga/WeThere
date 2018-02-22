package keym.dev.rwethereyet.util;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Luka on 10/05/2017.
 * The class implements the core of the app: the location to be saved.
 */

public class LocationItem implements Parcelable {

    public static final Integer ID_UNDEFINED = -1;

    public static final int M_TO_KM = 1000;

    private Integer id;
    private String label;
    private Integer radius;
    private LatLng location;
    private Uri tone;
    private Boolean active;

    public LocationItem(final Integer id,
                        final String label,
                        final Integer radius,
                        final LatLng location,
                        final Uri tone) {
        new LocationItem(id, label, radius, location, tone, false);
    }

    public LocationItem(final Integer id,
                        final String label,
                        final Integer radius,
                        final LatLng location,
                        final Uri tone,
                        final Boolean active) {
        this.id = id;
        this.label = label;
        this.radius = radius;
        this.location = location;
        this.tone = tone;
        this.active = active;
    }

    public LocationItem(final Parcel parcel) {
        // Read id.
        this.id = parcel.readInt();
        // Read name.
        this.label = parcel.readString();
        // Read radius.
        this.radius = parcel.readInt();
        // Read position.
        String lat = parcel.readString();
        String lng = parcel.readString();
        this.location = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        // Read ringtone.
        this.tone = Uri.parse(parcel.readString());
        // Read whether it's active or not.
        this.active = parcel.readByte() != 0;
    }

    /**
     * Getter.
     * @return
     *  The LocationItem id.
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Setter.
     * @param id
     *  The LocationItem id to set.
     */
    public void setId(final Integer id) {
        this.id = id;
    }

    /**
     * Getter.
     * @return
     *  The LocationItem label (showed name).
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Setter.
     * @param name
     *  The name to show for the LocationItem.
     */
    public void setLabel(final String name) {
        this.label = name;
    }

    /**
     * Getter.
     * @return
     *  The LocationItem radius.
     */
    public Integer getRadius() {
        return this.radius;
    }

    /**
     * Setter.
     * @param radius
     *  The radius to assign to the LocationItem.
     */
    public void setRadius(final Integer radius) {
        this.radius = radius;
    }

    /**
     * Getter.
     * @return
     *  The coordinates for the LocationItem position.
     */
    public LatLng getLocation() {
        return this.location;
    }

    /**
     * Setter.
     * @param location
     *  The coordinates for the LocationItem position.
     */
    public void setLocation(final LatLng location) {
        this.location = location;
    }

    /**
     * Getter.
     * @return
     *  The ringtone URI associated to the LocationItem.
     */
    public Uri getTone() {
        return this.tone;
    }

    /**
     * Setter.
     * @param tone
     *  The URI of the ringtone to link to the LocationItem.
     */
    public void setTone(final Uri tone) {
        this.tone = tone;
    }

    /**
     * Getter.
     * @return
     *  Whether the LocationItem is active or not.
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Setter.
     * @param active
     *  Whether the LocationItem needs to be activated or not.
     */
    public void setActive(final Boolean active) {
        this.active = active;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel parcel, int i) {
        // Write id.
        parcel.writeInt(this.id);
        // Write name.
        parcel.writeString(this.label);
        // Write radius.
        parcel.writeInt(this.radius);
        // Write position.
        parcel.writeString(String.valueOf(this.location.latitude));
        parcel.writeString(String.valueOf(this.location.longitude));
        // Write ringtone.
        parcel.writeString(this.tone.toString());
        // Write whether it's active or not.
        parcel.writeByte((byte) (this.active ? 1 : 0));
    }

    public static final Parcelable.Creator<LocationItem> CREATOR = new Parcelable.Creator<LocationItem>() {
        public LocationItem createFromParcel(final Parcel parcel) {
            return new LocationItem(parcel);
        }

        public LocationItem[] newArray(final int size) {
            return new LocationItem[size];
        }
    };

    @Override
    public String toString() {
        return "Id: " + this.id + "\n" +
               "Label: " + this.label + "\n" +
               "Radius: " + this.radius + "\n" +
               "Location: " + this.location + "\n" +
               "Ringtone: " + this.tone + "\n" +
               "Active: " + this.active;
    }
}
