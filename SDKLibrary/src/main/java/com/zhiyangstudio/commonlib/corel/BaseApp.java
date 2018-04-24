package com.zhiyangstudio.commonlib.corel;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;

import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.zhiyangstudio.commonlib.CommonConst;

/**
 * Created by zhiyang on 2018/2/9.
 */

public class BaseApp extends Application {

    private static BaseApp mAppInstance;
    private static Context mContext;
    private static int mThreadId;
    private static String mThreadName;
    private static Handler handler;
    private static Looper looper;
    private static int myPid;
    private int mScreenWidth, mScreenHeight;
    private Bitmap mCameraBitmap;

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

        DisplayMetrics mDisplayMetrics = getApplicationContext().getResources()
                .getDisplayMetrics();
        mScreenWidth = mDisplayMetrics.widthPixels;
        mScreenHeight = mDisplayMetrics.heightPixels;
        // TODO: 2018/3/14 Utils init
        Utils.init(this);

        initLogger();

//        CrashReport.initCrashReport(this);
    }

    private void initLogger() {
        // TODO: 2018/2/2 logger高级用法
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag(getLogTag())   // (Optional) Global tag for every log. Default
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                /*是否需要打印日志*/
                return isDebugModel();
            }
        });
    }

    /**
     * 是否需要打印日志默认是需要,可以使用自己工程的Build.ISDEBUG
     * @return
     */
    protected boolean isDebugModel() {
        return true;
    }

    protected String getLogTag() {
        return CommonConst.LOG_TAG;
    }

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

    public Bitmap getCameraBitmap() {
        return mCameraBitmap;
    }

    public int getScreenWidth() {
        return mScreenWidth;
    }

    public int getScreenHeight() {
        return mScreenHeight;
    }

    public void setCameraBitmap(Bitmap mCameraBitmap) {
        if (mCameraBitmap != null) {
            recycleCameraBitmap();
        }
        this.mCameraBitmap = mCameraBitmap;
    }

    public void recycleCameraBitmap() {
        if (mCameraBitmap != null) {
            if (!mCameraBitmap.isRecycled()) {
                mCameraBitmap.recycle();
            }
            mCameraBitmap = null;
        }
    }
}
