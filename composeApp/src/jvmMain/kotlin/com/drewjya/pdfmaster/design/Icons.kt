package com.drewjya.pdfmaster.design

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object Icons {
    val FileAdd: ImageVector
        get() {
            if (_FileAdd != null) return _FileAdd!!

            _FileAdd =
                ImageVector
                    .Builder(
                        name = "file-plus",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 24f,
                        viewportHeight = 24f,
                    ).apply {
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(14f, 3f)
                            verticalLineToRelative(4f)
                            arcToRelative(1f, 1f, 0f, false, false, 1f, 1f)
                            horizontalLineToRelative(4f)
                        }
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(17f, 21f)
                            horizontalLineToRelative(-10f)
                            arcToRelative(2f, 2f, 0f, false, true, -2f, -2f)
                            verticalLineToRelative(-14f)
                            arcToRelative(2f, 2f, 0f, false, true, 2f, -2f)
                            horizontalLineToRelative(7f)
                            lineToRelative(5f, 5f)
                            verticalLineToRelative(11f)
                            arcToRelative(2f, 2f, 0f, false, true, -2f, 2f)
                            close()
                        }
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(12f, 11f)
                            lineToRelative(0f, 6f)
                        }
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(9f, 14f)
                            lineToRelative(6f, 0f)
                        }
                    }.build()

            return _FileAdd!!
        }

    private var _FileAdd: ImageVector? = null

    val FolderPlus: ImageVector
        get() {
            if (_FolderPlus != null) return _FolderPlus!!

            _FolderPlus =
                ImageVector
                    .Builder(
                        name = "folder-plus",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 256f,
                        viewportHeight = 256f,
                    ).apply {
                        path(
                            fill = SolidColor(Color.Black),
                        ) {
                            // Folder outer shape — walls thickened from 20 to 28
                            moveTo(216f, 68f)
                            horizontalLineTo(133.39f)
                            lineToRelative(-26f, -29.29f)
                            arcToRelative(28f, 28f, 0f, false, false, -15f, -6.71f)
                            horizontalLineTo(40f)
                            arcTo(28f, 28f, 0f, false, false, 12f, 52f)
                            verticalLineTo(200.62f)
                            arcTo(27.41f, 27.41f, 0f, false, false, 39.38f, 228f)
                            horizontalLineTo(216.89f)
                            arcTo(27.13f, 27.13f, 0f, false, false, 244f, 200.89f)
                            verticalLineTo(88f)
                            arcTo(28f, 28f, 0f, false, false, 216f, 68f)
                            close()

                            // Tab top
                            moveTo(90.61f, 56f)
                            lineToRelative(10.67f, 12f)
                            horizontalLineTo(44f)
                            verticalLineTo(56f)
                            close()

                            // Folder body interior — thicker walls (was 44/212, now 52/204)
                            moveTo(204f, 196f)
                            horizontalLineTo(52f)
                            verticalLineTo(100f)
                            horizontalLineTo(204f)
                            close()

                            // Plus sign — arms radius 16 (was 12), so arms are ~33% thicker
                            moveToRelative(0f, 0f)
                            moveTo(128f, 108f)
                            verticalLineToRelative(20f)
                            horizontalLineTo(108f)
                            arcToRelative(16f, 16f, 0f, false, false, 0f, 32f)
                            horizontalLineTo(128f)
                            verticalLineToRelative(20f)
                            arcToRelative(16f, 16f, 0f, false, false, 32f, 0f)
                            verticalLineTo(160f)
                            horizontalLineTo(160f)
                            arcToRelative(16f, 16f, 0f, false, false, 0f, -32f)
                            horizontalLineTo(140f)
                            verticalLineTo(108f)
                            arcToRelative(16f, 16f, 0f, false, false, -32f, 0f)
                            close()
                        }
                    }.build()

            return _FolderPlus!!
        }

    private var _FolderPlus: ImageVector? = null

    val Trash: ImageVector
        get() {
            if (_Trash != null) return _Trash!!

            _Trash =
                ImageVector
                    .Builder(
                        name = "trash-2",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 24f,
                        viewportHeight = 24f,
                    ).apply {
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(3f, 6f)
                            lineTo(5f, 6f)
                            lineTo(21f, 6f)
                        }
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(19f, 6f)
                            verticalLineToRelative(14f)
                            arcToRelative(2f, 2f, 0f, false, true, -2f, 2f)
                            horizontalLineTo(7f)
                            arcToRelative(2f, 2f, 0f, false, true, -2f, -2f)
                            verticalLineTo(6f)
                            moveToRelative(3f, 0f)
                            verticalLineTo(4f)
                            arcToRelative(2f, 2f, 0f, false, true, 2f, -2f)
                            horizontalLineToRelative(4f)
                            arcToRelative(2f, 2f, 0f, false, true, 2f, 2f)
                            verticalLineToRelative(2f)
                        }
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(10f, 11f)
                            lineTo(10f, 17f)
                        }
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(14f, 11f)
                            lineTo(14f, 17f)
                        }
                    }.build()

            return _Trash!!
        }

    private var _Trash: ImageVector? = null

    val Merge: ImageVector
        get() {
            if (_Merge != null) return _Merge!!

            _Merge =
                ImageVector
                    .Builder(
                        name = "arrow-merge",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 24f,
                        viewportHeight = 24f,
                    ).apply {
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(8f, 7f)
                            lineToRelative(4f, -4f)
                            lineToRelative(4f, 4f)
                        }
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(12f, 3f)
                            verticalLineToRelative(5.394f)
                            arcToRelative(6.737f, 6.737f, 0f, false, true, -3f, 5.606f)
                            arcToRelative(6.737f, 6.737f, 0f, false, false, -3f, 5.606f)
                            verticalLineToRelative(1.394f)
                        }
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(12f, 3f)
                            verticalLineToRelative(5.394f)
                            arcToRelative(6.737f, 6.737f, 0f, false, false, 3f, 5.606f)
                            arcToRelative(6.737f, 6.737f, 0f, false, true, 3f, 5.606f)
                            verticalLineToRelative(1.394f)
                        }
                    }.build()

            return _Merge!!
        }

    private var _Merge: ImageVector? = null

    val Batch: ImageVector
        get() {
            if (_Layers != null) return _Layers!!

            _Layers =
                ImageVector
                    .Builder(
                        name = "layers",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 24f,
                        viewportHeight = 24f,
                    ).apply {
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(12f, 2f)
                            lineTo(2f, 7f)
                            lineTo(12f, 12f)
                            lineTo(22f, 7f)
                            lineTo(12f, 2f)
                            close()
                        }
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(2f, 17f)
                            lineTo(12f, 22f)
                            lineTo(22f, 17f)
                        }
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(2f, 12f)
                            lineTo(12f, 17f)
                            lineTo(22f, 12f)
                        }
                    }.build()

            return _Layers!!
        }

    private var _Layers: ImageVector? = null

    val Identity: ImageVector
        get() {
            if (_Identity != null) return _Identity!!

            _Identity =
                ImageVector
                    .Builder(
                        name = "type",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 24f,
                        viewportHeight = 24f,
                    ).apply {
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(4f, 7f)
                            lineTo(4f, 4f)
                            lineTo(20f, 4f)
                            lineTo(20f, 7f)
                        }
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(9f, 20f)
                            lineTo(15f, 20f)
                        }
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(12f, 4f)
                            lineTo(12f, 20f)
                        }
                    }.build()

            return _Identity!!
        }

    private var _Identity: ImageVector? = null

    val Numbering: ImageVector
        get() {
            if (_Numbering != null) return _Numbering!!

            _Numbering =
                ImageVector
                    .Builder(
                        name = "hash",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 24f,
                        viewportHeight = 24f,
                    ).apply {
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(4f, 9f)
                            lineTo(20f, 9f)
                        }
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(4f, 15f)
                            lineTo(20f, 15f)
                        }
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(10f, 3f)
                            lineTo(8f, 21f)
                        }
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(16f, 3f)
                            lineTo(14f, 21f)
                        }
                    }.build()

            return _Numbering!!
        }

    private var _Numbering: ImageVector? = null

    val FolderOutput: ImageVector
        get() {
            if (_FolderOutput != null) return _FolderOutput!!

            _FolderOutput =
                ImageVector
                    .Builder(
                        name = "folder-output",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 24f,
                        viewportHeight = 24f,
                    ).apply {
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(2f, 7.5f)
                            verticalLineTo(5f)
                            arcToRelative(2f, 2f, 0f, false, true, 2f, -2f)
                            horizontalLineToRelative(3.9f)
                            arcToRelative(2f, 2f, 0f, false, true, 1.69f, 0.9f)
                            lineToRelative(0.81f, 1.2f)
                            arcToRelative(2f, 2f, 0f, false, false, 1.67f, 0.9f)
                            horizontalLineTo(20f)
                            arcToRelative(2f, 2f, 0f, false, true, 2f, 2f)
                            verticalLineToRelative(10f)
                            arcToRelative(2f, 2f, 0f, false, true, -2f, 2f)
                            horizontalLineTo(4f)
                            arcToRelative(2f, 2f, 0f, false, true, -2f, -1.5f)
                        }
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(2f, 13f)
                            horizontalLineToRelative(10f)
                        }
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(5f, 10f)
                            lineToRelative(-3f, 3f)
                            lineToRelative(3f, 3f)
                        }
                    }.build()

            return _FolderOutput!!
        }

    private var _FolderOutput: ImageVector? = null

    val Pdf: ImageVector
        get() {
            if (_Pdf != null) return _Pdf!!

            _Pdf =
                ImageVector
                    .Builder(
                        name = "filetype-pdf",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 16f,
                        viewportHeight = 16f,
                    ).apply {
                        path(
                            fill = SolidColor(Color.Black),
                        ) {
                            moveTo(14f, 4.5f)
                            verticalLineTo(14f)
                            arcToRelative(2f, 2f, 0f, false, true, -2f, 2f)
                            horizontalLineToRelative(-1f)
                            verticalLineToRelative(-1f)
                            horizontalLineToRelative(1f)
                            arcToRelative(1f, 1f, 0f, false, false, 1f, -1f)
                            verticalLineTo(4.5f)
                            horizontalLineToRelative(-2f)
                            arcTo(1.5f, 1.5f, 0f, false, true, 9.5f, 3f)
                            verticalLineTo(1f)
                            horizontalLineTo(4f)
                            arcToRelative(1f, 1f, 0f, false, false, -1f, 1f)
                            verticalLineToRelative(9f)
                            horizontalLineTo(2f)
                            verticalLineTo(2f)
                            arcToRelative(2f, 2f, 0f, false, true, 2f, -2f)
                            horizontalLineToRelative(5.5f)
                            close()
                            moveTo(1.6f, 11.85f)
                            horizontalLineTo(0f)
                            verticalLineToRelative(3.999f)
                            horizontalLineToRelative(0.791f)
                            verticalLineToRelative(-1.342f)
                            horizontalLineToRelative(0.803f)
                            quadToRelative(0.43f, 0f, 0.732f, -0.173f)
                            quadToRelative(0.305f, -0.175f, 0.463f, -0.474f)
                            arcToRelative(1.4f, 1.4f, 0f, false, false, 0.161f, -0.677f)
                            quadToRelative(0f, -0.375f, -0.158f, -0.677f)
                            arcToRelative(1.2f, 1.2f, 0f, false, false, -0.46f, -0.477f)
                            quadToRelative(-0.3f, -0.18f, -0.732f, -0.179f)
                            moveToRelative(0.545f, 1.333f)
                            arcToRelative(0.8f, 0.8f, 0f, false, true, -0.085f, 0.38f)
                            arcToRelative(0.57f, 0.57f, 0f, false, true, -0.238f, 0.241f)
                            arcToRelative(0.8f, 0.8f, 0f, false, true, -0.375f, 0.082f)
                            horizontalLineTo(0.788f)
                            verticalLineTo(12.48f)
                            horizontalLineToRelative(0.66f)
                            quadToRelative(0.327f, 0f, 0.512f, 0.181f)
                            quadToRelative(0.185f, 0.183f, 0.185f, 0.522f)
                            moveToRelative(1.217f, -1.333f)
                            verticalLineToRelative(3.999f)
                            horizontalLineToRelative(1.46f)
                            quadToRelative(0.602f, 0f, 0.998f, -0.237f)
                            arcToRelative(1.45f, 1.45f, 0f, false, false, 0.595f, -0.689f)
                            quadToRelative(0.196f, -0.45f, 0.196f, -1.084f)
                            quadToRelative(0f, -0.63f, -0.196f, -1.075f)
                            arcToRelative(1.43f, 1.43f, 0f, false, false, -0.589f, -0.68f)
                            quadToRelative(-0.396f, -0.234f, -1.005f, -0.234f)
                            close()
                            moveToRelative(0.791f, 0.645f)
                            horizontalLineToRelative(0.563f)
                            quadToRelative(0.371f, 0f, 0.609f, 0.152f)
                            arcToRelative(0.9f, 0.9f, 0f, false, true, 0.354f, 0.454f)
                            quadToRelative(0.118f, 0.302f, 0.118f, 0.753f)
                            arcToRelative(2.3f, 2.3f, 0f, false, true, -0.068f, 0.592f)
                            arcToRelative(1.1f, 1.1f, 0f, false, true, -0.196f, 0.422f)
                            arcToRelative(0.8f, 0.8f, 0f, false, true, -0.334f, 0.252f)
                            arcToRelative(1.3f, 1.3f, 0f, false, true, -0.483f, 0.082f)
                            horizontalLineToRelative(-0.563f)
                            close()
                            moveToRelative(3.743f, 1.763f)
                            verticalLineToRelative(1.591f)
                            horizontalLineToRelative(-0.79f)
                            verticalLineTo(11.85f)
                            horizontalLineToRelative(2.548f)
                            verticalLineToRelative(0.653f)
                            horizontalLineTo(7.896f)
                            verticalLineToRelative(1.117f)
                            horizontalLineToRelative(1.606f)
                            verticalLineToRelative(0.638f)
                            close()
                        }
                    }.build()

            return _Pdf!!
        }

    private var _Pdf: ImageVector? = null


    val Empty: ImageVector
        get() {
            if (_Empty != null) return _Empty!!

            _Empty = ImageVector.Builder(
                name = "layout-template",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f
            ).apply {
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(4f, 3f)
                    horizontalLineTo(20f)
                    arcTo(1f, 1f, 0f, false, true, 21f, 4f)
                    verticalLineTo(9f)
                    arcTo(1f, 1f, 0f, false, true, 20f, 10f)
                    horizontalLineTo(4f)
                    arcTo(1f, 1f, 0f, false, true, 3f, 9f)
                    verticalLineTo(4f)
                    arcTo(1f, 1f, 0f, false, true, 4f, 3f)
                    close()
                }
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(4f, 14f)
                    horizontalLineTo(11f)
                    arcTo(1f, 1f, 0f, false, true, 12f, 15f)
                    verticalLineTo(20f)
                    arcTo(1f, 1f, 0f, false, true, 11f, 21f)
                    horizontalLineTo(4f)
                    arcTo(1f, 1f, 0f, false, true, 3f, 20f)
                    verticalLineTo(15f)
                    arcTo(1f, 1f, 0f, false, true, 4f, 14f)
                    close()
                }
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(17f, 14f)
                    horizontalLineTo(20f)
                    arcTo(1f, 1f, 0f, false, true, 21f, 15f)
                    verticalLineTo(20f)
                    arcTo(1f, 1f, 0f, false, true, 20f, 21f)
                    horizontalLineTo(17f)
                    arcTo(1f, 1f, 0f, false, true, 16f, 20f)
                    verticalLineTo(15f)
                    arcTo(1f, 1f, 0f, false, true, 17f, 14f)
                    close()
                }
            }.build()

            return _Empty!!
        }

    private var _Empty: ImageVector? = null
}
