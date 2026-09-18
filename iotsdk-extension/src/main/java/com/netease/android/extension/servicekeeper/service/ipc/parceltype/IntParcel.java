package com.netease.android.extension.servicekeeper.service.ipc.parceltype;

import android.os.Parcel;
import android.os.Parcelable;

public class IntParcel implements Parcelable {
    private Integer data;
    public static final Creator<IntParcel> CREATOR = new Creator<IntParcel>() {
        public IntParcel createFromParcel(Parcel in) {
            return new IntParcel(in);
        }

        public IntParcel[] newArray(int size) {
            return new IntParcel[size];
        }
    };

    public IntParcel(Integer data) {
        this.data = data;
    }

    protected IntParcel(Parcel in) {
        if (in.readByte() == 0) {
            this.data = null;
        } else {
            this.data = in.readInt();
        }

    }

    public void writeToParcel(Parcel dest, int flags) {
        if (this.data == null) {
            dest.writeByte((byte)0);
        } else {
            dest.writeByte((byte)1);
            dest.writeInt(this.data);
        }

    }

    public int describeContents() {
        return 0;
    }

    public Integer getData() {
        return this.data;
    }

    public int getData(int defaultValue) {
        return null == this.data ? defaultValue : this.data;
    }
}