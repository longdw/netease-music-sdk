package com.netease.android.extension.ext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.func.NFunc1R;
import com.netease.android.extension.func.NFunc2;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class CollectionExt {
    public static boolean isEmpty(@Nullable Collection collection) {
        return null == collection || collection.isEmpty();
    }

    public static boolean isEmpty(@Nullable Map collection) {
        return null == collection || collection.isEmpty();
    }

    public static <T> void forEach(@Nullable Iterable<T> sourceList, @NonNull NFunc2<T, Integer> func) {
        if (null != sourceList) {
            int index = 0;

            for(T source : sourceList) {
                func.call(source, index);
                ++index;
            }

        }
    }

    @Nullable
    public static <T, R> List<R> map(@Nullable Iterable<T> sourceList, @NonNull NFunc1R<T, R> converterFunc) {
        if (null == sourceList) {
            return null;
        } else {
            List<R> resultList = new ArrayList();

            for(T source : sourceList) {
                resultList.add(converterFunc.call(source));
            }

            return resultList;
        }
    }

    @Nullable
    public static <T> String join(@Nullable Iterable<T> sourceList, String delimiter, @NonNull NFunc1R<T, String> converterFunc) {
        if (null == sourceList) {
            return null;
        } else {
            Iterator<T> it = sourceList.iterator();
            if (!it.hasNext()) {
                return "";
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append((String)converterFunc.call(it.next()));

                while(it.hasNext()) {
                    String value = (String)converterFunc.call(it.next());
                    sb.append(delimiter);
                    sb.append(value);
                }

                return sb.toString();
            }
        }
    }

    @Nullable
    public static <T> String joinIfNotEmpty(@Nullable Iterable<T> sourceList, String delimiter, @NonNull NFunc1R<T, String> converterFunc) {
        if (null == sourceList) {
            return null;
        } else {
            Iterator<T> it = sourceList.iterator();
            if (!it.hasNext()) {
                return "";
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append((String)converterFunc.call(it.next()));

                while(it.hasNext()) {
                    String value = (String)converterFunc.call(it.next());
                    if (null != value && value.trim().length() > 0) {
                        sb.append(delimiter);
                        sb.append(value);
                    }
                }

                return sb.toString();
            }
        }
    }

    @Nullable
    public static <T> List<T> filter(@Nullable Iterable<T> sourceList, @NonNull NFunc1R<T, Boolean> conditionFunc) {
        if (null == sourceList) {
            return null;
        } else {
            List<T> result = new ArrayList();

            for(T current : sourceList) {
                Boolean conditionResult = (Boolean)conditionFunc.call(current);
                if (null != conditionResult && conditionResult) {
                    result.add(current);
                }
            }

            return result;
        }
    }

    @Nullable
    public static <T> T filterFirst(@Nullable Iterable<T> sourceList, @NonNull NFunc1R<T, Boolean> conditionFunc) {
        if (null == sourceList) {
            return null;
        } else {
            for(T current : sourceList) {
                Boolean conditionResult = (Boolean)conditionFunc.call(current);
                if (null != conditionResult && conditionResult) {
                    return current;
                }
            }

            return null;
        }
    }
}