package com.zhiyangstudio.commonlib.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zzg on 2018/4/25.
 */

public class FileUtils {
    public static String getContentByAssets(String fileName) {
        String content = "";
        InputStream inputStream = null;
        try {
            inputStream = UiUtils.getContext().getAssets().open(fileName);
            content = StreamUtils.convertStr4Is4(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtils.close(inputStream);
        }
        return content;
    }
}
