package com.netease.httpdns.downgrade;

import com.netease.httpdns.module.IpEnvironment;
import com.netease.httpdns.util.CollectionUtil;
import com.netease.httpdns.util.S;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DowngradeManager {
    private static final int MAX_ERROR_TIME = 2;
    private static final long ERROR_GAP_TIME = 1000L;
    private static final long FREEZE_TIME = 20000L;
    private static final long DEFAULT_TIME = -1L;
    private static volatile DowngradeManager instance;
    private final List<Long> mErrorList = new LinkedList();
    private List<IDowngradeHandler> downgradeHandlerList = new ArrayList();
    private int currentHandlerIndex = 0;
    private long downgradeStatusChangeTime = -1L;

    private DowngradeManager() {
    }

    public static DowngradeManager getInstance() {
        if (instance == null) {
            synchronized(DowngradeManager.class) {
                if (instance == null) {
                    instance = new DowngradeManager();
                }
            }
        }

        return instance;
    }

    public synchronized void resetDowngradeHandler(IpEnvironment useIpStack) {
        if (this.downgradeHandlerList == null) {
            if (S.LOG.showLog()) {
                S.LOG.i("[resetDowngradeHandler] downgradeHandlerList is null.");
            }

        } else {
            if (S.LOG.showLog()) {
                S.LOG.i("[resetDowngradeHandler] current useIpStack is :" + useIpStack);
            }

            this.downgradeHandlerList.clear();
            switch (useIpStack) {
                case DOMAIN:
                    this.downgradeHandlerList.add(DomainDowngradeHandler.handlerLazy.get());
                    break;
                case IP_DUAL_STACK:
                    this.downgradeHandlerList.add(IpDualStackDowngradeHandler.handlerLazy.get());
                    this.downgradeHandlerList.add(Ipv4DowngradeHandler.handlerLazy.get());
                    break;
                case IPV6:
                    this.downgradeHandlerList.add(Ipv6DowngradeHandler.handlerLazy.get());
                    this.downgradeHandlerList.add(Ipv4DowngradeHandler.handlerLazy.get());
                    break;
                case IPV4:
                    this.downgradeHandlerList.add(Ipv4DowngradeHandler.handlerLazy.get());
            }

            this.downgradeHandlerList.add(FreezeDowngradeHandler.handlerLazy.get());
        }
    }

    public synchronized boolean isDomainRequestFreeze() {
        IpEnvironment ipEnvironment = this.checkDowngradeStatus();
        if (S.LOG.showLog()) {
            S.LOG.i("[isDomainRequestFreeze]当前使用 IpEnvironment： " + ipEnvironment.getDesc());
        }

        return ipEnvironment == IpEnvironment.FAILED;
    }

    public synchronized IpEnvironment checkDowngradeStatus() {
        IDowngradeHandler currentHandler = this.getCurrentHandler();
        if (currentHandler == null) {
            return IpEnvironment.FAILED;
        } else {
            IpEnvironment useIpType = currentHandler.getUseIpType();
            if (this.downgradeStatusChangeTime == -1L) {
                if (S.LOG.showLog()) {
                    S.LOG.i("[DowngradeManager]/d 域名解析请求没有降级过，不需要升级.");
                }

                return useIpType;
            } else {
                long nowTime = System.currentTimeMillis();
                if (nowTime - this.downgradeStatusChangeTime < 20000L) {
                    if (S.LOG.showLog()) {
                        S.LOG.i("[DowngradeManager]距离上次降级状态变更还没有达到20秒，不需要升级.");
                    }

                    return useIpType;
                } else {
                    IDowngradeHandler handlerAfterUpgrade = this.tryUpgrade();
                    return handlerAfterUpgrade == null ? IpEnvironment.FAILED : handlerAfterUpgrade.getUseIpType();
                }
            }
        }
    }

    public synchronized void onDomainRequestFailed(long errorTime) {
        if (this.mErrorList != null) {
            if (this.mErrorList.size() < 1) {
                this.mErrorList.add(errorTime);
            } else {
                long firstTime = (Long)this.mErrorList.get(0);
                if (errorTime - firstTime >= 1000L) {
                    this.mErrorList.remove(0);
                    this.mErrorList.add(errorTime);
                    return;
                }

                this.tryDowngrade();
                this.mErrorList.clear();
            }

        }
    }

    private void setDowngradeStatusChangeTime(long downgradeStatusChangeTime) {
        this.downgradeStatusChangeTime = downgradeStatusChangeTime;
    }

    public synchronized void resetDowngradeStrategy() {
        this.setDowngradeStatusChangeTime(-1L);
        this.downgradeHandlerList.clear();
        this.mErrorList.clear();
    }

    private synchronized void tryDowngrade() {
        if (!CollectionUtil.isEmpty(this.downgradeHandlerList)) {
            int handlerSize = this.downgradeHandlerList.size();
            if (this.currentHandlerIndex < handlerSize - 1) {
                ++this.currentHandlerIndex;
                IDowngradeHandler downgradeHandler = (IDowngradeHandler)this.downgradeHandlerList.get(this.currentHandlerIndex);
                if (downgradeHandler != null) {
                    if (S.LOG.showLog()) {
                        S.LOG.i("[tryDowngrade]降级到了：" + downgradeHandler);
                    }

                    this.setDowngradeStatusChangeTime(System.currentTimeMillis());
                    downgradeHandler.onDowngrade();
                }
            }

        }
    }

    private synchronized IDowngradeHandler tryUpgrade() {
        if (CollectionUtil.isEmpty(this.downgradeHandlerList)) {
            return null;
        } else {
            int handlerSize = this.downgradeHandlerList.size();
            if (this.currentHandlerIndex >= handlerSize - 1) {
                this.currentHandlerIndex = 0;
                IDowngradeHandler downgradeHandler = this.getCurrentHandler();
                if (downgradeHandler != null) {
                    if (S.LOG.showLog()) {
                        S.LOG.i("[tryUpgrade]升级到了：" + downgradeHandler);
                    }

                    this.setDowngradeStatusChangeTime(System.currentTimeMillis());
                    downgradeHandler.onDowngrade();
                }

                return downgradeHandler;
            } else {
                if (S.LOG.showLog()) {
                    S.LOG.i("[tryUpgrade]不需要升级，当前 index：" + this.currentHandlerIndex);
                }

                return this.getCurrentHandler();
            }
        }
    }

    private synchronized IDowngradeHandler getCurrentHandler() {
        try {
            return (IDowngradeHandler)this.downgradeHandlerList.get(this.currentHandlerIndex);
        } catch (Exception e) {
            if (S.LOG.showLog()) {
                S.LOG.i("[getCurrentHandler] error: " + e.getMessage());
            }

            return null;
        }
    }
}