# Quickstart Guide: HerPace Android App

**Branch**: 001-android-app
**Date**: 2026-02-10
**Target**: Developers setting up the Android project for the first time

## Prerequisites

Before starting, ensure you have:

1. **Android Studio**: Latest stable version (Hedgehog 2023.1.1 or newer)
   - Download: https://developer.android.com/studio
   - Includes Android SDK, emulator, and Gradle

2. **JDK**: JDK 17 or newer (bundled with Android Studio)
   - Verify: `java -version`

3. **Git**: For cloning the repository
   - Verify: `git --version`

4. **Physical Device or Emulator**:
   - **Physical Device**: Android 8.0 (API 26) or higher
   - **Emulator**: Create an AVD with API 26+ (recommended: API 34 Pixel 6)

5. **API Access**:
   - Backend API URL (production or local development)
   - Firebase project for push notifications (optional for initial development)

---

## Project Setup

### Step 1: Clone Repository

```bash
cd C:\Dev\HerPaceAI\HerPace
git checkout 001-android-app
```

The Android app will be created in the `android/` directory (new directory).

---

### Step 2: Create Android Project

1. **Open Android Studio** → **New Project**

2. **Select Template**:
   - Choose **Empty Activity** (Compose)
   - This provides Jetpack Compose starter template

3. **Configure Project**:
   ```
   Name: HerPace
   Package name: com.herpace
   Save location: C:\Dev\HerPaceAI\HerPace\android
   Language: Kotlin
   Minimum SDK: API 26 (Android 8.0 - Oreo)
   Build configuration language: Kotlin DSL (build.gradle.kts)
   ```

4. **Click Finish** and wait for Gradle sync

---

### Step 3: Configure Build Files

#### 3.1 Project-Level `build.gradle.kts`

Located at `android/build.gradle.kts`:

```kotlin
// Top-level build file
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}
```

#### 3.2 App-Level `build.gradle.kts`

Located at `android/app/build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.herpace"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.herpace"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            buildConfigField("String", "API_BASE_URL", "\"https://localhost:7001\"")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField(
                "String",
                "API_BASE_URL",
                "\"https://herpace-api-330702404265.us-central1.run.app\""
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

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Hilt Dependency Injection
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // Retrofit & OkHttp (Networking)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // Kotlinx Serialization (JSON)
    implementation(libs.kotlinx.serialization.json)

    // Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // WorkManager (Background Tasks)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)

    // Firebase (Cloud Messaging, Crashlytics, Analytics)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    // Security (Encrypted SharedPreferences)
    implementation(libs.androidx.security.crypto)

    // Health Connect (Google Fit replacement)
    implementation(libs.androidx.health.connect)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
```

#### 3.3 Version Catalog (`libs.versions.toml`)

Create `android/gradle/libs.versions.toml`:

```toml
[versions]
agp = "8.2.0"
kotlin = "1.9.22"
compose-bom = "2024.02.00"
hilt = "2.50"
room = "2.6.1"
retrofit = "2.9.0"
okhttp = "4.12.0"
kotlinx-serialization = "1.6.2"
work = "2.9.0"
firebase-bom = "32.7.1"
health-connect = "1.1.0-alpha07"
ksp = "1.9.22-1.0.17"

[libraries]
# Jetpack Compose
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "compose-bom" }
androidx-compose-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-compose-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-compose-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }
androidx-compose-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version = "1.8.2" }
androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version = "2.7.6" }
androidx-lifecycle-runtime-compose = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version = "2.7.0" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version = "2.7.0" }

# Hilt
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-compiler", version.ref = "hilt" }
androidx-hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version = "1.1.0" }
androidx-hilt-work = { group = "androidx.hilt", name = "hilt-work", version = "1.1.0" }
androidx-hilt-compiler = { group = "androidx.hilt", name = "hilt-compiler", version = "1.1.0" }

# Networking
retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
retrofit-converter-kotlinx-serialization = { group = "com.jakewharton.retrofit", name = "retrofit2-kotlinx-serialization-converter", version = "1.0.0" }
okhttp = { group = "com.squareup.okhttp3", name = "okhttp", version.ref = "okhttp" }
okhttp-logging-interceptor = { group = "com.squareup.okhttp3", name = "logging-interceptor", version.ref = "okhttp" }

# Serialization
kotlinx-serialization-json = { group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-json", version.ref = "kotlinx-serialization" }

# Room
androidx-room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
androidx-room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
androidx-room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }

# WorkManager
androidx-work-runtime-ktx = { group = "androidx.work", name = "work-runtime-ktx", version.ref = "work" }

# Firebase
firebase-bom = { group = "com.google.firebase", name = "firebase-bom", version.ref = "firebase-bom" }
firebase-messaging = { group = "com.google.firebase", name = "firebase-messaging" }
firebase-analytics = { group = "com.google.firebase", name = "firebase-analytics" }
firebase-crashlytics = { group = "com.google.firebase", name = "firebase-crashlytics" }

# Security
androidx-security-crypto = { group = "androidx.security", name = "security-crypto", version = "1.1.0-alpha06" }

# Health Connect
androidx-health-connect = { group = "androidx.health.connect", name = "connect-client", version.ref = "health-connect" }

# Testing
junit = { group = "junit", name = "junit", version = "4.13.2" }
mockk = { group = "io.mockk", name = "mockk", version = "1.13.8" }
kotlinx-coroutines-test = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version = "1.7.3" }
turbine = { group = "app.cash.turbine", name = "turbine", version = "1.0.0" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version = "1.1.5" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version = "3.5.1" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
hilt-android = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
```

---

### Step 4: Initialize Hilt

#### 4.1 Create Application Class

Create `android/app/src/main/java/com/herpace/HerPaceApplication.kt`:

```kotlin
package com.herpace

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HerPaceApplication : Application()
```

#### 4.2 Update AndroidManifest.xml

Edit `android/app/src/main/AndroidManifest.xml`:

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

    <application
        android:name=".HerPaceApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.HerPace">

        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:theme="@style/Theme.HerPace">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

    </application>

</manifest>
```

---

### Step 5: Create Project Structure

Create the following directory structure:

```bash
cd android/app/src/main/java/com/herpace

# Data layer
mkdir -p data/local data/remote data/repository data/integrations

# Domain layer
mkdir -p domain/model domain/repository domain/usecase

# Presentation layer
mkdir -p presentation/auth presentation/dashboard presentation/races
mkdir -p presentation/plan presentation/session presentation/profile
mkdir -p presentation/common

# Infrastructure
mkdir -p di notification util
```

---

### Step 6: Set Up Networking (Retrofit)

#### 6.1 Create NetworkModule

Create `android/app/src/main/java/com/herpace/di/NetworkModule.kt`:

```kotlin
package com.herpace.di

import com.herpace.BuildConfig
import com.herpace.data.remote.HerPaceApiService
import com.herpace.data.repository.AuthTokenProvider
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authTokenProvider: AuthTokenProvider
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                val token = authTokenProvider.getToken()
                if (token != null && !chain.request().url.encodedPath.contains("/api/auth/")) {
                    request.addHeader("Authorization", "Bearer $token")
                }
                chain.proceed(request.build())
            }
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideHerPaceApiService(retrofit: Retrofit): HerPaceApiService {
        return retrofit.create(HerPaceApiService::class.java)
    }
}
```

---

### Step 7: Run the App

1. **Connect Device or Start Emulator**:
   - Physical device: Enable USB debugging in Developer Options
   - Emulator: Start from AVD Manager in Android Studio

2. **Run the App**:
   - Click **Run** (green play button) in Android Studio
   - Select target device
   - Wait for build and installation

3. **Verify**:
   - App launches successfully
   - No crashes in Logcat
   - Basic Compose UI displays

---

## Development Workflow

### Running the App

```bash
# From command line (optional, Android Studio is easier)
cd android
./gradlew installDebug
```

### Running Tests

```bash
# Unit tests
./gradlew test

# Instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Specific test class
./gradlew test --tests com.herpace.data.repository.AuthRepositoryTest
```

### Building Release APK

```bash
./gradlew assembleRelease
# Output: android/app/build/outputs/apk/release/app-release.apk
```

---

## Configuration for Local Development

### Backend API

If running the backend locally:

1. **Start Backend**:
   ```bash
   cd C:\Dev\HerPaceAI\HerPace
   dotnet run --project backend/src/HerPace.API
   ```

2. **Update API URL** in `build.gradle.kts` debug build type:
   ```kotlin
   buildConfigField("String", "API_BASE_URL", "\"https://10.0.2.2:7001\"")
   ```
   - Use `10.0.2.2` for Android emulator (maps to host's localhost)
   - Use actual IP (e.g., `192.168.1.100:7001`) for physical device

3. **Trust Self-Signed Certificate** (for HTTPS on localhost):
   - Add to `res/xml/network_security_config.xml`
   - Reference in AndroidManifest.xml: `android:networkSecurityConfig="@xml/network_security_config"`

---

## Troubleshooting

### Gradle Sync Failed

- **Solution**: File → Invalidate Caches → Invalidate and Restart
- Verify `gradle/wrapper/gradle-wrapper.properties` uses Gradle 8.2+
- Check internet connection (Gradle downloads dependencies)

### Hilt Compilation Errors

- Ensure `@HiltAndroidApp` is on Application class
- Verify KSP plugin version matches Kotlin version
- Clean build: `./gradlew clean build`

### Retrofit API Calls Failing

- Check `BuildConfig.API_BASE_URL` is correct
- Verify backend is running (visit URL in browser)
- Enable OkHttp logging (should be enabled in debug by default)
- For emulator + localhost: use `10.0.2.2` instead of `localhost` or `127.0.0.1`

### Compose Preview Not Working

- File → Settings → Experimental → Enable Compose Preview
- Rebuild project
- Ensure `@Preview` functions are not inside classes

---

## Implementation Notes

The following deviations from the original quickstart were made during implementation:

### Build Configuration Differences

- **AGP version**: Using `8.3.2` (not `8.2.0`) for latest Compose compiler compatibility
- **API Base URL**: Debug builds point to the production Cloud Run API (`https://herpace-api-330702404265.us-central1.run.app/`), not localhost. Change to `https://10.0.2.2:7001/` for local backend development.
- **Read timeout**: Extended to 4 minutes (from 30s) to accommodate AI plan generation latency
- **google-services plugin**: Added for Firebase integration (`com.google.gms.google-services`)
- **Firebase Crashlytics plugin**: Added (`com.google.firebase.crashlytics`)
- **Kotlin Compose plugin**: Using KSP-based compiler extension (`1.5.10`) instead of the `kotlin.plugin.compose` plugin

### Additional Dependencies (not in original quickstart)

- `material-icons-extended` for comprehensive icon set
- `lifecycle-runtime-ktx` for coroutine lifecycle support
- `sqlcipher-android` + `sqlite-ktx` for database encryption
- `firebase-crashlytics` + `firebase-analytics` for observability
- `biometric` for optional app lock feature

### Security Hardening

- `allowBackup="false"` in AndroidManifest (prevents data extraction)
- `networkSecurityConfig` added with cleartext traffic disabled and certificate pinning
- SQLCipher key derived from Android Keystore (not hardcoded)
- All SharedPreferences use `EncryptedSharedPreferences`
- Certificate pinning enabled for production builds (GTS Root R1/R2)

### Architecture Patterns

- **Offline-first**: All repositories follow try-API-then-cache pattern for reads, local-save-and-sync for writes
- **Background sync**: WorkManager-based hourly sync + on-demand sync via `SyncManager`
- **Conflict resolution**: Server-wins strategy with user notification via `SyncManager.recordConflictsResolved()`
- **Retry logic**: `safeApiCallWithRetry` with exponential backoff for GET operations (retries on 5xx, 429, network errors)

---

## Implemented Features

All 8 user stories have been implemented:

1. **Authentication**: Login, signup with JWT token storage in EncryptedSharedPreferences
2. **Onboarding**: Profile creation with fitness level, cycle data, weekly mileage
3. **Race Management**: Create, edit, delete races with offline support
4. **AI Plan Generation**: Generate training plans via backend Gemini API
5. **Daily Sessions**: Week-by-week and calendar views, session completion tracking
6. **Workout Logging**: Log distance, duration, RPE, notes for completed sessions
7. **Push Notifications**: FCM-based workout reminders with local scheduling
8. **Profile & Cycle Tracking**: Update cycle data, recalculate phases, manual sync

---

## Resources

- **Android Developer Documentation**: https://developer.android.com/
- **Jetpack Compose Tutorial**: https://developer.android.com/jetpack/compose/tutorial
- **Hilt Documentation**: https://developer.android.com/training/dependency-injection/hilt-android
- **Retrofit Documentation**: https://square.github.io/retrofit/
- **Room Documentation**: https://developer.android.com/training/data-storage/room
