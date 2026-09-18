package com.netease.android.extension.servicekeeper.service.ipc.parceltype;

import android.os.Parcel;
import android.os.Parcelable;

public class LongParcel implements Parcelable {
    private Long data;
    public static final Creator<LongParcel> CREATOR = new Creator<LongParcel>() {
        public LongParcel createFromParcel(Parcel in) {
            return new LongParcel(in);
        }

        public LongParcel[] newArray(int size) {
            return new LongParcel[size];
        }
    };

    public LongParcel(Long data) {
        this.data = data;
    }

    protected LongParcel(Parcel in) {
        if (in.readByte() == 0) {
            this.data = null;
        } else {
            this.data = in.readLong();
        }

    }

    public void writeToParcel(Parcel dest, int flags) {
        if (this.data == null) {
            dest.writeByte((byte)0);
        } else {
            dest.writeByte((byte)1);
            dest.writeLong(this.data);
        }

    }

    public int describeContents() {
        return 0;
    }

    public Long getData() {
        return this.data;
    }

    public long getData(long defaultValue) {
        return null == this.data ? defaultValue : this.data;
    }
}