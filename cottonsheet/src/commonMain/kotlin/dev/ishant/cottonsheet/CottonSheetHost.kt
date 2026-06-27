package dev.ishant.cottonsheet

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetValue
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
fun CottonSheetHost(content: @Composable () -> Unit) {

    val controller = remember { CottonSheetController() }

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

                ModalBottomSheet(
                    modifier = if (p.isFullScreen) {
                        Modifier.fillMaxWidth().fillMaxHeight()
                    } else {
                        Modifier
                    },
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
                    sheetMaxWidth = p.sheetMaxWidth,
                    shape = p.shape ?: BottomSheetDefaults.ExpandedShape,
                    containerColor = p.containerColor
                        ?: BottomSheetDefaults.ContainerColor,
                    contentColor = p.contentColor
                        ?: contentColorFor(
                            p.containerColor ?: BottomSheetDefaults.ContainerColor
                        ),
                    tonalElevation = p.tonalElevation,
                    scrimColor = p.scrimColor ?: BottomSheetDefaults.ScrimColor,
                    dragHandle = when {
                        p.customDragHandle != null -> p.customDragHandle
                        p.showDragHandle           -> ({ BottomSheetDefaults.DragHandle() })
                        else                       -> null
                    },
                ) {
                    entry.content(dismiss)
                }
            }
        }
    }
}
