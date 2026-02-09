plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    //alias(libs.plugins.kotlin.compose)
    /*Hilt Dependence Injection*/

    // ✅ Compose (Kotlin 2.x)
    alias(libs.plugins.kotlin.compose)

    // 🔑 KAPT
    id("kotlin-kapt")

    // 🔑 Hilt
    id("com.google.dagger.hilt.android")
    //id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"

    // 🔑 Kotlin Serialization (DESDE EL CATÁLOGO)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.lossabinos.serviceapp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.lossabinos.serviceapp"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // ✅ REFERENCIAR DATA
    implementation(project(":data"))
    implementation(project(":domain"))

    // Viewmodels
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // Material icons extensions
    implementation(libs.androidx.compose.material.icons.extended)
    // Material icons
    implementation(libs.material.v1120)
    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    // Hilt para ViewModel
    //implementation(libs.androidx.hilt.navigation.compose)

    // ejecutar servicios
    // Retrofit
    implementation(libs.retrofit.v2100)
    // Retrofit - Scalars Converter (para String responses)
    implementation(libs.converter.scalars)
    // OkHttp (explícito)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    // Navigation Compose
    implementation(libs.androidx.navigation.compose)
    // Hilt Navigation (para inyectar ViewModels)
    implementation(libs.androidx.hilt.navigation.compose)
    // room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    // Serialización JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // 🆕 ML Kit Barcode Scanning
    implementation("com.google.mlkit:barcode-scanning:17.3.0")

    // 🆕 CameraX (para capturar video)
    implementation("androidx.camera:camera-core:1.5.2")
    implementation("androidx.camera:camera-camera2:1.5.2")
    implementation("androidx.camera:camera-lifecycle:1.5.2")
    implementation("androidx.camera:camera-view:1.5.2")

    // 🆕 Permisos
    implementation("com.google.accompanist:accompanist-permissions:0.37.3")

    // Coil para cargar imágenes
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Material (necesario para pullrefresh)
    implementation("androidx.compose.material:material:1.6.0")

    implementation(libs.androidx.hilt.work)
    kapt(libs.androidx.hilt.compiler)

}