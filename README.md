# ZYMobildSdk
移动Android端快速开发sdk

## sdk介绍
### 1.常用工具类封装
### 2.android系统公用adapter封装,(Listview|gridview|recyclerview),viewholder封装
### 3.baseactivity,basepresenteractivity,baseabslistactivity封装(沉浸式状态栏支持)基于mvp模式
### 4.baseacfragment,basepresenterfragment,baseabslistfragment封装(懒加载支持)
### 5.smartrefresh+listview,smartrefresh+recyclerview(loading效果)支持封装(兼容mvp模式) 

                技术,就是要不断的革新，在不同的领域中，谋求发展，与时俱进!
                它山之石可以攻城


## 框架使用

 allprojects {
   repositories {
    ...
    maven { url 'https://jitpack.io' }
   }
  }

  dependencies {
          implementation 'com.github.syusikoku:ZYSdk:v1.0.11'
  }



Error:(138, 43) java: 找不到符号
  符号:   方法 metafactory(java.lang.invoke.MethodHandles.Lookup,java.lang.String,java.lang.invoke.MethodType,java.lang.invoke.MethodType,java.lang.invoke.MethodHandle,java.lang.invoke.MethodType)
  位置: 接口 java.lang.invoke.LambdaMetafactory

