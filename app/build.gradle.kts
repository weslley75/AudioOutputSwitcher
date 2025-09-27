plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "br.com.wasystems.audiooutputswitcher"
    compileSdk = 35

    defaultConfig {
        applicationId = "br.com.wasystems.audiooutputswitcher"
        minSdk = 35
        targetSdk = 35
        versionCode = 3
        versionName = "1.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }

    signingConfigs {
        create("release") {
            val envKeystorePassword = System.getenv("KEYSTORE_PASSWORD")
            val envKeyPassword = System.getenv("KEY_PASSWORD")

            if (envKeystorePassword != null && envKeyPassword != null) {
                storeFile = file("../AudioOutputSwitcher.jks")
                storePassword = envKeystorePassword
                keyAlias = "audiooutput"
                keyPassword = envKeyPassword
                println("✅ Signing config loaded successfully")
            } else {
                println("⚠️ Warning: Signing credentials not found, APK will be unsigned")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            val keystorePassword = System.getenv("KEYSTORE_PASSWORD")
            if (keystorePassword != null) {
                signingConfig = signingConfigs.getByName("release")
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

