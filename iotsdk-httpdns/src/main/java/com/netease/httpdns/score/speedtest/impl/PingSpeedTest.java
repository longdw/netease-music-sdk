package com.netease.httpdns.score.speedtest.impl;

import android.text.TextUtils;
import com.netease.httpdns.score.speedtest.BaseSpeedTest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PingSpeedTest extends BaseSpeedTest {
    public int speedTest(String ip) {
        try {
            return Ping.runCMD("ping -c1 -s1 -w1 " + ip);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getPriority() {
        return 0;
    }

    public boolean isActive() {
        return true;
    }

    public static class Ping {
        private static final String TAG_BYTES_FROM = "bytes from ";

        public static int runCMD(String cmd) throws IOException, InterruptedException {
            Runtime runtime = Runtime.getRuntime();
            String command = cmd.trim();
            long start = System.currentTimeMillis();
            Process process = runtime.exec(command);
            process.waitFor();
            long end = System.currentTimeMillis();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder resultBuilder = new StringBuilder();

            String line;
            while((line = reader.readLine()) != null) {
                resultBuilder.append(line);
            }

            reader.close();
            String result = resultBuilder.toString().toLowerCase().trim();
            return isResultValid(result) ? (int)(end - start) : -1;
        }

        private static boolean isResultValid(String result) {
            return !TextUtils.isEmpty(result) && result.indexOf("bytes from ") > 0;
        }
    }
}