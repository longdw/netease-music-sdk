package com.netease.httpdns.provider.dal.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.netease.httpdns.module.IpEnvironment;

public class DNSServer implements Parcelable {
    public static final String TABLE_NAME = "dns_server";
    public static final String COLUMN_NETWORK_TYPE = "network_type";
    public static final String COLUMN_CONTENT_JSON = "content_json";
    public static final String COLUMN_NETWORK_ENVIRONMENT = "network_environment";
    public static final String COLUMN_USE_ENVIRONMENT = "use_environment";
    public static final String COLUMN_CREATED_AT = "created_at";
    private String networkType;
    private String contentJson;
    private IpEnvironment ipEnvironment;
    private IpEnvironment useIpType;
    private long createdAt;
    public static final Creator<DNSServer> CREATOR = new Creator<DNSServer>() {
        public DNSServer createFromParcel(Parcel source) {
            return new DNSServer(source);
        }

        public DNSServer[] newArray(int size) {
            return new DNSServer[size];
        }
    };

    public DNSServer() {
        this((String)null, (String)null, (IpEnvironment)null, (IpEnvironment)null);
    }

    public DNSServer(String networkType, String contentJson, IpEnvironment ipStackType, IpEnvironment useIpType) {
        this.networkType = networkType;
        this.contentJson = contentJson;
        this.ipEnvironment = ipStackType;
        this.useIpType = useIpType;
        this.createdAt = System.currentTimeMillis();
    }

    public String getNetworkType() {
        return this.networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getContentJson() {
        return this.contentJson;
    }

    public void setContentJson(String contentJson) {
        this.contentJson = contentJson;
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public IpEnvironment getIpEnvironment() {
        return this.ipEnvironment;
    }

    public void setIpEnvironment(IpEnvironment ipEnvironment) {
        this.ipEnvironment = ipEnvironment;
    }

    public IpEnvironment getUseIpType() {
        return this.useIpType;
    }

    public void setUseIpType(IpEnvironment useIpType) {
        this.useIpType = useIpType;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.networkType);
        dest.writeString(this.contentJson);
        dest.writeInt(this.ipEnvironment == null ? -1 : this.ipEnvironment.ordinal());
        dest.writeInt(this.useIpType == null ? -1 : this.useIpType.ordinal());
        dest.writeLong(this.createdAt);
    }

    public void readFromParcel(Parcel source) {
        this.networkType = source.readString();
        this.contentJson = source.readString();
        int tmpIpStackType = source.readInt();
        this.ipEnvironment = tmpIpStackType == -1 ? null : IpEnvironment.values()[tmpIpStackType];
        int tmpUseIpType = source.readInt();
        this.useIpType = tmpUseIpType == -1 ? null : IpEnvironment.values()[tmpUseIpType];
        this.createdAt = source.readLong();
    }

    protected DNSServer(Parcel in) {
        this.networkType = in.readString();
        this.contentJson = in.readString();
        int tmpIpStackType = in.readInt();
        this.ipEnvironment = tmpIpStackType == -1 ? null : IpEnvironment.values()[tmpIpStackType];
        int tmpUseIpType = in.readInt();
        this.useIpType = tmpUseIpType == -1 ? null : IpEnvironment.values()[tmpUseIpType];
        this.createdAt = in.readLong();
    }
}
