plugins {
    if (ModuleConfig.isApp) {
        id("com.android.application")
    } else {
        id("com.android.library")
    }
//    id("org.jetbrains.kotlin.android")
    kotlin("android")
//    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdk =  AppConfig.compileSdk

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        if (ModuleConfig.isApp) {
//            applicationId = ModuleConfig.MODULE_MAP
        }
//        versionCode = AppConfig.versionCode
//        versionName = AppConfig.versionName
        // ARouter
        kapt {
            arguments {
                arg("AROUTER_MODULE_NAME", project.name)
            }
        }
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
    // 动态替换资源
    sourceSets {
        getByName("main") {
            if(ModuleConfig.isApp) {
                manifest.srcFile("src/main/manifest/AndroidManifest.xml")
            } else {
                manifest.srcFile("src/main/AndroidManifest.xml")
            }
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":lib_base"))
    // 运行时注解
    kapt(DependenciesConfig.AROUTER_COMPILER)
}