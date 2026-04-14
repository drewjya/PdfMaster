package com.drewjya.pdfmaster.updater

interface AssetInstaller {
    /**
     * Triggers the platform's native install flow for the file at [filePath].
     */
    fun install(filePath: String)
}