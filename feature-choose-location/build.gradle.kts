plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.kamikadze328.memo.feature.choose.location"
    compileSdk = 36

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core-android"))
    implementation(project(":core-ui"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.google.material)
    implementation(libs.androidx.activity.ktx)

    implementation(libs.osmdroid)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
