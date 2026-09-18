package com.netease.httpdns.module;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerAddress implements Parcelable {
    private static final String COLON = ":";
    private static final String SERVERS = "servers";
    private static final String HOST = "host";
    private static final String PORT = "port";
    private static final String SERVER_PREFER = "prefer";
    private String url;
    private String port;
    private String prefer;
    private String networkType;
    public static final Creator<ServerAddress> CREATOR = new Creator<ServerAddress>() {
        public ServerAddress createFromParcel(Parcel source) {
            return new ServerAddress(source);
        }

        public ServerAddress[] newArray(int size) {
            return new ServerAddress[size];
        }
    };

    public ServerAddress(String url) {
        this(url, (String)null);
    }

    public ServerAddress(String url, String port) {
        this.url = url;
        this.port = port;
    }

    public ServerAddress(String url, String port, String prefer) {
        this.url = url;
        this.port = port;
        this.prefer = prefer;
    }

    public String getNetworkType() {
        return this.networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPort() {
        return this.port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPrefer() {
        return this.prefer;
    }

    public ServerAddress setPrefer(String prefer) {
        this.prefer = prefer;
        return this;
    }

    public String toString() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("port", this.port);
            jsonObject.put("host", this.url);
            jsonObject.put("prefer", this.prefer);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    public String getRequestUrl(boolean isIpv6Request) {
        if (TextUtils.isEmpty(this.getUrl())) {
            return null;
        } else {
            return isIpv6Request ? "[" + this.getUrl() + "]" : this.getUrl();
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.port);
        dest.writeString(this.prefer);
        dest.writeString(this.networkType);
    }

    protected ServerAddress(Parcel in) {
        this.url = in.readString();
        this.port = in.readString();
        this.prefer = in.readString();
        this.networkType = in.readString();
    }
}