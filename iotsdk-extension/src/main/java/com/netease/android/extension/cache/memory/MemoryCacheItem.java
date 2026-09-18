package com.netease.android.extension.cache.memory;

public interface MemoryCacheItem<T> {
    T get();

    T get(T var1);

    void update(T var1);

    boolean isExpired();

    void clear();
}