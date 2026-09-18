package com.netease.httpdns.score.cache;

import com.netease.httpdns.module.IPModel;
import java.util.List;

public interface IScoreCache {
    IPModel getIPModel(String var1);

    void insertModel(String var1, IPModel var2);

    void addMemoryCache(String var1, IPModel var2);

    void clearAllCache();

    void clearMemoryCache();

    List<IPModel> getAllMemoryCache();
}