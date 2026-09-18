package com.netease.android.extension.servicekeeper.service.ipc.parceltype;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class IntegerArrayListParcel implements Parcelable {
    private static final String KEY = "DATA_KEY";
    private Bundle bundle = new Bundle();
    public static final Creator<IntegerArrayListParcel> CREATOR = new Creator<IntegerArrayListParcel>() {
        public IntegerArrayListParcel createFromParcel(Parcel in) {
            return new IntegerArrayListParcel(in);
        }

        public IntegerArrayListParcel[] newArray(int size) {
            return new IntegerArrayListParcel[size];
        }
    };

    public IntegerArrayListParcel(ArrayList<Integer> list) {
        this.bundle.putIntegerArrayList("DATA_KEY", list);
    }

    protected IntegerArrayListParcel(Parcel in) {
        this.bundle = in.readBundle();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.bundle);
    }

    public int describeContents() {
        return 0;
    }

    @Nullable
    public ArrayList<Integer> getData() {
        return this.bundle.getIntegerArrayList("DATA_KEY");
    }
}