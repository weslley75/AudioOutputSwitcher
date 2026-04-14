plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "br.com.wasystems.audiooutputswitcher"
    compileSdk = 36

    defaultConfig {
        applicationId = "br.com.wasystems.audiooutputswitcher"
        minSdk = 30
        targetSdk = 36
        versionCode = 9
        versionName = "1.0.8"

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
                val keystorePath = System.getenv("KEYSTORE_PATH") ?: "../AudioOutputSwitcher.jks"
                storeFile = file(keystorePath)
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
            isShrinkResources = true
            val hasSigningCredentials = System.getenv("KEYSTORE_PASSWORD") != null
            if (hasSigningCredentials) {
                signingConfig = signingConfigs.getByName("release")
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }


    buildFeatures {
        aidl = false
        renderScript = false
        resValues = false
        shaders = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

