plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.smart.neteasemusic.sdk.base"
    compileSdk = 33

    defaultConfig {
        minSdk = 28

        consumerProguardFiles("consumer-rules.pro")

        ndk {
            abiFilters.addAll(listOf("x86_64", "arm64-v8a", "armeabi-v7a"))
        }
        externalNativeBuild {
            cmake {
                cppFlags.add("-std=c++17")
            }
        }

        buildConfigField("String", "SDK_VERSION", "\"${project.property("SMART_VERSION")}\"")
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
        }
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

    publishing {
        singleVariant("release") {
//            withSourcesJar()
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.bundles.network)
    implementation(libs.mmkv)
    implementation(libs.timber)
    implementation(project(":iotsdk-httpdns"))
    implementation(project(":iotsdk-extension"))
    implementation(libs.adapter.rxjava2)

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
}