package com.zysdk.vulture.clib.utils;

public class ByteTool {
    public ByteTool() {
    }

    public static byte[] copyOfRange(byte[] data, int start, int end) {
        if (data != null && start <= end && data.length >= end) {
            byte[] dest = new byte[end - start];

            for (int i = 0; i < dest.length; ++i) {
                dest[i] = data[start + i];
            }

            return dest;
        } else {
            return null;
        }
    }

    public static String bytes2HexString(byte[] data) {
        String ret = "";

        for (int i = 0; i < data.length; ++i) {
            String hex = Integer.toHexString(data[i] & 255);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }

            ret = ret + hex.toUpperCase();
        }

        return ret;
    }

    public static String Byte2StringWithSpace(byte[] data) {
        String ret = "";

        for (int i = 0; i < data.length; ++i) {
            String hex = Integer.toHexString(data[i] & 255);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }

            ret = ret + hex.toUpperCase();
            ret = ret + " ";
        }

        return ret;
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString != null && !hexString.equals("")) {
            hexString = hexString.toUpperCase();
            int length = hexString.length() / 2;
            char[] hexChars = hexString.toCharArray();
            byte[] d = new byte[length];

            for (int i = 0; i < length; ++i) {
                int pos = i * 2;
                d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
            }

            return d;
        } else {
            return null;
        }
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static int Byte2Int(byte[] bytes) {
        int s = 0;

        for (int i = 0; i < bytes.length; ++i) {
            s |= (bytes[i] & 255) << (bytes.length - i - 1) * 8;
        }

        return s;
    }

    public static byte[] Int2Byte(int x) {
        byte[] b = new byte[]{(byte) ((x & '\uff00') >> 8), (byte) (x & 255)};
        return b;
    }

    public static void sleep(int times) {
        try {
            Thread.sleep((long) (1000 * times));
        } catch (InterruptedException var2) {
            var2.printStackTrace();
        }

    }
}
