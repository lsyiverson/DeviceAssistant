package com.lsyiverson.deviceassistant.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.lsyiverson.deviceassistant.R;

public class CpuUtils {
    private static final long KHZ_TO_MHZ = 1000;
    private static final long KHZ_TO_GHZ = 1000*1000;

    private static final String CPUINFO_PATH = "/proc/cpuinfo";
    private static final String CPU_MIN_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq";
    private static final String CPU_MAX_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";

    private static final String GHZ = "GHz";
    private static final String MHZ = "MHz";
    private static final String KHZ = "KHz";

    private static int mCoreCount = 0;

    public static ArrayList<HashMap<String, Object>> getCpuInfo(Context context) {
        String str="";
        int coresCount = 0;
        ArrayList<HashMap<String, Object>> cpuInfo = new ArrayList<HashMap<String,Object>>();
        try {
            FileReader fr = new FileReader(CPUINFO_PATH);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            int cpu_variant = 0;
            int cpu_revision = 0;
            while ((str = localBufferedReader.readLine()) != null) {
                String[] item = str.split(":\\s+");
                if (item.length == 2) {
                    String name = item[0].trim();
                    String value = item[1].trim();
                    if (name.equals("Processor")) {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put(Constants.LIST_NAME, context.getString(R.string.cpu_processor));
                        map.put(Constants.LIST_VALUE, value);
                        cpuInfo.add(map);
                    } else if (name.equals("processor")) {
                        coresCount++;
                    } else if (name.equals("CPU variant")) {
                        if (value.startsWith("0x")) {
                            value = value.substring(2);
                        }
                        cpu_variant = Integer.parseInt(value, 16);
                    } else if (name.equals("CPU revision")) {
                        cpu_revision = Integer.parseInt(value);
                    }
                }
            }
            if (coresCount == 0) {
                coresCount = 1;
            }
            if (mCoreCount < coresCount) {
                mCoreCount = coresCount;
            }
            HashMap<String, Object> coreCountMap = new HashMap<String, Object>();
            coreCountMap.put(Constants.LIST_NAME, context.getString(R.string.cpu_core_count));
            coreCountMap.put(Constants.LIST_VALUE, mCoreCount);
            cpuInfo.add(coreCountMap);

            HashMap<String, Object> revisionMap = new HashMap<String, Object>();
            revisionMap.put(Constants.LIST_NAME, context.getString(R.string.cpu_revision));
            revisionMap.put(Constants.LIST_VALUE, "r" + cpu_variant + "p" +cpu_revision);
            cpuInfo.add(revisionMap);

            cpuInfo.add(getCpuFreq(context));
            cpuInfo.addAll(getCurCpuCoresFreq(context, mCoreCount));
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return cpuInfo;
    }

    private static HashMap<String, Object> getCpuFreq(Context context) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(Constants.LIST_NAME, context.getString(R.string.cpu_clock_speed));
        map.put(Constants.LIST_VALUE, getMinCpuFreq() + " - " + getMaxCpuFreq());
        return map;
    }

    private static ArrayList<HashMap<String, Object>> getCurCpuCoresFreq(Context context, int coreCount) {
        ArrayList<HashMap<String, Object>> coresFreq = new ArrayList<HashMap<String,Object>>();
        for(int i = 0 ; i < coreCount; i++) {
            String freq = getCurCpuFreq(i);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(Constants.LIST_NAME, context.getString(R.string.cpu_cores, i));
            if (freq != null) {
                map.put(Constants.LIST_VALUE, freq);
            } else {
                map.put(Constants.LIST_VALUE, context.getString(R.string.cpu_stoped));
            }
            coresFreq.add(map);
        }
        return coresFreq;
    }

    public static String getCurCpuFreq(int core) {
        String str1 = "/sys/devices/system/cpu/cpu"+core+"/cpufreq/scaling_cur_freq";
        String str2 = "";
        String curCpuFreq = "N/A";
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            long minFreq = 0;
            while ((str2 = localBufferedReader.readLine()) != null) {
                minFreq = Long.parseLong(str2.trim());
            }
            curCpuFreq = parseCpuFreqToMHz(minFreq);
            localBufferedReader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return curCpuFreq;
    }

    public static String getMinCpuFreq() {
        String str = "";
        String minCpuFreq = "N/A";
        try {
            FileReader fr = new FileReader(CPU_MIN_FREQ);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            long minFreq = 0;
            while ((str = localBufferedReader.readLine()) != null) {
                minFreq = Long.parseLong(str.trim());
            }
            minCpuFreq = parseCpuFreq(minFreq);
            localBufferedReader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return minCpuFreq;
    }

    public static String getMaxCpuFreq() {
        String str = "";
        String maxCpuFreq = "N/A";
        try {
            FileReader fr = new FileReader(CPU_MAX_FREQ);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            long maxFreq = 0;
            while ((str = localBufferedReader.readLine()) != null) {
                maxFreq = Long.parseLong(str.trim());
            }
            maxCpuFreq = parseCpuFreq(maxFreq);
            localBufferedReader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return maxCpuFreq;
    }

    private static String parseCpuFreq(long freq) {
        if(freq < KHZ_TO_MHZ) {
            return freq + KHZ;
        } else if (freq < KHZ_TO_GHZ) {
            return freq / 1000 + MHZ;
        } else {
            return String.format("%.2f", freq / 1000.00 / 1000.00) + GHZ;
        }
    }

    private static String parseCpuFreqToMHz(long freq) {
        if(freq < KHZ_TO_MHZ) {
            return freq + KHZ;
        } else {
            return freq / 1000 + MHZ;
        }
    }
}
