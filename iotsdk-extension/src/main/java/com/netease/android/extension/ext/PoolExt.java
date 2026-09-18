package com.netease.android.extension.ext;

import androidx.annotation.NonNull;
import com.netease.android.extension.func.NFunc0R;

public final class PoolExt {
    private PoolExt() {
    }

    public static class SimplePool<T> implements Pool<T> {
        private final Object[] mPool;
        private int mPoolSize;
        @NonNull
        private NFunc0R<T> createNewFunc;

        public SimplePool(int maxPoolSize, @NonNull NFunc0R<T> createNewFunc) {
            if (maxPoolSize <= 0) {
                throw new IllegalArgumentException("The max pool size must be > 0");
            } else {
                this.mPool = new Object[maxPoolSize];
                this.createNewFunc = createNewFunc;
            }
        }

        @NonNull
        public T acquire() {
            if (this.mPoolSize > 0) {
                int lastPooledIndex = this.mPoolSize - 1;
                T instance = (T)this.mPool[lastPooledIndex];
                this.mPool[lastPooledIndex] = null;
                --this.mPoolSize;
                return instance;
            } else {
                return (T)this.createNewFunc.call();
            }
        }

        public boolean release(@NonNull T instance) {
            if (this.isInPool(instance)) {
                return false;
            } else if (this.mPoolSize < this.mPool.length) {
                this.mPool[this.mPoolSize] = instance;
                ++this.mPoolSize;
                return true;
            } else {
                return false;
            }
        }

        private boolean isInPool(@NonNull T instance) {
            for(int i = 0; i < this.mPoolSize; ++i) {
                if (this.mPool[i] == instance) {
                    return true;
                }
            }

            return false;
        }
    }

    public static class SynchronizedPool<T> extends SimplePool<T> {
        private final Object mLock = new Object();

        public SynchronizedPool(int maxPoolSize, @NonNull NFunc0R<T> createNewFunc) {
            super(maxPoolSize, createNewFunc);
        }

        @NonNull
        public T acquire() {
            synchronized(this.mLock) {
                return (T)super.acquire();
            }
        }

        public boolean release(@NonNull T element) {
            synchronized(this.mLock) {
                return super.release(element);
            }
        }
    }

    public interface Pool<T> {
        @NonNull
        T acquire();

        boolean release(@NonNull T var1);
    }
}