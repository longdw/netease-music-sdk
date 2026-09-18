package com.netease.httpdns.score.speedtest.impl;

import com.netease.httpdns.score.speedtest.BaseSpeedTest;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Socket80SpeedTest extends BaseSpeedTest {
    public static final int ERROR = -1;
    private static final int TIMEOUT = 2000;

    public int speedTest(String ip) {
        DatagramSocket datagramSocket = null;

        try {
            long start = System.currentTimeMillis();
            InetAddress inetAddress = InetAddress.getByName(ip);
            InetSocketAddress socketAddress = new InetSocketAddress(inetAddress, 80);
            datagramSocket = new DatagramSocket();
            datagramSocket.setSoTimeout(2000);
            datagramSocket.connect(socketAddress);
            long end = System.currentTimeMillis();
            int var9 = (int)(end - start);
            return var9;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (datagramSocket != null) {
                try {
                    datagramSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        return -1;
    }

    public int getPriority() {
        return 10;
    }

    public boolean isActive() {
        return true;
    }

    public int getTimeout() {
        return 2000;
    }
}