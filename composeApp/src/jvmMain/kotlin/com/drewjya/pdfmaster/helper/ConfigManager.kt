package com.drewjya.pdfmaster.helper


import io.github.vinceglb.filekit.utils.toPath
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import java.io.File
import org.openpdf.text.FontFactory

class ConfigManager {
    private val appDir = System.getProperty("user.home") + "/.pdfmaster"
    private val filePath = "$appDir/configs.json".toPath()

    init {
        File(appDir).mkdirs()

        FontFactory.registerDirectories()
    }

    private val defaultId =
        java.util.UUID
            .randomUUID()
            .toString()
    private val defaultInitialState =
        UserConfigs(
            activeConfigId = defaultId,
            savedConfigs =
                mapOf(
                    defaultId to
                            OutputConfiguration(
                                id = defaultId,
                                name = "Default Configuration",
                                targetDirectory = System.getProperty("user.home"),
                            ),
                ),
        )

    val store: KStore<UserConfigs> =
        storeOf(
            file = filePath,
            default = defaultInitialState,
        )

//    val configs: Flow<UserConfigs?> = store.updates

    suspend fun saveConfig(config: OutputConfiguration) {
        store.update { current ->
            val updatedMap = (current?.savedConfigs ?: emptyMap()) + (config.id to config)
            current?.copy(savedConfigs = updatedMap)
        }
    }

    suspend fun setActiveConfig(id: String) {
        store.update { current ->
            current?.copy(activeConfigId = id)
        }
    }

    /**
     * Deletes a configuration ONLY if there is more than one present.
     */
    suspend fun deleteConfig(id: String) {
        store.update { current ->
            val currentMap = current?.savedConfigs ?: emptyMap()

            // RULE: Min have 1 config. If only 1 exists, ignore the delete request.
            if (currentMap.size <= 1) return@update current

            val updatedMap = currentMap.minus(id)

            // If we deleted the active one, pick the next available key as the new active
            val newActiveId =
                if (current?.activeConfigId == id) {
                    updatedMap.keys.first()
                } else {
                    current?.activeConfigId ?: updatedMap.keys.first()
                }

            current?.copy(
                savedConfigs = updatedMap,
                activeConfigId = newActiveId,
            )
        }
    }
}
