package com.netease.android.extension.servicekeeper.service.ipc.parceltype;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class StringArrayListParcel implements Parcelable {
    @Nullable
    private List<String> data;
    public static final Creator<StringArrayListParcel> CREATOR = new Creator<StringArrayListParcel>() {
        public StringArrayListParcel createFromParcel(Parcel in) {
            return new StringArrayListParcel(in);
        }

        public StringArrayListParcel[] newArray(int size) {
            return new StringArrayListParcel[size];
        }
    };

    public StringArrayListParcel(@Nullable ArrayList<String> data) {
        this.data = data;
    }

    protected StringArrayListParcel(Parcel in) {
        this.data = in.createStringArrayList();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.data);
    }

    public int describeContents() {
        return 0;
    }

    @Nullable
    public List<String> getData() {
        return this.data;
    }
}
