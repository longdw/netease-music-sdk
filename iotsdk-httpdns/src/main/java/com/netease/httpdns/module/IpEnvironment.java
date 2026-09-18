package com.netease.httpdns.module;

public enum IpEnvironment {
    IPV4(16, "IPV4单栈环境"),
    IPV6(32, "IPV6单栈环境"),
    IP_DUAL_STACK(64, "IPV4、IPV6双栈环境"),
    NETWORK_UNKNOWN(80, "未知网络"),
    FAILED(128, "失败"),
    DOMAIN(144, "Domain");

    private int code;
    private String desc;

    private IpEnvironment(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    public static IpEnvironment parse(int code) {
        for(IpEnvironment status : values()) {
            if (code == status.code) {
                return status;
            }
        }

        return NETWORK_UNKNOWN;
    }
}