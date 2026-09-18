package com.netease.httpdns.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.netease.android.extension.func.NFunc0R;
import com.netease.android.extension.usage.NLazy;
import com.netease.httpdns.HttpDnsService;

public class NAppUtil {
    private static NLazy<String> appNameLazy = new NLazy(new NFunc0R<String>() {
        public String call() {
            return NAppUtil.getAppName(HttpDnsService.getInstance().getContext());
        }
    });

    private static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getAppNameLazy() {
        return (String)appNameLazy.get();
    }
}