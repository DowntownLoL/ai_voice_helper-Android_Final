/**
 * @author: Zhang
 * @description: 项目环境配置
 */

object KotlinConstants {
    const val gradle_version = "7.2.0"
    const val kotlin_version = "1.6.10"
}

object AppConfig {
    const val compileSdk = 32
    const val applicationId = "com.example.aivoicehelper"
    const val minSdk = 21
    const val targetSdk = 32
    const val versionCode = 1
    const val versionName = "1.0"
}

object DependenciesConfig {
    const val AND_MAT = "com.google.android.material:material:1.4.0"
    const val CST_LAY = "androidx.constraintlayout:constraintlayout:2.0.4"
    const val KTX_CORE = "androidx.core:core-ktx:1.7.0"
    const val APP_COMPT = "androidx.appcompat:appcompat:1.3.0"

    // EventBus 发布订阅事件
    const val EVENT_BUS = "org.greenrobot:eventbus:3.2.0"

    // ARouter 不同module之间调用
    const val AROUTER = "com.alibaba:arouter-api:1.5.2"
    const val AROUTER_COMPILER = "com.alibaba:arouter-compiler:1.5.2"

    // RecycleView
    const val RECYCLERVIEW = "androidx.recyclerview:recyclerview:1.2.1"

    // Permissions
    const val AND_PERMISSIONS = "com.yanzhenjie:permission:2.0.3"

    // Retrofit
    const val RETROFIT = "com.squareup.retrofit2:retrofit:2.8.1"
    const val RETROFIT_GSON = "com.squareup.retrofit2:converter-gson:2.8.1"

    // ViewPager
    const val VIEWPAGER = "com.zhy:magic-viewpager:1.0.1"
    const val MATERIAL = "com.google.android.material:material:1.0.0"

    // Lottie动画
    const val LOTTIE = "com.airbnb.android:lottie:3.4.0"

    // 刷新
    const val REFRESH_KERNEL = "com.scwang.smart:refresh-layout-kernel:2.0.1"
    const val REFRESH_HEADER = "com.scwang.smart:refresh-header-classics:2.0.1"
    const val REFRESH_FOOT = "com.scwang.smart:refresh-footer-classics:2.0.1"

    // 图表
    const val CHART = "com.github.PhilJay:MPAndroidChart:v3.1.0"
}

object ModuleConfig {
    var isApp = false

    const val MODULE_APP_MANAGER = "com.example.module_app_manager"
    const val MODULE_CONSTELLATION = "com.example.module_constellation"
    const val MODULE_JOKE = "com.example.module_joke"
    const val MODULE_VOICE_SETTING = "com.example.module_voice_setting"
    const val MODULE_WEATHER = "com.example.module_weather"
}