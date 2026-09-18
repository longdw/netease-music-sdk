package com.netease.android.extension.servicekeeper.service.ipc.parceltype;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

public class CharArrayParcel implements Parcelable {
    private static final String KEY = "DATA_KEY";
    private Bundle bundle = new Bundle();
    public static final Creator<CharArrayParcel> CREATOR = new Creator<CharArrayParcel>() {
        public CharArrayParcel createFromParcel(Parcel in) {
            return new CharArrayParcel(in);
        }

        public CharArrayParcel[] newArray(int size) {
            return new CharArrayParcel[size];
        }
    };

    public CharArrayParcel(char[] array) {
        this.bundle.putCharArray("DATA_KEY", array);
    }

    protected CharArrayParcel(Parcel in) {
        this.bundle = in.readBundle();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.bundle);
    }

    public int describeContents() {
        return 0;
    }

    @Nullable
    public char[] getData() {
        return this.bundle.getCharArray("DATA_KEY");
    }
}
