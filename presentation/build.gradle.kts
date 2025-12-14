plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.junit5)
    id("jacoco")
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}
val config = PriceTrackerConfig

android {

    namespace = "com.franciscogarciagarzon.pricetracker.presentation"
    compileSdk = config.compileSdk

    defaultConfig {
        minSdk = config.minSdk

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "com.franciscogarciagarzon.pricetracker.presentation.HiltTestRunner"
        consumerProguardFiles("consumer-rules.pro")
    }


    testCoverage {
        jacocoVersion = "0.8.12" // Match your data module version
    }
    buildTypes {
        debug {
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = config.javaVersion
        targetCompatibility = config.javaVersion
    }
    buildFeatures {
        compose = true
    }
    testOptions {
        unitTests.all {
            // Forces Gradle to use the JUnit 5 platform (Jupiter) for all unit tests
            it.useJUnitPlatform()
        }
        testOptions.unitTests.isReturnDefaultValues = true
    }
    kotlin {
        jvmToolchain(config.jvmToolChain)
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":commons"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)

    //dependency injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    testImplementation(libs.hilt.android.testing)
    kspTest(libs.dagger.compiler)

    // hilt viewmodel + navigation
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    testImplementation(platform(libs.junit.jupiter.bom))


    testImplementation(libs.junit.jupiter.aggregator)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.jupiter.launcher)
    testImplementation(libs.junit.jupiter.params)

    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.assertj)
    testImplementation(libs.mockito.junit5)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)

    testImplementation(libs.instancio.core)
    testImplementation(libs.instancio.junit)

    androidTestImplementation(libs.io.cucumber.android)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation(kotlin("test"))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.ui.test.manifest)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)
    androidTestImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.androidx.junit)

    androidTestImplementation(libs.mockito.android) {
        exclude(group = "net.bytebuddy", module = "byte-buddy")
        exclude(group = "net.bytebuddy", module = "byte-buddy-agent")
    }
    androidTestImplementation(libs.mockito.core) {
        exclude(group = "net.bytebuddy", module = "byte-buddy")
    }
    androidTestImplementation(libs.bytebuddy)

}

// Aggressive dependency resolution to prevent Byte Buddy
configurations.all {
    resolutionStrategy {
        eachDependency {
            // Force exclude Byte Buddy from all dependencies
            if (requested.group == "net.bytebuddy") {
                useVersion(libs.versions.bytebuddy.get()) // Use a compatible version if absolutely needed
            }
        }

        // Fail fast if any Byte Buddy dependency slips through
        failOnVersionConflict()

        // Prefer Android-compatible dependencies
        preferProjectModules()
    }
}

tasks.register("generateAllCoverageReports") {
    group = "verification"
    description = "Generates all test coverage reports"

    dependsOn(
        "testDebugUnitTest",
        "connectedDebugAndroidTest"
    )

    doLast {
        println("All coverage reports generated:")
        println("  - Unit tests: presentation/build/reports/coverage/debug/")
        println("  - Android tests: presentation/build/reports/androidTests/connected/")
        println("  - Combined HTML: presentation/build/reports/coverage/debug/index.html")

        // Show the report location
        val reportFile = file("${layout.buildDirectory.get()}/reports/coverage/debug/index.html")
        if (reportFile.exists()) {
            println("Coverage report: file://${reportFile.absolutePath}")
        }
    }
}

// Platform-independent way to suggest opening the report
tasks.register("showCoverageReportPath") {
    group = "verification"
    description = "Shows the path to the coverage report"

    doLast {
        val reportFile = file("${layout.buildDirectory.get()}/reports/coverage/debug/index.html")
        if (reportFile.exists()) {
            println("📊 Coverage report generated at:")
            println("file://${reportFile.absolutePath}")
            println("")
            println("To view the report, open this path in your browser:")
            println("file://${reportFile.absolutePath}")
        } else {
            println("Coverage report not found. Run 'generateAllCoverageReports' first.")
        }
    }
}