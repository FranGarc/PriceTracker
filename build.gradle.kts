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


/**
 * Tarea para generar un único informe JaCoCo combinado para la totalidad del proyecto.
 */
tasks.register<JacocoReport>("jacocoRootReport") {
    group = "verification"
    description = "Generates a combined JaCoCo coverage report for all modules."

    // ejecutar después de que todos los tests de cada módulo se hayan completado
    dependsOn(
        ":domain:test",
        ":data:testDebugUnitTest",
        ":data:connectedDebugAndroidTest",
        ":presentation:testDebugUnitTest",
        ":presentation:connectedDebugAndroidTest"
    )

    // --- AGREGA ---
    // 1. Directorios fuente: recoge el código fuente de todos los módulos  de los que queremos el informe.
    sourceDirectories.setFrom(
        files(
            "domain/src/main/java",
            "data/src/main/java",
            "presentation/src/main/java",
            "commons/src/main/java"
            // si hubiera directorios kotlin, se añadirían, ej., "domain/src/main/kotlin"
        )
    )

    // 2. Directorios de Clase: Recoge los ficheros de clases compiladas de todos los módulos.
    classDirectories.setFrom(
        files(
            fileTree("domain/build/classes/kotlin/main") {
                exclude(
                    "**/di/**",
                    "**/*_HiltModules*.*", "**/*_Factory*.*", "**/*_MembersInjector*.*",
                    "**/*Composable*.*", "**/*Kt.class",
                    "**/*Contract*.*" // EXCLUSIÓN: Interfaces/Contratos sin lógica
                )
            },
            fileTree("data/build/tmp/kotlin-classes/debug") {
                exclude(
                    "**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*",
                    "**/*Test*.*", "android/**/*.*", "**/di/**",
                    "**/*_HiltModules*.*", "**/*_Factory*.*", "**/*_MembersInjector*.*",
                    "**/*Composable*.*", "**/*Kt.class",
                    "**/*MapperImpl*.*" // EXCLUSIÓN: Código generado por MapStruct (si lo usas)
                )
            },
            fileTree("presentation/build/tmp/kotlin-classes/debug") {
                exclude(
                    "**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*",
                    "**/*Test*.*", "android/**/*.*", "**/di/**",
                    "**/*_HiltModules*.*", "**/*_Factory*.*", "**/*_MembersInjector*.*",
                    "**/*Composable*.*", "**/*Kt.class",
                    "**/MainActivity.*",        // EXCLUSIÓN: Actividad principal
                    "**/*ResourceProvider*.*"    // EXCLUSIÓN: Wrappers de recursos Android
                )
            },
            fileTree("commons/build/classes/kotlin/main") {
                exclude(
                    "**/di/**",
                    "**/*_HiltModules*.*", "**/*_Factory*.*", "**/*_MembersInjector*.*",
                    "**/*Composable*.*", "**/*Kt.class",
                    "**/*Logger*.*",     // EXCLUSIÓN: Implementaciones de loggers
                    "**/*Contract*.*"    // EXCLUSIÓN: Interfaces de commons
                )
            }
        )
    )

    // 3. Datos de Ejecución: Recoge todos los ficheros .exec y .ec de todos los módulos.
    executionData.setFrom(
        files(
            fileTree(project.rootDir) {
                include(
                    // Ruta para módulos Android (Presentation, Data)
                    "**/build/outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
                    // Ruta para tests instrumentados (AndroidTest)
                    "**/build/outputs/code_coverage/debugAndroidTest/connected/**/*.ec",
                    // Ruta para módulos Kotlin/JVM puros (Domain, Commons)
                    "**/build/jacoco/test.exec"
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
 * Comando único para ejecutar todos los tests y generar el informe de cobertura único.
 */
tasks.register("allTestsWithCoverage") {
    group = "verification"
    description = "Runs all unit tests, all Android tests, and generates a single combined JaCoCo report."

    // depende de la tarea de informe raíz
    // Ya que jacocoRootReport depende de otras tareas de tests,
    // Gradle ejecutará automáticamente los tests primero y luego generará el informe.
    dependsOn("jacocoRootReport")

    // imprime la ubicación del informe
    doLast {
        val reportPath = "${layout.buildDirectory.get().asFile}/reports/jacoco/jacocoRootReport/index.html"
        println("\n Informe unificado generado con éxito.")
        println("Haz Ctrl+Click para abrir: file://$reportPath")
    }
}