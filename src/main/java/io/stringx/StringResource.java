package io.stringx;

import android.os.Parcel;
import android.os.Parcelable;

public class StringResource implements Parcelable {
    public static final Creator<StringResource> CREATOR = new Creator<StringResource>() {
        @Override
        public StringResource createFromParcel(Parcel in) {
            return new StringResource(in);
        }

        @Override
        public StringResource[] newArray(int size) {
            return new StringResource[size];
        }
    };

    public int resourceId;
    public Language language;

    protected StringResource(Parcel in) {
        resourceId = in.readInt();
        language = Language.fromCode(in.readString());
    }

    public StringResource() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(resourceId);
        dest.writeString(language.getCode());
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
