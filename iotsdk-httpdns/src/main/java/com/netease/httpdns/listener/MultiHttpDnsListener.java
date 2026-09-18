package com.netease.httpdns.listener;

import com.netease.httpdns.module.DomainInfo;
import java.util.Map;

public interface MultiHttpDnsListener {
    void onIpsParsed(Map<String, DomainInfo> var1);
}