package com.netease.httpdns.score.plugin;

import com.netease.httpdns.module.IPModel;
import java.util.List;

public class ScoreManager implements IScore {
    private PluginManager mPluginManager = new PluginManager();

    public String[] serverIpScore(List<IPModel> ipModels) {
        if (ipModels != null && ipModels.size() > 1) {
            this.mPluginManager.run(ipModels);
        }

        return this.getIPs(ipModels);
    }

    public String[] getIPs(List<IPModel> ipModels) {
        if (ipModels != null && !ipModels.isEmpty()) {
            String[] ips = new String[ipModels.size()];

            for(int i = 0; i < ipModels.size(); ++i) {
                IPModel model = (IPModel)ipModels.get(i);
                if (model != null) {
                    ips[i] = model.ip;
                }
            }

            return ips;
        } else {
            return null;
        }
    }
}