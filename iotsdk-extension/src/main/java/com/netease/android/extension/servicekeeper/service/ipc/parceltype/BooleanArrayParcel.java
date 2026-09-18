package com.netease.android.extension.servicekeeper.service.ipc.parceltype;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

public class BooleanArrayParcel implements Parcelable {
    private static final String KEY = "DATA_KEY";
    private Bundle bundle = new Bundle();
    public static final Creator<BooleanArrayParcel> CREATOR = new Creator<BooleanArrayParcel>() {
        public BooleanArrayParcel createFromParcel(Parcel in) {
            return new BooleanArrayParcel(in);
        }

        public BooleanArrayParcel[] newArray(int size) {
            return new BooleanArrayParcel[size];
        }
    };

    public BooleanArrayParcel(boolean[] array) {
        this.bundle.putBooleanArray("DATA_KEY", array);
    }

    protected BooleanArrayParcel(Parcel in) {
        this.bundle = in.readBundle();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.bundle);
    }

    public int describeContents() {
        return 0;
    }

    @Nullable
    public boolean[] getData() {
        return this.bundle.getBooleanArray("DATA_KEY");
    }
}