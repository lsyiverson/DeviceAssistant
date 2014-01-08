package com.lsyiverson.deviceassistant.utils;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;

import com.lsyiverson.deviceassistant.R;


public class GpuUtils {

    private static final String GPU_PREF = "gpuutils.pref";

    private static final String PREF_RENDERER = "renderer";
    private static final String PREF_VENDOR = "vendor";

    private static GpuUtils sInstance;
    private Context mContext;
    private SharedPreferences mPrefs;

    private GpuUtils(Context context) {
        mContext = context;
        mPrefs = mContext.getSharedPreferences(GPU_PREF, Context.MODE_PRIVATE);
    }

    public synchronized static GpuUtils getInstance(Context context) {
        if (null == sInstance) {
            sInstance = new GpuUtils(context);
        }
        return sInstance;
    }

    public String getRenderer() {
        return mPrefs.getString(PREF_RENDERER, null);
    }

    public void setRenderer(String renderer) {
        mPrefs.edit().putString(PREF_RENDERER, renderer).commit();
    }

    public String getVendor() {
        return mPrefs.getString(PREF_VENDOR, null);
    }

    public void setVendor(String vendor) {
        mPrefs.edit().putString(PREF_VENDOR, vendor).commit();
    }

    public ArrayList<HashMap<String, Object>> getGpuInfo() {
        ArrayList<HashMap<String, Object>> gpuInfo = new ArrayList<HashMap<String,Object>>();

        HashMap<String, Object> vendor = new HashMap<String, Object>();
        vendor.put("name", mContext.getString(R.string.gpu_vendor));
        vendor.put("value", getVendor());
        gpuInfo.add(vendor);

        HashMap<String, Object> renderer = new HashMap<String, Object>();
        renderer.put("name", mContext.getString(R.string.gpu_renderer));
        renderer.put("value", getRenderer());
        gpuInfo.add(renderer);

        return gpuInfo;
    }
}
