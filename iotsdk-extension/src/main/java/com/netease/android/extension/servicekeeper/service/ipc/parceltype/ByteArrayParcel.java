package com.netease.android.extension.servicekeeper.service.ipc.parceltype;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

public class ByteArrayParcel implements Parcelable {
    private static final String KEY = "DATA_KEY";
    private Bundle bundle = new Bundle();
    public static final Creator<ByteArrayParcel> CREATOR = new Creator<ByteArrayParcel>() {
        public ByteArrayParcel createFromParcel(Parcel in) {
            return new ByteArrayParcel(in);
        }

        public ByteArrayParcel[] newArray(int size) {
            return new ByteArrayParcel[size];
        }
    };

    public ByteArrayParcel(byte[] array) {
        this.bundle.putByteArray("DATA_KEY", array);
    }

    protected ByteArrayParcel(Parcel in) {
        this.bundle = in.readBundle();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.bundle);
    }

    public int describeContents() {
        return 0;
    }

    @Nullable
    public byte[] getData() {
        return this.bundle.getByteArray("DATA_KEY");
    }
}
