# Dependencies.md — Exact Versions & Gradle Configuration

## ⚠️ Rules for the Agent
- **Do NOT** change any version that already exists in `libs.versions.toml`.
- **Only ADD** new entries for missing libraries.
- All new libraries must use `libs.versions.toml` catalog entries — no hardcoded version strings in `build.gradle.kts`.
- The `compileSdk` block in `build.gradle.kts` currently uses an invalid DSL. Replace it with `compileSdk = 36`.

---

## Existing Versions — DO NOT CHANGE

| Key | Value |
|---|---|
| `agp` | `9.1.0` |
| `kotlin` | `2.2.10` |
| `coreKtx` | `1.18.0` |
| `composeBom` | `2024.09.00` |
| `lifecycleRuntimeKtx` | `2.10.0` |
| `activityCompose` | `1.13.0` |
| `junit` | `4.13.2` |
| `junitVersion` | `1.3.0` |
| `espressoCore` | `3.7.0` |

---

## New Versions to ADD in `[versions]`

```toml
room = "2.6.1"
retrofit = "2.11.0"
okhttp = "4.12.0"
coil = "2.7.0"
navigationCompose = "2.8.5"
workManager = "2.9.1"
lifecycleViewmodelCompose = "2.8.7"
materialIconsExtended = "1.7.8"
```

---

## New Libraries to ADD in `[libraries]`

```toml
# Room
androidx-room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
androidx-room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
androidx-room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }

# Retrofit
retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
retrofit-gson = { group = "com.squareup.retrofit2", name = "converter-gson", version.ref = "retrofit" }
okhttp-logging = { group = "com.squareup.okhttp3", name = "logging-interceptor", version.ref = "okhttp" }

# Coil (image loading)
coil-compose = { group = "io.coil-kt", name = "coil-compose", version.ref = "coil" }

# Navigation
androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }

# WorkManager
androidx-work-runtime-ktx = { group = "androidx.work", name = "work-runtime-ktx", version.ref = "workManager" }

# ViewModel Compose
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleViewmodelCompose" }

# Material Icons Extended
androidx-compose-material-icons-extended = { group = "androidx.compose.material", name = "material-icons-extended", version.ref = "materialIconsExtended" }
```

---

## New Plugins to ADD in `[plugins]`

```toml
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
```

---

## Updated `build.gradle.kts` (App Level) — Full File

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.2.10-1.0.29"
}

android {
    namespace = "com.exmple.movieexplorer"
    compileSdk = 36                          // ← FIXED (was invalid DSL block)

    defaultConfig {
        applicationId = "com.exmple.movieexplorer"
        minSdk = 24
        targetSdk = 36
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

    buildFeatures {
        compose = true
    }
}

dependencies {
    // --- Existing (DO NOT CHANGE) ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // --- New: Room ---
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // --- New: Retrofit + OkHttp ---
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)

    // --- New: Coil ---
    implementation(libs.coil.compose)

    // --- New: Navigation ---
    implementation(libs.androidx.navigation.compose)

    // --- New: WorkManager ---
    implementation(libs.androidx.work.runtime.ktx)

    // --- New: ViewModel Compose ---
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // --- New: Material Icons Extended ---
    implementation(libs.androidx.compose.material.icons.extended)

    // --- Tests (DO NOT CHANGE) ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
```

---

## `AndroidManifest.xml` Required Permissions

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

---

## KSP Note
KSP (Kotlin Symbol Processing) is required for Room annotation processing.
Version `2.2.10-1.0.29` is the compatible KSP version for Kotlin `2.2.10`.
It is applied directly via `id("com.google.devtools.ksp") version "..."` since there is no existing KSP entry in the catalog.
