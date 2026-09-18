package com.netease.android.extension.ext;

import androidx.annotation.NonNull;
import com.netease.android.extension.func.NFunc0R;
import com.netease.android.extension.func.NFunc1B;
import com.netease.android.extension.func.NFunc1R;
import java.util.Arrays;
import java.util.List;

public final class ObjectExt {
    public static boolean isEquals(Object a, Object b) {
        return a == b || a != null && a.equals(b);
    }

    public static boolean equals(Object a, Object b) {
        return isEquals(a, b);
    }

    public static int hash(Object... values) {
        return Arrays.hashCode(values);
    }

    public static <T> T requireNonNull(T obj) {
        if (obj == null) {
            throw new NullPointerException();
        } else {
            return obj;
        }
    }

    public static boolean allNonNull(Object... objects) {
        if (objects.length <= 0) {
            return false;
        } else if (objects.length == 1) {
            return null != objects[0];
        } else {
            boolean allNonNull = true;

            for(Object object : objects) {
                if (null == object) {
                    allNonNull = false;
                    break;
                }
            }

            return allNonNull;
        }
    }

    public static <T> T takeFirstObjNotNull(T defaultObject, List<T> objects) {
        return (T)takeFirstObj(defaultObject, new NFunc1R<T, Boolean>() {
            public Boolean call(T item) {
                return null != item;
            }
        }, objects);
    }

    public static <T> T takeFirstObj(T defaultObject, @NonNull NFunc1R<T, Boolean> conditionFunc, List<T> objects) {
        if (CollectionExt.isEmpty(objects)) {
            return defaultObject;
        } else {
            for(T obj : objects) {
                Boolean hint = (Boolean)conditionFunc.call(obj);
                if (null != hint && hint) {
                    return obj;
                }
            }

            return defaultObject;
        }
    }

    public static <T> T takeFirstNotNull(T defaultObject, List<NFunc0R<T>> suppliers) {
        return (T)takeFirst(defaultObject, new NFunc1B<T>() {
            public boolean call(T t) {
                return null != t;
            }
        }, suppliers);
    }

    public static <T> T takeFirst(T defaultObject, NFunc1B<T> conditionFunc, List<NFunc0R<T>> suppliers) {
        if (CollectionExt.isEmpty(suppliers)) {
            return defaultObject;
        } else {
            for(NFunc0R<T> supplier : suppliers) {
                T t = (T)supplier.call();
                if (conditionFunc.call(t)) {
                    return t;
                }
            }

            return defaultObject;
        }
    }
}