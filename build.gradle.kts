// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false

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
tasks.register("debugGradleProperties") {
    doLast {
        println("=== Checking Gradle Properties ===")

        // Check system properties
        println("System Properties:")
        println("  java.home: ${System.getProperty("java.home")}")
        println("  gradle.user.home: ${System.getProperty("gradle.user.home")}")

        // Check project properties
        println("\nProject Properties:")
        println("  org.gradle.java.home: ${project.findProperty("org.gradle.java.home")}")
        println("  gradle.java.home: ${project.findProperty("gradle.java.home")}")

        // Check Gradle properties API
        println("\nGradle Properties (via provider):")
        val gradleJavaHome = gradle.startParameter.projectProperties["org.gradle.java.home"]
        println("  startParameter: $gradleJavaHome")

        // Check environment
        println("\nEnvironment Variables:")
        println("  JAVA_HOME: ${System.getenv("JAVA_HOME")}")
        println("  PATH: ${System.getenv("PATH")}")

        // Check actual Java executable
        println("\nJava Executable Path:")
        exec {
            commandLine("which", "java")
            standardOutput = System.out
        }
    }
}

tasks.register("debugJavaPaths") {
    doLast {
        println("=== Detailed Path Analysis ===")

        val systemJavaHome = System.getProperty("java.home")
        val envJavaHome = System.getenv("JAVA_HOME")

        println("1. System Property java.home: $systemJavaHome")
        println("2. Env Variable JAVA_HOME: $envJavaHome")

        // Convert to files and get canonical paths
        val systemJavaHomeFile = File(systemJavaHome)
        val envJavaHomeFile = if (envJavaHome != null) File(envJavaHome) else null

        println("\n3. Canonical (real) paths:")
        println("   System: ${systemJavaHomeFile.canonicalPath}")
        println("   Env: ${envJavaHomeFile?.canonicalPath}")

        println("\n4. Parent directories (where JDK root might be):")
        println("   System parent: ${systemJavaHomeFile.parentFile?.canonicalPath}")
        println("   Env parent: ${envJavaHomeFile?.parentFile?.canonicalPath}")

        println("\n5. Check if they're the same installation:")
        val systemJdkRoot = if (systemJavaHomeFile.name == "jre") {
            systemJavaHomeFile.parentFile
        } else {
            systemJavaHomeFile
        }

        val envJdkRoot = if (envJavaHomeFile?.name == "jre") {
            envJavaHomeFile.parentFile
        } else {
            envJavaHomeFile
        }

        println("   System JDK root: ${systemJdkRoot?.canonicalPath}")
        println("   Env JDK root: ${envJdkRoot?.canonicalPath}")

        println("\n6. Java executable:")
        exec {
            commandLine("which", "java")
            standardOutput = System.out
        }

        println("\n7. Actual java -version output:")
        exec {
            commandLine("java", "-version")
            standardOutput = System.out
            errorOutput = System.out
        }
    }
}
tasks.register("debugShellInterception") {
    doLast {
        println("=== Shell Command Interception Debug ===")

        println("1. Testing command execution methods:")

        // Method A: Direct execution
        println("\na) Direct path execution:")
        exec {
            commandLine("/home/frank/.sdkman/candidates/java/current/bin/java", "-version")
            standardOutput = System.out
            errorOutput = System.out
        }

        // Method B: Using 'command' to bypass functions/aliases
        println("\nb) Using 'command java' (bypasses functions/aliases):")
        exec {
            commandLine("command", "java", "-version")
            standardOutput = System.out
            errorOutput = System.out
        }

        // Method C: Using 'builtin' if it's a shell builtin
        println("\nc) Using sh -c to test in clean shell:")
        exec {
            commandLine("sh", "-c", "java -version")
            standardOutput = System.out
            errorOutput = System.out
        }

        println("\n2. Shell diagnostic:")
        exec {
            commandLine("sh", "-c", "type java; alias java 2>/dev/null || echo 'no alias'; hash java 2>/dev/null || echo 'not hashed'")
            standardOutput = System.out
        }

        println("\n3. Test with env command:")
        exec {
            commandLine("env", "java", "-version")
            standardOutput = System.out
            errorOutput = System.out
        }
    }
}