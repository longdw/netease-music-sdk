package com.netease.httpdns.score.plugin;

import com.netease.httpdns.module.IPModel;
import com.netease.httpdns.score.plugin.impl.ErrorNumPlugin;
import com.netease.httpdns.score.plugin.impl.PriorityPlugin;
import com.netease.httpdns.score.plugin.impl.SpeedTestPlugin;
import com.netease.httpdns.score.plugin.impl.SuccessNumPlugin;
import com.netease.httpdns.score.plugin.impl.SuccessTimePlugin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PluginManager {
    public static float sErrorNumPluginScore = 10.0F;
    public static float sPriorityPluginScore = 60.0F;
    public static float sSpeedTestPluginScore = 40.0F;
    public static float sSuccessNumPluginScore = 10.0F;
    public static float sSuccessTimePluginScore = 10.0F;
    private List<IPlugin> mPlugins = new ArrayList();

    public PluginManager() {
        this.mPlugins.add(new ErrorNumPlugin());
        this.mPlugins.add(new PriorityPlugin());
        this.mPlugins.add(new SpeedTestPlugin());
        this.mPlugins.add(new SuccessNumPlugin());
        this.mPlugins.add(new SuccessTimePlugin());
    }

    public void run(List<IPModel> ipModels) {
        if (ipModels != null && !ipModels.isEmpty()) {
            this.resetGrade(ipModels);

            for(IPlugin plugin : this.mPlugins) {
                if (plugin.isActivated()) {
                    plugin.run(ipModels);
                }
            }

            this.sortIPModel(ipModels);
        }
    }

    private void resetGrade(List<IPModel> ipModels) {
        for(IPModel model : ipModels) {
            if (model != null) {
                model.grade = 0.0F;
            }
        }

    }

    private void sortIPModel(List<IPModel> ipModels) {
        Collections.sort(ipModels, new Comparator<IPModel>() {
            public int compare(IPModel lhs, IPModel rhs) {
                return Float.compare(rhs.grade, lhs.grade);
            }
        });
    }
}
