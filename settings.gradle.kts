pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
        //阿里云
        maven { setUrl("https://maven.aliyun.com/nexus/content/groups/public/") }
        maven { setUrl("https://maven.aliyun.com/nexus/content/repositories/jcenter") }
        maven { setUrl("https://maven.aliyun.com/nexus/content/repositories/google") }
        maven { setUrl("https://maven.aliyun.com/nexus/content/repositories/gradle-plugin") }
    }
}

include(":app")
include(":lib_base")
include(":lib_network")
include(":lib_voice")
include(":module_app_manager")
include(":module_constellation")
include(":module_developer")
include(":module_joke")
include(":module_voice_setting")
include(":module_weather")
rootProject.name = "AiVoiceHelper"
rootProject.buildFileName = "build.gradle.kts"
