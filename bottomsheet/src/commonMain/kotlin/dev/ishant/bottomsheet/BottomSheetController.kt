package dev.ishant.bottomsheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateListOf

// ─────────────────────────────────────────────────────────────────────────────
// Internal model — one entry per sheet layer in the stack
// ─────────────────────────────────────────────────────────────────────────────

internal data class BottomSheetEntry(
    val id: Long,
    val params: BottomSheetParams,
    val content: @Composable (dismiss: () -> Unit) -> Unit,
)

// ─────────────────────────────────────────────────────────────────────────────
// BottomSheetController
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Drives the [BottomSheetHost].
 *
 * Supports a **stack of sheets**: each [show] call pushes a new layer on top.
 * Sheets are dismissed individually via the `dismiss` lambda or all at once
 * with [dismissAll].
 *
 * Obtain the instance via [LocalBottomSheetController] inside any composable
 * that lives under a [BottomSheetHost].
 *
 * ### Single sheet
 * ```kotlin
 * val controller = LocalBottomSheetController.current
 *
 * controller.show { dismiss ->
 *     Column(Modifier.fillMaxWidth().padding(24.dp)) {
 *         Text("Hello!")
 *         Button(onClick = dismiss) { Text("Close") }
 *     }
 * }
 * ```
 *
 * ### Stacked sheets
 * ```kotlin
 * controller.show { dismiss1 ->
 *     Column(Modifier.fillMaxWidth().padding(24.dp)) {
 *         Text("Sheet 1")
 *
 *         Button(onClick = {
 *             controller.show { dismiss2 ->
 *                 Column(Modifier.fillMaxWidth().padding(24.dp)) {
 *                     Text("Sheet 2")
 *                     // Close only Sheet 2
 *                     Button(onClick = dismiss2) { Text("Close Sheet 2") }
 *                     // Close every open sheet at once
 *                     Button(onClick = { controller.dismissAll() }) {
 *                         Text("Close All")
 *                     }
 *                 }
 *             }
 *         }) { Text("Open Sheet 2") }
 *
 *         Button(onClick = dismiss1) { Text("Close Sheet 1") }
 *     }
 * }
 * ```
 *
 * ### With custom params
 * ```kotlin
 * controller.show(
 *     params = BottomSheetParams(
 *         showDragHandle = false,
 *         containerColor = MaterialTheme.colorScheme.primaryContainer,
 *         sheetMaxWidth = 560.dp,
 *     )
 * ) { dismiss ->
 *     MyContent(onClose = dismiss)
 * }
 * ```
 */
class BottomSheetController {

    // Auto-incrementing id — no UUID / external dependency needed
    private var nextId = 0L

    /**
     * Live stack of sheet entries observed by [BottomSheetHost].
     * Index 0 = bottom-most sheet, last index = top-most (visible) sheet.
     */
    internal val stack = mutableStateListOf<BottomSheetEntry>()

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Push a new bottom sheet onto the stack.
     *
     * @param params  Visual / behavioural configuration. See [BottomSheetParams].
     * @param content Composable content lambda. Receives a [dismiss] function
     *                that removes **this specific sheet** (with animation).
     */
    fun show(
        params: BottomSheetParams = BottomSheetParams(),
        content: @Composable (dismiss: () -> Unit) -> Unit,
    ) {
        val id = nextId++
        stack.add(BottomSheetEntry(id = id, params = params, content = content))
    }

    /**
     * Dismiss the **top-most** sheet (equivalent to the user tapping outside).
     * Does nothing when the stack is empty.
     */
    fun dismiss() {
        if (stack.isNotEmpty()) stack.removeAt(stack.lastIndex)
    }

    /**
     * Dismiss **all** sheets in the stack simultaneously.
     */
    fun dismissAll() {
        stack.clear()
    }

    // ── Internal ──────────────────────────────────────────────────────────────

    /**
     * Remove a specific entry by its internal [id].
     * Called by [BottomSheetHost] after the hide animation completes.
     */
    internal fun dismissById(id: Long) {
        stack.removeAll { it.id == id }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// CompositionLocal
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Provides [BottomSheetController] to the composition tree.
 * Must be consumed inside a [BottomSheetHost].
 */
val LocalBottomSheetController = compositionLocalOf<BottomSheetController> {
    error(
        "LocalBottomSheetController not found. " +
                "Wrap your root composable with BottomSheetHost { … }"
    )
}
