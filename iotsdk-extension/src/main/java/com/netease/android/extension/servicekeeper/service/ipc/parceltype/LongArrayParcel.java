package com.netease.android.extension.servicekeeper.service.ipc.parceltype;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

public class LongArrayParcel implements Parcelable {
    private static final String KEY = "DATA_KEY";
    private Bundle bundle = new Bundle();
    public static final Creator<LongArrayParcel> CREATOR = new Creator<LongArrayParcel>() {
        public LongArrayParcel createFromParcel(Parcel in) {
            return new LongArrayParcel(in);
        }

        public LongArrayParcel[] newArray(int size) {
            return new LongArrayParcel[size];
        }
    };

    public LongArrayParcel(long[] array) {
        this.bundle.putLongArray("DATA_KEY", array);
    }

    protected LongArrayParcel(Parcel in) {
        this.bundle = in.readBundle();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.bundle);
    }

    public int describeContents() {
        return 0;
    }

    @Nullable
    public long[] getData() {
        return this.bundle.getLongArray("DATA_KEY");
    }
}
