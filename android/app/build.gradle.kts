plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.2.0"
    id("com.google.devtools.ksp")
}

android {
    namespace = "ru.mishgan325.cownose"
    compileSdk = 35

    defaultConfig {
        applicationId = "ru.mishgan325.cownose"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    packagingOptions {
        resources {
            excludes.add("META-INF/INDEX.LIST")
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    // coil
    implementation("io.coil-kt.coil3:coil-compose:3.2.0")

    val ktor_version = "3.2.1"

    // ktor
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-android:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    // ktor logging
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("uk.uuid.slf4j:slf4j-android:2.0.7-0")
    implementation("ch.qos.logback:logback-classic:1.5.6")

    // koin
    val koin_version = "4.1.0"
    implementation(project.dependencies.platform("io.insert-koin:koin-bom:$koin_version"))
    implementation("io.insert-koin:koin-core")
    implementation("io.insert-koin:koin-compose:$koin_version")
    implementation("io.insert-koin:koin-compose-viewmodel:$koin_version")
    implementation("io.insert-koin:koin-compose-viewmodel-navigation:$koin_version")
    implementation("io.insert-koin:koin-android:$koin_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    val nav_version = "2.9.1"

    implementation("androidx.navigation:navigation-compose:$nav_version")

    implementation("io.coil-kt.coil3:coil-network-okhttp:3.2.0")

    val roomVersion = "2.7.2"
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion") // For annotation processing (Kotlin)
    implementation("androidx.room:room-ktx:$roomVersion") // For Kotlin extensions and coroutines support

    implementation("androidx.exifinterface:exifinterface:1.4.1")


}