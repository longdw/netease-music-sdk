package com.netease.android.extension.servicekeeper.service.ipc.tx;

import android.os.Parcel;
import android.os.Parcelable;
import com.netease.android.extension.ext.ObjectExt;

public class SKCSerial implements Parcelable {
    private String app;
    private String skcName;
    private int pid;
    private String deviceNum;
    public static final Creator<SKCSerial> CREATOR = new Creator<SKCSerial>() {
        public SKCSerial createFromParcel(Parcel in) {
            return new SKCSerial(in);
        }

        public SKCSerial[] newArray(int size) {
            return new SKCSerial[size];
        }
    };

    public SKCSerial(String app, String skcName, int pid) {
        this(app, skcName, pid, "0");
    }

    public SKCSerial(String app, String skcName, int pid, String deviceNum) {
        this.app = app;
        this.skcName = skcName;
        this.pid = pid;
        this.deviceNum = deviceNum;
    }

    protected SKCSerial(Parcel in) {
        this.app = in.readString();
        this.skcName = in.readString();
        this.pid = in.readInt();
        this.deviceNum = in.readString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            SKCSerial skcSerial = (SKCSerial)o;
            return this.pid == skcSerial.pid && ObjectExt.equals(this.app, skcSerial.app) && ObjectExt.equals(this.skcName, skcSerial.skcName) && ObjectExt.equals(this.deviceNum, skcSerial.deviceNum);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return ObjectExt.hash(new Object[]{this.app, this.skcName, this.pid, this.deviceNum});
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.app);
        dest.writeString(this.skcName);
        dest.writeInt(this.pid);
        dest.writeString(this.deviceNum);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "SKCSerial{app='" + this.app + '\'' + ", skcName='" + this.skcName + '\'' + ", pid=" + this.pid + ", deviceNum='" + this.deviceNum + '\'' + '}';
    }

    public String getSkcName() {
        return this.skcName;
    }

    public void setSkcName(String skcName) {
        this.skcName = skcName;
    }

    public int getPid() {
        return this.pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getDeviceNum() {
        return this.deviceNum;
    }

    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum;
    }

    public String getApp() {
        return this.app;
    }
}
