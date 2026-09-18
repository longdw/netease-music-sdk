package com.netease.android.extension.servicekeeper.service.ipc.parceltype;

import android.os.Parcel;
import android.os.Parcelable;

public class FloatParcel implements Parcelable {
    private Float data;
    public static final Creator<FloatParcel> CREATOR = new Creator<FloatParcel>() {
        public FloatParcel createFromParcel(Parcel in) {
            return new FloatParcel(in);
        }

        public FloatParcel[] newArray(int size) {
            return new FloatParcel[size];
        }
    };

    public FloatParcel(Float data) {
        this.data = data;
    }

    protected FloatParcel(Parcel in) {
        if (in.readByte() == 0) {
            this.data = null;
        } else {
            this.data = in.readFloat();
        }

    }

    public void writeToParcel(Parcel dest, int flags) {
        if (this.data == null) {
            dest.writeByte((byte)0);
        } else {
            dest.writeByte((byte)1);
            dest.writeFloat(this.data);
        }

    }

    public int describeContents() {
        return 0;
    }

    public Float getData() {
        return this.data;
    }

    public float getData(float defaultValue) {
        return null == this.data ? defaultValue : this.data;
    }
}