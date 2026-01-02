plugins {
    kotlin("jvm")
    alias(libs.plugins.ksp)
    id("jacoco")

}
val config = PriceTrackerConfig

kotlin {
    jvmToolchain(config.jvmToolChain)
}


dependencies {
    implementation(project(":commons"))

    implementation(libs.kotlinx.coroutines.core)
//    implementation(libs.dagger.core)
//    ksp(libs.dagger.compiler)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.core)

    testImplementation(platform(libs.junit.jupiter.bom))

    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.jupiter.launcher)
    testImplementation(libs.assertj)

    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.assertj)
    testImplementation(libs.mockito.junit5)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)

    testImplementation(libs.instancio.core)
    testImplementation(libs.instancio.junit)

}
tasks.test {
    useJUnitPlatform()
}