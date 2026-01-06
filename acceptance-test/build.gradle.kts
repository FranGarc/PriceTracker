plugins {
    id("com.android.test")
    alias(libs.plugins.kotlin.android)
}
val config = PriceTrackerConfig

android {
    namespace = "com.franciscogarciagarzon.acceptance_test"
    compileSdk = config.compileSdk

    this.targetProjectPath = ":app"

    defaultConfig {
        minSdk = config.minSdk
        targetSdk = config.targetSdk
        testInstrumentationRunner = "com.franciscogarciagarzon.acceptance_test.test.CucumberTestRunner"
    }


    buildTypes {
        debug {
        }
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
    implementation(project(":presentation"))
    implementation(project(":data"))

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.gson)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.junit)

    implementation(libs.cucumber.java)
    implementation(libs.cucumber.android)
    implementation(libs.cucumber.picocontainer)
    implementation(libs.cucumber.cucumber.junit)

    implementation(libs.androidx.test.runner)
    implementation(libs.androidx.test.core)
    implementation(libs.androidx.test.core.ktx)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui.test.junit4)
    implementation(libs.androidx.compose.ui.test)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.androidx.uiautomator)
    implementation(libs.androidx.rules.v160)
}