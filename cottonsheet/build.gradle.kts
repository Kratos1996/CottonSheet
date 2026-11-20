plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.vanniktechPublish)
    alias(libs.plugins.dokka)
    id("signing")
}

group = "io.github.kratos1996"
version = "1.0.7"

kotlin {
    androidTarget {
        publishLibraryVariants("release")
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
    js { browser() }

    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
        }
    }
}

android {
    namespace = "dev.ishant.cottonsheet"
    compileSdk = 37

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = JavaVersion.VERSION_11.toString()
    targetCompatibility = JavaVersion.VERSION_11.toString()
}

extensions.configure<org.gradle.plugins.signing.SigningExtension>("signing") {
    val signingKeyId = project.findProperty("signing.keyId")?.toString()
    val signingPassword = project.findProperty("signing.password")?.toString()
    val signingSecretKey = project.findProperty("signing.secretKey")?.toString()

    if (!signingKeyId.isNullOrBlank() && 
        !signingKeyId.contains("YOUR_KEY_ID") && 
        !signingPassword.isNullOrBlank() && 
        !signingSecretKey.isNullOrBlank()) {
        useInMemoryPgpKeys(signingKeyId, signingSecretKey, signingPassword)
    } else if (project.hasProperty("signingInMemoryKey") && project.hasProperty("signingInMemoryKeyPassword")) {
        useInMemoryPgpKeys(
            project.property("signingInMemoryKey").toString(),
            project.property("signingInMemoryKeyPassword").toString()
        )
    }
}

mavenPublishing {
    coordinates(
        groupId = "io.github.kratos1996",
        artifactId = "cottonsheet",
        version = "1.0.7"
    )

    publishToMavenCentral()

    val isRelease = project.hasProperty("release") || project.hasProperty("publish")
    val signingKeyId = project.findProperty("signing.keyId")?.toString()
    val signingPassword = project.findProperty("signing.password")?.toString()
    val signingSecretKey = project.findProperty("signing.secretKey")?.toString()
    val signingSecretKeyRingFile = project.findProperty("signing.secretKeyRingFile")?.toString()

    val hasSigningKey = (!signingKeyId.isNullOrBlank() && !signingKeyId.contains("YOUR_KEY_ID")) ||
            project.hasProperty("signingInMemoryKey") ||
            project.hasProperty("signing.secretKey") ||
            project.hasProperty("signing.secretKeyRingFile") ||
            project.hasProperty("signing.gnupg.key")

    if (hasSigningKey && (isRelease || project.hasProperty("signing.keyId") || project.hasProperty("signingInMemoryKey"))) {
        signAllPublications()
    }

    pom {
        name.set("CottonSheet")
        description.set("A zero-boilerplate, fully parameterized ModalBottomSheet for Compose Multiplatform.")
        url.set("https://github.com/Kratos1996/CottonSheet")

        licenses {
            license {
                name.set("Apache License 2.0")
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
            url.set("https://github.com/Kratos1996/CottonSheet")
            connection.set("scm:git:https://github.com/Kratos1996/CottonSheet.git")
            developerConnection.set("scm:git:ssh://git@github.com:Kratos1996/CottonSheet.git")
        }
    }
}