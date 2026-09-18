package com.netease.httpdns.module;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class IPCDomainRequest implements Parcelable {
    private List<String> domains;
    public static final Creator<IPCDomainRequest> CREATOR = new Creator<IPCDomainRequest>() {
        public IPCDomainRequest createFromParcel(Parcel source) {
            return new IPCDomainRequest(source);
        }

        public IPCDomainRequest[] newArray(int size) {
            return new IPCDomainRequest[size];
        }
    };

    public IPCDomainRequest(List<String> domains) {
        this.domains = domains;
    }

    public List<String> getDomains() {
        return this.domains;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.domains);
    }

    public void readFromParcel(Parcel source) {
        this.domains = source.createStringArrayList();
    }

    protected IPCDomainRequest(Parcel in) {
        this.domains = in.createStringArrayList();
    }
}