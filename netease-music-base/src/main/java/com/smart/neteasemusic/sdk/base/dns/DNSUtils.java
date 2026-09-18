package com.smart.neteasemusic.sdk.base.dns;

import android.text.TextUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author iBiandev
 * DNS/ IP 相关的辅助类
 */
public class DNSUtils {
    /**
     * 将ips转化成InetAddress列表
     *
     * @param hostname 域名
     * @param ips      ip
     * @return
     * @throws UnknownHostException
     */
    public static List<InetAddress> buildAddressByIps(String hostname, List<String> ips) throws UnknownHostException {
        if (ips == null || ips.isEmpty() || hostname == null) {
            return new ArrayList<>();
        }

        List<InetAddress> addrs = new ArrayList<>(ips.size());
        // 创建一个新的ArrayList来防止并发修改异常
        List<String> temp = new ArrayList<>(ips);

        for (String ip : temp) {
            if (!TextUtils.isEmpty(ip)) {
                InetAddress address = InetAddress.getByName(ip);//这种写法，会有指定hostname
                byte[] bytes = address.getAddress();
                addrs.add(InetAddress.getByAddress(hostname, bytes));
            }
        }
        return addrs;
    }
}