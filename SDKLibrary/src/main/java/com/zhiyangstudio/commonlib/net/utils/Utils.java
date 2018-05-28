package com.zhiyangstudio.commonlib.net.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.zhiyangstudio.commonlib.utils.LoggerUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

public class Utils {
    private static final String TAG = "GizWifiSDKClient-Utils";
    private static Integer sn = null;

    Utils() {
    }

    protected static String getMethodName() {
        String clazz = Thread.currentThread().getStackTrace()[1].getClassName();
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        System.out.println("class name: " + clazz + " Method Name " + method);
        return method;
    }

    protected static List<String> needApkPermissions(Context context) {
        List<String> needPermissions = new ArrayList();
        ArrayList permissions = new ArrayList();

        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            String pkgName = pi.packageName;
            @SuppressLint("WrongConstant") PackageInfo pkgInfos = pm.getPackageInfo(pkgName, 4096);
            if (pkgInfos == null) {
                permissions.add("PackageInfo is NULL");
                return permissions;
            }

            String[] sharedPkgList = pkgInfos.requestedPermissions;
            if (sharedPkgList != null) {
                for (int i = 0; i < sharedPkgList.length; ++i) {
                    String permName = sharedPkgList[i];
                    permissions.add(permName);
                }
            }

            if (!permissions.contains("android.permission.INTERNET")) {
                needPermissions.add("android.permission.INTERNET");
            }

            if (!permissions.contains("android.permission.WRITE_EXTERNAL_STORAGE")) {
                needPermissions.add("android.permission.WRITE_EXTERNAL_STORAGE");
            }

            if (!permissions.contains("android.permission.ACCESS_WIFI_STATE")) {
                needPermissions.add("android.permission.ACCESS_WIFI_STATE");
            }

            if (!permissions.contains("android.permission.ACCESS_NETWORK_STATE")) {
                needPermissions.add("android.permission.ACCESS_NETWORK_STATE");
            }

            if (!permissions.contains("android.permission.CHANGE_WIFI_STATE")) {
                needPermissions.add("android.permission.CHANGE_WIFI_STATE");
            }
        } catch (PackageManager.NameNotFoundException var10) {
            LoggerUtils.loge("Retrieve package name exception " + var10);
        }

        return needPermissions;
    }

    protected static boolean isApkBackground(Context context) {
        boolean isBackground = false;

        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            String pkgName = pi.packageName;
            @SuppressLint("WrongConstant") ActivityManager activityManager = (ActivityManager)
                    context.getSystemService
                    ("activity");
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                    .getRunningAppProcesses();
            if (appProcesses != null) {
                Iterator var8 = appProcesses.iterator();

                while (var8.hasNext()) {
                    ActivityManager.RunningAppProcessInfo appProcess = (ActivityManager
                            .RunningAppProcessInfo) var8.next();
                    if (appProcess.processName.equals(pkgName)) {
                        if (appProcess.importance == 400) {
                            isBackground = true;
                        } else {
                            isBackground = false;
                        }
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException var9) {
            LoggerUtils.loge("Retrieve package name exception " + var9);
        }

        LoggerUtils.loge("is   background  :" + isBackground);
        return isBackground;
    }

    protected static boolean isWifiDisabled(Context context) {
        boolean isDisabled = false;
        @SuppressLint("WrongConstant") ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService
                ("connectivity");
        if (connectivity != null) {
            NetworkInfo[] infos = connectivity.getAllNetworkInfo();
            if (infos != null) {
                NetworkInfo[] var7 = infos;
                int var6 = infos.length;

                for (int var5 = 0; var5 < var6; ++var5) {
                    NetworkInfo ni = var7[var5];
                    if (ni.getTypeName().equals("WIFI") && ni.isConnected()) {
                        isDisabled = true;
                    }
                }
            }
        }

        return isDisabled;
    }

    protected static boolean isNetFree(Context context) {
        boolean isFree = false;
        @SuppressLint("WrongConstant") ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService
                ("connectivity");
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.getState() == NetworkInfo.State.CONNECTED && info.getType()
                    == 1) {
                @SuppressLint("WrongConstant") WifiManager wifiManager = (WifiManager) context
                        .getSystemService("wifi");
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String wifiSSID = wifiInfo.getSSID();
                if (!TextUtils.isEmpty(wifiSSID) && !wifiSSID.equals("CMCC") && !wifiSSID.equals
                        ("ChinaNet") && !wifiSSID.equals("ChinaUnicom")) {
                    isFree = true;
                }
            }
        }

        return isFree;
    }

    protected static boolean isInternetReachable(Context context) {
        boolean reachabled = false;

        try {
            String ip = "www.baidu.com";
            Process p = Runtime.getRuntime().exec("ping -c 3 -w 10 " + ip);
            int status = p.waitFor();
            if (status == 0) {
                reachabled = true;
            }
        } catch (Exception var5) {
            LoggerUtils.loge("ping www.baidu.com exception: " + var5);
        }

        return reachabled;
    }

    protected static String findDaemonInApk(Context context, String filename) {
        String daemonFileName = null;
        File filesDir = context.getFilesDir();
        File[] daemonFiles = filesDir.listFiles();
        String[] list = filesDir.list();
        if (daemonFiles != null && daemonFiles.length > 0) {
            LoggerUtils.loge("apk has daemon file");

            try {
                boolean hasValidDaemonfile = false;
                File[] var10 = daemonFiles;
                int var9 = daemonFiles.length;

                for (int var8 = 0; var8 < var9; ++var8) {
                    File daemonFile = var10[var8];
                    String daemonName = daemonFile.getName();
                    if (!TextUtils.isEmpty(daemonName) && !"GizWifiSDKDaemon".equals(daemonName)
                            && daemonName.length() > "GizWifiSDKDaemon".length()) {
                        hasValidDaemonfile = true;
                        daemonFileName = daemonName;
                        String daemonVersion = daemonName.substring("GizWifiSDKDaemon".length());
                        LoggerUtils.loge("daemoon version is " + daemonVersion);
                        break;
                    }

                    LoggerUtils.loge("daemon file is " + daemonName + ", but no version");
                }
            } catch (Exception var13) {
                var13.printStackTrace();
            }
        }

        return daemonFileName;
    }

    protected static String getLanguage(Context mContext) {
        Locale locale = mContext.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        return language.endsWith("zh") ? "zh-cn" : "en";
    }

    protected static String getWifiSSID(Context mContext) {
        @SuppressLint("WrongConstant") WifiManager wifiManager = (WifiManager) mContext
                .getSystemService("wifi");
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String wifissid = "";
        if (wifiInfo != null) {
            wifissid = wifiInfo.getSSID();
            if (wifissid != null && wifissid.length() >= 2) {
                StringBuffer buffer = new StringBuffer(wifissid);
                wifissid = buffer.substring(1, wifissid.length() - 1);
            }
        }

        return wifissid;
    }

    protected static boolean isNetAvailable(Context context) {
        @SuppressLint("WrongConstant") ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService
                ("connectivity");
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }


    public static String getApplicationName(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;

        try {
            packageManager = context.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException var4) {
            applicationInfo = null;
        }

        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception var4) {
            var4.printStackTrace();
            return "";
        }
    }

    public static Utils.WifiCipherType getCipherType(Context context, String ssid) {
        @SuppressLint("WrongConstant") WifiManager wifiManager = (WifiManager) context
                .getSystemService("wifi");
        return Utils.WifiCipherType.WIFICIPHER_INVALID;
    }

    public static String readFileContentStr(String fullFilename) {
        String readOutStr = null;

        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(fullFilename));

            try {
                long len = (new File(fullFilename)).length();
                if (len > 2147483647L) {
                    throw new IOException("File " + fullFilename + " too large, was " + len + " " +
                            "bytes.");
                }

                byte[] bytes = new byte[(int) len];
                dis.readFully(bytes);
                readOutStr = new String(bytes, "UTF-8");
            } finally {
                dis.close();
            }

            LoggerUtils.loge("Successfully to read out string from file " + fullFilename);
        } catch (IOException var10) {
            readOutStr = null;
            LoggerUtils.loge("Fail to read out string from file " + fullFilename + "errormessage " +
                    ": " + var10);
        }

        return readOutStr;
    }

    public static boolean ping() {
        String result = null;

        try {
            String ip = "114.114.114.114";
            Process p = Runtime.getRuntime().exec("ping -c 3 -w 10 " + ip);
            int status = p.waitFor();
            if (status == 0) {
                result = "successful~";
                return true;
            } else {
                result = "failed~ cannot reach the IP address";
                return false;
            }
        } catch (IOException var4) {
            result = "failed~ IOException";
            return false;
        } catch (InterruptedException var5) {
            result = "failed~ InterruptedException";
            return false;
        }
    }

    protected static boolean isWifi(Context mContext) {
        @SuppressLint("WrongConstant") ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService
                        ("connectivity");
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == 1;
    }

    protected static List<String> removeDuplicatePK(List<String> originalList) {
        List<String> filteredList = new ArrayList();
        if (originalList == null) {
            return filteredList;
        } else {
            Iterator var3 = originalList.iterator();

            while (var3.hasNext()) {
                String item = (String) var3.next();
                boolean isExist = false;
                Iterator var6 = filteredList.iterator();

                while (var6.hasNext()) {
                    String filteredItem = (String) var6.next();
                    if (item.equals(filteredItem)) {
                        isExist = true;
                        break;
                    }
                }

                if (!isExist) {
                    filteredList.add(item);
                }
            }

            return filteredList;
        }
    }


    public static String changeString(String ss) {
        return "\"" + ss + "\"";
    }

    public static String isEmpty(String ss) {
        return TextUtils.isEmpty(ss) ? "null" : "******";
    }

    public static List<ConcurrentHashMap<String, Object>> getList(JSONArray array) {
        try {
            List<ConcurrentHashMap<String, Object>> list = new ArrayList();

            for (int i = 0; i < array.length(); ++i) {
                JSONObject obj = (JSONObject) array.get(i);
                ConcurrentHashMap<String, Object> map = new ConcurrentHashMap();
                ConcurrentHashMap<String, Object> attrmap = new ConcurrentHashMap();
                map.put("did", obj.getString("did"));
                map.put("product_key", obj.getString("product_key"));
                JSONObject attrsobj = obj.getJSONObject("attrs");
                Iterator it_params = attrsobj.keys();

                while (it_params.hasNext()) {
                    String param = it_params.next().toString();
                    Object value = attrsobj.get(param);
                    attrmap.put(param, value);
                }

                map.put("attrs", attrmap);
                list.add(map);
            }

            return list;
        } catch (JSONException var10) {
            var10.printStackTrace();
            return null;
        }
    }

    public static String getmils() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String str = formatter.format(new Date());
        return str;
    }

    public static boolean isX86() {
        try {
            Process process = Runtime.getRuntime().exec("getprop");
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString().contains("x86");
        } catch (IOException var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public static String GetNetworkType(Context context) {
        String strNetworkType = "";
        @SuppressLint("WrongConstant") ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService("connectivity");
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == 1) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == 0) {
                String _strSubTypeName = networkInfo.getSubtypeName();
                LoggerUtils.loge("Network getSubtypeName : " + _strSubTypeName);
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case 1:
                    case 2:
                    case 4:
                    case 7:
                    case 11:
                        strNetworkType = "2G";
                        break;
                    case 3:
                    case 5:
                    case 6:
                    case 8:
                    case 9:
                    case 10:
                    case 12:
                    case 14:
                    case 15:
                        strNetworkType = "3G";
                        break;
                    case 13:
                        strNetworkType = "4G";
                        break;
                    default:
                        if (!_strSubTypeName.equalsIgnoreCase("TD-SCDMA") && !_strSubTypeName
                                .equalsIgnoreCase("WCDMA") && !_strSubTypeName.equalsIgnoreCase
                                ("CDMA2000")) {
                            strNetworkType = _strSubTypeName;
                        } else {
                            strNetworkType = "3G";
                        }
                }
            }
        }

        return strNetworkType;
    }


    public static JSONObject getJsonString() {
        try {
            TimeZone tz = TimeZone.getDefault();
            String id = tz.getID();
            int sn2 = getSn();
            JSONObject obj = new JSONObject();
            obj.put("cmd", 1105);
            obj.put("sn", sn2);
            obj.put("tzCity", id);
            return obj;
        } catch (JSONException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static int getSn() {
        if (sn == null) {
            sn = Integer.valueOf(0);
        } else {
            sn = Integer.valueOf(sn.intValue() + 1);
        }

        return sn.intValue();
    }

    public static JSONObject getTaskJsonObject(ConcurrentHashMap<String, Object> attrs) throws
            JSONException {
        JSONObject obj = null;
        if (attrs != null) {
            Iterator var3 = attrs.keySet().iterator();

            while (var3.hasNext()) {
                String key = (String) var3.next();
                obj = new JSONObject();
                obj.put(key, attrs.get(key));
            }
        }

        return obj;
    }

    public static Bitmap createQRImage(String url, int width, int height) {
        /*try {
            if (url != null && !"".equals(url) && url.length() >= 1) {
                Hashtable<EncodeHintType, String> hints = new Hashtable();
                hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
                BitMatrix bitMatrix = (new QRCodeWriter()).encode(url, BarcodeFormat.QR_CODE,
                        width, height, hints);
                int[] pixels = new int[width * height];

                for (int y = 0; y < height; ++y) {
                    for (int x = 0; x < width; ++x) {
                        if (bitMatrix.get(x, y)) {
                            pixels[y * width + x] = -16777216;
                        } else {
                            pixels[y * width + x] = -1;
                        }
                    }
                }

                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
                return bitmap;
            } else {
                return null;
            }
        } catch (WriterException var8) {
            var8.printStackTrace();
            return null;
        }*/

        return null;
    }

    private static enum WifiCipherType {
        WIFICIPHER_WEP,
        WIFICIPHER_WPA,
        WIFICIPHER_NOPASS,
        WIFICIPHER_INVALID;

        private WifiCipherType() {
        }
    }

    private static class DaemonFilter implements FilenameFilter {
        private DaemonFilter() {
        }

        public boolean accept(File dir, String filename) {
            return this.isDaemon(filename);
        }

        public boolean isDaemon(String filename) {
            return TextUtils.isEmpty(filename) ? false : filename.contains("GizWifiSDKDaemon");
        }
    }
}