package com.zhiyangstudio.sdklibrary.common.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by zhiyang on 2018/2/14.
 */

public class IoUtils {
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeable = null;
            }
        }
    }
}
