package com.netease.httpdns.provider.dal.model;

@Deprecated
public class DNSCacheItem {
    public static final String TABLE_NAME = "dns_cache_item";
    public static final String COLUMN_CONFIG_ID = "config_id";
    public static final String COLUMN_HOST = "host";
    public static final String COLUMN_NETWORK_TYPE = "network_type";
    public static final String COLUMN_CONTENT_JSON = "content_json";
    public static final String COLUMN_CREATED_AT = "created_at";
    private String configId = "DEFAULT";
    private String host;
    private String networkType;
    private String contentJson;
    private long createdAt;

    public String getConfigId() {
        return this.configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getContentJson() {
        return this.contentJson;
    }

    public void setContentJson(String contentJson) {
        this.contentJson = contentJson;
    }

    public String getNetworkType() {
        return this.networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}