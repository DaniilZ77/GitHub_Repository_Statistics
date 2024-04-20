import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

group = "ru.senin.kotlin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.commons.compress)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.swing)
    implementation(libs.coroutines.debug)

    val retrofitVersion: String by project
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.jackson)
    implementation(libs.retrofit.mock)

    implementation(libs.jackson.module.kotlin)
    implementation(libs.logback)

    testImplementation(libs.coroutines.test)
    testImplementation(kotlin("test"))
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<KotlinCompilationTask<*>>("compileKotlin").configure {
    compilerOptions.freeCompilerArgs.add(
        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
    )
}

kotlin {
    jvmToolchain(JavaVersion.VERSION_21.majorVersion.toInt())
}
