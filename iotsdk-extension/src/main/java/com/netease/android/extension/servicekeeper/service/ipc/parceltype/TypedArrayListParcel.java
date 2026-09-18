package com.netease.android.extension.servicekeeper.service.ipc.parceltype;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class TypedArrayListParcel<T extends Parcelable> implements Parcelable {
    private static final String KEY = "DATA_KEY";
    private Bundle bundle = new Bundle();
    public static final Creator<TypedArrayListParcel> CREATOR = new Creator<TypedArrayListParcel>() {
        public TypedArrayListParcel createFromParcel(Parcel in) {
            return new TypedArrayListParcel(in);
        }

        public TypedArrayListParcel[] newArray(int size) {
            return new TypedArrayListParcel[size];
        }
    };

    public TypedArrayListParcel(ArrayList<T> list) {
        this.bundle.putParcelableArrayList("DATA_KEY", list);
    }

    protected TypedArrayListParcel(Parcel in) {
        this.bundle = in.readBundle();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.bundle);
    }

    public int describeContents() {
        return 0;
    }

    @Nullable
    public ArrayList<T> getData() {
        return this.bundle.getParcelableArrayList("DATA_KEY");
    }
}