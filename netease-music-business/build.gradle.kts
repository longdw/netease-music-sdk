plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.smart.neteasemusic.business"
    compileSdk = 33

    defaultConfig {
        minSdk = 28
    }

    buildTypes {
        release {
            // Library 模块不应在发布时混淆，由使用方进行混淆
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

    //开启viewBinding
    buildFeatures {
        viewBinding = true
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(project(":netease-music-base"))
    implementation(project(":netease-music-player"))
    implementation(project(":netease-music-data"))
    implementation(libs.lifecycle.viewmodel.ktx)
//    implementation(libs.adapter.rxjava2)
    implementation(libs.timber)

    implementation(libs.moshi.kotlin)
    implementation(libs.bundles.network)
}