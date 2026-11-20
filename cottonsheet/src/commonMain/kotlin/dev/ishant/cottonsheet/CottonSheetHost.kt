package dev.ishant.cottonsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Root host composable. Place this **once** at the very top of your composition,
 * typically inside [MaterialTheme] and outside your navigation graph.
 *
 * ### Setup (once per app)
 * ```kotlin
 * @Composable
 * fun App() {
 *     MaterialTheme {
 *         CottonSheetHost {
 *             MyNavGraph()
 *         }
 *     }
 * }
 * ```
 *
 * ### Trigger from anywhere under the host
 * ```kotlin
 * @Composable
 * fun SomeDeepScreen() {
 *     val controller = LocalCottonSheetController.current
 *
 *     // ── Single sheet ──────────────────────────────────────────────────────
 *     Button(onClick = {
 *         controller.show { dismiss ->
 *             Column(Modifier.fillMaxWidth().padding(24.dp)) {
 *                 Text("Sheet 1")
 *                 Button(onClick = dismiss) { Text("Close") }
 *             }
 *         }
 *     }) { Text("Open Sheet") }
 *
 *     // ── Stacked sheets ────────────────────────────────────────────────────
 *     Button(onClick = {
 *         controller.show { dismiss1 ->
 *             Column(Modifier.fillMaxWidth().padding(24.dp)) {
 *                 Text("Sheet 1")
 *
 *                 Button(onClick = {
 *                     controller.show { dismiss2 ->
 *                         Column(Modifier.fillMaxWidth().padding(24.dp)) {
 *                             Text("Sheet 2")
 *                             Button(onClick = dismiss2) { Text("Close Sheet 2") }
 *                             Button(onClick = { controller.dismissAll() }) {
 *                                 Text("Close All Sheets")
 *                             }
 *                         }
 *                     }
 *                 }) { Text("Open Sheet 2") }
 *
 *                 Button(onClick = dismiss1) { Text("Close Sheet 1") }
 *             }
 *         }
 *     }) { Text("Open Stacked Sheets") }
 *
 *     // ── Custom params ─────────────────────────────────────────────────────
 *     Button(onClick = {
 *         controller.show(
 *             params = CottonSheetParams(
 *                 showDragHandle = false,
 *                 containerColor = MaterialTheme.colorScheme.primaryContainer,
 *                 sheetMaxWidth = 560.dp,
 *                 skipPartiallyExpanded = false,
 *             )
 *         ) { dismiss ->
 *             MyContent(onClose = dismiss)
 *         }
 *     }) { Text("Open Custom Sheet") }
 * }
 * ```
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CottonSheetHost(
    controller: CottonSheetController = rememberCottonSheetController(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalCottonSheetController provides controller) {

        // ── App content ───────────────────────────────────────────────────────
        content()

        // ── Sheet stack ───────────────────────────────────────────────────────
        // Snapshot so Compose can diff the list correctly across recompositions
        val entries = controller.stack.toList()

        entries.forEach { entry ->

            // key() gives every sheet its own isolated composition + animation state.
            // Without this, Compose would reuse state when sheets are pushed / popped.
            key(entry.id) {

                val p = entry.params

                val sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = p.skipPartiallyExpanded,
                    confirmValueChange = { newValue ->
                        if (p.isDismissable) true
                        else newValue != SheetValue.Hidden
                    },
                )
                val scope = rememberCoroutineScope()

                // Animated dismiss: play hide animation, then remove from stack
                val dismiss: () -> Unit = {
                    scope.launch { sheetState.hide() }
                        .invokeOnCompletion { controller.dismissById(entry.id) }
                }

                val sheetModifier = Modifier
                    .statusBarsPadding()
                    .padding(p.sheetPadding)
                    .then(
                        if (p.isFullScreen) {
                            Modifier.fillMaxHeight()
                        } else {
                            Modifier
                        }
                    )
                    .then(
                        if (p.sheetWidth != null) {
                            Modifier.width(p.sheetWidth)
                        } else {
                            Modifier
                        }
                    )

                ModalBottomSheet(
                    modifier = sheetModifier,
                    onDismissRequest = {
                        if (p.isDismissable) {
                            p.dismissRequest?.invoke()
                            controller.dismissById(entry.id)
                        }
                    },
                    sheetState = sheetState,
                    properties = ModalBottomSheetProperties(
                        shouldDismissOnBackPress = p.isDismissable,
                    ),
                    sheetMaxWidth = if (p.sheetWidth != null) Dp.Unspecified else p.sheetMaxWidth,
                    shape = if (p.isDragHandleOutside) RectangleShape else (p.shape ?: BottomSheetDefaults.ExpandedShape),
                    containerColor = if (p.isDragHandleOutside) Color.Transparent else (p.containerColor ?: BottomSheetDefaults.ContainerColor),
                    contentColor = if (p.isDragHandleOutside) Color.Transparent else (p.contentColor ?: contentColorFor(p.containerColor ?: BottomSheetDefaults.ContainerColor)),
                    tonalElevation = if (p.isDragHandleOutside) 0.dp else p.tonalElevation,
                    scrimColor = p.scrimColor ?: BottomSheetDefaults.ScrimColor,
                    dragHandle = if (p.isDragHandleOutside || !p.showDragHandle) {
                        null
                    } else {
                        when {
                            p.customDragHandle != null -> p.customDragHandle
                            else                       -> ({
                                BottomSheetDefaults.DragHandle(
                                    width = p.dragHandleWidth,
                                    height = p.dragHandleHeight,
                                    color = p.dragHandleColor,
                                )
                            })
                        }
                    },
                    contentWindowInsets = { p.windowInsets ?: BottomSheetDefaults.windowInsets },
                ) {
                    if (p.isDragHandleOutside) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (p.showDragHandle) {
                                Box(
                                    modifier = Modifier.padding(bottom = p.dragHandleOutsideSpacing),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (p.customDragHandle != null) {
                                        p.customDragHandle.invoke()
                                    } else {
                                        val handleColor = if (p.dragHandleColor == Color.Gray.copy(alpha = 0.4f)) {
                                            Color.White.copy(alpha = 0.8f)
                                        } else {
                                            p.dragHandleColor
                                        }
                                        Surface(
                                            modifier = Modifier.size(width = p.dragHandleWidth, height = p.dragHandleHeight),
                                            shape = RoundedCornerShape(p.dragHandleHeight / 2),
                                            color = handleColor,
                                        ) {}
                                    }
                                }
                            }
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .then(if (p.isFullScreen) Modifier.fillMaxHeight() else Modifier),
                                shape = p.shape ?: BottomSheetDefaults.ExpandedShape,
                                color = p.containerColor ?: BottomSheetDefaults.ContainerColor,
                                contentColor = p.contentColor ?: contentColorFor(
                                    p.containerColor ?: BottomSheetDefaults.ContainerColor
                                ),
                                tonalElevation = p.tonalElevation,
                            ) {
                                entry.content(dismiss)
                            }
                        }
                    } else {
                        entry.content(dismiss)
                    }
                }
            }
        }
    }
}
