// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.library) apply false
//    alias(libs.plugins.hilt) apply false
//    alias(libs.plugins.ksp) apply false
}


// This ensures all unit tests across all subprojects are configured with the Mockito agent.
subprojects {
    // We target the standard Gradle 'Test' task (which runs local unit tests)
    tasks.withType<Test>().configureEach {

        // Use doFirst to ensure this code executes immediately before the test JVM starts.
        doFirst {
            // Find the Mockito dependency that contains the necessary ByteBuddy agent JAR.
            // We search the Test task's own resolved classpath, which is the most reliable source,
            // bypassing the need to guess configuration names (like testRuntimeClasspath).
            val mockitoAgent = classpath
                .files
                .find { it.name.contains("byte-buddy-agent") }

            // If the agent JAR is found, configure the JVM arguments.
            if (mockitoAgent != null) {
                val agentArg = "-javaagent:${mockitoAgent.absolutePath}"

                // Get current JVM args safely
                val currentJvmArgs = jvmArgs?.filterNotNull() ?: emptyList()

                // 1. Add -javaagent to jvmArgs (future-proofing)
                // We check if it's already there to prevent duplication.
                if (currentJvmArgs.none { it.startsWith("-javaagent:") }) {
                    jvmArgs = currentJvmArgs + agentArg
                    // SUCCESS Logging: This will confirm the agent was set!
                    println("SUCCESS: Mockito agent configured for test task: $path")
                }

                // 2. Set System Property: Force the use of the inline mock maker
                // This tells Mockito not to rely on auto-attachment/discovery mechanisms.
                if (systemProperties.get("mockito.mockmaker.inline") == null) {
                    systemProperty("mockito.mockmaker.inline", "true")
                }
            }
        }
    }
}

