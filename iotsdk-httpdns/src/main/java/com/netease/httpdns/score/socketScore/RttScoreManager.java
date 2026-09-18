package com.netease.httpdns.score.socketScore;

import android.text.TextUtils;
import com.netease.httpdns.cache.DomainCacheManager;
import com.netease.httpdns.ipc.ResultNotifyService;
import com.netease.httpdns.module.DomainInfo;
import com.netease.httpdns.module.IpEnvironment;
import com.netease.httpdns.request.HttpDnsRequestManager;
import com.netease.httpdns.util.CollectionUtil;
import com.netease.httpdns.util.S;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RttScoreManager {
    private static final String TAG = RttScoreManager.class.getSimpleName();
    private static final int MAX_SOCKET_NUM = 10;
    private static final int DELAY_ZERO = 0;
    private static final byte[] lock = new byte[1];
    private static volatile RttScoreManager instance;
    private ExecutorService socketThreadPool = Executors.newFixedThreadPool(10);
    private DetectIpsListener detectIpsListener;

    private RttScoreManager() {
    }

    public static RttScoreManager getInstance() {
        if (instance == null) {
            synchronized(RttScoreManager.class) {
                if (instance == null) {
                    instance = new RttScoreManager();
                }
            }
        }

        return instance;
    }

    public void sort(DomainInfo domainInfo) {
        if (domainInfo != null) {
            String domain = domainInfo.getHost();
            List<String> ipv4s = domainInfo.getIps();
            List<String> ipv6s = domainInfo.getIpv6s();
            int ipv4Count = ipv4s == null ? 0 : ipv4s.size();
            int ipv6Count = ipv6s == null ? 0 : ipv6s.size();
            List<IpDetectModel> rttModelTaskList = new ArrayList();
            if (S.LOG.showLog()) {
                S.LOG.i("[" + TAG + "]SDK 内部跑马开始, domain  " + domain + "; ipv4s: " + ipv4s + "; ipv6s: " + ipv6s);
            }

            IpEnvironment currentNetwork = HttpDnsRequestManager.getInstance().getCurrentIpStackType();
            switch (currentNetwork) {
                case IPV4:
                    domainInfo.setScoreCount(ipv4Count);
                    this.addIpToDetectList(rttModelTaskList, domain, ipv4s, 0);
                    break;
                case IPV6:
                    domainInfo.setScoreCount(ipv6Count);
                    this.addIpToDetectList(rttModelTaskList, domain, ipv6s, 0);
                    break;
                case IP_DUAL_STACK:
                    String prefer = domainInfo.getScorePrefer();
                    int delay = domainInfo.getIpv4ScoreDelay();
                    domainInfo.setScoreCount(ipv4Count + ipv6Count);
                    this.addIpToDetectList(rttModelTaskList, domain, ipv6s, 0);
                    if (TextUtils.equals(prefer, "ipv6") && ipv6Count > 0) {
                        this.addIpToDetectList(rttModelTaskList, domain, ipv4s, delay);
                    } else {
                        this.addIpToDetectList(rttModelTaskList, domain, ipv4s, 0);
                    }
            }

            this.executeRttScoreTask(domain, rttModelTaskList);
        }
    }

    private void addIpToDetectList(List<IpDetectModel> rttModelTaskList, String domain, List<String> ipList, int delay) {
        if (rttModelTaskList != null && ipList != null) {
            for(String ip : ipList) {
                IpDetectModel ipDetectModel = new IpDetectModel(domain, ip, delay);
                rttModelTaskList.add(ipDetectModel);
            }

        }
    }

    private void executeRttScoreTask(final String domain, List<IpDetectModel> rttModelTaskList) {
        if (CollectionUtil.isEmpty(rttModelTaskList)) {
            if (S.LOG.showLog()) {
                S.LOG.i("[" + TAG + "]待探测 ip 数组为空");
            }

        } else {
            synchronized(lock) {
                RttScoreTask serialRttScoreTask = new RttScoreTask(rttModelTaskList, this.socketThreadPool, new RttScoreTask.RttScoreListener() {
                    public void onRttScoreSuccess(IpDetectModel ipDetectModel, List<IpDetectModel> ipDetectModelValidList) {
                        String domain = ipDetectModel.getDomain();
                        DomainInfo domainInfo = DomainCacheManager.getInstance().getDomainCache(domain);
                        if (domainInfo != null) {
                            domainInfo.setAvailableIps(RttScoreManager.this.getIPs(ipDetectModelValidList));
                            DomainCacheManager.getInstance().addDomainCache(domain, domainInfo);
                        }

                    }

                    public void onRttScoreFail(IpDetectModel ipDetectModel) {
                    }

                    public void onRttScoreFinish(List<IpDetectModel> ipDetectModelList) {
                        if (S.LOG.showLog()) {
                            S.LOG.i("[" + RttScoreManager.TAG + "]SDK 内部跑马结束, domain  " + domain + "; result : " + ipDetectModelList);
                        }

                        DomainInfo domainInfo = DomainCacheManager.getInstance().getDomainCache(domain);
                        if (domainInfo != null) {
                            domainInfo.setPending(false);
                            DomainCacheManager.getInstance().addDomainCache(domain, domainInfo);
                            ResultNotifyService.getInstance().notifyDomainResult(domainInfo);
                        }

                        if (RttScoreManager.this.detectIpsListener != null) {
                            RttScoreManager.this.detectIpsListener.onDetectIpsFinish(domain, ipDetectModelList);
                        }

                    }
                });
                serialRttScoreTask.execute();
            }
        }
    }

    public void finishCurrentScore(String currentNetwork) {
        synchronized(lock) {
            DomainCacheManager.getInstance().finishCurrentDomainScore(currentNetwork);
            if (this.socketThreadPool != null) {
                this.socketThreadPool.shutdown();
                this.socketThreadPool = Executors.newFixedThreadPool(10);
            }

        }
    }

    private List<String> getIPs(List<IpDetectModel> ipModels) {
        if (ipModels != null && !ipModels.isEmpty()) {
            List<String> ips = new ArrayList();

            for(int i = 0; i < ipModels.size(); ++i) {
                IpDetectModel model = (IpDetectModel)ipModels.get(i);
                if (model != null) {
                    ips.add(model.getIp());
                }
            }

            return ips;
        } else {
            return Collections.emptyList();
        }
    }

    public void detectIps(List<String> ips, final DetectIpsListener listener) {
        if (CollectionUtil.isEmpty(ips)) {
            if (listener != null) {
                listener.onDetectIpsFinish("", new ArrayList());
            }

        } else {
            if (S.LOG.showLog()) {
                S.LOG.i("[" + TAG + "]业务方主动跑马开始, ips: " + ips);
            }

            List<IpDetectModel> rttModelTaskList = new ArrayList();

            for(String ip : ips) {
                IpDetectModel ipModel = new IpDetectModel();
                ipModel.setIp(ip);
                rttModelTaskList.add(ipModel);
            }

            synchronized(lock) {
                RttScoreTask rttScoreTask = new RttScoreTask(rttModelTaskList, this.socketThreadPool, new RttScoreTask.RttScoreListener() {
                    public void onRttScoreSuccess(IpDetectModel ipDetectModel, List<IpDetectModel> ipDetectModelValidList) {
                    }

                    public void onRttScoreFail(IpDetectModel ipDetectModel) {
                    }

                    public void onRttScoreFinish(List<IpDetectModel> ipDetectModelList) {
                        if (S.LOG.showLog()) {
                            S.LOG.i("[" + RttScoreManager.TAG + "]业务方主动跑马结束, result: " + ipDetectModelList);
                        }

                        if (listener != null) {
                            listener.onDetectIpsFinish("", ipDetectModelList);
                        }

                    }
                });
                rttScoreTask.execute();
            }
        }
    }

    public void removeDetectIpsListener() {
        this.detectIpsListener = null;
    }

    public void setDetectIpsListener(DetectIpsListener listener) {
        this.detectIpsListener = listener;
    }
}