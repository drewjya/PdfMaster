package com.drewjya.pdfmaster.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drewjya.pdfmaster.components.SnackbarMessage
import com.drewjya.pdfmaster.data.AppPreferences
import com.drewjya.pdfmaster.helper.getAvailableFonts
import java.io.File
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PdfViewModel(
    private val prefs: AppPreferences,
) : ViewModel() {
    private val _pdfFiles = MutableStateFlow<List<File>>(emptyList())
    val pdfFiles: StateFlow<List<File>> = _pdfFiles.asStateFlow()


    fun addFiles(newFiles: List<File>) {
        val onlyPdfs = newFiles.filter { it.extension.equals("pdf", ignoreCase = true) }
        // Emitting a brand new list reference triggers the StateFlow collector
        _pdfFiles.value = _pdfFiles.value + onlyPdfs

        println("PDFs: ${_pdfFiles.value.size}")
    }

    fun removeFile(file: File) {
        _pdfFiles.value = _pdfFiles.value.filter { it != file }
    }

    val snackbarMessage = mutableStateOf<SnackbarMessage?>(null)

    val fonts = MutableStateFlow(getAvailableFonts())

    fun moveFile(
        file: File,
        isUp: Boolean,
    ) {
        val index = _pdfFiles.value.indexOf(file)
        if (index != -1) {
            if (isUp && index > 0) {
                _pdfFiles.value =
                    _pdfFiles.value.toMutableList().apply {
                        add(index - 1, removeAt(index))
                    }
            } else if (!isUp && index < _pdfFiles.value.size - 1) {
                _pdfFiles.value =
                    _pdfFiles.value.toMutableList().apply {
                        add(index + 1, removeAt(index))
                    }
            }
        }
    }

    fun clearFiles() {
        _pdfFiles.value = emptyList()
    }

    val isDragging = mutableStateOf(false)

    fun setDragging(value: Boolean) {
        if (value != isDragging.value) {
            isDragging.value = value
        }
    }

    val selectedDirectory = mutableStateOf(prefs.selectedDirectory)
    val selectedName = mutableStateOf(prefs.selectedName)
    val x = mutableStateOf(prefs.x)
    val y = mutableStateOf(prefs.y)
    val color = mutableStateOf(Color(prefs.color.toULong()))
    val opacity = mutableStateOf(prefs.opacity)
    val position = mutableStateOf(prefs.position)
    val rotation = mutableStateOf(prefs.rotation)
    val pageFormat = mutableStateOf(prefs.pageFormat)
    val watermarkFontSize = mutableStateOf(prefs.watermarkFontSize)
    val numberingFontSize = mutableStateOf(prefs.numberingFontSize)
    val watermarkText = mutableStateOf(prefs.watermarkText)
    val font = mutableStateOf(prefs.font)
    val dateFormat = mutableStateOf(prefs.dateFormat)

    val monthlyDirectory = mutableStateOf(prefs.monthlyDirectory)
    val inputDirectory = mutableStateOf(prefs.inputDirectory)
    val selectedDate = mutableStateOf(prefs.selectedDate)

    init {
        // Automatically save to local storage whenever a state changes
        snapshotFlow { selectedDate.value }.onEach { prefs.selectedDate = it }.launchIn(viewModelScope)
        snapshotFlow { dateFormat.value }.onEach { prefs.dateFormat = it }.launchIn(viewModelScope)
        snapshotFlow { inputDirectory.value }.onEach { prefs.inputDirectory = it }.launchIn(viewModelScope)
        snapshotFlow { monthlyDirectory.value }.onEach { prefs.monthlyDirectory = it }.launchIn(viewModelScope)
        snapshotFlow { selectedDirectory.value }.onEach { prefs.selectedDirectory = it }.launchIn(viewModelScope)
        snapshotFlow { selectedName.value }.onEach { prefs.selectedName = it }.launchIn(viewModelScope)
        snapshotFlow { x.value }.onEach { prefs.x = it }.launchIn(viewModelScope)
        snapshotFlow { y.value }.onEach { prefs.y = it }.launchIn(viewModelScope)
        snapshotFlow { color.value }.onEach { prefs.color = it.value.toLong() }.launchIn(viewModelScope)
        snapshotFlow { opacity.value }.onEach { prefs.opacity = it }.launchIn(viewModelScope)
        snapshotFlow { position.value }.onEach { prefs.position = it }.launchIn(viewModelScope)
        snapshotFlow { rotation.value }.onEach { prefs.rotation = it }.launchIn(viewModelScope)
        snapshotFlow { pageFormat.value }.onEach { prefs.pageFormat = it }.launchIn(viewModelScope)
        snapshotFlow { watermarkFontSize.value }.onEach { prefs.watermarkFontSize = it }.launchIn(viewModelScope)
        snapshotFlow { numberingFontSize.value }.onEach { prefs.numberingFontSize = it }.launchIn(viewModelScope)
        snapshotFlow { watermarkText.value }.onEach { prefs.watermarkText = it }.launchIn(viewModelScope)
        snapshotFlow { font.value }.onEach { prefs.font = it }.launchIn(viewModelScope)
    }

    // Keep these if you prefer using methods, otherwise you can modify the .value directly
    // from UI like you do with `opacity` and `rotation`.
    fun setDirectory(directory: String) {
        selectedDirectory.value = directory
    }

    fun setName(newName: String) {
        selectedName.value = newName
    }
}
