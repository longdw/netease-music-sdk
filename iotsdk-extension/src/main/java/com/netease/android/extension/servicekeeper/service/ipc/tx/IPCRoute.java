package com.netease.android.extension.servicekeeper.service.ipc.tx;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

public class IPCRoute implements Parcelable {
    private SKCSerial fromSKCSerial;
    @Nullable
    private Bundle paramExtra;
    @Nullable
    private Bundle receiveExtra;
    public static final Creator<IPCRoute> CREATOR = new Creator<IPCRoute>() {
        public IPCRoute createFromParcel(Parcel in) {
            return new IPCRoute(in);
        }

        public IPCRoute[] newArray(int size) {
            return new IPCRoute[size];
        }
    };

    public IPCRoute() {
    }

    protected IPCRoute(Parcel in) {
        this.fromSKCSerial = (SKCSerial)in.readParcelable(SKCSerial.class.getClassLoader());
        this.paramExtra = in.readBundle();
        this.receiveExtra = in.readBundle();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.fromSKCSerial, flags);
        dest.writeBundle(this.paramExtra);
        dest.writeBundle(this.receiveExtra);
    }

    public int describeContents() {
        return 0;
    }

    @Nullable
    public Bundle getParamExtra() {
        return this.paramExtra;
    }

    public void setParamExtra(@Nullable Bundle paramExtra) {
        this.paramExtra = paramExtra;
    }

    @Nullable
    public Bundle getReceiveExtra() {
        return this.receiveExtra;
    }

    public void setReceiveExtra(@Nullable Bundle receiveExtra) {
        this.receiveExtra = receiveExtra;
    }

    public SKCSerial getFromSKCSerial() {
        return this.fromSKCSerial;
    }

    public void setFromSKCSerial(SKCSerial fromSKCSerial) {
        this.fromSKCSerial = fromSKCSerial;
    }

    public String toString() {
        return "IPCRoute{fromSKCSerial=" + this.fromSKCSerial + ", paramExtra=" + this.paramExtra + ", receiveExtra=" + this.receiveExtra + '}';
    }
}