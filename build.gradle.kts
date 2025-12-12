// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    id("jacoco")

}

tasks.register("checkJavaVersions") {
    doFirst {
        println("=== Java Environment Information ===")

        // 1. JAVA_HOME from environment
        println("JAVA_HOME (environment): " + System.getenv("JAVA_HOME"))

        // 2. Java version Gradle is actually using
        println("Gradle Java Home: " + System.getProperty("java.home"))

        // 3. Java version details
        println("Java Version: " + System.getProperty("java.version"))
        println("Java Vendor: " + System.getProperty("java.vendor"))

        // 4. Check Gradle properties
        println("\n=== Gradle Properties ===")
        println("Gradle Version: " + gradle.gradleVersion)
        println("Gradle User Home: " + gradle.gradleUserHomeDir.absolutePath)

        // 5. Check if running in daemon
        println("\n=== Daemon Info ===")
        println("Is Daemon: " + java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("daemon"))
    }
}

// ... at the end of the root /build.gradle.kts file
/**
 * Task to generate a single, combined JaCoCo report for the entire project.
 * It aggregates source code, class files, and execution data from all relevant submodules.
 */
tasks.register<JacocoReport>("jacocoRootReport") {
    group = "verification"
    description = "Generates a combined JaCoCo coverage report for all modules."

    // This task should run after all the module-level tests have completed.
    dependsOn(
        ":domain:test",
        ":data:test",
        ":data:connectedDebugAndroidTest",
        ":presentation:test",
        ":presentation:connectedDebugAndroidTest"
    )

    // --- AGGREGATION ---
    // 1. Source Directories: Collect the source code from all modules you want to report on.
    sourceDirectories.setFrom(
        files(
            "domain/src/main/java",
            "data/src/main/java",
            "presentation/src/main/java",
            "commons/src/main/java"
            // Add kotlin directories if they exist, e.g., "domain/src/main/kotlin"
        )
    )

    // 2. Class Directories: Collect the compiled class files from all modules.
    classDirectories.setFrom(
        files(
            fileTree("domain/build/classes/kotlin/main") {
                exclude(
                    "**/di/**",
                    "**/*_HiltModules*.*", "**/*_Factory*.*", "**/*_MembersInjector*.*",
                    "**/*Composable*.*", "**/*Kt.class"
                )
            },
            fileTree("data/build/tmp/kotlin-classes/debug") {
                exclude(
                    "**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*",
                    "**/*Test*.*", "android/**/*.*", "**/di/**",
                    "**/*_HiltModules*.*", "**/*_Factory*.*", "**/*_MembersInjector*.*",
                    "**/*Composable*.*", "**/*Kt.class"
                )
            },
            fileTree("presentation/build/tmp/kotlin-classes/debug") {
                exclude(
                    "**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*",
                    "**/*Test*.*", "android/**/*.*", "**/di/**",
                    "**/*_HiltModules*.*", "**/*_Factory*.*", "**/*_MembersInjector*.*",
                    "**/*Composable*.*", "**/*Kt.class"
                )
            },
            fileTree("commons/build/classes/kotlin/main") {
                exclude(
                    "**/di/**",
                    "**/*_HiltModules*.*", "**/*_Factory*.*", "**/*_MembersInjector*.*",
                    "**/*Composable*.*", "**/*Kt.class"
                )
            }
        )
    )

    // 3. Execution Data: Collect all .exec and .ec files from all modules.
    executionData.setFrom(
        files(
            fileTree(project.rootDir) {
                include(
                    "**/build/outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
                    "**/build/outputs/code_coverage/debugAndroidTest/connected/**/*.ec"
                )
            }
        )
    )

    // --- REPORT CONFIGURATION ---
    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/jacocoRootReport"))
    }
}


/**
 * This is the single command to run all tests and generate the single, combined coverage report.
 */
tasks.register("allTestsWithCoverage") {
    group = "verification"
    description = "Runs all unit tests, all Android tests, and generates a single combined JaCoCo report."

    // This task now depends on the root report task.
    // Since jacocoRootReport depends on the test tasks, Gradle will automatically
    // run the tests first, then generate the report.
    dependsOn("jacocoRootReport")

    // Optional: Add a doLast block to print the final report location.
    doLast {
        val reportPath = "${layout.buildDirectory.get().asFile}/reports/jacoco/jacocoRootReport/html/index.html"
        println("")
        println("✅ All tests executed and combined coverage report generated.")
        println("Combined Project Report: file://${reportPath}/reports/jacoco/jacocoRootReport/html/index.html")
    }
}