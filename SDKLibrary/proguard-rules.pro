# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
# 保留Annotation不混淆
-keepattributes *Annotation*,InnerClasses

# 避免混淆泛型
-keepattributes Signature

# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/cast,!field/*,!class/merging/*


#############################################
#
# Android开发中一些需要保留的公共部分
#
#############################################

# 保留我们使用的四大组件，自定义的Application等等这些类不被混淆
# 因为这些子类都有可能被外部调用
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService


# 保留support下的所有类及其内部类
-keep class android.support.** {*;}

# 保留继承的
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**

# 保留R下面的资源
-keep class **.R$* {*;}

# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留在Activity中的方法参数是view的方法，
# 这样以来我们在layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# webView处理，项目中没有使用到webView忽略即可
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}
-keep class android.support.design.widget.**{
    *;
}

 # EventBus 3.0
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆 eventbus配置
-keepclassmembers class * {
    void *(**Event**);
    void *(**On*Listener);
}
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# FastJson
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }
-keepattributes Signature
-keepattributes *Annotation*


## Fresco
-keep class com.facebook.fresco.** {*;}
-keep interface com.facebook.fresco.** {*;}
-keep enum com.facebook.fresco.** {*;}

# 高德相关依赖
# 集合包:3D地图3.3.2 导航1.8.0 定位2.5.0
-dontwarn com.amap.api.**
-dontwarn com.autonavi.**
-keep class com.amap.api.**{*;}
-keep class com.autonavi.**{*;}
# 地图服务
-dontwarn com.amap.api.services.**
-keep class com.map.api.services.** {*;}
# 3D地图
-dontwarn com.amap.api.mapcore.**
-dontwarn com.amap.api.maps.**
-dontwarn com.autonavi.amap.mapcore.**
-keep class com.amap.api.mapcore.**{*;}
-keep class com.amap.api.maps.**{*;}
-keep class com.autonavi.amap.mapcore.**{*;}

# 定位
-dontwarn com.amap.api.location.**
-dontwarn com.aps.**
-keep class com.amap.api.location.**{*;}
-keep class com.aps.**{*;}
#搜索
-keep class com.amap.api.services.**{*;}

# OkHttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**

#xutils定义的实体类
-keepattributes *Singature
-keep public class org.xutils.** {
    public protected *;
}
-keep public interface org.xutils.** {
    public protected *;
}
-keepclassmembers class * extends org.xutils.** {
    public protected *;
}
-keepclassmembers @org.xutils.db.annotation.* class * {*;}
-keepclassmembers @org.xutils.http.annotation.* class * {*;}
-keepclassmembers class * {
    @org.xutils.view.annotation.Event <methods>;
}
#utilCode
-keep class com.blankj.utilcode.** { *; }
-keepclassmembers class com.blankj.utilcode.** { *; }
-dontwarn com.blankj.utilcode.**
# signal
-keep class com.github.gcacace.signaturepad.**{*;}
#circleimageview
-keep class de.hdodenhof.circleimageview.**{*;}

#忽略警告
-ignorewarnings

#保证是独立的jar,没有任何项目引用,如果不写就会认为我们所有的代码是无用的,从而把所有的代码压缩掉,导出一个空的jar
-dontshrink
#保护泛型
-keepattributes Signature

#配置保留 保持不混淆的类
#-keepclasseswithmembers xx.xx.xx.**{*;}
-keep class com.zysdk.vulture.clib.utils.**{*;}
-keep class com.zysdk.vulture.clib.mvp.**{*;}
-keep class com.zysdk.vulture.clib.adapter.**{*;}
-keep class com.zysdk.vulture.clib.bean.**{*;}
-keep class com.zysdk.vulture.clib.refreshsupport.**{*;}
-keep class com.zysdk.vulture.clib.sample.**{*;}
-keep class com.zysdk.vulture.clib.svg.**{*;}
-keep class com.zysdk.vulture.clib.widget.**{*;}
-keep class com.zysdk.vulture.clib.wrapper.**{*;}
-keep class com.zysdk.vulture.clib.exception.**{*;}
-keep class com.zysdk.vulture.clib.net.callback.**{*;}
-keep class com.zysdk.vulture.clib.corel.BaseInternalHandler
-keep class com.zysdk.vulture.clib.CommonConst
#不混淆类
-keep class com.zysdk.vulture.clib.corel.BaseApp
-keep class com.zysdk.vulture.clib.corel.rx.**{*;}
-keep interface com.zysdk.vulture.clib.inter.**{*;}

#不混淆成员变量名和方法名
-keepclassmembers class com.zysdk.vulture.clib.CommonConst {
    *;
}
-keepclasseswithmembernames class com.zysdk.vulture.clib.CommonConst {
   *;
}
-keepclassmembers class com.zysdk.vulture.clib.corel.BaseApp {
    *;
}
-keepclasseswithmembernames class com.zysdk.vulture.clib.corel.BaseApp {
   *;
}

#不混淆内部类 方法和属性
#-keepnames class com.zysdk.vulture.clib.CommonConst$* {
#     *;
#}
-keepnames class com.zysdk.vulture.clib.CommonConst$* {
      public <fields>;
      public <methods>;
}

#日志信息去除 dontoptimize这个一定要取消否则会不生效
# https://m.aliyun.com/jiaocheng/20774.html
-assumenosideeffects class android.util.Log{
      public static *** d(...);
      public static *** v(...);
      public static *** i(...);
      public static *** e(...);
      public static *** w(...);
}

-assumenosideeffects class com.blankj.utilcode.util.LogUtils{
      public static *** d(...);
      public static *** v(...);
      public static *** i(...);
      public static *** e(...);
      public static *** w(...);
}

-assumenosideeffects class com.just.agentweb.LogUtils{
      public static *** d(...);
      public static *** v(...);
      public static *** i(...);
      public static *** e(...);
      public static *** w(...);
}

-assumenosideeffects class com.orhanobut.logger.Logger{
      public static *** d(...);
      public static *** v(...);
      public static *** i(...);
      public static *** e(...);
      public static *** w(...);
}

#-keep enum xx.xx.xx..**{*;}