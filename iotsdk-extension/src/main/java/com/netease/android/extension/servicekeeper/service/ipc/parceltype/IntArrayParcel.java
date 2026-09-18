package com.netease.android.extension.servicekeeper.service.ipc.parceltype;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

public class IntArrayParcel implements Parcelable {
    private static final String KEY = "DATA_KEY";
    private Bundle bundle = new Bundle();
    public static final Creator<IntArrayParcel> CREATOR = new Creator<IntArrayParcel>() {
        public IntArrayParcel createFromParcel(Parcel in) {
            return new IntArrayParcel(in);
        }

        public IntArrayParcel[] newArray(int size) {
            return new IntArrayParcel[size];
        }
    };

    public IntArrayParcel(int[] array) {
        this.bundle.putIntArray("DATA_KEY", array);
    }

    protected IntArrayParcel(Parcel in) {
        this.bundle = in.readBundle();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.bundle);
    }

    public int describeContents() {
        return 0;
    }

    @Nullable
    public int[] getData() {
        return this.bundle.getIntArray("DATA_KEY");
    }
}
