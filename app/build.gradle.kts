import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.chris.geminibasedapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.chris.geminibasedapp"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        //load the values from .properties file
        val keystoreFile = project.rootProject.file("local.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())

        //return empty key in case something goes wrong
        val apiKey = properties.getProperty("API_KEY") ?: ""

        buildConfigField(
            type = "String",
            name = "API_KEY",
            value = "\"${apiKey}\""
        )

        val webClient = properties.getProperty("WEB_CLIENT_ID") ?: ""

        buildConfigField(
            type = "String",
            name = "WEB_CLIENT_ID",
            value = "\"${webClient}\""
        )







//        val properties = Properties()
//        properties.load(project.rootProject.file("local.properties").inputStream())
//        // Set API keys in BuildConfig
//        buildConfigField("String", "apiKey", "\"${properties.getProperty("apiKey")}\"")



    }

    buildTypes {

        android.buildFeatures.buildConfig = true


        release {

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            val keystoreFile = project.rootProject.file("local.properties")
            val properties = Properties()
            properties.load(keystoreFile.inputStream())
            val apiKey = properties.getProperty("API_KEY") ?: ""

            buildConfigField(
                type = "String",
                name = "API_KEY",
                value = "\"${apiKey}\""
            )

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        //buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.generativeai)
    implementation(libs.firebase.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("io.coil-kt:coil-compose:2.7.0")

    // add the dependency for the Google AI client SDK for Android
    implementation("com.google.ai.client.generativeai:generativeai:0.7.0")


    implementation ("com.google.dagger:hilt-android:2.51.1")
    kapt ("com.google.dagger:hilt-compiler:2.51.1")

    // For instrumentation tests
    androidTestImplementation  ("com.google.dagger:hilt-android-testing:2.51.1")
    kaptAndroidTest ("com.google.dagger:hilt-compiler:2.51.1")

    // For local unit tests
    testImplementation ("com.google.dagger:hilt-android-testing:2.51.1")
    kaptTest ("com.google.dagger:hilt-compiler:2.51.1")

    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

}

kapt {
    correctErrorTypes = true
}