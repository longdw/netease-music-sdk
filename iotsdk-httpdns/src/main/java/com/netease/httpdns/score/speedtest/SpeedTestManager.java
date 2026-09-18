package com.netease.httpdns.score.speedtest;

import com.netease.httpdns.configuration.DNSRequestUrl;
import com.netease.httpdns.log.DNSLog;
import com.netease.httpdns.score.speedtest.impl.PingSpeedTest;
import com.netease.httpdns.score.speedtest.impl.Socket80SpeedTest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SpeedTestManager implements ISpeedTest {
    public static final int OCCUR_ERROR = -1;
    public static final int MAX_OVERTIME_RTT = 9999;
    private List<BaseSpeedTest> mSpeedTests = new ArrayList();
    private BaseSpeedTest mSpeedTest;

    public SpeedTestManager() {
        this.mSpeedTests.add(new Socket80SpeedTest());
        this.mSpeedTests.add(new PingSpeedTest());
    }

    public void init() {
        this.sort();
        this.mSpeedTest = (BaseSpeedTest)this.mSpeedTests.get(0);
        String ip = DNSRequestUrl.REQUEST_URL_IPV4[0];

        for(BaseSpeedTest speedTest : this.mSpeedTests) {
            if (speedTest.isActive()) {
                DNSLog.d("测速模块 " + speedTest.getClass().getSimpleName() + " 启动, 优先级： " + speedTest.getPriority() + ", ip 地址 " + ip);
                int rtt = speedTest.speedTest(ip);
                DNSLog.d("测速模块 " + speedTest.getClass().getSimpleName() + " 结束, 测速 RTT = " + rtt);
                if (rtt > -1) {
                    this.mSpeedTest = speedTest;
                    DNSLog.d("SpeedTest = " + this.mSpeedTest.getClass().getSimpleName());
                    break;
                }
            }
        }

    }

    public int speedTest(String ip) {
        return this.mSpeedTest.speedTest(ip);
    }

    private void sort() {
        Collections.sort(this.mSpeedTests, new Comparator<BaseSpeedTest>() {
            public int compare(BaseSpeedTest lhs, BaseSpeedTest rhs) {
                return lhs != null && rhs != null ? rhs.getPriority() - lhs.getPriority() : 0;
            }
        });
    }
}