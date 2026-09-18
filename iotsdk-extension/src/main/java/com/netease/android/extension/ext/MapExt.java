package com.netease.android.extension.ext;

import android.os.Build.VERSION;
import com.netease.android.extension.func.NFunc1R;
import java.util.Map;
import java.util.function.Function;

public final class MapExt {
    public static <K, V> V computeIfAbsent(Map<K, V> map, K key, final NFunc1R<? super K, ? extends V> mappingFunction) {
        if (VERSION.SDK_INT >= 24) {
            return (V)map.computeIfAbsent(key, new Function<K, V>() {
                public V apply(K k) {
                    return (V)mappingFunction.call(k);
                }
            });
        } else {
            ObjectExt.requireNonNull(mappingFunction);
            V v;
            V newValue;
            if ((v = (V)map.get(key)) == null && (newValue = (V)mappingFunction.call(key)) != null) {
                map.put(key, newValue);
                return newValue;
            } else {
                return v;
            }
        }
    }
}