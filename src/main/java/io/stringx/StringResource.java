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
    public String string;
    public String fieldName;
    public int resourceId;
    public Language language;

    protected StringResource(Parcel in) {
        string = in.readString();
        fieldName = in.readString();
        resourceId = in.readInt();
        language = Language.fromCode(in.readString());
    }

    public StringResource() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(string);
        dest.writeString(fieldName);
        dest.writeInt(resourceId);
        dest.writeString(language.getCode());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringResource that = (StringResource) o;

        if (resourceId != that.resourceId) return false;
        return string.equals(that.string);
    }

    @Override
    public int hashCode() {
        int result = string.hashCode();
        result = 31 * result + resourceId;
        return result;
    }
}
