package com.drewjya.pdfmaster.di

import com.drewjya.pdfmaster.data.AppPreferences
import com.drewjya.pdfmaster.viewmodel.PdfViewModel
import org.koin.dsl.module

val appModule =
    module {
        single { AppPreferences() }
        single { PdfViewModel(get()) }
    }
