package com.netease.httpdns.score.plugin.impl;

import android.text.TextUtils;
import com.netease.httpdns.module.IPModel;
import com.netease.httpdns.score.plugin.IPlugin;
import com.netease.httpdns.score.plugin.PluginManager;
import java.util.List;

public class SuccessTimePlugin implements IPlugin {
    public void run(List<IPModel> ipModels) {
        float dayTime = 1440.0F;
        float bi = this.getWeight() / 1440.0F;

        for(IPModel model : ipModels) {
            if (!TextUtils.isEmpty(model.finallySuccessTime)) {
                long lastSuccessTime = Long.parseLong(model.finallySuccessTime);
                long now = System.currentTimeMillis();
                long offTime = (now - lastSuccessTime) / 1000L / 60L;
                if (1440.0F > (float)offTime) {
                    model.grade += this.getWeight() - (float)offTime * bi;
                }
            }
        }

    }

    public float getWeight() {
        return PluginManager.sSuccessTimePluginScore;
    }

    public boolean isActivated() {
        return true;
    }
}