package com.netease.android.extension.servicekeeper.service.ipc.parceltype;

import android.os.Parcel;
import android.os.Parcelable;

public class StringParcel implements Parcelable {
    private String data;
    public static final Creator<StringParcel> CREATOR = new Creator<StringParcel>() {
        public StringParcel createFromParcel(Parcel in) {
            return new StringParcel(in);
        }

        public StringParcel[] newArray(int size) {
            return new StringParcel[size];
        }
    };

    public StringParcel(String data) {
        this.data = data;
    }

    protected StringParcel(Parcel in) {
        this.data = in.readString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.data);
    }

    public int describeContents() {
        return 0;
    }

    public String getData() {
        return this.data;
    }
}