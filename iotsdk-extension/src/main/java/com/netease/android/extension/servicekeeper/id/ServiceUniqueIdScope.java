package com.netease.android.extension.servicekeeper.id;

public enum ServiceUniqueIdScope {
    LOCAL,
    PARENT,
    ROOT;

    public static boolean isLocal(ServiceUniqueIdScope expectServiceUniqueIdScope) {
        return LOCAL == expectServiceUniqueIdScope;
    }
}