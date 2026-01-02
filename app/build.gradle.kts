plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.junit5)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
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

            it.jvmArgs(
                // 1. Fuerza el uso del motor de mocking en línea (inline)
                // Esto instruye a Mockito a usar el motor 'inline' que tienes en tu archivo de recursos.
                "-Dorg.mockito.mock.maker.config=mock-maker-inline",

                // 2. Silencia la advertencia de carga dinámica del JDK (Recomendado por la advertencia misma)
                // Esto resuelve la queja sobre el "self-attaching" de Byte Buddy.
                "-XX:+EnableDynamicAgentLoading",

                // 3. Permite el acceso a módulos cerrados del JDK (Necesario para Mockito en Java 17+)
                // Los aplicamos siempre para evitar la comprobación de versión que fallaba.
                "--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED",
                "--add-opens", "java.base/java.util=ALL-UNNAMED"
            )
        }
    }
    kotlin {
        jvmToolchain(config.jvmToolChain)
    }

    buildFeatures {
        compose = true
    }

}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    api(project(":presentation"))
    implementation(project(":commons"))

    testImplementation(files("src/test/resources"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    //DI
    implementation(libs.hilt.android)
    testImplementation(libs.hilt.android.testing)
    ksp(libs.hilt.compiler)

    // JUnit 5 Dependencies
//    testImplementation(libs.junit.jupiter.aggregator)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.jupiter.launcher)
    testImplementation(libs.assertj)

    // Cucumber/Android Test Dependencies
    androidTestImplementation(libs.io.cucumber.android)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Esto DEBE estar en :app para que la jerarquía de Compose sea visible a los tests externos
    debugImplementation(libs.androidx.compose.ui.test.manifest)

}