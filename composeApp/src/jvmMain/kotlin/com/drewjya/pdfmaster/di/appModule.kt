package com.drewjya.pdfmaster.di

import com.drewjya.pdfmaster.data.ConfigManager
import com.drewjya.pdfmaster.design.AppTheme
import com.drewjya.pdfmaster.design.PrimaryAppTheme
import com.drewjya.pdfmaster.updater.AppUpdater
import com.drewjya.pdfmaster.updater.DesktopAssetDownloader
import com.drewjya.pdfmaster.updater.DesktopAssetInstaller
import com.drewjya.pdfmaster.updater.GitHubUpdateSource
import com.drewjya.pdfmaster.viewmodel.ConfigViewModel
import com.drewjya.pdfmaster.viewmodel.PdfViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule =
    module {
        viewModelOf(::PdfViewModel)
        single { ConfigManager() }
        viewModelOf(::ConfigViewModel)

        single {
            AppUpdater(
                source =
                    GitHubUpdateSource(
                        owner = "drewjya",
                        repo = "pdfmaster",
                    ),
                currentVersion = "1.1.6",
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

        single<AppTheme> {
            PrimaryAppTheme()
        }
    }
