package com.netease.httpdns.score.plugin.impl;

import android.text.TextUtils;
import com.netease.httpdns.module.IPModel;
import com.netease.httpdns.score.plugin.IPlugin;
import com.netease.httpdns.score.plugin.PluginManager;
import java.util.List;

public class SpeedTestPlugin implements IPlugin {
    public void run(List<IPModel> ipModels) {
        float maxSpeed = 0.0F;

        for(IPModel model : ipModels) {
            if (!TextUtils.isEmpty(model.rtt)) {
                float speed = Float.parseFloat(model.rtt);
                maxSpeed = Math.max(maxSpeed, speed);
            }
        }

        if (maxSpeed != 0.0F) {
            float bi = this.getWeight() / maxSpeed;

            for(IPModel model : ipModels) {
                if (!TextUtils.isEmpty(model.rtt)) {
                    float speed = Float.parseFloat(model.rtt);
                    model.grade += this.getWeight() - speed * bi;
                }
            }

        }
    }

    public float getWeight() {
        return PluginManager.sSpeedTestPluginScore;
    }

    public boolean isActivated() {
        return true;
    }
}
