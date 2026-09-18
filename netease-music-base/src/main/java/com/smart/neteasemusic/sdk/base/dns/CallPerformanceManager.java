package com.smart.neteasemusic.sdk.base.dns;

import androidx.annotation.Keep;

import java.net.InetAddress;
import java.util.List;

/**
 * 请求性能监控器工厂, 根据请求类型，生成对应的call monitor
 * 这个可以认为是云音乐专用的CallPerformanceManager， 其他app要继承AbsCallPerformanceManager重新实现
 */
@Keep
public class CallPerformanceManager {
    private static final CallPerformanceManager INSTANCE = new CallPerformanceManager();
    /**
     * 云音乐域名
     */
    String[] urls = new String[]{"music.163.com", "music.126.net"};
    /**
     * 其他竞品的域名
     */
    String[] pkUrls = new String[]{"https://music.163.com/", "https://y.qq.com/",
            "https://www.kugou.com/", "https://www.iqiyi.com/", "https://www.mi.com/"};

    public static CallPerformanceManager getInstance() {
        return INSTANCE;
    }

    public CallPerformanceRecorder callPerformanceRecorder = new CallPerformanceRecorder();

    public List<InetAddress> sort(String hostname, List<InetAddress> addrs, String prefer) {
        if (isCloudMusicUrl(hostname)) {
            callPerformanceRecorder.sort(hostname, addrs,prefer);
        }
        return addrs;
    }

    private boolean isCloudMusicUrl(String hostname) {
        boolean result = false;
        for (String url : urls) {
            if (hostname.contains(url)) {
                result = true;
                break;
            }
        }
        return result;
    }
}