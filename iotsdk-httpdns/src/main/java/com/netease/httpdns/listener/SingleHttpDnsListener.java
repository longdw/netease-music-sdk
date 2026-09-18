package com.netease.httpdns.listener;

import java.util.List;

public interface SingleHttpDnsListener {
    void onIpParsed(String var1);

    void onIpsParsed(List<String> var1);
}