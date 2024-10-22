plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.state.network"
    compileSdk = 34

    defaultConfig {
        minSdk = 25

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
        sourceCompatibility = JavaVersion.VERSION_22
        targetCompatibility = JavaVersion.VERSION_22
    }
    kotlin {
        jvmToolchain(22)
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material)
    implementation(libs.androidx.test.core)
    androidTestRuntimeOnly(libs.androidx.test.core)
    implementation(libs.work.manager)
    testImplementation(libs.androidx.junit)
    testImplementation(libs.mockito.core)
    androidTestImplementation(libs.mockito.android)
    androidTestImplementation(libs.androidx.ext)
    androidTestImplementation(libs.androidx.espresso.core)
}