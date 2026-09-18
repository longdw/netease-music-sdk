package com.netease.android.extension.servicekeeper.service.ipc.tx;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class IPCPack<Content extends Parcelable> implements Parcelable {
    @Nullable
    private Content content;
    @NonNull
    private IPCRoute ipcRoute;
    public static final Creator<IPCPack> CREATOR = new Creator<IPCPack>() {
        public IPCPack createFromParcel(Parcel source) {
            return new IPCPack(source);
        }

        public IPCPack[] newArray(int size) {
            return new IPCPack[size];
        }
    };

    public IPCPack(@Nullable Content content) {
        this.content = content;
        this.ipcRoute = new IPCRoute();
    }

    @Nullable
    public Content getContent() {
        return this.content;
    }

    @NonNull
    public IPCRoute getIpcRoute() {
        return this.ipcRoute;
    }

    public String toString() {
        return "IPCPack{content=" + this.content + ", ipcRoute=" + this.ipcRoute + '}';
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.content, flags);
        dest.writeParcelable(this.ipcRoute, flags);
    }

    protected IPCPack(Parcel in) {
        this.content = in.readParcelable(IPCPack.class.getClassLoader());
        this.ipcRoute = (IPCRoute)in.readParcelable(IPCRoute.class.getClassLoader());
    }
}
