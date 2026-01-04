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

        testInstrumentationRunner = "com.franciscogarciagarzon.pricetracker.presentation.HiltTestRunner"
        consumerProguardFiles("consumer-rules.pro")
    }


    testCoverage {
        jacocoVersion = libs.versions.jacoco.get()
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
            // Fuerza a que Gradle use la plataforma JUnit 5 (Jupiter) para las pruebas unitarias
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
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    testImplementation(kotlin("test"))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.compose.ui.test.manifest)
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

val jacocoFullReport = tasks.register<JacocoReport>("jacocoFullReport") {
    group = "verification"
    description = "Generates JaCoCo coverage reports for the presentation module"

    executionData(
        layout.buildDirectory.file("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec"),
        fileTree(layout.buildDirectory.dir("outputs/code_coverage/debugAndroidTest/connected")) {
            include("**/coverage.ec")
        }
    )

    classDirectories.setFrom(fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug")) {
        exclude(
            // 1. ELIMINAR EL DESAJUSTE (Full Infrastructure)
            "**/HiltTestActivity*",
            "**/Hilt_*",
            "**/*_HiltModules*",
            "**/*_Factory*",
            "**/*_MembersInjector*",
            "**/*_ComponentTree*",
            "**/com/franciscogarciagarzon/pricetracker/*.class", // Quita la MainActivity

            // 2. LIMPIAR EL RUIDO DE COMPOSE
            "**/*\$Composable*",
            "**/*\$Content*",
            "**/*\$lambda*",
            "**/*\$inlined*",
            "**/*Preview*",
            "**/presentation/ui/theme/**",
            "**/composables/**",
            "**/*Composable*",
            "**/*Screen*.*",

            // 3. ENFOCARSE EN LA LÓGICA (Lo que sí quieres medir)
            "**/di/**",
            "**/utils/**",
            "**/R.class",
            "**/R$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*"
        )
    })

    sourceDirectories.setFrom(files("$project.projectDir/src/main/java", "$project.projectDir/src/main/kotlin"))

    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/full"))
    }

    dependsOn("testDebugUnitTest", "connectedDebugAndroidTest")
}





configurations.all {
    resolutionStrategy {
        eachDependency {
            // Fuerza la exclusión de Byte Buddy de todas las dependencias
            if (requested.group == "net.bytebuddy") {
                useVersion(libs.versions.bytebuddy.get()) // Use a compatible version if absolutely needed
            }
        }

        // Falla si se cuela alguna dependencia de Byte Buddy
        failOnVersionConflict()

        // Preferir dependencias compatibles con Android
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

        // ver ubicación del informe
        val reportFile = file("${layout.buildDirectory.get()}/reports/coverage/debug/index.html")
        if (reportFile.exists()) {
            println("Coverage report: file://${reportFile.absolutePath}")
        }
    }
}

// Forma independiente de la plataforma de sugerir abrir el informe
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