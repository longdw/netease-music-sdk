package com.netease.httpdns.score.plugin.impl;

import android.text.TextUtils;
import com.netease.httpdns.module.IPModel;
import com.netease.httpdns.score.plugin.IPlugin;
import com.netease.httpdns.score.plugin.PluginManager;
import java.util.List;

public class PriorityPlugin implements IPlugin {
    public void run(List<IPModel> ipModels) {
        float maxPriority = 0.0F;

        for(IPModel model : ipModels) {
            if (!TextUtils.isEmpty(model.priority)) {
                float priority = Float.parseFloat(model.priority);
                maxPriority = Math.max(maxPriority, priority);
            }
        }

        if (maxPriority != 0.0F) {
            float bi = this.getWeight() / maxPriority;

            for(IPModel model : ipModels) {
                if (!TextUtils.isEmpty(model.priority)) {
                    float priority = Float.parseFloat(model.priority);
                    model.grade += priority * bi;
                }
            }

        }
    }

    public float getWeight() {
        return PluginManager.sPriorityPluginScore;
    }

    public boolean isActivated() {
        return true;
    }
}