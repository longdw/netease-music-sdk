package com.netease.android.extension.servicekeeper.service.ipc.parceltype;

import android.os.Parcel;
import android.os.Parcelable;

public class ByteParcel implements Parcelable {
    private Byte data;
    public static final Creator<ByteParcel> CREATOR = new Creator<ByteParcel>() {
        public ByteParcel createFromParcel(Parcel in) {
            return new ByteParcel(in);
        }

        public ByteParcel[] newArray(int size) {
            return new ByteParcel[size];
        }
    };

    public ByteParcel(Byte data) {
        this.data = data;
    }

    protected ByteParcel(Parcel in) {
        if (in.readByte() == 0) {
            this.data = null;
        } else {
            this.data = in.readByte();
        }

    }

    public void writeToParcel(Parcel dest, int flags) {
        if (this.data == null) {
            dest.writeByte((byte)0);
        } else {
            dest.writeByte((byte)1);
            dest.writeByte(this.data);
        }

    }

    public int describeContents() {
        return 0;
    }

    public Byte getData() {
        return this.data;
    }

    public byte getData(byte defaultValue) {
        return null == this.data ? defaultValue : this.data;
    }
}
