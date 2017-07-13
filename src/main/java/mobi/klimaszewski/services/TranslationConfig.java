package mobi.klimaszewski.services;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TranslationConfig implements Parcelable {
    public List<String> strings;
    public String originalCode;
    public String desiredCode;
    public String packageName;

    public TranslationConfig() {

    }

    protected TranslationConfig(Parcel in) {
        strings = in.createStringArrayList();
        originalCode = in.readString();
        desiredCode = in.readString();
        packageName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(strings);
        dest.writeString(originalCode);
        dest.writeString(desiredCode);
        dest.writeString(packageName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

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

    @Override
    public String toString() {
        return packageName + ": " + originalCode + "->" + desiredCode;
    }
}
