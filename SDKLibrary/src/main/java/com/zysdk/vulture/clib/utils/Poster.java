package com.zysdk.vulture.clib.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by zhiyang on 2018/2/14.
 * 将数据从子线程发到主线程
 */

public class Poster extends Handler {
    private static Poster mInstance;

    private Poster() {
        super(Looper.getMainLooper());
    }

    public static Poster getInstance() {
        if (mInstance == null) {
            synchronized (Poster.class) {
                if (mInstance == null) {
                    mInstance = new Poster();
                }
            }
        }
        return mInstance;
    }
}
