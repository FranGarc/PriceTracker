// buildSrc/build.gradle.kts
plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // this module can't access the versions catalog
//    implementation("com.android.tools.build:gradle:8.10.1")
//    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.0")
}