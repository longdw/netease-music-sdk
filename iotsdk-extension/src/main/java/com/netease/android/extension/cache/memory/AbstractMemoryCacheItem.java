package com.netease.android.extension.cache.memory;

import com.netease.android.extension.func.NFunc0R;

public abstract class AbstractMemoryCacheItem<T> implements MemoryCacheItem<T> {
    protected T target;
    private NFunc0R<T> calcTargetFunc;
    private boolean isSync;

    public AbstractMemoryCacheItem(NFunc0R<T> calcTargetFunc) {
        this(true, calcTargetFunc);
    }

    public AbstractMemoryCacheItem(boolean isSync, NFunc0R<T> calcTargetFunc) {
        this.isSync = isSync;
        this.calcTargetFunc = calcTargetFunc;
        if (null == calcTargetFunc) {
            throw new IllegalArgumentException("calcTargetFunc is null!");
        }
    }

    public final T get() {
        return (T)(this.isSync ? this.getInternalSync() : this.getInternal());
    }

    public final T get(T defaultValue) {
        T t = (T)this.get();
        return (T)(null == t ? defaultValue : t);
    }

    private T getInternalSync() {
        T result = this.target;
        if (null == result || this.isExpired()) {
            synchronized(this) {
                result = this.target;
                if (null == result || this.isExpired()) {
                    this.target = result = (T)this.calcTargetFunc.call();
                    this.onUpdate();
                }
            }
        }

        return result;
    }

    private T getInternal() {
        if (null == this.target || this.isExpired()) {
            this.target = (T)this.calcTargetFunc.call();
            this.onUpdate();
        }

        return this.target;
    }

    public final void update(T t) {
        if (this.isSync) {
            this.updateInternalSync(t);
        } else {
            this.updateInternal(t);
        }

    }

    private void updateInternal(T t) {
        this.target = t;
        this.onUpdate();
    }

    private void updateInternalSync(T t) {
        synchronized(this) {
            this.target = t;
            this.onUpdate();
        }
    }

    protected abstract void onUpdate();

    public void clear() {
        if (this.isSync) {
            this.clearInternalSync();
        } else {
            this.clearInternal();
        }

    }

    private void clearInternal() {
        this.target = null;
        this.onClear();
    }

    private void clearInternalSync() {
        synchronized(this) {
            this.target = null;
            this.onClear();
        }
    }

    protected abstract void onClear();
}