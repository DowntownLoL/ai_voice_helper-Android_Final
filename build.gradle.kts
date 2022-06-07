// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    id("com.android.application") version "${KotlinConstants.gradle_version}" apply false
    id("com.android.library") version "${KotlinConstants.gradle_version}" apply false
    id("org.jetbrains.kotlin.android") version "${KotlinConstants.kotlin_version}" apply false
}

tasks {
    val clean by registering(Delete::class){
        delete(buildDir)
    }
}
