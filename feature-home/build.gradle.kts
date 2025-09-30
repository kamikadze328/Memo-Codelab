plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.kamikadze328.memo.feature.home"
    compileSdk = 36

    defaultConfig {
        minSdk = 27
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation((project(":domain")))
    implementation((project(":core-android")))
    implementation((project(":core-ui")))
    implementation((project(":feature-memo-create")))
    implementation((project(":feature-memo-details")))
    implementation(project(":background-location"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.google.material)
    implementation(libs.androidx.activity.ktx)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
