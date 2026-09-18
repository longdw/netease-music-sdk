package com.netease.android.extension.servicekeeper.service.ipc.parceltype;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

public class ShortArrayParcel implements Parcelable {
    private static final String KEY = "DATA_KEY";
    private Bundle bundle = new Bundle();
    public static final Creator<ShortArrayParcel> CREATOR = new Creator<ShortArrayParcel>() {
        public ShortArrayParcel createFromParcel(Parcel in) {
            return new ShortArrayParcel(in);
        }

        public ShortArrayParcel[] newArray(int size) {
            return new ShortArrayParcel[size];
        }
    };

    public ShortArrayParcel(short[] array) {
        this.bundle.putShortArray("DATA_KEY", array);
    }

    protected ShortArrayParcel(Parcel in) {
        this.bundle = in.readBundle();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.bundle);
    }

    public int describeContents() {
        return 0;
    }

    @Nullable
    public short[] getData() {
        return this.bundle.getShortArray("DATA_KEY");
    }
}