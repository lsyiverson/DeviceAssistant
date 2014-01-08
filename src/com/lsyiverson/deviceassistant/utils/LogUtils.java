package com.lsyiverson.deviceassistant.utils;

import android.util.Log;

public class LogUtils {

    // constant log level
    public final static int VERBOSE = 0;
    public final static int DEBUG = 1;
    public final static int INFO = 2;
    public final static int WARN = 3;
    public final static int ERROR = 4;
    public final static int NONE = 5;
    private static int mLevel = NONE;

    /** The Constant TAG. */
    private static final String TAG = "com.lsyiverson.deviceassistant";

    public static void setLogLevel(int level) {
        Log.i(TAG, "LogUtil: setLogLevel to be: " + level);
        if(VERBOSE > level) {
            mLevel = VERBOSE;
        } else if(NONE <= level) {
            mLevel = NONE;
        } else{
            mLevel = level;
        }
    }

    private static String getClassName(Class<?> clazz){
        if(null == clazz) {
            return "";
        }

        return "[" + clazz.getSimpleName() + "] ";
    }

    /**
     * Verbose.
     * 
     * @param message: the message
     */
    public static void v(Class<?> clazz, String message) {
        verbose(clazz, message, null);
    }

    /**
     * Debug.
     * 
     * @param message: the message
     */
    public static void d(Class<?> clazz, String message) {
        debug(clazz, message, null);
    }

    /**
     * Info.
     * 
     * @param message: the message
     */
    public static void i(Class<?> clazz, String message) {
        info(clazz, message, null);
    }

    /**
     * Warn.
     * 
     * @param message: the message
     */
    public static void w(Class<?> clazz, String message) {
        warn(clazz, message, null);
    }

    /**
     * Error.
     * 
     * @param message: the message
     */
    public static void e(Class<?> clazz, String message) {
        error(clazz, message, null);
    }

    /**
     * Verbose.
     * 
     * @param message the message
     * @param t the t
     */
    public static void verbose(Class<?>clazz, String message, Throwable t) {
        if (VERBOSE < mLevel)
            return;

        if (message != null)
            Log.v(TAG, clazz + " Line: " + getLineNumber() + " : " + message);
        if (t != null)
            Log.v(TAG, clazz + " Line: " + getLineNumber() + " : " + t.toString());
    }

    /**
     * Debug.
     * 
     * @param message the message
     * @param t the t
     */
    public static void debug(Class<?>clazz, String message, Throwable t) {
        if (DEBUG < mLevel)
            return;

        if (message != null)
            Log.d(TAG, getClassName(clazz) + " Line: " + getLineNumber() + " : " + message);
        if (t != null)
            Log.d(TAG, getClassName(clazz) + " Line: " + getLineNumber() + " : " + t.toString());
    }

    /**
     * Info.
     * 
     * @param message the message
     * @param t the t
     */
    public static void info(Class<?>clazz, String message, Throwable t) {
        if (INFO < mLevel)
            return;
        if (message != null)
            Log.i(TAG, getClassName(clazz) + " Line: " + getLineNumber() + " : " + message);
        if (t != null)
            Log.i(TAG, getClassName(clazz) + " Line: " + getLineNumber() + " : " + t.toString());
    }

    /**
     * Warn.
     * 
     * @param message the message
     * @param t the t
     */
    public static void warn(Class<?>clazz, String message, Throwable t) {
        if (WARN < mLevel)
            return;

        if (message != null)
            Log.w(TAG, getClassName(clazz) + " Line: " + getLineNumber() + " : " + message);
        if (t != null)
            Log.w(TAG, getClassName(clazz) + " Line: " + getLineNumber() + " : " + t.toString());
    }

    /**
     * Error.
     * 
     * @param message the message
     * @param t the t
     */
    public static void error(Class<?>clazz, String message, Throwable t) {
        if (ERROR < mLevel)
            return;

        if (message != null)
            Log.e(TAG, getClassName(clazz) + " Line: " + getLineNumber() + " : " + message);
        if (t != null)
            Log.e(TAG, getClassName(clazz) + " Line: " + getLineNumber() + " : " + t.toString());
    }

    /**
     * Gets the line number.
     * 
     * @return the line number
     */
    private static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[5].getLineNumber();
    }

    public static void e(String tag, String infoString, Throwable t) {
        if (ERROR < mLevel) {
            return;
        } else {
            Log.e(TAG, tag + "->" + infoString + (t == null ? "" : t.getMessage()));
        }
    }

    public static void w(String tag, String infoString) {
        if (WARN < mLevel) {
            return;
        } else {
            Log.w(TAG, tag + "->" + infoString);
        }
    }

    public static void i(String tag, String infoString) {
        if (INFO < mLevel) {
            return;
        } else {
            Log.i(TAG, tag + "->" + infoString);
        }
    }

    public static void d(String tag, String infoString) {
        if (DEBUG < mLevel) {
            return;
        } else {
            Log.d(TAG, tag + "->" + infoString);
        }
    }

    public static void v(String tag, String infoString) {
        if (VERBOSE < mLevel) {
            return;
        } else {
            Log.v(TAG, tag + "->" + infoString);
        }
    }
}
