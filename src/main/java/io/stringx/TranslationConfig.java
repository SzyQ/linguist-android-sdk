package io.stringx;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TranslationConfig implements Parcelable {

    public static final Creator<TranslationConfig> CREATOR = new Creator<TranslationConfig>() {
        @Override
        public TranslationConfig createFromParcel(Parcel in) {
            return new TranslationConfig(in);
        }

        @Override
        public TranslationConfig[] newArray(int size) {
            return new TranslationConfig[size];
        }
    };
    public String packageName;
    public Language defaultLanguage;
    public Language desiredLanguage;
    public List<StringResource> resources;

    protected TranslationConfig(Parcel in) {
        packageName = in.readString();
        resources = in.createTypedArrayList(StringResource.CREATOR);
        defaultLanguage = Language.fromCode(in.readString());
        desiredLanguage = Language.fromCode(in.readString());
    }

    public TranslationConfig() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(packageName);
        dest.writeTypedList(resources);
        dest.writeString(defaultLanguage.getCode());
        dest.writeString(desiredLanguage.getCode());
    }

    @Override
    public String toString() {
        return packageName + "-" + defaultLanguage;
    }

}
