资料:
https://www.jianshu.com/p/e8b41382a7c2

PS: 这些配置是为了打包 *.jar 包配置的，若只要导出 *.aar 包，则无需这些配置。

在 mylibrary 目录下的 build.gradle 中添加如下代码：

task makeJar(type: proguard.gradle.ProGuardTask, dependsOn: "build") {
    injars 'build/intermediates/bundles/default/classes.jar' // 未混淆的jar路径
    outjars 'build/outputs/mylibrary-1.0.0.jar' // 混淆后的jar输出路径
    configuration 'proguard-rules.pro' // 混淆协议
}

task clearJar(type: Delete) {
    //这行表示如果你已经打过一次包了，再进行打包则把原来的包删掉
    delete 'build/libs/mylibrary-1.0.0.jar'
}

task makeJar(type: Copy) {
    from('build/intermediates/bundles/default/') //这行表示要打包的文件的路径，根据下面的内容，其实是该路径下的classes.jar
    into('build/libs/')  //这行表示打包完毕后包的生成路径，也就是生成的包存在哪
    include('classes.jar')  //看到这行，如果你对分包有了解的话，你就可以看出来这行它只是将一些类打包了
    rename ('classes.jar', 'mylibrary-1.0.0.jar')
}

makeJar.dependsOn(clearJar, build)


// 这是需要添加的
allprojects {
    repositories {
        flatDir {
            dirs 'libs'
        }
    }
}

dependencies {
    ...

    // 添加这一行
    compile(name: 'mylibrary-1.0.0', ext: 'aar')
}


aar生成

 gradle makeJar aarRelease
