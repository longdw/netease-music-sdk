package com.netease.httpdns.request;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class DomainRequestTask implements Parcelable {
    private List<String> domains;
    private boolean isIpv6Request = false;
    private boolean isHttp = false;
    public static final Creator<DomainRequestTask> CREATOR = new Creator<DomainRequestTask>() {
        public DomainRequestTask createFromParcel(Parcel source) {
            return new DomainRequestTask(source);
        }

        public DomainRequestTask[] newArray(int size) {
            return new DomainRequestTask[size];
        }
    };

    public List<String> getDomains() {
        return this.domains;
    }

    public void setDomains(List<String> domains) {
        this.domains = domains;
    }

    public boolean isIpv6Request() {
        return this.isIpv6Request;
    }

    public void setIpv6Request(boolean ipv6Request) {
        this.isIpv6Request = ipv6Request;
    }

    public boolean isHttp() {
        return this.isHttp;
    }

    public DomainRequestTask setHttp(boolean http) {
        this.isHttp = http;
        return this;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.domains);
        dest.writeByte((byte)(this.isIpv6Request ? 1 : 0));
        dest.writeByte((byte)(this.isHttp ? 1 : 0));
    }

    public void readFromParcel(Parcel source) {
        this.domains = source.createStringArrayList();
        this.isIpv6Request = source.readByte() != 0;
        this.isHttp = source.readByte() != 0;
    }

    public DomainRequestTask() {
    }

    protected DomainRequestTask(Parcel in) {
        this.domains = in.createStringArrayList();
        this.isIpv6Request = in.readByte() != 0;
        this.isHttp = in.readByte() != 0;
    }
}