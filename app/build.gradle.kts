plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.cse441.ergon"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cse441.ergon"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("com.google.android.material:material:1.10.0")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("com.google.firebase:firebase-auth:21.0.1")
    implementation(libs.appcompat)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.circleimageview)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.firestore)
    implementation(libs.viewpager2)
    implementation(libs.itextg)
    implementation(libs.itext7.core)
    implementation(libs.mhiew.android.pdf.viewer)
    implementation(libs.okhttp) // iText cho xử lý PDF
//     implementation(libs.itextpdf) // Bạn có thể giữ dòng này nếu cần
    // implementation(libs.pdfbox.android) // Đảm bảo dòng này đã được comment lại nếu không sử dụng PDFBox

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

