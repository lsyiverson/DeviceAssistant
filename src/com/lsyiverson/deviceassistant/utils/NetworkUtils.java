package com.lsyiverson.deviceassistant.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.lsyiverson.deviceassistant.R;

public class NetworkUtils {

    public static String getOperatorName(Context context) {
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String name = telManager.getNetworkOperatorName();
        LogUtils.d(NetworkUtils.class, "network operator name: " + name);
        if (TextUtils.isEmpty(name)) {
            name = context.getString(R.string.network_operator_none);
        }
        return name;
    }

    private static int getNetworkType (Context context) {
        ConnectivityManager conManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conManager.getActiveNetworkInfo();
        if (info == null) {
            return 999;
        }
        return info.getType();
    }

    public static String getNetworkTypeName(Context context) {
        String typename = "";
        switch (getNetworkType(context)) {
            case ConnectivityManager.TYPE_BLUETOOTH:
                typename = context.getString(R.string.network_type_bluetooth);
                break;
            case ConnectivityManager.TYPE_DUMMY:
                typename = context.getString(R.string.network_type_dummy);
                break;
            case ConnectivityManager.TYPE_ETHERNET:
                typename = context.getString(R.string.network_type_ethernet);
                break;
            case ConnectivityManager.TYPE_MOBILE:
                typename = context.getString(R.string.network_type_mobile);
                break;
            case ConnectivityManager.TYPE_WIFI:
                typename = context.getString(R.string.network_type_wifi);
                break;
            case ConnectivityManager.TYPE_WIMAX:
                typename = context.getString(R.string.network_type_wimax);
                break;
            case 999:
                typename = context.getString(R.string.network_no_network);
                break;
        }
        return typename;
    }

    public static String getApnType(Context context) {
        String apntype = "nomatch";
        try {
            switch(getNetworkType(context)) {
                case ConnectivityManager.TYPE_WIFI:
                    LogUtils.d(NetworkUtils.class, "Network use wifi");
                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    apntype = wifiInfo.getSSID();
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    ConnectivityManager conManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    LogUtils.d(NetworkUtils.class, "Network use mobile");
                    NetworkInfo mobileInfo = conManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    apntype = mobileInfo.getExtraInfo();
                    break;
                case ConnectivityManager.TYPE_BLUETOOTH:
                    apntype = context.getString(R.string.network_type_bluetooth);
                    break;
                case ConnectivityManager.TYPE_ETHERNET:
                    apntype = context.getString(R.string.network_type_ethernet);
                    break;
                case 999:
                    apntype = context.getString(R.string.network_no_network);
                    break;
            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            apntype = context.getString(R.string.network_no_network);
        }
        return apntype;
    }

    public static String getMacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String macAddress = wifiInfo.getMacAddress().toUpperCase();
        return macAddress;
    }

    public static String[] getDeviceString(Context context) {
        TelephonyManager telephonyManager=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String phone = telephonyManager.getLine1Number();
        String imei=telephonyManager.getDeviceId();
        String imsi=telephonyManager.getSubscriberId();
        String simid = telephonyManager.getSimSerialNumber();
        String[] ids = {phone, imei, imsi, simid};
        return ids;
    }

}
