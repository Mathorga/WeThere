package keym.dev.rwethereyet.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by luka on 13/02/18.
 * The class implements methods to convert Parcelables to byte arrays and viceversa.
 * Code taken from https://stackoverflow.com/a/18000094/7009491
 */

public class ParcelableUtil {

    public static byte[] marshall(Parcelable parceable) {
        Parcel parcel = Parcel.obtain();
        parceable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    public static Parcel unmarshall(byte[] bytes) {
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0); // This is extremely important!
        return parcel;
    }

    public static <T> T unmarshall(byte[] bytes, Parcelable.Creator<T> creator) {
        Parcel parcel = unmarshall(bytes);
        T result = creator.createFromParcel(parcel);
        parcel.recycle();
        return result;
    }
}
