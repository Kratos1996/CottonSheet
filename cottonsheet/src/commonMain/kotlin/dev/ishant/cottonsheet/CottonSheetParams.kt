package dev.ishant.cottonsheet

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Visual / behavioural configuration for a single CottonSheet layer.
 *
 * @param skipPartiallyExpanded  Skip the half-expanded state (default: true).
 * @param sheetWidth             Explicit width for the sheet (e.g. 480.dp). When null, uses [sheetMaxWidth].
 * @param sheetMaxWidth          Max sheet width — useful on tablet / desktop (default: [BottomSheetDefaults.SheetMaxWidth]).
 * @param sheetPadding           Padding / margin around the bottom sheet (e.g. for floating card style or edge spacing).
 * @param shape                  Corner shape. Defaults to [BottomSheetDefaults.ExpandedShape].
 * @param containerColor         Sheet background color.
 * @param contentColor           Foreground color. Auto-derived from [containerColor] when null.
 * @param tonalElevation         Material tonal elevation overlay.
 * @param scrimColor             Dimmed background scrim color.
 * @param showDragHandle         Show the drag handle knob (set to false to completely hide it).
 * @param isDragHandleOutside    Position the drag handle outside/above the bottom sheet card.
 * @param dragHandleOutsideSpacing Spacing between the outside drag handle and the top of the sheet.
 * @param dragHandleColor        Color of the default drag handle knob.
 * @param dragHandleWidth        Width of the default drag handle knob.
 * @param dragHandleHeight       Height of the default drag handle knob.
 * @param isFullScreen           Whether the sheet fills full screen height.
 * @param isDismissable          Whether tapping outside or back press closes the sheet.
 * @param dismissRequest         Called when a dismiss is requested by the system/user.
 * @param onDismissed            Called when the sheet finishes dismissing completely.
 * @param customDragHandle       Fully replace the drag handle with your own composable.
 * @param windowInsets           Window insets for the sheet content. Defaults to [BottomSheetDefaults.windowInsets].
 */
@OptIn(ExperimentalMaterial3Api::class)
data class CottonSheetParams(
    val skipPartiallyExpanded: Boolean = true,
    val sheetWidth: Dp? = null,
    val sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    val sheetPadding: PaddingValues = PaddingValues(0.dp),
    val shape: Shape? = null,
    val containerColor: Color? = null,
    val contentColor: Color? = null,
    val tonalElevation: Dp = BottomSheetDefaults.Elevation,
    val scrimColor: Color? = null,
    val showDragHandle: Boolean = true,
    val isDragHandleOutside: Boolean = false,
    val dragHandleOutsideSpacing: Dp = 8.dp,
    val dragHandleColor: Color = Color.Gray.copy(alpha = 0.4f),
    val dragHandleWidth: Dp = 32.dp,
    val dragHandleHeight: Dp = 4.dp,
    val isFullScreen: Boolean = false,
    val isDismissable: Boolean = true,
    val dismissRequest: (() -> Unit)? = null,
    val onDismissed: (() -> Unit)? = null,
    val customDragHandle: (@Composable () -> Unit)? = null,
    val windowInsets: WindowInsets? = null,
)
