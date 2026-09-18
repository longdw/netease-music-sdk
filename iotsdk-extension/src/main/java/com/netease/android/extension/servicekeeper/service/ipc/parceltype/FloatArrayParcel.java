package com.netease.android.extension.servicekeeper.service.ipc.parceltype;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

public class FloatArrayParcel implements Parcelable {
    private static final String KEY = "DATA_KEY";
    private Bundle bundle = new Bundle();
    public static final Creator<FloatArrayParcel> CREATOR = new Creator<FloatArrayParcel>() {
        public FloatArrayParcel createFromParcel(Parcel in) {
            return new FloatArrayParcel(in);
        }

        public FloatArrayParcel[] newArray(int size) {
            return new FloatArrayParcel[size];
        }
    };

    public FloatArrayParcel(float[] array) {
        this.bundle.putFloatArray("DATA_KEY", array);
    }

    protected FloatArrayParcel(Parcel in) {
        this.bundle = in.readBundle();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.bundle);
    }

    public int describeContents() {
        return 0;
    }

    @Nullable
    public float[] getData() {
        return this.bundle.getFloatArray("DATA_KEY");
    }
}
