import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.kamikadze328.memo"
    compileSdk = 36
    buildToolsVersion = "36.1.0"

    defaultConfig {
        applicationId = "com.kamikadze328.memo"
        minSdk = 27
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

kotlin {
    target {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":core-android"))
    implementation(project(":core-ui"))
    implementation(project(":background-location"))
    implementation(project(":feature-home"))
    implementation(project(":feature-memo-details"))
    implementation(project(":feature-choose-location"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.lifecycle.process)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}