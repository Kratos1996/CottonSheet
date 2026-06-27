package dev.ishant.cottonsheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateListOf

// ─────────────────────────────────────────────────────────────────────────────
// Internal model — one entry per CottonSheet layer in the stack
// ─────────────────────────────────────────────────────────────────────────────

internal data class CottonSheetEntry(
    val id: Long,
    val params: CottonSheetParams,
    val content: @Composable (dismiss: () -> Unit) -> Unit,
)

// ─────────────────────────────────────────────────────────────────────────────
// CottonSheetController
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Drives the [CottonSheetHost].
 *
 * Supports a **stack of CottonSheets**: each [show] call pushes a new layer on top.
 * CottonSheets are dismissed individually via the `dismiss` lambda or all at once
 * with [dismissAll].
 *
 * Obtain the instance via [LocalCottonSheetController] inside any composable
 * that lives under a [CottonSheetHost].
 *
 * ### Single CottonSheet
 * ```kotlin
 * val controller = LocalCottonSheetController.current
 *
 * controller.show { dismiss ->
 *     Column(Modifier.fillMaxWidth().padding(24.dp)) {
 *         Text("Hello!")
 *         Button(onClick = dismiss) { Text("Close") }
 *     }
 * }
 * ```
 *
 * ### Stacked CottonSheets
 * ```kotlin
 * controller.show { dismiss1 ->
 *     Column(Modifier.fillMaxWidth().padding(24.dp)) {
 *         Text("CottonSheet 1")
 *
 *         Button(onClick = {
 *             controller.show { dismiss2 ->
 *                 Column(Modifier.fillMaxWidth().padding(24.dp)) {
 *                     Text("CottonSheet 2")
 *                     // Close only CottonSheet 2
 *                     Button(onClick = dismiss2) { Text("Close CottonSheet 2") }
 *                     // Close every open CottonSheet at once
 *                     Button(onClick = { controller.dismissAll() }) {
 *                         Text("Close All")
 *                     }
 *                 }
 *             }
 *         }) { Text("Open CottonSheet 2") }
 *
 *         Button(onClick = dismiss1) { Text("Close CottonSheet 1") }
 *     }
 * }
 * ```
 *
 * ### With custom params
 * ```kotlin
 * controller.show(
 *     params = CottonSheetParams(
 *         showDragHandle = false,
 *         containerColor = MaterialTheme.colorScheme.primaryContainer,
 *         sheetMaxWidth = 560.dp,
 *     )
 * ) { dismiss ->
 *     MyContent(onClose = dismiss)
 * }
 * ```
 */
class CottonSheetController {

    // Auto-incrementing id — no UUID / external dependency needed
    private var nextId = 0L

    /**
     * Live stack of CottonSheet entries observed by [CottonSheetHost].
     * Index 0 = bottom-most CottonSheet, last index = top-most (visible) CottonSheet.
     */
    internal val stack = mutableStateListOf<CottonSheetEntry>()

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Push a new CottonSheet onto the stack.
     *
     * @param params  Visual / behavioural configuration. See [CottonSheetParams].
     * @param content Composable content lambda. Receives a [dismiss] function
     *                that removes **this specific CottonSheet** (with animation).
     */
    fun show(
        params: CottonSheetParams = CottonSheetParams(),
        content: @Composable (dismiss: () -> Unit) -> Unit,
    ) {
        val id = nextId++
        stack.add(CottonSheetEntry(id = id, params = params, content = content))
    }

    /**
     * Dismiss the **top-most** CottonSheet (equivalent to the user tapping outside).
     * Does nothing when the stack is empty.
     */
    fun dismiss() {
        if (stack.isNotEmpty()) stack.removeAt(stack.lastIndex)
    }

    /**
     * Dismiss **all** CottonSheets in the stack simultaneously.
     */
    fun dismissAll() {
        stack.clear()
    }

    // ── Internal ──────────────────────────────────────────────────────────────

    /**
     * Remove a specific entry by its internal [id].
     * Called by [CottonSheetHost] after the hide animation completes.
     */
    internal fun dismissById(id: Long) {
        stack.removeAll { it.id == id }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// CompositionLocal
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Provides [CottonSheetController] to the composition tree.
 * Must be consumed inside a [CottonSheetHost].
 */
val LocalCottonSheetController = compositionLocalOf<CottonSheetController> {
    error(
        "LocalCottonSheetController not found. " +
                "Wrap your root composable with CottonSheetHost { … }"
    )
}
