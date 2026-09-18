package com.netease.android.extension.servicekeeper.service.ipc.parceltype;

import android.os.Parcel;
import android.os.Parcelable;

public class DoubleParcel implements Parcelable {
    private Double data;
    public static final Creator<DoubleParcel> CREATOR = new Creator<DoubleParcel>() {
        public DoubleParcel createFromParcel(Parcel in) {
            return new DoubleParcel(in);
        }

        public DoubleParcel[] newArray(int size) {
            return new DoubleParcel[size];
        }
    };

    public DoubleParcel(Double data) {
        this.data = data;
    }

    protected DoubleParcel(Parcel in) {
        if (in.readByte() == 0) {
            this.data = null;
        } else {
            this.data = in.readDouble();
        }

    }

    public void writeToParcel(Parcel dest, int flags) {
        if (this.data == null) {
            dest.writeByte((byte)0);
        } else {
            dest.writeByte((byte)1);
            dest.writeDouble(this.data);
        }

    }

    public int describeContents() {
        return 0;
    }

    public Double getData() {
        return this.data;
    }

    public double getData(double defaultValue) {
        return null == this.data ? defaultValue : this.data;
    }
}