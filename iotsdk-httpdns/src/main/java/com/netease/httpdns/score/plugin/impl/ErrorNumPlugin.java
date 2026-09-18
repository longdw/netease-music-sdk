package com.netease.httpdns.score.plugin.impl;

import android.text.TextUtils;
import com.netease.httpdns.module.IPModel;
import com.netease.httpdns.score.plugin.IPlugin;
import com.netease.httpdns.score.plugin.PluginManager;
import java.util.List;

public class ErrorNumPlugin implements IPlugin {
    public void run(List<IPModel> ipModels) {
        float maxErrorNum = 0.0F;

        for(IPModel model : ipModels) {
            if (!TextUtils.isEmpty(model.errNum)) {
                float errNmu = Float.parseFloat(model.errNum);
                maxErrorNum = Math.max(maxErrorNum, errNmu);
            }
        }

        if (maxErrorNum != 0.0F) {
            float bi = this.getWeight() / maxErrorNum;

            for(IPModel model : ipModels) {
                if (!TextUtils.isEmpty(model.errNum)) {
                    float errNum = Float.parseFloat(model.errNum);
                    model.grade += errNum - errNum * bi;
                }
            }

        }
    }

    public float getWeight() {
        return PluginManager.sErrorNumPluginScore;
    }

    public boolean isActivated() {
        return true;
    }
}