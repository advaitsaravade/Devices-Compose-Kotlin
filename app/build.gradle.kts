plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.dagger.hilt.android")
    id("kotlinx-serialization")
}

android {
    namespace = "com.example.iverify.devices"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.iverify.devices"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            val domain = "hiring.iverify.io"
            buildConfigField(type = "String", name = "API_AUTH_TOKEN", value = "\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6ImlWZXJpZnkiLCJ1c2VySWQiOjYyLCJleHAiOjE3MjUxMjg5Mjl9.Vzy-WfuNVplsuv9yuSgPQQNivRWmtywM144j4BcScPs\"")
            buildConfigField(type = "String", name = "API_DOMAIN", value = "\"$domain\"")
            buildConfigField(type = "String", name = "API_ENDPOINT", value = "\"https://$domain/api/\"")
            buildConfigField(type = "String", name = "API_CERTIFICATE_HASH", value = "\"sha256/ZjcFzYwMw6pbwZQ0x12I2Ujeh1mroqaePJx8RrK9Ntc=\"")
        }
        debug {
            isMinifyEnabled = false
            val domain = "hiring.iverify.io"
            buildConfigField(type = "String", name = "API_AUTH_TOKEN", value = "\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6ImlWZXJpZnkiLCJ1c2VySWQiOjYyLCJleHAiOjE3MjUxMjg5Mjl9.Vzy-WfuNVplsuv9yuSgPQQNivRWmtywM144j4BcScPs\"")
            buildConfigField(type = "String", name = "API_DOMAIN", value = "\"$domain\"")
            buildConfigField(type = "String", name = "API_ENDPOINT", value = "\"https://$domain/api/\"")
            buildConfigField(type = "String", name = "API_CERTIFICATE_HASH", value = "\"sha256/ZjcFzYwMw6pbwZQ0x12I2Ujeh1mroqaePJx8RrK9Ntc=\"")
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Core
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")

    // Dependency Injection
    val hiltVersion = "2.47"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")

    // Compose
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.7.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.google.code.gson:gson:2.10.1")

    // Persistence
    implementation("androidx.security:security-crypto:1.1.0-alpha03")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

kapt {
    correctErrorTypes = true
}