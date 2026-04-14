package com.drewjya.pdfmaster.di

import com.drewjya.pdfmaster.data.AppPreferences
import com.drewjya.pdfmaster.viewmodel.PdfViewModel
import com.pavi2410.appupdater.AppUpdater
import com.pavi2410.appupdater.github
import org.koin.dsl.module

val appModule =
    module {
        single { AppPreferences() }
        single { PdfViewModel(get()) }
        single {
            AppUpdater.github(
                owner = "drewjya",
                repo = "pdfmaster",
                currentVersion = "1.0.3",
            )
        }
    }
