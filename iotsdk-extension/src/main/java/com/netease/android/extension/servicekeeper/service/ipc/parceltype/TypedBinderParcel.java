package com.netease.android.extension.servicekeeper.service.ipc.parceltype;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

public class TypedBinderParcel<T extends IBinder> implements Parcelable {
    private T data;
    public static final Creator<TypedBinderParcel> CREATOR = new Creator<TypedBinderParcel>() {
        public TypedBinderParcel createFromParcel(Parcel source) {
            return new TypedBinderParcel(source);
        }

        public TypedBinderParcel[] newArray(int size) {
            return new TypedBinderParcel[size];
        }
    };

    public TypedBinderParcel(T data) {
        this.data = data;
    }

    public T getData() {
        return this.data;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStrongBinder(this.data);
    }

    protected TypedBinderParcel(Parcel in) {
        this.data = (T) in.readStrongBinder();
    }
}