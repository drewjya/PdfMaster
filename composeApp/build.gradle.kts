import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

val koinVersion = "3.5.6"
val koinComposeVersion = "1.1.5"

kotlin {
    jvmToolchain(21)
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.filekit.core)
            implementation(libs.filekit.dialogs.compose)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.material.icons.core)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation("org.jetbrains.compose.material3.adaptive:adaptive:1.0.0")
            implementation("com.github.skydoves:colorpicker-compose:1.1.3")
            implementation("io.insert-koin:koin-core:$koinVersion")
            implementation("io.insert-koin:koin-compose:$koinComposeVersion")
            implementation("com.cheonjaeung.compose.grid:grid:2.7.1")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation("org.apache.pdfbox:pdfbox:3.0.1")
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
            // Added Msi and Exe for Windows
            targetFormats(
                TargetFormat.Dmg,
                TargetFormat.Pkg,
                TargetFormat.Msi,
                TargetFormat.Exe,
            )

            packageName = "PdfMaster"
            packageVersion = "1.0.2"

            macOS {
                bundleID = "com.drewjya.pdfmaster"
                iconFile.set(project.file("icons/icon.icns"))
            }

            // New block for Windows-specific installer settings
            windows {
                iconFile.set(project.file("icons/icon.ico"))
                menuGroup = "PdfMaster" // Creates a folder in the Start Menu
                dirChooser = true // Lets the user pick where to install it
                perUserInstall = true
                upgradeUuid = "100041b7-4242-4f62-bb44-395e4bc34e27"

                shortcut = true // Creates a Desktop shortcut
            }
        }
    }
}
