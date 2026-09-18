package com.netease.android.extension.servicekeeper.service.ipc.parceltype;

import android.os.Parcel;
import android.os.Parcelable;

public class CharacterParcel implements Parcelable {
    private Character data;
    public static final Creator<CharacterParcel> CREATOR = new Creator<CharacterParcel>() {
        public CharacterParcel createFromParcel(Parcel in) {
            return new CharacterParcel(in);
        }

        public CharacterParcel[] newArray(int size) {
            return new CharacterParcel[size];
        }
    };

    public CharacterParcel(Character data) {
        this.data = data;
    }

    protected CharacterParcel(Parcel in) {
        int tmpData = in.readInt();
        this.data = tmpData != Integer.MAX_VALUE ? (char)tmpData : null;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.data != null ? this.data : Integer.MAX_VALUE);
    }

    public int describeContents() {
        return 0;
    }

    public Character getData() {
        return this.data;
    }

    public char getData(char defaultValue) {
        return null == this.data ? defaultValue : this.data;
    }
}
