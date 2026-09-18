package com.netease.android.extension.servicekeeper.service.ipc.server.lock;

import com.netease.android.extension.ext.ObjectExt;
import com.netease.android.extension.servicekeeper.service.ipc.tx.SKCSerial;

public class LockItem<T> {
    private T key;
    private SKCSerial skcSerial;
    private String serviceUniqueId;
    private int ttl;
    private long lastUpdateTime;
    private boolean isForceLocked = false;

    public LockItem() {
    }

    public LockItem(T key) {
        this.key = key;
    }

    public synchronized boolean lock(SKCSerial skcSerial, String serviceUniqueId, int ttl) {
        if (null == skcSerial) {
            return false;
        } else {
            return ttl == -1 ? this.forceLock(skcSerial, serviceUniqueId, ttl) : this.ttlLock(skcSerial, serviceUniqueId, ttl);
        }
    }

    private boolean forceLock(SKCSerial skcSerial, String serviceUniqueId, int ttl) {
        if (this.isForceLocked) {
            return this.isOwner(skcSerial, serviceUniqueId);
        } else {
            this.isForceLocked = true;
            this.skcSerial = skcSerial;
            this.serviceUniqueId = serviceUniqueId;
            this.ttl = ttl;
            this.lastUpdateTime = System.currentTimeMillis();
            return true;
        }
    }

    private boolean ttlLock(SKCSerial skcSerial, String serviceUniqueId, int ttl) {
        if (!this.isExpired()) {
            return this.isOwner(skcSerial, serviceUniqueId);
        } else {
            this.skcSerial = skcSerial;
            this.serviceUniqueId = serviceUniqueId;
            this.ttl = ttl;
            this.lastUpdateTime = System.currentTimeMillis();
            return true;
        }
    }

    public synchronized boolean unlock(SKCSerial skcSerial, String serviceUniqueId) {
        if (!this.isOwner(skcSerial, serviceUniqueId)) {
            throw new SecurityException("[LockItem]unlock fail, the lock owner[" + this.skcSerial + ", " + this.serviceUniqueId + "], actual[" + skcSerial + ", " + serviceUniqueId + "]");
        } else {
            this.skcSerial = null;
            this.ttl = 0;
            this.lastUpdateTime = 0L;
            this.isForceLocked = false;
            return true;
        }
    }

    public LockItem<T> reset(T key) {
        this.key = key;
        this.skcSerial = null;
        this.serviceUniqueId = null;
        this.ttl = 0;
        this.lastUpdateTime = 0L;
        this.isForceLocked = false;
        return this;
    }

    private boolean isExpired() {
        return System.currentTimeMillis() >= this.lastUpdateTime + (long)this.ttl;
    }

    public T getKey() {
        return this.key;
    }

    private boolean isOwner(SKCSerial skcSerial, String serviceUniqueId) {
        return ObjectExt.isEquals(this.skcSerial, skcSerial) && ObjectExt.isEquals(this.serviceUniqueId, serviceUniqueId);
    }
}