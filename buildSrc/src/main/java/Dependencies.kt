import org.gradle.api.JavaVersion

object Versions {
    const val kotlin = "1.7.0"
    const val coreKtx = "1.7.0"
    const val hilt = "2.38.1"
    const val appCompat = "1.4.1"
    const val material = "1.6.0"
    const val constraint = "2.1.3"
    const val navigation_fragment = "2.4.2"
    const val navigation_ui = "2.4.2"
    const val junit = "4.13.2"
    const val junit_ext = "1.1.3"
    const val espresso = "3.4.0"
    const val viewModelLifeCycle = "2.4.1"
    const val lifecyleExtension = "2.2.0"
    const val room = "2.4.2"
}

object Libs {
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltCompiler ="com.google.dagger:hilt-compiler:${Versions.hilt}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraint = "androidx.constraintlayout:constraintlayout:${Versions.constraint}"
    const val navigationFragment ="androidx.navigation:navigation-fragment-ktx:${Versions.navigation_fragment}"
    const val navigationUI = "androidx.navigation:navigation-ui-ktx:${Versions.navigation_ui}"
    const val junit = "junit:junit:${Versions.junit}"
    const val junit_ext = "androidx.test.ext:junit:${Versions.junit_ext}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val lifeCycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.viewModelLifeCycle}"
    const val room = "androidx.room:room-ktx:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val lifecyleExtension = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecyleExtension}"
}

object Configs {
    const val compileSdk = 32
    const val minSdk = 23
    const val targetSdk = 32
    const val applicationId = "com.chatapp"
    const val versionCode = 1
    const val versionName = "1.0"
}

object ClassPlugins {
    const val hilt =  "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
}

object Options {
    const val jvmTarget = "1.8"
    val sourceCompatibility = JavaVersion.VERSION_1_8
    val targetCompatibility = JavaVersion.VERSION_1_8
}