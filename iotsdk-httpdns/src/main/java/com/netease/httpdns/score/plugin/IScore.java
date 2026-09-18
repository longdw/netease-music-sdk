package com.netease.httpdns.score.plugin;

import com.netease.httpdns.module.IPModel;
import java.util.List;

public interface IScore {
    String[] serverIpScore(List<IPModel> var1);

    String[] getIPs(List<IPModel> var1);
}