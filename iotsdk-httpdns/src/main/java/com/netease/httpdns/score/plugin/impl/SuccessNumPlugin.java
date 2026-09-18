package com.netease.httpdns.score.plugin.impl;

import android.text.TextUtils;
import com.netease.httpdns.module.IPModel;
import com.netease.httpdns.score.plugin.IPlugin;
import com.netease.httpdns.score.plugin.PluginManager;
import java.util.List;

public class SuccessNumPlugin implements IPlugin {
    public void run(List<IPModel> ipModels) {
        float maxSuccessNum = 0.0F;

        for(IPModel model : ipModels) {
            if (!TextUtils.isEmpty(model.successNum)) {
                float successNum = Float.parseFloat(model.successNum);
                maxSuccessNum = Math.max(maxSuccessNum, successNum);
            }
        }

        if (maxSuccessNum != 0.0F) {
            float bi = this.getWeight() / maxSuccessNum;

            for(IPModel model : ipModels) {
                if (!TextUtils.isEmpty(model.successNum)) {
                    float successNum = Float.parseFloat(model.successNum);
                    model.grade += successNum * bi;
                }
            }

        }
    }

    public float getWeight() {
        return PluginManager.sSuccessNumPluginScore;
    }

    public boolean isActivated() {
        return true;
    }
}