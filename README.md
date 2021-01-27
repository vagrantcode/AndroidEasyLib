# AndroidEasyLib
Create a easy library for rapid project establishment
使用说明：
1，在安卓项目的gradle的allprojects添加引用格式如下：

allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }

2，在需要使用的模块添加需要的版本：

dependencies {
            implementation 'com.github.vagrantcode:AndroidEasyLib:版本号'
    }
