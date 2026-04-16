package com.drewjya.pdfmaster.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drewjya.pdfmaster.data.ConfigManager
import com.drewjya.pdfmaster.helper.BatchSettings
import com.drewjya.pdfmaster.helper.EnhancementType
import com.drewjya.pdfmaster.helper.IdentitySettings
import com.drewjya.pdfmaster.helper.MergeSettings
import com.drewjya.pdfmaster.helper.NumberingSettings
import com.drewjya.pdfmaster.helper.OutputConfiguration
import com.drewjya.pdfmaster.helper.ProcessMode
import com.drewjya.pdfmaster.helper.UserConfigs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ConfigViewModel(
    private val configManager: ConfigManager,
) : ViewModel() {
    // 1. Source of truth: The entire configuration state from the JSON file
    val uiState: StateFlow<UserConfigs?> =
        configManager.store.updates
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null,
            )

    // 2. Helper to get the currently active configuration object
    val activeConfig: Flow<OutputConfiguration?> =
        uiState.map { state ->
            state?.savedConfigs?.get(state.activeConfigId)
        }

    /**
     * CORE LOGIC: Enforces the "Max 3" rule.
     * Checks if adding a new mode or enhancement exceeds the limit.
     */
    private fun canToggle(
        mode: ProcessMode,
        enhancements: Set<EnhancementType>,
    ): Boolean {
        val modeActiveCount = if (mode != ProcessMode.None) 1 else 0
        return (modeActiveCount + enhancements.size) <= 3
    }

    // --- CONFIGURATION MANAGEMENT ---

//    fun createNewConfig(name: String) {
//        viewModelScope.launch {
//            val newId =
//                java.util.UUID
//                    .randomUUID()
//                    .toString()
//            val newConfig =
//                OutputConfiguration(
//                    id = newId,
//                    name = name,
//                    targetDirectory = System.getProperty("user.home"),
//                )
//            configManager.saveConfig(newConfig)
//            configManager.setActiveConfig(newId)
//        }
//    }
//
//    fun switchActiveConfig(id: String) {
//        viewModelScope.launch {
//            configManager.setActiveConfig(id)
//        }
//    }
//
//    fun deleteConfig(id: String) {
//        viewModelScope.launch {
//            configManager.deleteConfig(id)
//        }
//    }

    fun updateProcessMode(newMode: ProcessMode) {
        val current = uiState.value?.savedConfigs?.get(uiState.value?.activeConfigId) ?: return
        if (canToggle(newMode, current.activeEnhancements)) {
            viewModelScope.launch {
                configManager.saveConfig(current.copy(mode = newMode))
            }
        }
    }

    fun toggleEnhancement(type: EnhancementType) {
        val current = uiState.value?.savedConfigs?.get(uiState.value?.activeConfigId) ?: return
        val isEnabled = current.activeEnhancements.contains(type)

        val newSet =
            if (isEnabled) {
                current.activeEnhancements - type
            } else {
                current.activeEnhancements + type
            }

        viewModelScope.launch {
            configManager.saveConfig(current.copy(activeEnhancements = newSet))
        }
    }

    // --- FINE-GRAINED SETTINGS UPDATES ---

    fun updateBasicInfo(
        name: String? = null,
        targetDir: String? = null,
    ) {
        updateActive {
            it.copy(
                name = name ?: it.name,
                targetDirectory = targetDir ?: it.targetDirectory,
            )
        }
    }

    fun updateBatchSettings(update: (BatchSettings) -> BatchSettings) {
        updateActive { it.copy(batchSettings = update(it.batchSettings)) }
    }

    fun updateMergeSettings(update: (MergeSettings) -> MergeSettings) {
        updateActive { it.copy(mergeSettings = update(it.mergeSettings)) }
    }

    fun updateIdentitySettings(update: (IdentitySettings) -> IdentitySettings) {
        updateActive { it.copy(identitySettings = update(it.identitySettings)) }
    }

    fun updateNumberingSettings(update: (NumberingSettings) -> NumberingSettings) {
        updateActive { it.copy(numberingSettings = update(it.numberingSettings)) }
    }

    /**
     * Private helper to reduce boilerplate for saving changes to the active config
     */
    private fun updateActive(block: (OutputConfiguration) -> OutputConfiguration) {
        val state = uiState.value ?: return
        val active = state.savedConfigs[state.activeConfigId] ?: return
        viewModelScope.launch {
            configManager.saveConfig(block(active))
        }
    }
}
