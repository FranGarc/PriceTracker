plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

}
val config = PriceTrackerConfig

android {
    namespace = "com.franciscogarciagarzon.pricetracker"
    compileSdk = config.compileSdk
    defaultConfig {
        minSdk = config.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = config.javaVersion
        targetCompatibility = config.javaVersion
    }
    kotlin {
        jvmToolchain(config.jvmToolChain)
    }
}

dependencies {
    testImplementation(project(":domain"))
    testImplementation(project(":presentation"))
    testImplementation(project(":data"))
    // JUnit 5
    testImplementation(platform(libs.junit.jupiter.bom))
    testRuntimeOnly(libs.junit.jupiter.launcher)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    // ArchUnit
    testImplementation(libs.archunit)
    testImplementation(libs.assertj)
    testRuntimeOnly(libs.archunit.engine)
}

tasks.withType<Test> {
    useJUnitPlatform()
}