package com.zysdk.vulture.clib.corel;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.zysdk.vulture.clib.CommonConst;

/**
 * Created by zhiyang on 2018/2/9.
 */

public class BaseApp extends Application {

    protected static BaseApp mAppInstance;
    protected static Context mContext;
    protected static int mThreadId;
    protected static String mThreadName;
    protected static Handler handler;
    protected static Looper looper;
    protected static int myPid;

    public static String getMainThreadName() {
        return mThreadName;
    }

    public static int getAppProcessId() {
        return myPid;
    }

    public static int getMainThreadId() {
        return mThreadId;
    }

    public static Looper getAppLooper() {
        return looper;
    }

    public static Handler getAppHandler() {
        return handler;
    }

    public static BaseApp getAppInstance() {
        return mAppInstance;
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mAppInstance = this;
        mContext = getApplicationContext();
        handler = new Handler();
        looper = getMainLooper();
        myPid = android.os.Process.myPid();
        mThreadId = android.os.Process.myTid();
        mThreadName = Thread.currentThread().getName();

        SystemApi.get(mAppInstance).start();

        Utils.init(this);

        initLogger();
    }

    private void initLogger() {
        String logTag = getLogTag();
        CommonConst.LOG_TAG = logTag;
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag(CommonConst.LOG_TAG)   // (Optional) Global tag for every log. Default
                .build();

        if (CheckUtils.hasLog()) {
            Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
                @Override
                public boolean isLoggable(int priority, String tag) {
                    /*非商业版,是否需要打印日志*/
                    return isDebugModel();
                }
            });
        } else {
            Logger.clearLogAdapters();
        }

    }

    protected String getLogTag() {
        return CommonConst.LOG_TAG;
    }

    /**
     * 是否需要打印日志默认是需要,可以使用自己工程的Build.ISDEBUG
     */
    protected boolean isDebugModel() {
        return true;
    }
}
