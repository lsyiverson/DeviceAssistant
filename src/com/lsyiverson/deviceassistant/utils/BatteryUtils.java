package com.lsyiverson.deviceassistant.utils;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BatteryManager;

import com.lsyiverson.deviceassistant.R;

public class BatteryUtils {
    private static final String PERCENT = " %";
    private static final String CELSIUS = " ℃";
    private static final String FAHRENHEIT = " ℉";
    private static final String MV = " mV";

    private static final String BATTERY_PREF = "batteryutils.pref";
    private static final String PREF_USE_CELSIUS = "use_celsius";

    public static ArrayList<HashMap<String, Object>> getBatteryInfo(Context context, Intent intent) {
        ArrayList<HashMap<String, Object>> batteryInfo = new ArrayList<HashMap<String,Object>>();

        int batteryHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_UNKNOWN);
        LogUtils.d(BatteryUtils.class, "health : " + batteryHealth);
        batteryInfo.add(getBatteryHealth(context, batteryHealth));

        int batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 100);
        LogUtils.d(BatteryUtils.class, "level : " + batteryLevel);
        batteryInfo.add(getBatteryLevel(context, batteryLevel));

        int batteryPlugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
        LogUtils.d(BatteryUtils.class, "plugged : " + batteryPlugged);
        batteryInfo.add(getBatteryPlugged(context, batteryPlugged));

        int batteryStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN);
        LogUtils.d(BatteryUtils.class, "status : " + batteryStatus);
        batteryInfo.add(getBatteryStatus(context, batteryStatus));

        String batteryTechnology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
        LogUtils.d(BatteryUtils.class, "technology : " + batteryTechnology);
        HashMap<String, Object> techMap = new HashMap<String, Object>();
        techMap.put(Constants.LIST_NAME, context.getString(R.string.battery_technology));
        techMap.put(Constants.LIST_VALUE, batteryTechnology);
        batteryInfo.add(techMap);

        int batteryTemperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 200);
        LogUtils.d(BatteryUtils.class, "temperature : " + batteryTemperature);
        batteryInfo.add(getBatteryTemperature(context, batteryTemperature));

        int batteryVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 4000);
        LogUtils.d(BatteryUtils.class, "voltage : " + batteryVoltage);
        HashMap<String, Object> voltMap = new HashMap<String, Object>();
        voltMap.put(Constants.LIST_NAME, context.getString(R.string.battery_voltage));
        voltMap.put(Constants.LIST_VALUE, batteryVoltage + MV);
        batteryInfo.add(voltMap);

        int batteryScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 10);
        LogUtils.d(BatteryUtils.class, "scale : " + batteryScale);
        batteryInfo.add(getBatteryScale(context,batteryScale));

        return batteryInfo;
    }

    private static HashMap<String, Object> getBatteryScale(Context context, int scale) {
        HashMap<String, Object> scaleMap = new HashMap<String, Object>();
        scaleMap.put(Constants.LIST_NAME, context.getString(R.string.battery_scale));
        scaleMap.put(Constants.LIST_VALUE, scale + PERCENT);
        return scaleMap;
    }

    public static void setUseCelsius(Context context, boolean use_celsius) {
        SharedPreferences pref = context.getSharedPreferences(BATTERY_PREF, Context.MODE_PRIVATE);
        pref.edit().putBoolean(PREF_USE_CELSIUS, use_celsius).commit();
    }

    public static boolean getUseCelsius(Context context) {
        SharedPreferences pref = context.getSharedPreferences(BATTERY_PREF, Context.MODE_PRIVATE);
        return pref.getBoolean(PREF_USE_CELSIUS, true);
    }

    private static HashMap<String, Object> getBatteryTemperature(Context context, int temperature) {
        double t_celsius = temperature / 10.0;

        boolean use_celsius = getUseCelsius(context);

        HashMap<String, Object> temperatureMap = new HashMap<String, Object>();
        temperatureMap.put(Constants.LIST_NAME, context.getString(R.string.battery_temperature));
        if (use_celsius) {
            temperatureMap.put(Constants.LIST_VALUE, String.format("%.1f", t_celsius) + CELSIUS);
        } else {
            double t_fahrenheit = t_celsius * 1.8 + 32;
            temperatureMap.put(Constants.LIST_VALUE, String.format("%.1f", t_fahrenheit) + FAHRENHEIT);
        }
        return temperatureMap;
    }

    private static HashMap<String, Object> getBatteryStatus(Context context, int status) {
        String battery_status = context.getString(R.string.battery_status_unknown);
        switch (status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                battery_status = context.getString(R.string.battery_status_charging);
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                battery_status = context.getString(R.string.battery_status_discharging);
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                battery_status = context.getString(R.string.battery_status_full);
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                battery_status = context.getString(R.string.battery_status_not_charging);
                break;
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                battery_status = context.getString(R.string.battery_status_unknown);
                break;
        }
        HashMap<String, Object> statusMap = new HashMap<String, Object>();
        statusMap.put(Constants.LIST_NAME, context.getString(R.string.battery_status));
        statusMap.put(Constants.LIST_VALUE, battery_status);
        return statusMap;
    }

    private static HashMap<String, Object> getBatteryPlugged(Context context, int plugged) {
        String power_source = context.getString(R.string.battery_plugged_non);
        switch (plugged) {
            case 0:
                power_source = context.getString(R.string.battery_plugged_non);
                break;
            case BatteryManager.BATTERY_PLUGGED_AC:
                power_source = context.getString(R.string.battery_plugged_ac);
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                power_source = context.getString(R.string.battery_plugged_usb);
                break;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                power_source = context.getString(R.string.battery_plugged_wireless);
                break;
        }
        HashMap<String, Object> pluggedMap = new HashMap<String, Object>();
        pluggedMap.put(Constants.LIST_NAME, context.getString(R.string.battery_plugged));
        pluggedMap.put(Constants.LIST_VALUE, power_source);
        return pluggedMap;
    }

    private static HashMap<String, Object> getBatteryLevel(Context context, int level) {
        HashMap<String, Object> levelMap = new HashMap<String, Object>();
        levelMap.put(Constants.LIST_NAME, context.getString(R.string.battery_level));
        levelMap.put(Constants.LIST_VALUE, level + PERCENT);
        return levelMap;
    }

    private static HashMap<String, Object> getBatteryHealth(Context context, int health) {
        String health_status = context.getString(R.string.battery_health_unknown);
        switch (health) {
            case BatteryManager.BATTERY_HEALTH_COLD:
                health_status = context.getString(R.string.battery_health_cold);
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
                health_status = context.getString(R.string.battery_health_dead);
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                health_status = context.getString(R.string.battery_health_good);
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                health_status = context.getString(R.string.battery_health_over_voltage);
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                health_status = context.getString(R.string.battery_health_overheat);
                break;
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                health_status = context.getString(R.string.battery_health_unknown);
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                health_status = context.getString(R.string.battery_health_unspecified_failure);
                break;
        }
        HashMap<String, Object> healthMap = new HashMap<String, Object>();
        healthMap.put(Constants.LIST_NAME, context.getString(R.string.battery_health));
        healthMap.put(Constants.LIST_VALUE, health_status);
        return healthMap;
    }
}
