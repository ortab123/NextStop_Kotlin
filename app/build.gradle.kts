plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin") version "2.9.0"
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")


}

android {
    namespace = "com.example.final_nextstop"
    compileSdk = 35

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.final_nextstop"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.cardview)
    implementation(libs.glide)
    kapt(libs.glide.compiler)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.dhaval2404.imagepicker)
    implementation (libs.circleindicator)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
//    implementation(platform(libs.firebase.bom))
//    implementation(libs.firebase.analytics)
//    implementation (libs.firebase.auth.ktx)
//    implementation(libs.firebase.firestore.ktx)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.play.services.maps)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    kapt (libs.androidx.hilt.compiler)





}

// לא הצליח
//implementation("androidx.hilt:hilt-navigation-fragment:1.0.0")//
//implementation (libs.androidx.hilt.lifecycle.viewmodel)
//implementation (libs.androidx.lifecycle.common.java8)


//הצליח
////kapt("com.google.dagger:hilt-android-compiler:2.51.1")
////kapt("androidx.hilt:hilt-compiler:1.0.0")//
////implementation("com.google.dagger:hilt-android:2.51.1")