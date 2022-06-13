plugins {
    id("com.android.library")
//    id("org.jetbrains.kotlin.android")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdk =  AppConfig.compileSdk

//    buildFeatures {
//        viewBinding = true
//    }

    defaultConfig {
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
//        versionCode = AppConfig.versionCode
//        versionName = AppConfig.versionName
        // ARouter
        kapt {
            arguments {
                arg("AROUTER_MODULE_NAME", project.getName())
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
}

dependencies {
    api(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    api(DependenciesConfig.AND_MAT)
    api(DependenciesConfig.CST_LAY)
    api(DependenciesConfig.KTX_CORE)
    api(DependenciesConfig.APP_COMPT)

    // EventBus
    api(DependenciesConfig.EVENT_BUS)
    // ARouter
    api(DependenciesConfig.AROUTER)
    // ARouter运行时注解
    kapt(DependenciesConfig.AROUTER_COMPILER)
    // RecyclerView
    api(DependenciesConfig.RECYCLERVIEW)
    // AndPermissions
    api(DependenciesConfig.AND_PERMISSIONS)

    api(project(":lib_voice"))
    api(project(":lib_network"))
}