plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.ksp)
//    alias(libs.plugins.hilt)
}
val config = PriceTrackerConfig

java {
    sourceCompatibility = config.javaVersion
    targetCompatibility = config.javaVersion
}
kotlin {
    jvmToolchain(config.jvmToolChain)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    //dependency injection
    implementation(libs.dagger.core)
//    ksp(libs.dagger.compiler)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.core)
//    implementation(libs.dagger.jvm.inject)

}
