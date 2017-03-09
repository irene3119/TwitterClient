
package com.codepath.apps.twitterclient.models;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import org.parceler.Generated;
import org.parceler.IdentityCollection;
import org.parceler.ParcelWrapper;
import org.parceler.ParcelerRuntimeException;

@Generated(value = "org.parceler.ParcelAnnotationProcessor", date = "2017-03-10T01:06+0800")
@SuppressWarnings({
    "unchecked",
    "deprecation"
})
public class Url$$Parcelable
    implements Parcelable, ParcelWrapper<com.codepath.apps.twitterclient.models.Url>
{

    private com.codepath.apps.twitterclient.models.Url url$$0;
    @SuppressWarnings("UnusedDeclaration")
    public final static Creator<Url$$Parcelable>CREATOR = new Creator<Url$$Parcelable>() {


        @Override
        public Url$$Parcelable createFromParcel(android.os.Parcel parcel$$2) {
            return new Url$$Parcelable(read(parcel$$2, new IdentityCollection()));
        }

        @Override
        public Url$$Parcelable[] newArray(int size) {
            return new Url$$Parcelable[size] ;
        }

    }
    ;

    public Url$$Parcelable(com.codepath.apps.twitterclient.models.Url url$$2) {
        url$$0 = url$$2;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel$$0, int flags) {
        write(url$$0, parcel$$0, flags, new IdentityCollection());
    }

    public static void write(com.codepath.apps.twitterclient.models.Url url$$1, android.os.Parcel parcel$$1, int flags$$0, IdentityCollection identityMap$$0) {
        int identity$$0 = identityMap$$0 .getKey(url$$1);
        if (identity$$0 != -1) {
            parcel$$1 .writeInt(identity$$0);
        } else {
            parcel$$1 .writeInt(identityMap$$0 .put(url$$1));
            parcel$$1 .writeString(url$$1 .displayUrl);
            if (url$$1 .indices == null) {
                parcel$$1 .writeInt(-1);
            } else {
                parcel$$1 .writeInt(url$$1 .indices.size());
                for (java.lang.Integer integer$$0 : ((List<java.lang.Integer> ) url$$1 .indices)) {
                    if (integer$$0 == null) {
                        parcel$$1 .writeInt(-1);
                    } else {
                        parcel$$1 .writeInt(1);
                        parcel$$1 .writeInt(integer$$0);
                    }
                }
            }
            parcel$$1 .writeString(url$$1 .expandedUrl);
            parcel$$1 .writeString(url$$1 .url);
        }
    }

    @Override
    public int describeContents() {
        return  0;
    }

    @Override
    public com.codepath.apps.twitterclient.models.Url getParcel() {
        return url$$0;
    }

    public static com.codepath.apps.twitterclient.models.Url read(android.os.Parcel parcel$$3, IdentityCollection identityMap$$1) {
        int identity$$1 = parcel$$3 .readInt();
        if (identityMap$$1 .containsKey(identity$$1)) {
            if (identityMap$$1 .isReserved(identity$$1)) {
                throw new ParcelerRuntimeException("An instance loop was detected whild building Parcelable and deseralization cannot continue.  This error is most likely due to using @ParcelConstructor or @ParcelFactory.");
            }
            return identityMap$$1 .get(identity$$1);
        } else {
            com.codepath.apps.twitterclient.models.Url url$$4;
            int reservation$$0 = identityMap$$1 .reserve();
            url$$4 = new com.codepath.apps.twitterclient.models.Url();
            identityMap$$1 .put(reservation$$0, url$$4);
            url$$4 .displayUrl = parcel$$3 .readString();
            int int$$0 = parcel$$3 .readInt();
            ArrayList<java.lang.Integer> list$$0;
            if (int$$0 < 0) {
                list$$0 = null;
            } else {
                list$$0 = new ArrayList<java.lang.Integer>(int$$0);
                for (int int$$1 = 0; (int$$1 <int$$0); int$$1 ++) {
                    int int$$2 = parcel$$3 .readInt();
                    java.lang.Integer integer$$1;
                    if (int$$2 < 0) {
                        integer$$1 = null;
                    } else {
                        integer$$1 = parcel$$3 .readInt();
                    }
                    list$$0 .add(integer$$1);
                }
            }
            url$$4 .indices = list$$0;
            url$$4 .expandedUrl = parcel$$3 .readString();
            url$$4 .url = parcel$$3 .readString();
            com.codepath.apps.twitterclient.models.Url url$$3 = url$$4;
            identityMap$$1 .put(identity$$1, url$$3);
            return url$$3;
        }
    }

}
