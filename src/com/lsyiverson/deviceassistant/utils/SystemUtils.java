package com.lsyiverson.deviceassistant.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.lsyiverson.deviceassistant.R;

public class SystemUtils {
    private static final String DPI = "dpi";

    private static final String MEMINFO_PATH = "/proc/meminfo";

    public static ArrayList<HashMap<String, Object>> getSystemInfo(Context context) {
        ArrayList<HashMap<String, Object>> systemInfo = new ArrayList<HashMap<String,Object>>();

        LogUtils.d(SystemUtils.class, "build brand:" + Build.BRAND);
        HashMap<String, Object> brand = new HashMap<String, Object>();
        brand.put(Constants.LIST_NAME, context.getString(R.string.system_brand));
        brand.put(Constants.LIST_VALUE, Build.BRAND);
        systemInfo.add(brand);

        LogUtils.d(SystemUtils.class, "build model:" + Build.MODEL +"-"+Build.PRODUCT);
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put(Constants.LIST_NAME, context.getString(R.string.system_model));
        model.put(Constants.LIST_VALUE, Build.MODEL + " ("+ Build.PRODUCT +")");
        systemInfo.add(model);

        LogUtils.d(SystemUtils.class, "build manufacturer:" + Build.MANUFACTURER);
        HashMap<String, Object> manufacturer = new HashMap<String, Object>();
        manufacturer.put(Constants.LIST_NAME, context.getString(R.string.system_manufacturer));
        manufacturer.put(Constants.LIST_VALUE, Build.MANUFACTURER);
        systemInfo.add(manufacturer);

        LogUtils.d(SystemUtils.class, "build board:" + Build.BOARD);
        HashMap<String, Object> board = new HashMap<String, Object>();
        board.put(Constants.LIST_NAME, context.getString(R.string.system_board));
        board.put(Constants.LIST_VALUE, Build.BOARD);
        systemInfo.add(board);

        LogUtils.d(SystemUtils.class, "build display:" + Build.DISPLAY);
        HashMap<String, Object> display = new HashMap<String, Object>();
        display.put(Constants.LIST_NAME, context.getString(R.string.system_display));
        display.put(Constants.LIST_VALUE, Build.DISPLAY);
        systemInfo.add(display);

        LogUtils.d(SystemUtils.class, "build hardware:" + Build.HARDWARE);
        HashMap<String, Object> hardware = new HashMap<String, Object>();
        hardware.put(Constants.LIST_NAME, context.getString(R.string.system_hardware));
        hardware.put(Constants.LIST_VALUE, Build.HARDWARE);
        systemInfo.add(hardware);

        LogUtils.d(SystemUtils.class, "build version:" + Build.VERSION.RELEASE);
        HashMap<String, Object> android_version = new HashMap<String, Object>();
        android_version.put(Constants.LIST_NAME, context.getString(R.string.system_android_version));
        android_version.put(Constants.LIST_VALUE, Build.VERSION.RELEASE);
        systemInfo.add(android_version);

        LogUtils.d(SystemUtils.class, "build kernel architecture:" + Build.CPU_ABI);
        HashMap<String, Object> kernel_arch = new HashMap<String, Object>();
        kernel_arch.put(Constants.LIST_NAME, context.getString(R.string.system_kernel_arch));
        kernel_arch.put(Constants.LIST_VALUE, Build.CPU_ABI);
        systemInfo.add(kernel_arch);

        LogUtils.d(SystemUtils.class, "build kernel version:" + getFormattedKernelVersion());
        HashMap<String, Object> kernel_version = new HashMap<String, Object>();
        kernel_version.put(Constants.LIST_NAME, context.getString(R.string.system_kernel_version));
        kernel_version.put(Constants.LIST_VALUE, getFormattedKernelVersion());
        systemInfo.add(kernel_version);

        // Show screen resolution
        systemInfo.addAll(getScreenResolution(context));

        getMemInfo(context);
        return systemInfo;
    }

    public static ArrayList<HashMap<String, Object>> getMemInfo(Context context) {
        String str = "";
        ArrayList<HashMap<String, Object>> memInfo = new ArrayList<HashMap<String,Object>>();
        try {
            FileReader fr = new FileReader(MEMINFO_PATH);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            long totalMemo = 0;
            long freeMemo = 0;
            while ((str = localBufferedReader.readLine()) != null) {
                String[] item = str.split(":\\s+");
                if (item.length == 2) {
                    String name = item[0].trim();
                    String value = item[1].trim();
                    if (name.equals("MemTotal")) {
                        LogUtils.d(SystemUtils.class, "Total memory = " + value);
                        Pattern p = Pattern.compile("^\\d+");
                        Matcher m = p.matcher(value);
                        if (m.find()) {
                            totalMemo = Long.parseLong(m.group());
                        }
                        LogUtils.d(SystemUtils.class, "Total memory = " + totalMemo);
                    } else if (name.equals("MemFree")) {
                        LogUtils.d(SystemUtils.class, "Total free = " + value);
                        Pattern p = Pattern.compile("^\\d+");
                        Matcher m = p.matcher(value);
                        if (m.find()) {
                            freeMemo = Long.parseLong(m.group());
                        }
                        LogUtils.d(SystemUtils.class, "Total free = " + freeMemo);
                        break;
                    }
                }
            }
            localBufferedReader.close();
        }catch (IOException ex) {

        }
        return memInfo;
    }

    @SuppressLint("NewApi")
    private static ArrayList<HashMap<String, Object>> getScreenResolution(Context context) {
        ArrayList<HashMap<String, Object>> screenResolution = new ArrayList<HashMap<String,Object>>();
        boolean hasMenuKey = true;
        boolean hasBackKey = true;
        boolean hasHomeKey = true;
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        LogUtils.d(SystemUtils.class, "screen resolution = " + width + " x " + height);
        HashMap<String, Object> resolution = new HashMap<String, Object>();
        resolution.put(Constants.LIST_NAME, context.getString(R.string.system_screen_resolution));
        resolution.put(Constants.LIST_VALUE, width + " x " + height);
        screenResolution.add(resolution);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
            hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
            try {
                if (!hasMenuKey && !hasBackKey && !hasHomeKey) {
                    Display display = wm.getDefaultDisplay();
                    Point outSize = new Point();
                    display.getRealSize(outSize);
                    int real_width = outSize.x;
                    int real_height = outSize.y;
                    LogUtils.d(SystemUtils.class, "screen real resolution = " + real_width + " x " + real_height);
                    HashMap<String, Object> real_resolution = new HashMap<String, Object>();
                    real_resolution.put(Constants.LIST_NAME, context.getString(R.string.system_real_resolution));
                    real_resolution.put(Constants.LIST_VALUE, real_width + " x " + real_height);
                    screenResolution.add(real_resolution);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        HashMap<String, Object> screen_density = new HashMap<String, Object>();
        screen_density.put(Constants.LIST_NAME, context.getString(R.string.system_screen_density));
        screen_density.put(Constants.LIST_VALUE, dm.densityDpi + DPI);
        screenResolution.add(screen_density);

        return screenResolution;
    }

    private static String readLine(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename), 256);
        try {
            return reader.readLine();
        } finally {
            reader.close();
        }
    }

    private static String getFormattedKernelVersion() {
        String procVersionStr;
        try {
            procVersionStr = readLine("/proc/version");

            final String PROC_VERSION_REGEX =
                    "\\w+\\s+" + /* ignore: Linux */
                            "\\w+\\s+" + /* ignore: version */
                            "([^\\s]+)\\s+" + /* group 1: 2.6.22-omap1 */
                            "\\(([^\\s@]+(?:@[^\\s.]+)?)[^)]*\\)\\s+" + /* group 2: (xxxxxx@xxxxx.constant) */
                            "\\((?:[^(]*\\([^)]*\\))?[^)]*\\)\\s+" + /* ignore: (gcc ..) */
                            "([^\\s]+)\\s+" + /* group 3: #26 */
                            "(?:PREEMPT\\s+)?" + /* ignore: PREEMPT (optional) */
                            "(.+)"; /* group 4: date */

            Pattern p = Pattern.compile(PROC_VERSION_REGEX);
            Matcher m = p.matcher(procVersionStr);

            if (!m.matches()) {
                LogUtils.e(SystemUtils.class, "Regex did not match on /proc/version: " + procVersionStr);
                return "Unavailable";
            } else if (m.groupCount() < 4) {
                LogUtils.e(SystemUtils.class, "Regex match on /proc/version only returned " + m.groupCount()
                        + " groups");
                return "Unavailable";
            } else {
                return (new StringBuilder(m.group(1)).append("\n").append(
                        m.group(2)).append(" ").append(m.group(3)).append("\n")
                        .append(m.group(4))).toString();
            }
        } catch (IOException e) {
            LogUtils.error(SystemUtils.class,
                    "IO Exception when getting kernel version for Device Info screen",
                    e);

            return "Unavailable";
        }
    }
}
