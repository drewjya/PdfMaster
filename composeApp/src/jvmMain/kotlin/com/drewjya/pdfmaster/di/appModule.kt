package com.drewjya.pdfmaster.di

import com.drewjya.pdfmaster.data.AppPreferences
import com.drewjya.pdfmaster.updater.AppUpdater
import com.drewjya.pdfmaster.updater.DesktopAssetDownloader
import com.drewjya.pdfmaster.updater.DesktopAssetInstaller
import com.drewjya.pdfmaster.updater.GitHubUpdateSource
import com.drewjya.pdfmaster.viewmodel.PdfViewModel
import org.koin.dsl.module

val appModule =
    module {
        single { AppPreferences() }
        single { PdfViewModel(get()) }

        single {
            AppUpdater(
                source = GitHubUpdateSource(
                    owner = "drewjya",
                    repo = "pdfmaster",
                ),
                currentVersion = "1.0.6",
                assetMatcher = { name ->
                    name.endsWith(".msi") ||
                            name.endsWith(".exe") ||
                            name.endsWith(".dmg") ||
                            name.endsWith(".AppImage") ||
                            name.endsWith(".deb") ||
                            name.endsWith(".rpm") ||
                            name.endsWith(".jar")
                },
                downloader = DesktopAssetDownloader(),
                installer = DesktopAssetInstaller(),
            )
        }
    }
