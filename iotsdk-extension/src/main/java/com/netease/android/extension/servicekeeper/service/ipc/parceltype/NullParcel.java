package com.netease.android.extension.servicekeeper.service.ipc.parceltype;

import android.os.Parcel;
import android.os.Parcelable;

public class NullParcel implements Parcelable {
    private byte nullByte;
    public static final Creator<NullParcel> CREATOR = new Creator<NullParcel>() {
        public NullParcel createFromParcel(Parcel in) {
            return new NullParcel(in);
        }

        public NullParcel[] newArray(int size) {
            return new NullParcel[size];
        }
    };

    public NullParcel() {
    }

    protected NullParcel(Parcel in) {
        this.nullByte = in.readByte();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.nullByte);
    }

    public int describeContents() {
        return 0;
    }
}