//import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.google.protobuf)


    // 🔑 KAPT
    id("kotlin-kapt")

    // 🔑 Hilt
    id("dagger.hilt.android.plugin")
    //id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"

    // 🔑 Kotlin Serialization
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.lossabinos.data"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.3"
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}


dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // ✅ REFERENCIAR DOMAIN
    implementation(project(":domain"))

    implementation(libs.okhttp)

    // ✅ Retrofit (core)
    implementation(libs.retrofit)
    // room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    // Serialización JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    //implementation("com.google.dagger:hilt-android:2.50")
    //kapt("com.google.dagger:hilt-android-compiler:2.50")

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // WorkManager para sincronización
    implementation("androidx.work:work-runtime-ktx:2.9.1")

    // 🔑 DataStore Proto
    implementation("androidx.datastore:datastore-core:1.1.1")

    // 🔑 Protobuf (lite)
    implementation("com.google.protobuf:protobuf-javalite:3.25.3")

    // 🔑 DataStore (delegate Context.dataStore)
    implementation("androidx.datastore:datastore:1.1.1")

}