plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.junit5)

    id("jacoco")
//    id("de.mannodermaus.android-junit5")
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)

}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

val config = PriceTrackerConfig
android {
    namespace = "com.franciscogarciagarzon.pricetracker.data"
    compileSdk = config.compileSdk

    defaultConfig {
        minSdk = config.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["runnerBuilder"] =
            "de.mannodermaus.junit5.AndroidJUnit5Builder"
        testInstrumentationRunnerArguments["junit5.classpath.alignment.check.disabled"] = "true"
        consumerProguardFiles("consumer-rules.pro")

    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
        }
        release {
            isMinifyEnabled = true
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
        animationsDisabled = true
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
    kotlin {
        jvmToolchain(config.jvmToolChain)
    }
    packaging {
        resources {
            excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
            // También es común excluir otros archivos problemáticos de META-INF
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
            excludes += "META-INF/LICENCE.txt"
            excludes += "META-INF/AL2.0"
            excludes += "META-INF/LGPL2.1"
        }
    }
    room { schemaDirectory("$projectDir/schemas") }
}




dependencies {
    implementation(project(":commons"))
    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.gson)

    //dependency injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    // ---------------------------------------------------------------------
    // UNIT TESTS (Local JVM - domain & repository mapping tests)
    // ---------------------------------------------------------------------

    // JUnit 5 Dependencies
    testImplementation(platform(libs.junit.jupiter.bom))


    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.jupiter.launcher)

    testImplementation(libs.assertj)
    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.mockito.junit5)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.instancio.core)
    testImplementation(libs.instancio.junit)

    // ---------------------------------------------------------------------
    // ANDROID INSTRUMENTATION TESTS (Emulator/Device - DAO/Integration Tests)
    // ---------------------------------------------------------------------

    // CORE ANDROID TEST FRAMEWORK (Needed for ApplicationProvider, etc.)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.core.ktx)
    androidTestImplementation(libs.androidx.test.runner)

    // Room Testing
    androidTestImplementation(libs.androidx.room.testing)

    // JUNIT 5 FOR ANDROID TESTS (Replaces @RunWith(AndroidJUnit4))
    androidTestImplementation(libs.android.junit5.runner)
    androidTestImplementation(libs.junit.jupiter.api)
    androidTestImplementation(libs.junit.jupiter.params)
    androidTestRuntimeOnly(libs.junit.jupiter.engine) // Runtime only for android tests

    androidTestImplementation(libs.mockito.junit5)
    androidTestImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.mockito.android) {
        exclude(group = "net.bytebuddy", module = "byte-buddy")
        exclude(group = "net.bytebuddy", module = "byte-buddy-agent")
    }
    androidTestImplementation(libs.mockito.core) {
        exclude(group = "net.bytebuddy", module = "byte-buddy")
    }
    //da problemas con la versión integrada de mockito, hay que importarlo por separado
    androidTestImplementation(libs.bytebuddy)

    // COROUTINES TEST (Needed for runTest)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    // ASSERTIONS/
    androidTestImplementation(libs.assertj)
    testImplementation(kotlin("test"))
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


// JaCoCo configuration for Android test coverage
tasks.withType<Test> {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}


// Unit Test Coverage Report
val jacocoUnitTestReport = tasks.register<JacocoReport>("jacocoUnitTestReport") {
    group = "verification"
    description = "Generates JaCoCo coverage reports for unit tests"

    executionData(fileTree(layout.buildDirectory) {
        include("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
    })

    classDirectories.setFrom(fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug")) {
        exclude(
            "**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*",
            "**/*Test*.*", "android/**/*.*",
//            "**/models/**", "**/entity/**",
//            "**/databinding/**", "**/binding/**", "**/BR.*", "**/androidx/**",
//            "**/dagger/**", "**/*MapperImpl*.*", "**/*\$*.*"
        )
    })

    sourceDirectories.setFrom(files("$project.projectDir/src/main/java", "$project.projectDir/src/main/kotlin"))

    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/test"))
    }

    dependsOn("testDebugUnitTest")
}

// Android Test Coverage Report
val jacocoAndroidTestReport = tasks.register<JacocoReport>("jacocoAndroidTestReport") {
    group = "verification"
    description = "Generates JaCoCo coverage reports for Android tests"

    executionData(fileTree(layout.buildDirectory) {
        include("outputs/code_coverage/debugAndroidTest/connected/*/coverage.ec")
    })

    classDirectories.setFrom(fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug")) {
        exclude(
            "**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*",
            "**/*Test*.*", "android/**/*.*",
//            "**/models/**", "**/entity/**",
//            "**/databinding/**", "**/binding/**", "**/BR.*", "**/androidx/**",
//            "**/dagger/**", "**/*MapperImpl*.*", "**/*\$*.*"
        )
    })

    sourceDirectories.setFrom(files("$project.projectDir/src/main/java", "$project.projectDir/src/main/kotlin"))

    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/androidTest"))
    }

    dependsOn("connectedDebugAndroidTest")


    doFirst {
        println("=== Android Test Coverage Debug ===")
        val classesDir = layout.buildDirectory.dir("intermediates/javac/debug/classes").get().asFile
        println("Classes directory: ${classesDir.absolutePath}")
        println("Exists: ${classesDir.exists()}")

        if (classesDir.exists()) {
            // Check for DAO classes specifically
            val daoClasses = fileTree(classesDir) {
                include("**/*Dao*.class")
            }
            println("Found ${daoClasses.files.size} DAO classes:")
            daoClasses.files.take(10).forEach { println("  - ${it.absolutePath}") }
        }
        println("=== End Debug ===")
    }

}

// Combined Report - FIXED PATHS
val jacocoCombinedTestReport = tasks.register<JacocoReport>("jacocoCombinedTestReport") {
    group = "verification"
    description = "Generates combined JaCoCo coverage reports for both unit and Android tests"

    // Use the exact paths we found
    executionData(
        layout.buildDirectory.file("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec"),
        fileTree(layout.buildDirectory.dir("outputs/code_coverage/debugAndroidTest/connected")) {
            include("**/coverage.ec")
        }
    )

    classDirectories.setFrom(fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug")) {
        exclude(
            "**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*",
            "**/*Test*.*", "android/**/*.*",
//            "**/models/**", "**/entity/**",
//            "**/databinding/**", "**/binding/**", "**/BR.*", "**/androidx/**",
//            "**/dagger/**", "**/*MapperImpl*.*", "**/*\$*.*"
        )
    })

    sourceDirectories.setFrom(files("$project.projectDir/src/main/java", "$project.projectDir/src/main/kotlin"))

    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/combined"))
    }

    dependsOn("testDebugUnitTest", "connectedDebugAndroidTest")

    // Debug: Print execution data files
    doFirst {
        println("=== JaCoCo Execution Data Files ===")
        executionData.files.forEach { file ->
            println("Found: ${file.absolutePath} - Exists: ${file.exists()}")
        }
        println("=== End Execution Data Files ===")
    }
}

// Coverage verification
val jacocoCoverageVerification = tasks.register<JacocoCoverageVerification>("jacocoCoverageVerification") {
    group = "verification"
    description = "Verifies code coverage based on unit tests"

    executionData(layout.buildDirectory.file("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec"))

    classDirectories.setFrom(fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug")) {
        exclude(
            "**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*",
            "**/*Test*.*", "android/**/*.*"
        )
    })

    sourceDirectories.setFrom(files("$project.projectDir/src/main/java", "$project.projectDir/src/main/kotlin"))

    violationRules {
        rule {
            limit {
                minimum = "0.7".toBigDecimal()
            }
        }
    }

    dependsOn("testDebugUnitTest")
}

tasks.named("check") {
    dependsOn(jacocoCoverageVerification)
}

tasks.register<Delete>("cleanJacocoReports") {
    delete(
        fileTree(layout.buildDirectory.dir("reports/jacoco")),
        fileTree(layout.buildDirectory.dir("outputs/code_coverage")),
        fileTree(layout.buildDirectory.dir("outputs/unit_test_code_coverage"))
    )
}

tasks.named("clean") {
    dependsOn("cleanJacocoReports")
}

// MASTER REPORT TASK - Combines all reports into one
val jacocoFullReport = tasks.register<JacocoReport>("jacocoFullReport") {
    group = "verification"
    description = "Generates ALL JaCoCo coverage reports (unit, Android, and combined)"

    // Use the exact paths we found
    executionData(
        layout.buildDirectory.file("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec"),
        fileTree(layout.buildDirectory.dir("outputs/code_coverage/debugAndroidTest/connected")) {
            include("**/coverage.ec")
        }
    )

    classDirectories.setFrom(fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug")) {
        exclude(
            "**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*",
            "**/*Test*.*", "android/**/*.*", "**/models/**", "**/entity/**",
            "**/databinding/**", "**/binding/**", "**/BR.*", "**/androidx/**",
            "**/dagger/**", "**/*MapperImpl*.*", "**/*\$*.*"
        )
    })

    sourceDirectories.setFrom(files("$project.projectDir/src/main/java", "$project.projectDir/src/main/kotlin"))

    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/full"))
    }

    // This task depends on running all tests first
    dependsOn("testDebugUnitTest", "connectedDebugAndroidTest")
}

// Or if you want a task that just aggregates the existing report tasks (without regenerating):
val jacocoAllReports = tasks.register("jacocoAllReports") {
    group = "verification"
    description = "Runs all individual JaCoCo report tasks"

    dependsOn(
        "jacocoUnitTestReport",
        "jacocoAndroidTestReport",
        "jacocoCombinedTestReport"
    )

    // Optional: Add a message when complete
    doLast {
        println("All JaCoCo reports generated:")
        println("  - Unit tests: data/build/reports/jacoco/test/")
        println("  - Android tests: data/build/reports/jacoco/androidTest/")
        println("  - Combined: data/build/reports/jacoco/combined/")
        println("  - Full: data/build/reports/jacoco/full/")
    }
}

// Make check depend on the full report
tasks.named("check") {
    dependsOn("jacocoFullReport", "jacocoCoverageVerification")
}