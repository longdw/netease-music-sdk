package com.netease.httpdns.score.socketScore;

import java.util.List;

public interface DetectIpsListener {
    void onDetectIpsFinish(String var1, List<IpDetectModel> var2);
}