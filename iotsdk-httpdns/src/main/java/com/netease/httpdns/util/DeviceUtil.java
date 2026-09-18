package com.netease.httpdns.util;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.netease.httpdns.HttpDnsService;
import java.lang.reflect.Method;

public class DeviceUtil {
    private static final String TAG = "WowSystemUtil";
    private static String currentProcessName;

    public static boolean isUIProcess() {
        Context context = HttpDnsService.getInstance().getContext();
        if (context == null) {
            return false;
        } else {
            try {
                String packageName = context.getPackageName();
                if (TextUtils.isEmpty(currentProcessName)) {
                    currentProcessName = getCurrentProcessName(context);
                }

                return TextUtils.equals(packageName, currentProcessName);
            } catch (Exception e) {
                if (S.LOG.showLog()) {
                    S.LOG.i("WowSystemUtil[isUIProcess] error: " + e.getMessage());
                }

                return true;
            }
        }
    }

    public static String getCurrentProcessName(Context context) {
        String processName = "";
        processName = getCurrentProcessNameByApplication();
        if (!TextUtils.isEmpty(processName)) {
            return processName;
        } else {
            processName = getCurrentProcessNameByActivityThread();
            return !TextUtils.isEmpty(processName) ? processName : getCurrentProcessNameByActivityManager(context);
        }
    }

    private static String getCurrentProcessNameByApplication() {
        return VERSION.SDK_INT >= 28 ? Application.getProcessName() : "";
    }

    public static String getCurrentProcessNameByActivityThread() {
        String processName = null;

        try {
            Method declaredMethod = Class.forName("android.app.ActivityThread", false, Application.class.getClassLoader()).getDeclaredMethod("currentProcessName", new Class[0]);
            declaredMethod.setAccessible(true);
            Object invoke = declaredMethod.invoke((Object)null);
            if (invoke instanceof String) {
                processName = (String)invoke;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return processName;
    }

    public static String getCurrentProcessNameByActivityManager(Context context) {
        if (context == null) {
            return null;
        } else {
            String processName = null;
            int pid = Process.myPid();

            try {
                ActivityManager mActivityManager = (ActivityManager)context.getSystemService("activity");

                for(ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
                    if (appProcess.pid == pid) {
                        processName = appProcess.processName;
                        break;
                    }
                }

                return processName;
            } catch (Exception var6) {
                return null;
            }
        }
    }
}