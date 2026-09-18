package com.netease.android.extension.ext;

import java.util.UUID;

public final class StringExt {
    public static boolean isEmpty(CharSequence charSequence) {
        return null == charSequence || charSequence.length() <= 0;
    }

    public static boolean isBlank(String str) {
        return null == str || str.trim().length() <= 0;
    }

    public static String randomUUIDString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}