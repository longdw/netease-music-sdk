package com.netease.android.extension.servicekeeper.service.ipc.parceltype;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;

public class TypedSerializableParcel<T extends Serializable> implements Parcelable {
    private T data;
    public static final Creator<TypedSerializableParcel> CREATOR = new Creator<TypedSerializableParcel>() {
        public TypedSerializableParcel createFromParcel(Parcel source) {
            return new TypedSerializableParcel(source);
        }

        public TypedSerializableParcel[] newArray(int size) {
            return new TypedSerializableParcel[size];
        }
    };

    public TypedSerializableParcel(T data) {
        this.data = data;
    }

    public T getData() {
        return this.data;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.data);
    }

    protected TypedSerializableParcel(Parcel in) {
        this.data = (T) in.readSerializable();
    }
}