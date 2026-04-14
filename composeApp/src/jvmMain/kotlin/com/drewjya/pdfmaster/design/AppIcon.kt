package com.drewjya.pdfmaster.design

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object AppIcon {
    private var _Watermark: ImageVector? = null
    val Watermark: ImageVector
        get() {
            if (_Watermark != null) return _Watermark!!

            _Watermark =
                ImageVector
                    .Builder(
                        name = "branding_watermark",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 960f,
                        viewportHeight = 960f,
                    ).apply {
                        path(
                            fill = SolidColor(Color.Black),
                        ) {
                            moveTo(400f, 680f)
                            horizontalLineToRelative(360f)
                            verticalLineToRelative(-240f)
                            horizontalLineTo(400f)
                            verticalLineToRelative(240f)
                            close()
                            moveTo(160f, 800f)
                            quadToRelative(-33f, 0f, -56.5f, -23.5f)
                            reflectiveQuadTo(80f, 720f)
                            verticalLineToRelative(-480f)
                            quadToRelative(0f, -33f, 23.5f, -56.5f)
                            reflectiveQuadTo(160f, 160f)
                            horizontalLineToRelative(640f)
                            quadToRelative(33f, 0f, 56.5f, 23.5f)
                            reflectiveQuadTo(880f, 240f)
                            verticalLineToRelative(480f)
                            quadToRelative(0f, 33f, -23.5f, 56.5f)
                            reflectiveQuadTo(800f, 800f)
                            horizontalLineTo(160f)
                            close()
                        }
                    }.build()

            return _Watermark!!
        }
    private var _Merge: ImageVector? = null

    val Merge: ImageVector
        get() {
            if (_Merge != null) return _Merge!!

            _Merge =
                ImageVector
                    .Builder(
                        name = "file-stack",
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
                            moveTo(5f, 12f)
                            verticalLineToRelative(-7f)
                            arcToRelative(2f, 2f, 0f, false, true, 2f, -2f)
                            horizontalLineToRelative(7f)
                            lineToRelative(5f, 5f)
                            verticalLineToRelative(4f)
                        }
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(5f, 21f)
                            horizontalLineToRelative(14f)
                        }
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(5f, 18f)
                            horizontalLineToRelative(14f)
                        }
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(5f, 15f)
                            horizontalLineToRelative(14f)
                        }
                    }.build()

            return _Merge!!
        }

    val Number: ImageVector
        get() {
            if (_Number != null) return _Number!!

            _Number =
                ImageVector
                    .Builder(
                        name = "format_list_numbered",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 24f,
                        viewportHeight = 24f,
                    ).apply {
                        path(
                            fill = SolidColor(Color.Transparent),
                        ) {
                            moveTo(0f, 0f)
                            horizontalLineToRelative(24f)
                            verticalLineToRelative(24f)
                            horizontalLineTo(0f)
                            verticalLineTo(0f)
                            close()
                        }
                        path(
                            fill = SolidColor(Color.Black),
                        ) {
                            moveTo(8f, 7f)
                            horizontalLineToRelative(12f)
                            curveToRelative(0.55f, 0f, 1f, -0.45f, 1f, -1f)
                            reflectiveCurveToRelative(-0.45f, -1f, -1f, -1f)
                            horizontalLineTo(8f)
                            curveToRelative(-0.55f, 0f, -1f, 0.45f, -1f, 1f)
                            reflectiveCurveToRelative(0.45f, 1f, 1f, 1f)
                            close()
                            moveToRelative(12f, 10f)
                            horizontalLineTo(8f)
                            curveToRelative(-0.55f, 0f, -1f, 0.45f, -1f, 1f)
                            reflectiveCurveToRelative(0.45f, 1f, 1f, 1f)
                            horizontalLineToRelative(12f)
                            curveToRelative(0.55f, 0f, 1f, -0.45f, 1f, -1f)
                            reflectiveCurveToRelative(-0.45f, -1f, -1f, -1f)
                            close()
                            moveToRelative(0f, -6f)
                            horizontalLineTo(8f)
                            curveToRelative(-0.55f, 0f, -1f, 0.45f, -1f, 1f)
                            reflectiveCurveToRelative(0.45f, 1f, 1f, 1f)
                            horizontalLineToRelative(12f)
                            curveToRelative(0.55f, 0f, 1f, -0.45f, 1f, -1f)
                            reflectiveCurveToRelative(-0.45f, -1f, -1f, -1f)
                            close()
                            moveTo(4.5f, 16f)
                            horizontalLineToRelative(-2f)
                            curveToRelative(-0.28f, 0f, -0.5f, 0.22f, -0.5f, 0.5f)
                            reflectiveCurveToRelative(0.22f, 0.5f, 0.5f, 0.5f)
                            horizontalLineTo(4f)
                            verticalLineToRelative(0.5f)
                            horizontalLineToRelative(-0.5f)
                            curveToRelative(-0.28f, 0f, -0.5f, 0.22f, -0.5f, 0.5f)
                            reflectiveCurveToRelative(0.22f, 0.5f, 0.5f, 0.5f)
                            horizontalLineTo(4f)
                            verticalLineToRelative(0.5f)
                            horizontalLineTo(2.5f)
                            curveToRelative(-0.28f, 0f, -0.5f, 0.22f, -0.5f, 0.5f)
                            reflectiveCurveToRelative(0.22f, 0.5f, 0.5f, 0.5f)
                            horizontalLineToRelative(2f)
                            curveToRelative(0.28f, 0f, 0.5f, -0.22f, 0.5f, -0.5f)
                            verticalLineToRelative(-3f)
                            curveToRelative(0f, -0.28f, -0.22f, -0.5f, -0.5f, -0.5f)
                            close()
                            moveToRelative(-2f, -11f)
                            horizontalLineTo(3f)
                            verticalLineToRelative(2.5f)
                            curveToRelative(0f, 0.28f, 0.22f, 0.5f, 0.5f, 0.5f)
                            reflectiveCurveToRelative(0.5f, -0.22f, 0.5f, -0.5f)
                            verticalLineToRelative(-3f)
                            curveToRelative(0f, -0.28f, -0.22f, -0.5f, -0.5f, -0.5f)
                            horizontalLineToRelative(-1f)
                            curveToRelative(-0.28f, 0f, -0.5f, 0.22f, -0.5f, 0.5f)
                            reflectiveCurveToRelative(0.22f, 0.5f, 0.5f, 0.5f)
                            close()
                            moveToRelative(2f, 5f)
                            horizontalLineToRelative(-2f)
                            curveToRelative(-0.28f, 0f, -0.5f, 0.22f, -0.5f, 0.5f)
                            reflectiveCurveToRelative(0.22f, 0.5f, 0.5f, 0.5f)
                            horizontalLineToRelative(1.3f)
                            lineToRelative(-1.68f, 1.96f)
                            curveToRelative(-0.08f, 0.09f, -0.12f, 0.21f, -0.12f, 0.32f)
                            verticalLineToRelative(0.22f)
                            curveToRelative(0f, 0.28f, 0.22f, 0.5f, 0.5f, 0.5f)
                            horizontalLineToRelative(2f)
                            curveToRelative(0.28f, 0f, 0.5f, -0.22f, 0.5f, -0.5f)
                            reflectiveCurveToRelative(-0.22f, -0.5f, -0.5f, -0.5f)
                            horizontalLineTo(3.2f)
                            lineToRelative(1.68f, -1.96f)
                            curveToRelative(0.08f, -0.09f, 0.12f, -0.21f, 0.12f, -0.32f)
                            verticalLineToRelative(-0.22f)
                            curveToRelative(0f, -0.28f, -0.22f, -0.5f, -0.5f, -0.5f)
                            close()
                        }
                    }.build()

            return _Number!!
        }

    private var _Number: ImageVector? = null

    val Upload: ImageVector
        get() {
            if (_Upload != null) return _Upload!!

            _Upload =
                ImageVector
                    .Builder(
                        name = "upload",
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
                            moveTo(12f, 3f)
                            verticalLineToRelative(12f)
                        }
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(17f, 8f)
                            lineToRelative(-5f, -5f)
                            lineToRelative(-5f, 5f)
                        }
                        path(
                            fill = SolidColor(Color.Transparent),
                            stroke = SolidColor(Color.Black),
                            strokeLineWidth = 2f,
                            strokeLineCap = StrokeCap.Round,
                            strokeLineJoin = StrokeJoin.Round,
                        ) {
                            moveTo(21f, 15f)
                            verticalLineToRelative(4f)
                            arcToRelative(2f, 2f, 0f, false, true, -2f, 2f)
                            horizontalLineTo(5f)
                            arcToRelative(2f, 2f, 0f, false, true, -2f, -2f)
                            verticalLineToRelative(-4f)
                        }
                    }.build()

            return _Upload!!
        }

    private var _Upload: ImageVector? = null

    val Folder: ImageVector
        get() {
            if (_Folder != null) return _Folder!!

            _Folder =
                ImageVector
                    .Builder(
                        name = "folder",
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
                            moveTo(5f, 4f)
                            horizontalLineToRelative(4f)
                            lineToRelative(3f, 3f)
                            horizontalLineToRelative(7f)
                            arcToRelative(2f, 2f, 0f, false, true, 2f, 2f)
                            verticalLineToRelative(8f)
                            arcToRelative(2f, 2f, 0f, false, true, -2f, 2f)
                            horizontalLineToRelative(-14f)
                            arcToRelative(2f, 2f, 0f, false, true, -2f, -2f)
                            verticalLineToRelative(-11f)
                            arcToRelative(2f, 2f, 0f, false, true, 2f, -2f)
                        }
                    }.build()

            return _Folder!!
        }

    private var _Folder: ImageVector? = null

    val File: ImageVector
        get() {
            if (_File != null) return _File!!

            _File =
                ImageVector
                    .Builder(
                        name = "file",
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
                    }.build()

            return _File!!
        }

    private var _File: ImageVector? = null

    val Download: ImageVector
        get() {
            if (_Download != null) return _Download!!

            _Download = ImageVector.Builder(
                name = "download",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 16f,
                viewportHeight = 16f
            ).apply {
                path(
                    fill = SolidColor(Color.Black)
                ) {
                    moveTo(3.5f, 13f)
                    horizontalLineTo(12.5f)
                    curveTo(12.7761f, 13f, 13f, 13.2239f, 13f, 13.5f)
                    curveTo(13f, 13.7455f, 12.8231f, 13.9496f, 12.5899f, 13.9919f)
                    lineTo(12.5f, 14f)
                    horizontalLineTo(3.5f)
                    curveTo(3.22386f, 14f, 3f, 13.7761f, 3f, 13.5f)
                    curveTo(3f, 13.2545f, 3.17688f, 13.0504f, 3.41012f, 13.0081f)
                    lineTo(3.5f, 13f)
                    close()
                    moveTo(7.91012f, 1.00806f)
                    lineTo(8f, 1f)
                    curveTo(8.24546f, 1f, 8.44961f, 1.17688f, 8.49194f, 1.41012f)
                    lineTo(8.5f, 1.5f)
                    verticalLineTo(10.292f)
                    lineTo(11.182f, 7.61091f)
                    curveTo(11.3555f, 7.43735f, 11.625f, 7.41806f, 11.8198f, 7.55306f)
                    lineTo(11.8891f, 7.61091f)
                    curveTo(12.0627f, 7.78448f, 12.0819f, 8.0539f, 11.9469f, 8.24877f)
                    lineTo(11.8891f, 8.31802f)
                    lineTo(8.35355f, 11.8536f)
                    curveTo(8.17999f, 12.0271f, 7.91056f, 12.0464f, 7.71569f, 11.9114f)
                    lineTo(7.64645f, 11.8536f)
                    lineTo(4.11091f, 8.31802f)
                    curveTo(3.91565f, 8.12276f, 3.91565f, 7.80617f, 4.11091f, 7.61091f)
                    curveTo(4.28448f, 7.43735f, 4.5539f, 7.41806f, 4.74877f, 7.55306f)
                    lineTo(4.81802f, 7.61091f)
                    lineTo(7.5f, 10.292f)
                    verticalLineTo(1.5f)
                    curveTo(7.5f, 1.25454f, 7.67688f, 1.05039f, 7.91012f, 1.00806f)
                    close()
                }
            }.build()

            return _Download!!
        }

    private var _Download: ImageVector? = null
}
