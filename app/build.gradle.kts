import org.jetbrains.kotlin.gradle.utils.`is`

plugins {
    id("com.android.application")
//    id("org.jetbrains.kotlin.android")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdk = AppConfig.compileSdk

//    buildFeatures {
//        viewBinding = true
//    }

    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        // ARouter
        kapt {
            arguments {
                arg("AROUTER_MODULE_NAME", project.name)
            }
        }

    }

    // 签名类型
    signingConfigs {
        register("release") {
            keyAlias = "AiVoiceHelper"
            keyPassword = "000915"
            storeFile = file("AiVoiceHelper")
            storePassword = "000915"
        }
    }

    // 输出类型
    android.applicationVariants.all {
        val buildType = this.buildType.name
        outputs.all {
            // 输出APK
            if(this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                this.outputFileName = "AiVoiceHelper_V${defaultConfig.versionName}_$buildType.apk"
            }
        }
    }

    buildTypes {
        getByName("debug") {

        }

        getByName("release") {
            isMinifyEnabled = false

            // 自动打包
            signingConfig = signingConfigs.getByName("release")
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
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":lib_base"))

    // 如果不是App
    if(!ModuleConfig.isApp) {
        implementation(project(":module_app_manager"))
        implementation(project(":module_constellation"))
        implementation(project(":module_developer"))
        implementation(project(":module_joke"))
        implementation(project(":module_map"))
        implementation(project(":module_setting"))
        implementation(project(":module_voice_setting"))
        implementation(project(":module_weather"))
    }

    // 运行时注解
    kapt(DependenciesConfig.AROUTER_COMPILER)
}