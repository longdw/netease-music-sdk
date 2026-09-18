package com.netease.httpdns.configuration;

public interface DNSRequestUrl {
    String PORT_80 = "80";
    String PORT_443 = "443";
    String[] REQUEST_URL_IPV4 = new String[]{"59.111.179.213", "59.111.179.214", "59.111.239.61", "59.111.239.62"};
    String[] REQUEST_URL_IPV6 = new String[]{"2407:ae80:500:1003:59:111:239:61", "2407:ae80:500:1003:59:111:239:62", "2407:ae80:200:100c:59:111:179:213", "2407:ae80:200:100c:59:111:179:214"};
}