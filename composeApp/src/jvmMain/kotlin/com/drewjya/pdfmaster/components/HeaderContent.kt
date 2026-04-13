package com.drewjya.pdfmaster.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drewjya.pdfmaster.Screen
import com.drewjya.pdfmaster.design.AppColor
import com.drewjya.pdfmaster.helper.NumberPosition
import com.drewjya.pdfmaster.helper.PdfConfig
import com.drewjya.pdfmaster.helper.PdfProcessor
import com.drewjya.pdfmaster.helper.ProcessType
import com.drewjya.pdfmaster.viewmodel.PdfViewModel
import org.koin.compose.koinInject
import java.io.File

private val Slate900 = Color(0xFF0F172A)

@Composable
fun HeaderContent(
    screen: Screen,
    modifier: Modifier = Modifier,
) {
    val pdfViewModel: PdfViewModel = koinInject()
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = screen.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Slate900,
            )
            Text(
                text = screen.description,
                fontSize = 14.sp,
                color = Color.Gray,
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (screen != Screen.Files) {
                FileUploader()
            } else {
                Button(
                    onClick = {
                        val inputDirectory = pdfViewModel.inputDirectory.value
                        val directory = File(inputDirectory)
                        if (directory.exists()) {
                            val pdfFiles =
                                directory
                                    .listFiles { file ->
                                        file.isFile && file.extension.equals("pdf", ignoreCase = true)
                                    }.orEmpty()
                                    .toList()
                            val files = pdfFiles.filter { it.name.contains("-") }
                            pdfViewModel.addFiles(files)
                        }
                    },
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = AppColor.PRIMARY,
                            contentColor = Color.White,
                        ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "",
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
            Button(
                onClick = {
                    when (screen) {
                        Screen.Merge -> {
                            PdfProcessor.mergePdfs(
                                pdfViewModel.pdfFiles.value,
                                outputDirectoryPath = pdfViewModel.selectedDirectory.value,
                                outputFileName = pdfViewModel.selectedName.value,
                            )
                        }

                        Screen.Watermark -> {
                            PdfProcessor.batchProcess(
                                pdfViewModel.pdfFiles.value,
                                outputDirectoryPath = pdfViewModel.selectedDirectory.value,
                                config =
                                    PdfConfig(
                                        type = ProcessType.Watermark,
                                        watermarkText = pdfViewModel.watermarkText.value,
                                        watermarkFontSize = pdfViewModel.watermarkFontSize.value,
                                        color = pdfViewModel.color.value,
                                        rotation = pdfViewModel.rotation.value,
                                        font = pdfViewModel.font.value,
                                        position = pdfViewModel.position.value,
                                        opacity = pdfViewModel.opacity.value,
                                    ),
                            )
                        }

                        Screen.Numbering -> {
                            PdfProcessor.batchProcess(
                                pdfViewModel.pdfFiles.value,
                                outputDirectoryPath = pdfViewModel.selectedDirectory.value,
                                config =
                                    PdfConfig(
                                        type = ProcessType.Numbering,
                                        color = pdfViewModel.color.value,
                                        rotation = pdfViewModel.rotation.value,
                                        font = pdfViewModel.font.value,
                                        position = pdfViewModel.position.value,
                                        opacity = pdfViewModel.opacity.value,
                                        numberingFontSize = pdfViewModel.numberingFontSize.value,
                                        pageFormat = pdfViewModel.pageFormat.value,
                                        numberPosition =
                                            NumberPosition(
                                                x = pdfViewModel.x.value.toInt(),
                                                y = pdfViewModel.y.value.toInt(),
                                            ),
                                    ),
                            )
                        }

                        Screen.Files -> {
                            PdfProcessor.batchMergePdfs(
                                pdfViewModel.pdfFiles.value,
                                outputDirectoryPath = pdfViewModel.monthlyDirectory.value,
                                selectedDate = pdfViewModel.selectedDate.value,
                                pattern = pdfViewModel.dateFormat.value,
                            )
                        }
                    }
                },
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = AppColor.PRIMARY,
                        contentColor = Color.White,
                    ),
                shape = RoundedCornerShape(12.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "",
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}
