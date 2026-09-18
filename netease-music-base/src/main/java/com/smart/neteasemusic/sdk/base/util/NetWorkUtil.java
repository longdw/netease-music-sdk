package com.smart.neteasemusic.sdk.base.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;

import com.smart.neteasemusic.sdk.base.SDKContext;
import com.smart.neteasemusic.sdk.base.util.logger.SmartLogUtil;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 网络工具
 *
 * @author hm
 */
public final class NetWorkUtil {
    public static final String TAG = "NetWorkUtil";
    private static Listener mListener;
    private static NetworkCheckInterceptor mNetworkCheckInterceptor;

    /**
     * 判断网络是否连接
     *
     * @return true  false
     */
    public static boolean isNetworkConnected() {
        if (mNetworkCheckInterceptor != null && !mNetworkCheckInterceptor.isNetworkConnected()) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) SDKContext.INSTANCE.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connectivityManager) {
            SmartLogUtil.d(TAG, "connectivityManager is null");
            return false;
        }
        NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo networkInfo : info) {
            // 车机这个很特殊 一直是连接状态
            if ("Ethernet".equals(networkInfo.getTypeName())) {
                continue;
            }
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    public static void getNetWork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                if (null != mListener){
                    mListener.onLost(NetWorkUtil.isNetworkConnected());
                }
            }

            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                if (null != mListener){
                    mListener.onAvailable(NetWorkUtil.isNetworkConnected());
                }
            }
        };
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        if (cm != null) {
            cm.registerNetworkCallback(builder.build(), callback);
        }
    }

    /**
     * 当前网络可用
     * @return true  false
     */
    public static boolean isNetworkEnable(){

        ConnectivityManager connectivityManager= (ConnectivityManager) SDKContext.INSTANCE.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network= connectivityManager.getActiveNetwork();
        if (network==null){
            return false;
        }
        NetworkCapabilities status= connectivityManager.getNetworkCapabilities(network);
        if (status==null){
            return false;
        }
        return status.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)&&
                status.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)&&
                (status.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)||status.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));

    }


    /**
     * 没有网络连接
     */
    public static final int NETWORK_NONE = 0;
    /**
     * wifi连接
     */
    public static final int NETWORK_WIFI = 1;
    /**
     * 手机网络2G
     */
    public static final int NETWORK_2G = 2;
    /**
     * 手机网络3G
     */
    public static final int NETWORK_3G = 3;
    /**
     * 手机网络4G
     */
    public static final int NETWORK_4G = 4;
    /**
     * 手机网络5G
     */
    public static final int NETWORK_5G = 5;
    /**
     * 手机网络
     */
    public static final int NETWORK_MOBILE = 6;
    /**
     * 移动3G
     */
    public static final String TD_SCDMA = "TD-SCDMA";
    /**
     * 联通3G
     */
    public static final String WCDMA = "WCDMA";
    /**
     * 电信3G
     */
    public static final String CDMA2000 = "CDMA2000";

    /**
     * 获取当前网络连接类型
     *
     * @param context 上下文
     * @return 网络状态
     */
    public static int getNetworkState(Context context) {
        //获取系统的网络服务
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //如果当前没有网络
        if (null == connManager) {
            return NETWORK_NONE;
        }
        //获取当前网络类型，如果为空，返回无网络
        @SuppressLint("MissingPermission") NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return NETWORK_NONE;
        }
        // 判断是不是连接的是不是wifi
        @SuppressLint("MissingPermission") NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return NETWORK_WIFI;
                }
            }
        }
        // 如果不是wifi，则判断当前连接的是运营商的哪种网络2g、3g、4g等
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (null != networkInfo) {
            NetworkInfo.State state = networkInfo.getState();
            String strSubTypeName = networkInfo.getSubtypeName();
            if (null != state) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    switch (activeNetInfo.getSubtype()) {
                            // 联通2g
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                            // 电信2g
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                            // 移动2g
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            return NETWORK_2G;
                        //如果是3g类型
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            return NETWORK_3G;
                        //如果是4g类型
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            return NETWORK_4G;
                        //对应的20 只有依赖为android 10.0才有此属性
                        case TelephonyManager.NETWORK_TYPE_NR:
                            return NETWORK_5G;
                        default:
                            //中国移动 联通 电信 三种3G制式
                            if (TD_SCDMA.equalsIgnoreCase(strSubTypeName) || WCDMA.equalsIgnoreCase(strSubTypeName) || CDMA2000.equalsIgnoreCase(strSubTypeName)) {
                                return NETWORK_3G;
                            } else {
                                return NETWORK_MOBILE;
                            }
                    }
                }
            }
        }
        return NETWORK_NONE;
    }


    /**
     * 获取IP地址
     */
    public static String getIpAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            // 当前使用2G/3G/4G网络
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface networkInterface = en.nextElement();
                        for (Enumeration<InetAddress> addressEnumeration = networkInterface.getInetAddresses(); addressEnumeration.hasMoreElements(); ) {
                            InetAddress inetAddress = addressEnumeration.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            }
            // 当前使用无线网络
            else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                // 得到IPV4地址
                return getStringIp(wifiInfo.getIpAddress());
            }
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     */
    private static String getStringIp(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    public static void setListener(Listener listener) {
        mListener = listener;
    }

    public static void setNetworkCheckInterceptor(NetworkCheckInterceptor networkCheckInterceptor) {
        mNetworkCheckInterceptor = networkCheckInterceptor;
    }

    public interface Listener {
        /**
         * 连接上网络
         */
        void onAvailable(boolean networkConnected);

        /**
         * 断开了网络
         */
        void onLost(boolean networkConnected);
    }

    /**
     * HY车型如果在隐私模式下，所有网络均不可用
     */
    public interface NetworkCheckInterceptor {
        boolean isNetworkConnected();
    }
}
