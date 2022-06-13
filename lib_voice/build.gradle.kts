plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("android.extensions")
}

android {
    compileSdk =  AppConfig.compileSdk

    defaultConfig {
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
//        versionCode = AppConfig.versionCode
//        versionName = AppConfig.versionName
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

//    repositories{
//        flatDir{
//            dirs("libs")
//        }
//    }
}

dependencies {
    api(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    // 百度语音识别库
    api(files("libs/baiduasr.jar"))
    // 百度语音合成库
    api(files("libs/baidutts.jar"))
}