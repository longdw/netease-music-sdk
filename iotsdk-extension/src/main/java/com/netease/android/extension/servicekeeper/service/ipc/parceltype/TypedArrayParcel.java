package com.netease.android.extension.servicekeeper.service.ipc.parceltype;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import com.netease.android.extension.util.ELog;

public class TypedArrayParcel<T extends Parcelable> implements Parcelable {
    private static final String KEY = "DATA_KEY";
    private Bundle bundle = new Bundle();
    public static final Creator<TypedArrayParcel> CREATOR = new Creator<TypedArrayParcel>() {
        public TypedArrayParcel createFromParcel(Parcel in) {
            return new TypedArrayParcel(in);
        }

        public TypedArrayParcel[] newArray(int size) {
            return new TypedArrayParcel[size];
        }
    };

    public TypedArrayParcel(T[] array) {
        this.bundle.putParcelableArray("DATA_KEY", array);
    }

    protected TypedArrayParcel(Parcel in) {
        this.bundle = in.readBundle();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.bundle);
    }

    public int describeContents() {
        return 0;
    }

    @Nullable
    public T[] getData() {
        try {
            return (T[])(this.bundle.getParcelableArray("DATA_KEY"));
        } catch (ClassCastException e) {
            ELog.e("[TypedArrayParcel]getData error: ", e);
            return null;
        }
    }
}