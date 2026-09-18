package com.netease.httpdns.util;

import android.text.TextUtils;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class LocalDNSUtil {
    public static List<String> getLocalDomainIp(String domain) {
        if (domain != null && !TextUtils.isEmpty(domain.trim())) {
            InetAddress[] address = null;

            try {
                address = InetAddress.getAllByName(domain);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (address != null && address.length != 0) {
                int len = address.length;
                List<String> ips = new ArrayList(len);

                for(int i = 0; i < len; ++i) {
                    ips.add(address[i].getHostAddress());
                }

                return ips;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}