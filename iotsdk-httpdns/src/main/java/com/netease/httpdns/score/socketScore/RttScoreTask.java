package com.netease.httpdns.score.socketScore;

import com.netease.httpdns.score.speedtest.impl.Socket80SpeedTest;
import com.netease.httpdns.util.S;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class RttScoreTask {
    private static final int ERROR = -1;
    private List<IpDetectModel> rttModelTaskList = new ArrayList();
    private List<IpDetectModel> rttModelResultList = new ArrayList();
    private List<IpDetectModel> rttModelValidResultList = new ArrayList();
    private final Socket80SpeedTest socket80SpeedTest;
    private final RttScoreListener listener;
    private final ExecutorService executorService;

    public RttScoreTask(List<IpDetectModel> rttModelList, ExecutorService executorService, RttScoreListener listener) {
        this.rttModelTaskList.addAll(rttModelList);
        this.listener = listener;
        this.socket80SpeedTest = new Socket80SpeedTest();
        this.executorService = executorService;
    }

    public void execute() {
        for(final IpDetectModel ipDetectModel : this.rttModelTaskList) {
            this.executorService.submit(new Runnable() {
                public void run() {
                    try {
                        String ip = ipDetectModel.getIp();
                        int time = RttScoreTask.this.socket80SpeedTest.speedTest(ip) + ipDetectModel.getDelay();
                        IpDetectModel result = new IpDetectModel(ipDetectModel);
                        if (time == -1) {
                            result.setTime(-1);
                            RttScoreTask.this.onRttScoreFail(result);
                            return;
                        }

                        result.setTime(time);
                        RttScoreTask.this.onRttScoreSuccess(result);
                    } catch (Throwable e) {
                        if (S.LOG.showLog()) {
                            S.LOG.e("[SerialRttScoreTask]run error : " + e.getMessage());
                        }
                    }

                }
            });
        }

    }

    private void rttSort(List<IpDetectModel> ipModelList) {
        Collections.sort(ipModelList, new Comparator<IpDetectModel>() {
            public int compare(IpDetectModel ipDetectModel1, IpDetectModel ipDetectModel2) {
                if (ipDetectModel1 == null) {
                    return 1;
                } else if (ipDetectModel2 == null) {
                    return -1;
                } else {
                    int rtt1 = ipDetectModel1.getTime();
                    int rtt2 = ipDetectModel2.getTime();
                    if (rtt1 == -1) {
                        return 1;
                    } else {
                        return rtt2 == -1 ? -1 : rtt1 - rtt2;
                    }
                }
            }
        });
    }

    private synchronized void onRttScoreSuccess(IpDetectModel ipDetectModel) {
        this.rttModelResultList.add(ipDetectModel);
        this.rttModelValidResultList.add(ipDetectModel);
        this.rttSort(this.rttModelValidResultList);
        if (this.listener != null) {
            this.listener.onRttScoreSuccess(ipDetectModel, this.rttModelValidResultList);
        }

        this.checkoutIfScoreFinish();
    }

    private synchronized void onRttScoreFail(IpDetectModel ipDetectModel) {
        this.rttModelResultList.add(ipDetectModel);
        if (this.listener != null) {
            this.listener.onRttScoreFail(ipDetectModel);
        }

        this.checkoutIfScoreFinish();
    }

    private void onRttScoreFinish() {
        this.rttSort(this.rttModelResultList);
        if (this.listener != null) {
            this.listener.onRttScoreFinish(this.rttModelResultList);
        }

    }

    private boolean checkoutIfScoreFinish() {
        if (this.rttModelResultList.size() != this.rttModelTaskList.size()) {
            return false;
        } else {
            this.onRttScoreFinish();
            return true;
        }
    }

    public interface RttScoreListener {
        void onRttScoreSuccess(IpDetectModel var1, List<IpDetectModel> var2);

        void onRttScoreFail(IpDetectModel var1);

        void onRttScoreFinish(List<IpDetectModel> var1);
    }
}