package com.netease.android.extension.usage;

import com.netease.android.extension.cache.memory.AbstractMemoryCacheItem;
import com.netease.android.extension.func.NFunc0R;

public class NLazy<T> extends AbstractMemoryCacheItem<T> {
    public NLazy(NFunc0R<T> calcTargetFunc) {
        super(calcTargetFunc);
    }

    public NLazy(boolean isSync, NFunc0R<T> calcTargetFunc) {
        super(isSync, calcTargetFunc);
    }

    protected void onUpdate() {
    }

    protected void onClear() {
    }

    public boolean isExpired() {
        return false;
    }

    public boolean isTargetNull() {
        return null == this.target;
    }
}