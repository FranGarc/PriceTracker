plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.junit5)
}
val config = PriceTrackerConfig
android {
    namespace = "com.franciscogarciagarzon.pricetracker.data"
    compileSdk = config.compileSdk

    defaultConfig {
        minSdk = config.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = config.javaVersion
        targetCompatibility = config.javaVersion
    }
    testOptions {
        unitTests.all {
            // Forces Gradle to use the JUnit 5 platform (Jupiter) for all unit tests
            it.useJUnitPlatform()
        }
    }
    kotlinOptions {
        jvmTarget = config.jvmTarget
    }
}


dependencies {
    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

//    androidTestImplementation(libs.androidx.junit)
    // JUnit 5 Dependencies
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.assertj)

    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.assertj)
    testImplementation(libs.mockito.junit5)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)

    testImplementation(libs.instancio.core)
    testImplementation(libs.instancio.junit)
    testImplementation(kotlin("test"))
}