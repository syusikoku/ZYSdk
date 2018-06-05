package com.zysdk.vulture.clib.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

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

    public static void flush(OutputStream stream) {
        if (stream != null) {
            try {
                stream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
