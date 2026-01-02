import org.gradle.api.JavaVersion
object PriceTrackerConfig {
    const val compileSdk = 36
    const val targetSdk = 36
    const val minSdk = 27
    const val jvmToolChain = 17
// should the java version be changed,
// remember change this JvmTarget.JVM_17 on the jvm modules
    val javaVersion = JavaVersion.VERSION_17

}
