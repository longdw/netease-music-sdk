plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.smart.neteasemusic.sdk.data"
    compileSdk = 33

    defaultConfig {
        minSdk = 28
    }

    buildTypes {
        release {
            // Library 模块不应在发布时混淆,由使用方进行混淆
            // consumer-rules.pro 会自动提供给使用方
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

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(project(":netease-music-base"))
    implementation(libs.bundles.network)
    implementation(libs.zxing)
    implementation(libs.bundles.room)
}