package com.netease.httpdns.listener;

import java.util.List;
import java.util.Map;

public interface RequestStatusListener {
    void requestSuccess(String var1, String var2, int var3, String var4, Map<String, List<String>> var5);

    void requestFailed(String var1, int var2, String var3);
}