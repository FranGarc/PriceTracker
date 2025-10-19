plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.junit5)
}

// Explicitly exclude the JUnit 4 Vintage Engine from all configurations.
// This prevents transitive dependencies (like Mockito or Cucumber) from forcing JUnit 4.
configurations.all {
    resolutionStrategy {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}


val config = PriceTrackerConfig
android {
    namespace = "com.franciscogarciagarzon.pricetracker"
    compileSdk = config.compileSdk

    defaultConfig {
        applicationId = "com.franciscogarciagarzon.pricetracker"
        minSdk = config.minSdk
        targetSdk = config.targetSdk
        versionCode = 1
        versionName = "1.0"

//        testInstrumentationRunner = "com.franciscogarciagarzon.pricetracker.CucumberTestInstrumentation"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

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
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":presentation"))

    testImplementation(files("src/test/resources"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    //dependency injection
//    implementation(libs.hilt.android)
//    ksp(libs.hilt.compiler)

    // JUnit 5 Dependencies
//    testImplementation(libs.junit.jupiter.aggregator)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.assertj)


    // ArchUnit Dependencies
    testImplementation(libs.archunit)

    // Cucumber/Android Test Dependencies
    androidTestImplementation(libs.io.cucumber.android)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register("checkMockitoConfig") {
    doLast {
        val configFile = file("src/test/resources/mockito-extensions/org.mockito.plugins.MockMaker")
        println("Config file exists: ${configFile.exists()}")
        println("Config file path: ${configFile.absolutePath}")
        if (configFile.exists()) {
            println("Config file content: '${configFile.readText()}'")
        }
    }
}
tasks.register("verifyTestResources") {
    doLast {
        val buildResourcesDir = file("build/resources/test/mockito-extensions")
        println("Build resources dir exists: ${buildResourcesDir.exists()}")
        if (buildResourcesDir.exists()) {
            println("Files in build resources:")
            buildResourcesDir.listFiles()?.forEach { file ->
                println(" - ${file.name}")
            }
        }
    }
}
tasks.register("copyTestResources", Copy::class) {
    from("src/test/resources")
    into("build/resources/test")
}

// Make test tasks depend on the copy task
tasks.withType<Test> {
    dependsOn("copyTestResources")
    useJUnitPlatform()
}