package com.netease.android.extension.cache.memory;

import com.netease.android.extension.func.NFunc0R;

public class SampleMemoryCacheItem<T> extends AbstractMemoryCacheItem<T> {
    public static final int NEVER_EXPIRED = -1;
    private long expiredMillis = -1L;
    private int intervalMillis = 5000;

    public SampleMemoryCacheItem(NFunc0R<T> calcTargetFunc) {
        super(calcTargetFunc);
        this.init(-1);
    }

    public SampleMemoryCacheItem(int intervalMillis, NFunc0R<T> calcTargetFunc) {
        super(calcTargetFunc);
        this.init(intervalMillis);
    }

    public SampleMemoryCacheItem(int intervalMillis, boolean isSync, NFunc0R<T> calcTargetFunc) {
        super(isSync, calcTargetFunc);
        this.init(intervalMillis);
    }

    private void init(int intervalMillis) {
        this.intervalMillis = intervalMillis;
    }

    protected void onUpdate() {
        if (this.intervalMillis != -1) {
            this.expiredMillis = System.currentTimeMillis() + (long)this.intervalMillis;
        }
    }

    protected void onClear() {
        if (this.intervalMillis != -1) {
            this.expiredMillis = 0L;
        }
    }

    public boolean isExpired() {
        if (this.intervalMillis == -1) {
            return false;
        } else {
            return System.currentTimeMillis() > this.expiredMillis;
        }
    }

    public int getIntervalMillis() {
        return this.intervalMillis;
    }

    public long getExpiredMillis() {
        return this.expiredMillis;
    }
}
