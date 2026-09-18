package com.netease.httpdns.score.speedtest;

public abstract class BaseSpeedTest {
    protected static final int SOCKET_PRIORITY = 10;
    protected static final int PING_PRIORITY = 0;

    public abstract int speedTest(String var1);

    public abstract int getPriority();

    public abstract boolean isActive();
}
