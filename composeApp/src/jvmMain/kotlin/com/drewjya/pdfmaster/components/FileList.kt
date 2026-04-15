package com.drewjya.pdfmaster.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drewjya.pdfmaster.design.AppIcon
import com.drewjya.pdfmaster.design.Slate100
import com.drewjya.pdfmaster.design.Slate200
import com.drewjya.pdfmaster.design.Slate400
import com.drewjya.pdfmaster.design.Slate500
import com.drewjya.pdfmaster.design.Slate700
import com.drewjya.pdfmaster.viewmodel.PdfViewModel
import java.io.File
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FileList(pdfViewModel: PdfViewModel = koinViewModel()) {

    val files = pdfViewModel.pdfFiles.collectAsState()
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, Slate200, RoundedCornerShape(8.dp))
                .background(Slate100),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(Slate100)
                    .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Selected Files (${files.value.size})",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
            )
            if (files.value.isNotEmpty()) {
                Text(
                    text = "Clear All",
                    fontSize = 12.sp,
                    color = Color.Red,
                    modifier = Modifier.clickable { pdfViewModel.clearFiles() },
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

        if (files.value.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().height(140.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "No files selected",
                    fontSize = 14.sp,
                    color = Slate400,
                    fontStyle = FontStyle.Italic,
                )
            }
        } else {
            LazyColumn(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .background(Color.White)
                        .heightIn(max = 400.dp),
            ) {
                items(files.value) { file ->
                    FileListItem(
                        file,
                        onRemove = { pdfViewModel.removeFile(file) },
                        onUp = { pdfViewModel.moveFile(file, true) },
                        onDown = { pdfViewModel.moveFile(file, false) },
                    )
                    HorizontalDivider(color = Slate100, thickness = 8.dp)
                }
            }
        }
    }
}

@Composable
fun FileListItem(
    file: File,
    onRemove: () -> Unit,
    onUp: () -> Unit,
    onDown: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(
                imageVector = AppIcon.File,
                contentDescription = AppIcon.File.name,
            )
            Spacer(Modifier.width(12.dp))
            Column(
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = file.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = Slate700,
                    lineHeight = 18.sp,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = file.path,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = Slate500,
                    lineHeight = 18.sp,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        ActionButton(
            onClick = onUp,
            imageVector = Icons.Default.KeyboardArrowUp,
        )

        ActionButton(
            onClick = onDown,
            imageVector = Icons.Default.KeyboardArrowDown,
        )

        ActionButton(
            onClick = onRemove,
            imageVector = Icons.Default.Delete,
        )
    }
}

@Composable
fun ActionButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String = imageVector.name,
) {
    Box(
        modifier =
            Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = Slate700,
        )
    }
}
