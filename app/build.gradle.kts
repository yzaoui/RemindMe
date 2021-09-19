import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

apply(plugin = "de.mannodermaus.android-junit5")
apply(plugin = "androidx.navigation.safeargs.kotlin")

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "com.bitwiserain.remindme"
        minSdk = 26
        targetSdk = 30
        versionCode = 3
        versionName = "0.2.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    sourceSets.all {
        java.srcDirs("src/$name/kotlin")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all {
                it.useJUnitPlatform()
            }
        }
    }
}

dependencies {
    implementation(project(":core"))

    val room_version = "2.3.0"
    val coroutines_version = "1.5.2"
    val junit5_version = "5.7.2"
    val lifecycle_version = "2.3.1"

    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar")))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0-beta01")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.fragment:fragment-ktx:1.3.6")
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    implementation("androidx.navigation:navigation-fragment-ktx:${rootProject.extra["navigation_version"]}")
    implementation("androidx.navigation:navigation-ui-ktx:${rootProject.extra["navigation_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    kapt("androidx.room:room-compiler:$room_version")

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit5_version")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit5_version")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_version")
    testImplementation("io.kotest:kotest-assertions-core-jvm:4.2.6") // Kotest core JVM assertions
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events = setOf(PASSED, SKIPPED, FAILED)
    }
    addTestListener(object : TestListener {
        override fun beforeSuite(suite: TestDescriptor) {}
        override fun afterSuite(suite: TestDescriptor, result: TestResult) {
            if (suite.parent == null) {
                println("\nTest result: ${result.resultType}")
                println("Test summary: ${result.testCount} tests, " +
                        "${result.successfulTestCount} succeeded, " +
                        "${result.failedTestCount} failed, " +
                        "${result.skippedTestCount} skipped")
            }
        }
        override fun beforeTest(testDescriptor: TestDescriptor) {}
        override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {}
    })
}
