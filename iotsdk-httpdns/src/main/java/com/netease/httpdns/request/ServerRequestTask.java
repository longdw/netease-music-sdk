package com.netease.httpdns.request;

import android.text.TextUtils;
import com.netease.httpdns.listener.RequestStatusListener;
import com.netease.httpdns.module.IpEnvironment;
import java.util.ArrayList;
import java.util.List;

public class ServerRequestTask {
    private String networkSessionId;
    private IpEnvironment networkEnvironment;
    private IpEnvironment useIpType;
    private RequestStatusListener listener;
    private List<String> usedListIpv4 = new ArrayList();
    private List<String> usedListIpv6 = new ArrayList();
    private int retryTime = 0;

    public RequestStatusListener getListener() {
        return this.listener;
    }

    public void setListener(RequestStatusListener listener) {
        this.listener = listener;
    }

    public String getNetworkSessionId() {
        return this.networkSessionId;
    }

    public void setNetworkSessionId(String networkSessionId) {
        this.networkSessionId = networkSessionId;
    }

    public IpEnvironment getUseIpType() {
        return this.useIpType;
    }

    public void setUseIpType(IpEnvironment useIpType) {
        this.useIpType = useIpType;
    }

    public IpEnvironment getNetworkEnvironment() {
        return this.networkEnvironment;
    }

    public void setNetworkEnvironment(IpEnvironment networkEnvironment) {
        this.networkEnvironment = networkEnvironment;
    }

    public List<String> getUsedListIpv4() {
        return this.usedListIpv4;
    }

    public void addUsedIpv4(String usedIpv4) {
        if (!TextUtils.isEmpty(usedIpv4)) {
            this.usedListIpv4.add(usedIpv4);
        }
    }

    public List<String> getUsedListIpv6() {
        return this.usedListIpv6;
    }

    public void addUsedIpv6(String usedIpv6) {
        if (!TextUtils.isEmpty(usedIpv6)) {
            this.usedListIpv6.add(usedIpv6);
        }
    }

    public int getRetryTime() {
        return this.retryTime;
    }

    public void addRetryTime() {
        ++this.retryTime;
    }
}