package com.netease.httpdns.score;

import android.content.Context;
import com.netease.httpdns.listener.ISort;
import com.netease.httpdns.module.IPModel;
import com.netease.httpdns.score.cache.ScoreCacheManager;
import com.netease.httpdns.score.plugin.ScoreManager;
import com.netease.httpdns.score.speedtest.SpeedTestManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScoreSort implements ISort {
    private SpeedTestManager mSpeedTestManager;
    private ScoreManager mScoreManager;
    private ScoreCacheManager mScoreCacheManager;

    private ScoreSort() {
    }

    public void init(Context context) {
        this.mSpeedTestManager = new SpeedTestManager();
        this.mSpeedTestManager.init();
        this.mScoreCacheManager = new ScoreCacheManager(context);
        this.mScoreManager = new ScoreManager();
    }

    public static ScoreSort getInstance() {
        return Holder.INSTANCE;
    }

    public List<String> sort(List<String> ips) {
        if (ips != null && ips.size() >= 2) {
            List<IPModel> ipModels = this.createIPModels(ips);

            for(IPModel ipModel : ipModels) {
                int rtt = this.mSpeedTestManager.speedTest(ipModel.ip);
                boolean success = rtt > -1;
                if (success) {
                    ipModel.rtt = String.valueOf(rtt);
                    ipModel.successNum = String.valueOf(Integer.valueOf(ipModel.successNum) + 1);
                    ipModel.finallySuccessTime = String.valueOf(System.currentTimeMillis());
                } else {
                    ipModel.rtt = String.valueOf(9999);
                    ipModel.errNum = String.valueOf(Integer.valueOf(ipModel.errNum) + 1);
                    ipModel.finallyFailTime = String.valueOf(System.currentTimeMillis());
                }

                this.mScoreCacheManager.insertModel(ipModel.ip, ipModel);
            }

            String[] ipScore = this.mScoreManager.serverIpScore(ipModels);
            return Arrays.asList(ipScore);
        } else {
            return ips;
        }
    }

    private List<IPModel> createIPModels(List<String> ips) {
        List<IPModel> ipModels = new ArrayList();

        for(int i = 0; i < ips.size(); ++i) {
            String ip = (String)ips.get(i);
            IPModel temp = this.mScoreCacheManager.getIPModel(ip);
            if (temp == null) {
                temp = new IPModel(ip);
            }

            ipModels.add(temp);
        }

        return ipModels;
    }

    public List<IPModel> getAllMemoryCache() {
        return this.mScoreCacheManager.getAllMemoryCache();
    }

    public void clearMemory() {
        this.mScoreCacheManager.clearMemoryCache();
    }

    public IPModel getIPModel(String ip) {
        return this.mScoreCacheManager.getIPModel(ip);
    }

    private static class Holder {
        private static final ScoreSort INSTANCE = new ScoreSort();
    }
}
