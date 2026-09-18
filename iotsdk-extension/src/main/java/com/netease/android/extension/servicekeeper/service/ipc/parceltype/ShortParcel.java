package com.netease.android.extension.servicekeeper.service.ipc.parceltype;

import android.os.Parcel;
import android.os.Parcelable;

public class ShortParcel implements Parcelable {
    private Short data;
    public static final Creator<ShortParcel> CREATOR = new Creator<ShortParcel>() {
        public ShortParcel createFromParcel(Parcel in) {
            return new ShortParcel(in);
        }

        public ShortParcel[] newArray(int size) {
            return new ShortParcel[size];
        }
    };

    public ShortParcel(Short data) {
        this.data = data;
    }

    protected ShortParcel(Parcel in) {
        int tmpData = in.readInt();
        this.data = tmpData != Integer.MAX_VALUE ? (short)tmpData : null;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.data != null ? this.data : Integer.MAX_VALUE);
    }

    public int describeContents() {
        return 0;
    }

    public Short getData() {
        return this.data;
    }

    public short getData(short defaultValue) {
        return null == this.data ? defaultValue : this.data;
    }
}