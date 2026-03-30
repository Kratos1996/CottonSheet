import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.vanniktechPublish)
}

kotlin {
    androidTarget {
        publishLibraryVariants("release")
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm("desktop")
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs { browser() }
    js(IR) { browser() }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
        }
    }
}

android {
    namespace = "dev.ishant.bottomsheet"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    coordinates(
        groupId = "dev.ishant",
        artifactId = "compose-multiplatform-bottomsheet",
        version = "1.0.1"
    )

    pom {
        name.set("CMP-Bottomsheet")
        description.set(
            "A zero-boilerplate, fully parameterized ModalBottomSheet for " +
            "Compose Multiplatform (Android, iOS, Desktop, Web)."
        )
        url.set("https://github.com/Kratos1996/CMP-Bottomsheet")
        licenses {
            license {
                name.set("Apache-2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0")
            }
        }
        developers {
            developer {
                id.set("Kratos1996")
                name.set("Ishant Sharma")
                email.set("ishant.sharma1947@gmail.com")
            }
        }
        scm {
            url.set("https://github.com/Kratos1996/CMP-Bottomsheet")
            connection.set("scm:git:git://github.com/Kratos1996/CMP-Bottomsheet.git")
            developerConnection.set("scm:git:ssh://git@github.com/Kratos1996/CMP-Bottomsheet.git")
        }
    }
}
