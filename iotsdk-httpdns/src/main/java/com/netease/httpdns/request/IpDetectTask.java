package com.netease.httpdns.request;

import com.netease.httpdns.HttpDnsService;
import com.netease.httpdns.cache.ServerCacheManager;
import com.netease.httpdns.configuration.DnsOptions;
import com.netease.httpdns.configuration.ThreadPool;
import com.netease.httpdns.module.IpEnvironment;
import com.netease.httpdns.score.speedtest.impl.Socket80SpeedTest;
import java.util.concurrent.CountDownLatch;

public class IpDetectTask {
    private IpDetectListener listener;
    private boolean isIpv4Connect = false;
    private boolean isIpv6Connect = false;

    public IpDetectTask(IpDetectListener listener) {
        this.listener = listener;
    }

    public void startDetect() {
        DnsOptions options = HttpDnsService.getInstance().getOptions();
        if (!options.isOpenIpv6Request()) {
            this.notifyIpDetectFinish(IpEnvironment.IPV4);
        } else {
            final String ipv4Url = ServerCacheManager.getInstance().getSurveyUrl(IpEnvironment.IPV4);
            final String ipv6Url = ServerCacheManager.getInstance().getSurveyUrl(IpEnvironment.IPV6);
            final CountDownLatch countDownLatch = new CountDownLatch(2);
            ThreadPool.submit(new Runnable() {
                public void run() {
                    Socket80SpeedTest ipv4SpeedTest = new Socket80SpeedTest();
                    int rtt = ipv4SpeedTest.speedTest(ipv4Url);
                    if (rtt != -1 && rtt <= ipv4SpeedTest.getTimeout()) {
                        IpDetectTask.this.isIpv4Connect = true;
                    }

                    countDownLatch.countDown();
                }
            });
            ThreadPool.submit(new Runnable() {
                public void run() {
                    Socket80SpeedTest ipv6SpeedTest = new Socket80SpeedTest();
                    int rtt = ipv6SpeedTest.speedTest(ipv6Url);
                    if (rtt != -1 && rtt <= ipv6SpeedTest.getTimeout()) {
                        IpDetectTask.this.isIpv6Connect = true;
                    }

                    countDownLatch.countDown();
                }
            });

            try {
                countDownLatch.await();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (this.isIpv6Connect && this.isIpv4Connect) {
                this.notifyIpDetectFinish(IpEnvironment.IP_DUAL_STACK);
            } else if (this.isIpv6Connect) {
                this.notifyIpDetectFinish(IpEnvironment.IPV6);
            } else if (this.isIpv4Connect) {
                this.notifyIpDetectFinish(IpEnvironment.IPV4);
            } else {
                this.notifyIpDetectFinish(IpEnvironment.FAILED);
            }
        }
    }

    private void notifyIpDetectFinish(IpEnvironment ipStackType) {
        if (this.listener != null) {
            this.listener.OnIpDetect(ipStackType);
        }

    }

    interface IpDetectListener {
        void OnIpDetect(IpEnvironment var1);
    }
}