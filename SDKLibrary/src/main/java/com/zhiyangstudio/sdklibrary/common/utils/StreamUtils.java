package com.zhiyangstudio.sdklibrary.common.utils;

import com.orhanobut.logger.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by zhiyang on 2018/2/14.
 */

public class StreamUtils {

    public static String convertStr4Is1(InputStream inputStream) {
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 从流中读取下一个字节
        int c;
        try {
            c = bis.read();
            while (c != -1) {
                baos.write(c);
                c = bis.read();
            }
            bis.close();
            String result = new String(baos.toByteArray());
            Logger.e("result = " + result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtils.close(inputStream);
            IoUtils.close(bis);
            IoUtils.close(baos);
        }
        return "";
    }

    public static String convertStr4Is2(InputStream inputStream) {
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        StringBuffer sb = new StringBuffer();
        int len = -1;
        byte[] buffer = new byte[1024 * 4];
        try {
            while ((len = bis.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, len));
            }
            Logger.e("result = " + sb.toString());
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtils.close(inputStream);
            IoUtils.close(bis);
        }
        return sb.toString();
    }

    /**
     * 验证-
     * 普通的读取方式
     *
     * @param inputStream
     */
    public static String convertStr4Is3(InputStream inputStream) {
        StringBuffer sb = new StringBuffer();
        int len = -1;
        byte[] buffer = new byte[1024 * 4];
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, len));
            }
            Logger.e("result = " + sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtils.close(inputStream);
        }
        return sb.toString();
    }

    /**
     * 验证-
     *
     * @param inputStream
     */
    public static String convertStr4Is4(InputStream inputStream) {
        StringBuffer sb = new StringBuffer();
        // 将字节流转换成字符流才能使用reader
        InputStreamReader isr = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            Logger.e("result = " + sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtils.close(inputStream);
            IoUtils.close(br);
            IoUtils.close(isr);
        }
        return sb.toString();
    }

    public static String convertStr4Is5(InputStream inputStream) {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            String result = new String(baos.toByteArray());
            Logger.e("result = " + result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtils.close(inputStream);
            IoUtils.close(baos);
        }
        return "";
    }

}
