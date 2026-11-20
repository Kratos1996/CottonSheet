package dev.ishant.cottonsheet

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CottonSheetControllerTest {

    @Test
    fun testInitialStateIsEmpty() {
        val controller = CottonSheetController()
        assertTrue(controller.stack.isEmpty())
    }

    @Test
    fun testShowAddsEntryToStack() {
        val controller = CottonSheetController()
        controller.show { }
        assertEquals(1, controller.stack.size)
    }

    @Test
    fun testMultipleShowsCreateStack() {
        val controller = CottonSheetController()
        controller.show { }
        controller.show { }
        controller.show { }
        assertEquals(3, controller.stack.size)
    }

    @Test
    fun testDismissRemovesTopEntryAndInvokesCallback() {
        val controller = CottonSheetController()
        var dismissed1 = false
        var dismissed2 = false

        controller.show(params = CottonSheetParams(onDismissed = { dismissed1 = true })) { }
        controller.show(params = CottonSheetParams(onDismissed = { dismissed2 = true })) { }

        assertEquals(2, controller.stack.size)

        controller.dismiss()

        assertEquals(1, controller.stack.size)
        assertTrue(dismissed2, "Topmost entry callback should be invoked")
        assertTrue(!dismissed1, "Bottom entry callback should not be invoked yet")
    }

    @Test
    fun testDismissAllClearsStackAndInvokesAllCallbacks() {
        val controller = CottonSheetController()
        var count = 0

        controller.show(params = CottonSheetParams(onDismissed = { count++ })) { }
        controller.show(params = CottonSheetParams(onDismissed = { count++ })) { }
        controller.show(params = CottonSheetParams(onDismissed = { count++ })) { }

        controller.dismissAll()

        assertTrue(controller.stack.isEmpty())
        assertEquals(3, count)
    }

    @Test
    fun testDismissByIdRemovesSpecificEntry() {
        val controller = CottonSheetController()
        var dismissedMid = false

        controller.show { }
        controller.show(params = CottonSheetParams(onDismissed = { dismissedMid = true })) { }
        controller.show { }

        val midId = controller.stack[1].id
        controller.dismissById(midId)

        assertEquals(2, controller.stack.size)
        assertTrue(dismissedMid)
        assertTrue(controller.stack.none { it.id == midId })
    }

    @Test
    fun testDismissOnEmptyStackDoesNotCrash() {
        val controller = CottonSheetController()
        controller.dismiss()
        assertTrue(controller.stack.isEmpty())
    }

    @Test
    fun testDefaultParamsValues() {
        val params = CottonSheetParams()
        assertTrue(params.isDismissable)
        assertTrue(!params.isFullScreen)
        assertTrue(params.showDragHandle)
        assertTrue(!params.isDragHandleOutside)
        assertEquals(8.dp, params.dragHandleOutsideSpacing)
        assertEquals(null, params.sheetWidth)
        assertEquals(null, params.windowInsets)
    }

    @Test
    fun testCustomWidthAndPaddingParams() {
        val params = CottonSheetParams(
            sheetWidth = 400.dp,
            sheetPadding = PaddingValues(16.dp),
            showDragHandle = false,
            isDragHandleOutside = true,
            dragHandleOutsideSpacing = 16.dp,
        )
        assertEquals(400.dp, params.sheetWidth)
        assertTrue(!params.showDragHandle)
        assertTrue(params.isDragHandleOutside)
        assertEquals(16.dp, params.dragHandleOutsideSpacing)
    }
}
