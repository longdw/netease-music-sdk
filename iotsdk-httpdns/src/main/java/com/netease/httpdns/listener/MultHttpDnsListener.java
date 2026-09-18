package com.netease.httpdns.listener;

import com.netease.httpdns.module.DomainInfo;
import java.util.List;

public interface MultHttpDnsListener {
    void onIpsParsed(List<DomainInfo> var1);
}