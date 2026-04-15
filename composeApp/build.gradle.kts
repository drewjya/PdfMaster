import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    jvmToolchain(21)
    jvm()

    sourceSets {
        commonMain.dependencies {
            // Compose & UI
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)
            implementation(libs.compose.material3)
            implementation(libs.compose.material3.adaptive)
            implementation(libs.compose.components.resources)
            implementation(libs.material.icons.core)
            implementation(libs.colorpicker.compose)

            // Lifecycle & DI
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.core.viewmodel)

            // Ktor & Logic
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation("io.github.xxfast:kstore:0.9.1")
            implementation("io.github.xxfast:kstore-file:0.9.1")

            // File Handling
            implementation(libs.filekit.core)
            implementation(libs.filekit.compose)

            implementation(libs.slf4j.simple)
            implementation(libs.jcl.over.slf4j)

            implementation("com.github.librepdf:openpdf:2.0.3")
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.apache.pdfbox)
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.drewjya.pdfmaster.MainKt"

        buildTypes.release.proguard {
            isEnabled.set(false)
        }

        nativeDistributions {

//            modules("jdk.incubator.vector", "java.logging", "jdk.unsupported")
//
//            jvmArgs(
//                "--add-opens=java.base/java.nio=ALL-UNNAMED",
//                "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED"
//            )

            targetFormats(
                TargetFormat.Dmg,
                TargetFormat.Pkg,
                TargetFormat.Msi,
                TargetFormat.Exe,
            )

            packageName = "PdfMaster"
            packageVersion = "1.1.7"

            macOS {
                bundleID = "com.drewjya.pdfmaster"
                iconFile.set(project.file("icons/icon.icns"))
            }

            windows {
                iconFile.set(project.file("icons/icon.ico"))
                menuGroup = "PdfMaster"
                dirChooser = true
                perUserInstall = true
                upgradeUuid = "100041b7-4242-4f62-bb44-395e4bc34e27"
                shortcut = true
            }
        }
    }
}
