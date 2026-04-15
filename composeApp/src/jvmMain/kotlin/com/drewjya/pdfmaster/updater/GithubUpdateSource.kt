package com.drewjya.pdfmaster.updater

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * [UpdateSource] backed by GitHub Releases API.
 *
 * Fetches releases from `https://api.github.com/repos/{owner}/{repo}/releases`.
 */
class GitHubUpdateSource(
    val owner: String,
    val repo: String,
    httpClient: HttpClient? = null,
) : UpdateSource {
    init {
        require(owner.isNotBlank()) { "owner must not be blank" }
        require(repo.isNotBlank()) { "repo must not be blank" }
    }

    internal val apiUrl: String
        get() = "https://api.github.com/repos/$owner/$repo/releases"

    private val client: HttpClient =
        httpClient ?: HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    },
                )
            }
        }

    override suspend fun fetchReleases(): ReleaseInfo {
        val url = "$apiUrl/latest"

        val response: HttpResponse =
            client.get(url) {
                header("Accept", "application/vnd.github+json")
                header("User-Agent", "pdfstudio-app-updater")
            }

        val ghRelease: GitHubRelease = response.body()
        return ghRelease.toReleaseInfo()
    }

    fun close() {
        client.close()
    }
}
