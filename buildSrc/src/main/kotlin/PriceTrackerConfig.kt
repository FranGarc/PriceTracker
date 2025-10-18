import org.gradle.api.JavaVersion
object PriceTrackerConfig {
    const val compileSdk = 36
    const val targetSdk = 36
    const val minSdk = 27
    const val jvmTarget = "11"
    val javaVersion = JavaVersion.VERSION_11

    const val kotlinVersion = "1.9.0"
    const val composeVersion = "1.5.0"
    const val composeCompilerVersion = "1.5.0"
}
//extra["config"] = PriceTrackerConfig