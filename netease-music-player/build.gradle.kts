plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.smart.neteasemusic.sdk.player"
    compileSdk = 33

    defaultConfig {
        minSdk = 28
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            // consumer-rules.pro 会自动提供给使用方
            isMinifyEnabled = true
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

    publishing {
        singleVariant("release") {
//            withSourcesJar()
        }
    }
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.media3.datasource.okhttp)
    implementation(libs.exoplayer)
    implementation(project(":netease-music-base"))
    implementation(project(":netease-music-data"))
    implementation(libs.bundles.network)

    ksp(libs.room.compiler)
    implementation(libs.bundles.room)
    implementation(project(":decoder-flac-aar"))
}