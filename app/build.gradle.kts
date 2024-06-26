plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.registration_page"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.registration_page"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {

        viewBinding = true

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation ("androidx.appcompat:appcompat:1.4.1")
    implementation ("androidx.core:core-ktx:1.7.0")
    implementation ("androidx.webkit:webkit:1.4.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.firebase:firebase-firestore:23.0.0")
    implementation ("com.itextpdf:itextg:5.5.10")
    //implementation ("com.google.android.material:material:1.4.0")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.navigation:navigation-fragment:2.5.3")
    implementation("androidx.navigation:navigation-ui:2.5.3")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation(files("libs\\sendgrid-0.1.2-jar.jar"))
    testImplementation("junit:junit:4.13.2")
    implementation ("com.itextpdf:itext7-core:7.1.15")
    implementation("androidx.browser:browser:1.7.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.25")
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-storage:20.3.0")
}