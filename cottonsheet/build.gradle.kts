plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.vanniktechPublish)
    alias(libs.plugins.dokka)
    id("signing")
}

kotlin {
    android {
        namespace = "dev.ishant.cottonsheet"
        compileSdk = 37
        minSdk = 24

        optimization {
            consumerKeepRules.apply {
                publish = true
                file("consumer-rules.pro")
            }
        }

        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    val isMac = org.gradle.internal.os.OperatingSystem.current().isMacOsX
    if (isMac) {
        iosX64()
        iosArm64()
        iosSimulatorArm64()
    }

    jvm("desktop")

    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    js {
        browser()
    }

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

extensions.configure<org.gradle.plugins.signing.SigningExtension>("signing") {
    val signingKeyId = project.findProperty("signing.keyId")?.toString()
    val signingPassword = project.findProperty("signing.password")?.toString()
    val signingSecretKey = project.findProperty("signing.secretKey")?.toString()

    if (!signingKeyId.isNullOrBlank() && 
        !signingKeyId.contains("YOUR_KEY_ID") && 
        !signingPassword.isNullOrBlank() && 
        !signingSecretKey.isNullOrBlank()) {
        useInMemoryPgpKeys(signingKeyId, signingSecretKey, signingPassword)
    }
}

mavenPublishing {
    coordinates(
        groupId = "io.github.kratos1996",
        artifactId = "cottonsheet",
        version = "1.0.1"
    )

    publishToMavenCentral()

    val isRelease = project.hasProperty("release") || project.hasProperty("publish")
    val signingKeyId = project.findProperty("signing.keyId")?.toString()
    val signingPassword = project.findProperty("signing.password")?.toString()
    val signingSecretKey = project.findProperty("signing.secretKey")?.toString()
    val signingSecretKeyRingFile = project.findProperty("signing.secretKeyRingFile")?.toString()

    val hasSigningKey = !signingKeyId.isNullOrBlank() &&
            !signingKeyId.contains("YOUR_KEY_ID") &&
            !signingPassword.isNullOrBlank() &&
            (!signingSecretKey.isNullOrBlank() || !signingSecretKeyRingFile.isNullOrBlank())

    if (hasSigningKey && (isRelease || project.hasProperty("signing.keyId"))) {
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