package com.netease.android.extension.servicekeeper.service.ipc.parceltype;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

public class DoubleArrayParcel implements Parcelable {
    private static final String KEY = "DATA_KEY";
    private Bundle bundle = new Bundle();
    public static final Creator<DoubleArrayParcel> CREATOR = new Creator<DoubleArrayParcel>() {
        public DoubleArrayParcel createFromParcel(Parcel in) {
            return new DoubleArrayParcel(in);
        }

        public DoubleArrayParcel[] newArray(int size) {
            return new DoubleArrayParcel[size];
        }
    };

    public DoubleArrayParcel(double[] array) {
        this.bundle.putDoubleArray("DATA_KEY", array);
    }

    protected DoubleArrayParcel(Parcel in) {
        this.bundle = in.readBundle();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.bundle);
    }

    public int describeContents() {
        return 0;
    }

    @Nullable
    public double[] getData() {
        return this.bundle.getDoubleArray("DATA_KEY");
    }
}