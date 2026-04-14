package com.drewjya.pdfmaster.updater

interface UpdateSource {
    /** Returns available releases, newest first. */
    suspend fun fetchReleases(): ReleaseInfo
}
