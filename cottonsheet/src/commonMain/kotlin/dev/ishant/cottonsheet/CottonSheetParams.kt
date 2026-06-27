package dev.ishant.cottonsheet

import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

/**
 * Visual / behavioural configuration for a single CottonSheet layer.
 *
 * @param skipPartiallyExpanded  Skip the half-expanded state (default: true).
 * @param sheetMaxWidth          Max sheet width — useful on tablet / desktop.
 * @param shape                  Corner shape. Defaults to [BottomSheetDefaults.ExpandedShape].
 * @param containerColor         Sheet background color.
 * @param contentColor           Foreground color. Auto-derived from [containerColor] when null.
 * @param tonalElevation         Material tonal elevation overlay.
 * @param scrimColor             Dimmed background scrim color.
 * @param showDragHandle         Show the default drag handle knob.
 * @param customDragHandle       Fully replace the drag handle with your own composable.
 */
@OptIn(ExperimentalMaterial3Api::class)
data class CottonSheetParams(
    val skipPartiallyExpanded: Boolean = true,
    val sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    val shape: Shape? = null,
    val containerColor: Color? = null,
    val contentColor: Color? = null,
    val tonalElevation: Dp = BottomSheetDefaults.Elevation,
    val scrimColor: Color? = null,
    val showDragHandle: Boolean = true,
    val isFullScreen: Boolean = false,
    val isDismissable: Boolean = true,
    val dismissRequest: (() -> Unit)? = null,
    val customDragHandle: (@Composable () -> Unit)? = null,
)
