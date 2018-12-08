编译出现版本不一致的时候

project->build.gradle

allprojects {
    //解决版本不一致的问题
    project.configurations.all {
        resolutionStrategy.eachDependency { details ->
            if (details.requested.group == 'com.android.support'
                    && !details.requested.name.contains('multidex') ) {
                details.useVersion "27.1.1"
            }
        }
    }
}