package com.netease.httpdns.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.netease.httpdns.HttpDnsService;
import com.netease.httpdns.log.DNSLog;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Random;

public class NetworkUtil {
    private static final String TAG = NetworkUtil.class.getSimpleName();
    public static final String NETWORK_TYPE_NO_CONNECT = "";
    public static final String NETWORK_TYPE_WIFI_PREFIX = "wifi_";
    private static final String NETWORK_TYPE_OTHER = "other";
    public static final String NETWORK_TYPE_MOBILE_PREFIX = "mobile_";
    public static final String OPERATOR_MOBILE = "mobile";
    public static final String OPERATOR_UNICOM = "unicom";
    public static final String OPERATOR_TELECOM = "telecom";
    public static final String OPERATOR_CELLPHONE = "mobile_cellphone";
    private static final Random sRandomNetworkType = new Random();
    private static volatile String strNetworkType = "";
    private static volatile boolean is2GNetwork = false;

    public static String getNetworkType() {
        return strNetworkType;
    }

    @SuppressLint({"MissingPermission"})
    public static void syncNetworkType(Context context) {
        DNSLog.i("[" + TAG + "] syncNetworkType");

        try {
            String networkType = "";
            strNetworkType = String.valueOf(sRandomNetworkType.nextInt());
            is2GNetwork = false;
            ConnectivityManager manager = (ConnectivityManager)context.getSystemService("connectivity");
            NetworkInfo networkInfo = null;
            if (manager != null) {
                networkInfo = manager.getActiveNetworkInfo();
            }

            if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
                if (networkInfo.getType() == 1) {
                    String address = getIPAddress(context);
                    networkType = "wifi_" + address;
                } else if (networkInfo.getType() == 0) {
                    networkType = "mobile_cellphone";
                    String operator = getOperator(context);
                    if (!TextUtils.isEmpty(operator)) {
                        networkType = "mobile_" + operator;
                    }

                    switch (networkInfo.getSubtype()) {
                        case 1:
                        case 2:
                        case 4:
                        case 7:
                        case 11:
                            is2GNetwork = true;
                        case 3:
                        case 5:
                        case 6:
                        case 8:
                        case 9:
                        case 10:
                    }
                }
            }

            strNetworkType = networkType;
        } catch (Throwable throwable) {
            S.LOG.e("[NetworkUtil]syncNetworkType error :" + throwable.getMessage());
        }

    }

    @SuppressLint({"MissingPermission"})
    public static String getIPAddress(Context context) throws Exception {
        if (context == null) {
            return "";
        } else {
            NetworkInfo info = ((ConnectivityManager)context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getType() == 0) {
                    try {
                        Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();

                        while(en.hasMoreElements()) {
                            NetworkInterface intf = (NetworkInterface)en.nextElement();
                            Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();

                            while(enumIpAddr.hasMoreElements()) {
                                InetAddress inetAddress = (InetAddress)enumIpAddr.nextElement();
                                if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                    return inetAddress.getHostAddress();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (info.getType() == 1) {
                    WifiManager wifiManager = (WifiManager)context.getSystemService("wifi");
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    return intIP2StringIP(wifiInfo.getIpAddress());
                }
            }

            return "";
        }
    }

    public static String intIP2StringIP(int ip) {
        return (ip & 255) + "." + (ip >> 8 & 255) + "." + (ip >> 16 & 255) + "." + (ip >> 24 & 255);
    }

    public static String getOperator(Context context) {
        if (context == null) {
            return null;
        } else {
            TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService("phone");
            String operator = "";
            if (telephonyManager != null) {
                operator = telephonyManager.getSimOperator();
                if (operator == null) {
                    return "";
                }

                if (!operator.equals("46000") && !operator.equals("46002") && !operator.equals("46007")) {
                    if (operator.equals("46001")) {
                        operator = "unicom";
                    } else if (operator.equals("46003")) {
                        operator = "telecom";
                    }
                } else {
                    operator = "mobile";
                }
            }

            return operator;
        }
    }

    public static boolean isWeakNetwork() {
        IWeakNetwork weakNetwork = HttpDnsService.getInstance().getOptions().getWeakNetwork();
        if (weakNetwork != null) {
            return weakNetwork.isWeakNetwork();
        } else {
            return is2GNetwork || TextUtils.equals(strNetworkType, "");
        }
    }
}
