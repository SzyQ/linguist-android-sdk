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
    public List<String> strings;
    public Language original;
    public Language desired;
    public String packageName;

    public TranslationConfig() {

    }

    protected TranslationConfig(Parcel in) {
        strings = in.createStringArrayList();
        packageName = in.readString();
        original = Language.fromCode(in.readString());
        desired = Language.fromCode(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(strings);
        dest.writeString(packageName);
        dest.writeString(original.getCode());
        dest.writeString(desired.getCode());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return packageName + ": " + original + "->" + desired;
    }
}
