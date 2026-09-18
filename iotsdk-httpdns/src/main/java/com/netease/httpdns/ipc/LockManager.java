package com.netease.httpdns.ipc;

import com.netease.android.extension.func.NFunc0;
import com.netease.android.extension.func.NFuncB;
import com.netease.android.extension.servicekeeper.service.ipc.error.SDKIPCServerNotConnectedException;
import com.netease.android.extension.servicekeeper.service.ipc.lock.IPCLockService;
import com.netease.httpdns.HttpDnsService;
import com.netease.httpdns.util.NetworkUtil;
import com.netease.httpdns.util.S;
import com.netease.httpdns.util.S.Service;

public class LockManager {
    private static final String TAG = LockManager.class.getSimpleName();
    private IPCLockService lockService;

    public void start() throws Exception {
        this.lockService = new IPCLockService(Service.SUID_IPC_LOCK_HTTP_DNS);
        ResultNotifyService.getInstance().getServiceKeeperController().register(this.lockService);
    }

    public void destroy() throws Exception {
        if (null != this.lockService) {
            ResultNotifyService.getInstance().getServiceKeeperController().unregister(this.lockService);
        }

    }

    public void tryServerRequest(int timeoutMillis, NFunc0 nFuncGetLock, NFunc0 nFuncNoGetLock) {
        String currentNetworkTypeServerLockName = HttpDnsService.getInstance().getContext().getPackageName() + ".SERVER_LOCK";
        if (S.LOG.showLog()) {
            S.LOG.i("[LockManager]tryServerRequest lock name :" + currentNetworkTypeServerLockName + ", timeoutMillis:" + timeoutMillis);
        }

        this.tryLockAutoRelease(timeoutMillis, nFuncGetLock, nFuncNoGetLock, currentNetworkTypeServerLockName);
    }

    public void tryDomainRequest(String domain, int timeoutMillis, NFunc0 nFuncGetLock, NFunc0 nFuncNoGetLock) {
        String currentNetworkType = NetworkUtil.getNetworkType();
        String currentNetworkTypeDomainLockName = HttpDnsService.getInstance().getContext().getPackageName() + ".DOMAIN_LOCK." + currentNetworkType + "-" + domain;
        this.tryLockAutoRelease(timeoutMillis, nFuncGetLock, nFuncNoGetLock, currentNetworkTypeDomainLockName);
    }

    public void release(String domain) {
        String currentNetworkType = NetworkUtil.getNetworkType();
        final String currentNetworkTypeDomainLockName = HttpDnsService.getInstance().getContext().getPackageName() + ".DOMAIN_LOCK." + currentNetworkType + "-" + domain;
        if (this.lockService != null) {
            this.lockService.release(currentNetworkTypeDomainLockName, new NFunc0() {
                public void call() {
                    if (S.LOG.showLog()) {
                        S.LOG.w("[LockManager]release, lock : " + currentNetworkTypeDomainLockName);
                    }

                }
            });
        }

    }

    private void tryLockAutoRelease(int timeoutMillis, final NFunc0 nFuncGetLock, final NFunc0 nFuncNoGetLock, String lockTarget) {
        try {
            this.lockService.tryLock(lockTarget, timeoutMillis, new NFuncB() {
                public void call(boolean isGetLock) {
                    if (isGetLock) {
                        if (S.LOG.showLog()) {
                            S.LOG.w("[LockManager]tryLockAutoRelease, get lock");
                        }

                        nFuncGetLock.call();
                    } else {
                        if (S.LOG.showLog()) {
                            S.LOG.w("[LockManager]tryLockAutoRelease, lock missed");
                        }

                        nFuncNoGetLock.call();
                    }

                }
            });
        } catch (SDKIPCServerNotConnectedException e) {
            if (S.LOG.showLog()) {
                S.LOG.i("[LockManager]release error : " + e.getMessage());
            }

            nFuncGetLock.call();
        }

    }
}