package com.netease.httpdns.score.plugin;

import com.netease.httpdns.module.IPModel;
import java.util.List;

public interface IPlugin {
    void run(List<IPModel> var1);

    float getWeight();

    boolean isActivated();
}