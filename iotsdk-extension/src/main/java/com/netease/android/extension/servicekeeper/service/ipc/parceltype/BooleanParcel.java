package com.netease.android.extension.servicekeeper.service.ipc.parceltype;

import android.os.Parcel;
import android.os.Parcelable;

public class BooleanParcel implements Parcelable {
    private Boolean data;
    public static final Creator<BooleanParcel> CREATOR = new Creator<BooleanParcel>() {
        public BooleanParcel createFromParcel(Parcel in) {
            return new BooleanParcel(in);
        }

        public BooleanParcel[] newArray(int size) {
            return new BooleanParcel[size];
        }
    };

    public BooleanParcel(Boolean data) {
        this.data = data;
    }

    protected BooleanParcel(Parcel in) {
        byte tmpData = in.readByte();
        this.data = tmpData == 0 ? null : tmpData == 1;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte)(this.data == null ? 0 : (this.data ? 1 : 2)));
    }

    public int describeContents() {
        return 0;
    }

    public Boolean getData() {
        return this.data;
    }

    public boolean getData(boolean defaultValue) {
        return null == this.data ? defaultValue : this.data;
    }
}