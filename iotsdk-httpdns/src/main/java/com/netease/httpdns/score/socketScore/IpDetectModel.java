package com.netease.httpdns.score.socketScore;

public class IpDetectModel {
    private String domain;
    private String ip;
    private int time;
    private int delay;

    public IpDetectModel(IpDetectModel ipDetectModel) {
        this.domain = ipDetectModel.getDomain();
        this.ip = ipDetectModel.getIp();
        this.time = ipDetectModel.getTime();
        this.delay = ipDetectModel.getDelay();
    }

    public IpDetectModel() {
    }

    public IpDetectModel(String domain, String ip, int delay) {
        this.domain = domain;
        this.ip = ip;
        this.delay = delay;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getDelay() {
        return this.delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String toString() {
        return "IpDetectModel{domain='" + this.domain + '\'' + ", ip='" + this.ip + '\'' + ", time=" + this.time + ", delay=" + this.delay + '}';
    }
}