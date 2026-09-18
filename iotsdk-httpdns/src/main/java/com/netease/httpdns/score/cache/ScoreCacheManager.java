package com.netease.httpdns.score.cache;

import android.content.Context;
import android.text.TextUtils;
import com.netease.httpdns.module.IPModel;
import com.netease.httpdns.util.MD5Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreCacheManager implements IScoreCache {
    private static final String SCORE_SP_NAME = "score_sp_name";
    private final Map<String, IPModel> mMemoryCache = new HashMap();
    private Context mContext;

    public ScoreCacheManager(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public IPModel getIPModel(String ip) {
        if (TextUtils.isEmpty(ip)) {
            return null;
        } else {
            String md5Ip = MD5Util.getMD5(ip);
            IPModel temp = (IPModel)this.mMemoryCache.get(md5Ip);
            return temp;
        }
    }

    public void insertModel(String ip, IPModel model) {
        if (!TextUtils.isEmpty(ip) && model != null) {
            this.addMemoryCache(ip, model);
        }
    }

    public void addMemoryCache(String ip, IPModel model) {
        if (!TextUtils.isEmpty(ip) && model != null) {
            ip = MD5Util.getMD5(ip);
            IPModel temp = (IPModel)this.mMemoryCache.get(ip);
            if (temp != null) {
                this.mMemoryCache.remove(ip);
            }

            this.mMemoryCache.put(ip, model);
        }
    }

    public void clearAllCache() {
        this.mMemoryCache.clear();
    }

    public void clearMemoryCache() {
        this.mMemoryCache.clear();
    }

    public List<IPModel> getAllMemoryCache() {
        List<IPModel> ipModels = new ArrayList();

        for(Map.Entry<String, IPModel> entry : this.mMemoryCache.entrySet()) {
            IPModel temp = (IPModel)this.mMemoryCache.get(entry.getKey());
            ipModels.add(temp);
        }

        return ipModels;
    }
}
