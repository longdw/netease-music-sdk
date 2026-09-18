package com.netease.httpdns.request;

import android.text.TextUtils;
import com.netease.httpdns.HttpDnsService;
import com.netease.httpdns.cache.DomainCacheManager;
import com.netease.httpdns.cache.IpStackTypeListener;
import com.netease.httpdns.cache.ServerCacheManager;
import com.netease.httpdns.configuration.ThreadPool;
import com.netease.httpdns.downgrade.DowngradeManager;
import com.netease.httpdns.ipc.ResultNotifyService;
import com.netease.httpdns.listener.RequestStatusListener;
import com.netease.httpdns.module.DomainInfo;
import com.netease.httpdns.module.IPCDomainRequest;
import com.netease.httpdns.module.IpEnvironment;
import com.netease.httpdns.module.NAHttpEntity;
import com.netease.httpdns.provider.dal.manager.DNSServerManager;
import com.netease.httpdns.provider.dal.model.DNSServer;
import com.netease.httpdns.request.handler.DomainRequestHandler;
import com.netease.httpdns.request.handler.IRequestHandler;
import com.netease.httpdns.request.handler.IpDualStackRequestHandler;
import com.netease.httpdns.request.handler.Ipv4RequestHandler;
import com.netease.httpdns.request.handler.Ipv6RequestHandler;
import com.netease.httpdns.score.socketScore.RttScoreManager;
import com.netease.httpdns.util.CollectionUtil;
import com.netease.httpdns.util.DeviceUtil;
import com.netease.httpdns.util.NetworkUtil;
import com.netease.httpdns.util.S;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HttpDnsRequestManager {
    private static final String TAG = "[" + HttpDnsRequestManager.class.getSimpleName() + "]";
    private static final int LIST_INIT_COUNT = 8;
    private static volatile HttpDnsRequestManager instance;
    private boolean isServerRequestSuccess = false;
    private String currentNetSessionId = "";
    private String serverDiscoverySessionId = "";
    private IpStackTypeListener ipStackTypeListener;
    private IpEnvironment currentIpStackType;
    private IpEnvironment useIpType;
    private boolean isUseHttpRequest;

    private HttpDnsRequestManager() {
        this.currentIpStackType = IpEnvironment.NETWORK_UNKNOWN;
        this.useIpType = IpEnvironment.NETWORK_UNKNOWN;
        this.isUseHttpRequest = true;
    }

    public static HttpDnsRequestManager getInstance() {
        if (instance == null) {
            synchronized(HttpDnsRequestManager.class) {
                if (instance == null) {
                    instance = new HttpDnsRequestManager();
                }
            }
        }

        return instance;
    }

    public void detectNetworkEnv() {
        if (DeviceUtil.isUIProcess()) {
            DNSServerManager.getInstance().clearTable();
            this.detectNetworkEnvInternal(true, IpEnvironment.DOMAIN);
        } else {
            ServerCacheManager.getInstance().updateServerCacheFromDataBase();
        }

    }

    public void onNetChange() {
        this.setUseHttpRequest(true);
        ThreadPool.submit(new Runnable() {
            public void run() {
                if (DeviceUtil.isUIProcess()) {
                    HttpDnsRequestManager.this.detectNetworkEnvInternal(true, IpEnvironment.DOMAIN);
                }

            }
        });
    }

    private void detectNetworkEnvInternal(final boolean isForceUseIpType, final IpEnvironment forceUseIpType) {
        this.currentNetSessionId = UUID.randomUUID().toString();
        if (S.LOG.showLog()) {
            S.LOG.i(TAG + "发起网络环境探测.");
        }

        final String tempSessionId = this.currentNetSessionId;
        IpDetectTask ipDetectTask = new IpDetectTask(new IpDetectTask.IpDetectListener() {
            public void OnIpDetect(IpEnvironment ipStackType) {
                if (!TextUtils.equals(tempSessionId, HttpDnsRequestManager.this.currentNetSessionId)) {
                    if (S.LOG.showLog()) {
                        S.LOG.i(HttpDnsRequestManager.TAG + "currentSessionId 发生变化，忽略当前网络探测结果.");
                    }

                } else {
                    if (S.LOG.showLog()) {
                        S.LOG.i(HttpDnsRequestManager.TAG + "网络环境探测結束，真实网络环境：" + ipStackType.getDesc());
                    }

                    HttpDnsRequestManager.this.currentIpStackType = ipStackType;
                    if (HttpDnsRequestManager.this.ipStackTypeListener != null) {
                        HttpDnsRequestManager.this.ipStackTypeListener.onIpStackTypeChange(ipStackType);
                    }

                    if (isForceUseIpType) {
                        HttpDnsRequestManager.this.useIpType = forceUseIpType;
                    } else {
                        HttpDnsRequestManager.this.useIpType = ipStackType;
                    }

                    if (S.LOG.showLog()) {
                        S.LOG.i(HttpDnsRequestManager.TAG + "网络环境探测結束，实际使用：" + HttpDnsRequestManager.this.useIpType.getDesc());
                    }

                    HttpDnsRequestManager.this.serverRequest(HttpDnsService.getInstance().getRequestStatusListener(), HttpDnsRequestManager.this.currentNetSessionId);
                }
            }
        });
        ipDetectTask.startDetect();
    }

    private void serverRequest(RequestStatusListener listener, String sessionId) {
        this.isServerRequestSuccess = false;
        if (!this.ifCanRequestServer()) {
            if (S.LOG.showLog()) {
                S.LOG.i(TAG + "[serverRequest] 当前网络环境不能请求 server: " + this.currentIpStackType);
            }

        } else {
            IpEnvironment tempIpStackType = this.useIpType;
            IRequestHandler requestHandler = this.getRequestHandler(tempIpStackType);
            if (requestHandler == null) {
                if (S.LOG.showLog()) {
                    S.LOG.i(TAG + "[serverRequest] getRequestHandler is null.");
                }

            } else {
                this.serverDiscoverySessionId = UUID.randomUUID().toString();
                ServerRequestTask serverRequestTask = new ServerRequestTask();
                serverRequestTask.setNetworkEnvironment(this.currentIpStackType);
                serverRequestTask.setUseIpType(tempIpStackType);
                serverRequestTask.setListener(listener);
                serverRequestTask.setNetworkSessionId(sessionId);
                DNSServer dnsServer = requestHandler.requestServerDiscovery(serverRequestTask);
                if (dnsServer == null) {
                    if (S.LOG.showLog()) {
                        S.LOG.i(TAG + "[serverRequest] /s 请求失败.");
                    }

                    this.isServerRequestSuccess = false;
                } else {
                    DNSServerManager.getInstance().createOrUpdate(dnsServer);
                    ResultNotifyService.getInstance().notifyServerResult(dnsServer);
                    DowngradeManager.getInstance().resetDowngradeHandler(tempIpStackType);
                    HttpDnsService.getInstance().refreshPreDomain();
                }
            }
        }
    }

    public void onServerRequestSuccess() {
        this.isServerRequestSuccess = true;
    }

    public List<DomainInfo> handlerMultiHttpDNS(List<String> domains) {
        if (CollectionUtil.isEmpty(domains)) {
            return Collections.emptyList();
        } else if (!DeviceUtil.isUIProcess()) {
            ResultNotifyService.getInstance().sendDomainRequest(new IPCDomainRequest(domains));
            return Collections.emptyList();
        } else if (DowngradeManager.getInstance().isDomainRequestFreeze()) {
            if (S.LOG.showLog()) {
                S.LOG.i("[handlerMultiHttpDNS] 当前处于冻结状态。");
            }

            return Collections.emptyList();
        } else {
            DomainRequestTask domainRequestTask = new DomainRequestTask();
            domainRequestTask.setDomains(domains);
            boolean isUseHttp = this.isUseHttpRequest && HttpDnsService.getInstance().getOptions().isUseHttp();
            domainRequestTask.setHttp(isUseHttp);
            String networkType = NetworkUtil.getNetworkType();
            NAHttpEntity httpResponse = getInstance().getDomainResult(domainRequestTask);
            if (httpResponse != null && httpResponse.isSuccess()) {
                String response = httpResponse.getResponse();
                if (S.LOG.showLog()) {
                    S.LOG.i(TAG + "handlerMultiHttpDNS /d response: " + response);
                }

                List<DomainInfo> domainInfoList = DomainInfo.parseDomainInfoList(response, networkType);
                Map<String, List<String>> hostInfoList = new HashMap(8);
                if (domainInfoList != null && !domainInfoList.isEmpty()) {
                    for(DomainInfo domainInfo : domainInfoList) {
                        if (domainInfo != null) {
                            if (domainInfo.isScore()) {
                                domainInfo.setPending(true);
                                final DomainInfo rttScoreInfo = new DomainInfo(domainInfo);
                                ThreadPool.submit(new Runnable() {
                                    public void run() {
                                        DomainCacheManager.getInstance().setDomainCache(rttScoreInfo);
                                        RttScoreManager.getInstance().sort(rttScoreInfo);
                                    }
                                });
                            } else {
                                domainInfo.saveIps(getInstance().getCurrentIpStackType());
                            }

                            ResultNotifyService.getInstance().notifyDomainResult(domainInfo);
                            hostInfoList.put(domainInfo.getHost(), domainInfo.getAvailableIps());
                        }
                    }

                    RequestStatusListener listener = HttpDnsService.getInstance().getRequestStatusListener();
                    if (listener != null) {
                        listener.requestSuccess(httpResponse.getUrl(), domains.toString(), httpResponse.getResponseCode(), response, hostInfoList);
                    }

                    return domainInfoList;
                } else {
                    return Collections.emptyList();
                }
            } else {
                if (S.LOG.showLog()) {
                    S.LOG.i(TAG + "getDomainInfoList, httpResponse is not success !");
                }

                return Collections.emptyList();
            }
        }
    }

    public NAHttpEntity getDomainResult(DomainRequestTask domainRequestTask) {
        if (domainRequestTask == null) {
            if (S.LOG.showLog()) {
                S.LOG.i(TAG + "getDomainResult, domainRequestTask is null !");
            }

            return null;
        } else {
            IRequestHandler requestHandler = this.getRequestHandler(this.useIpType);
            if (requestHandler == null) {
                if (S.LOG.showLog()) {
                    S.LOG.i(TAG + "getDomainResult, requestHandler is null !");
                }

                return null;
            } else {
                try {
                    return requestHandler.requestDomainResolve(domainRequestTask);
                } catch (Exception e) {
                    if (S.LOG.showLog()) {
                        S.LOG.e(TAG + "getDomainResult error: " + e.getMessage());
                    }

                    return null;
                }
            }
        }
    }

    private IRequestHandler getRequestHandler(IpEnvironment ipEnvironment) {
        IRequestHandler requestHandler = null;
        switch (ipEnvironment) {
            case IPV4:
                requestHandler = new Ipv4RequestHandler();
                break;
            case IPV6:
                requestHandler = new Ipv6RequestHandler();
                break;
            case IP_DUAL_STACK:
                requestHandler = new IpDualStackRequestHandler();
                break;
            case DOMAIN:
                requestHandler = new DomainRequestHandler();
        }

        return requestHandler;
    }

    private boolean ifCanRequestServer() {
        return this.currentIpStackType != IpEnvironment.FAILED && this.currentIpStackType != IpEnvironment.NETWORK_UNKNOWN;
    }

    public boolean ifCanRequestDomain() {
        if (this.currentIpStackType != IpEnvironment.FAILED && this.currentIpStackType != IpEnvironment.NETWORK_UNKNOWN) {
            if (this.useIpType == IpEnvironment.DOMAIN) {
                if (S.LOG.showLog()) {
                    S.LOG.i(TAG + "ifCanRequestDomain, 当前 use ip type 为 domain，可以发起请求.");
                }

                return true;
            } else {
                if (S.LOG.showLog()) {
                    S.LOG.i(TAG + "ifCanRequestDomain, 当前 /s 状态: " + this.isServerRequestSuccess);
                }

                return this.isServerRequestSuccess;
            }
        } else {
            if (S.LOG.showLog()) {
                S.LOG.i(TAG + "ifCanRequestDomain, 当前 currentIpStackType 不允许请求: " + this.useIpType);
            }

            return false;
        }
    }

    public void setIpStackTypeListener(IpStackTypeListener listener) {
        this.ipStackTypeListener = listener;
    }

    public void removeIpStackTypeListener() {
        this.ipStackTypeListener = null;
    }

    public void onDnsError() {
        this.changeToIpRequestMode();
    }

    public void onHttpHijacking(boolean isHttp) {
        if (isHttp) {
            if (S.LOG.showLog()) {
                S.LOG.i(TAG + "onHttpHijacking, 升级到 https.");
            }

            this.setUseHttpRequest(false);
        } else {
            this.changeToIpRequestMode();
        }

    }

    private void changeToIpRequestMode() {
        if (S.LOG.showLog()) {
            S.LOG.i(TAG + "changeToIpRequestMode, 切换到 ip 请求模式.");
        }

        this.useIpType = this.currentIpStackType;
        DowngradeManager.getInstance().resetDowngradeStrategy();
        this.serverRequest(HttpDnsService.getInstance().getRequestStatusListener(), this.currentNetSessionId);
    }

    public boolean isUseHttpRequest() {
        return this.isUseHttpRequest;
    }

    public void setUseHttpRequest(boolean useHttpRequest) {
        this.isUseHttpRequest = useHttpRequest;
    }

    public IpEnvironment getCurrentIpStackType() {
        return this.currentIpStackType;
    }

    public void setUseIpType(IpEnvironment useIpType) {
        this.useIpType = useIpType;
    }

    public String getServerDiscoverySessionId() {
        return this.serverDiscoverySessionId;
    }

    public void setCurrentIpStackType(IpEnvironment currentIpStackType) {
        this.currentIpStackType = currentIpStackType;
    }
}
