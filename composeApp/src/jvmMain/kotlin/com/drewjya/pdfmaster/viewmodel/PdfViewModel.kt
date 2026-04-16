package com.drewjya.pdfmaster.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.drewjya.pdfmaster.components.SnackbarMessage
import com.drewjya.pdfmaster.helper.getAvailableFonts
import java.io.File
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PdfViewModel : ViewModel() {
    private val _pdfFiles = MutableStateFlow<List<File>>(emptyList())
    val pdfFiles: StateFlow<List<File>> = _pdfFiles.asStateFlow()

    fun addFiles(newFiles: List<File>) {
        val onlyPdfs = newFiles.filter { it.extension.equals("pdf", ignoreCase = true) }
        _pdfFiles.value += onlyPdfs
        _pdfFiles.value = _pdfFiles.value.distinctBy { it.absolutePath }
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


}
