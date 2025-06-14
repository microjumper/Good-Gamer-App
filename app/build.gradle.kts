import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // Google services Gradle plugin
    id("com.google.gms.google-services")
}

val localProperties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}
val gameByIdUrl = localProperties.getProperty("GAME_BY_ID_URL")
    ?: throw GradleException("GAME_BY_ID_URL not found in local.properties")
val searchGamesUrl = localProperties.getProperty("SEARCH_GAMES_URL")
    ?: throw GradleException("SEARCH_GAMES_URL not found in local.properties")

android {
    namespace = "com.microjumper.goodgamer"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.microjumper.goodgamer"
        minSdk = 36
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildFeatures {
            buildConfig = true
        }

        buildConfigField("String", "GAME_BY_ID_URL", "\"$gameByIdUrl\"")
        buildConfigField("String", "SEARCH_GAMES_URL", "\"$searchGamesUrl\"")
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

    // Coil for image loading in Jetpack Compose
    implementation(libs.coil.compose)
    // Coil OkHttp library for network operations
    implementation(libs.coil.network.okhttp)

    // Navigation component for Jetpack Compose
    implementation(libs.androidx.navigation.compose)

    // Firebase BoM
    implementation(platform(libs.firebase.bom))
    // Realtime Database library
    implementation(libs.firebase.database)
}