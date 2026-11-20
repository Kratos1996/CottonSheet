import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.ishant.cottonsheet.CottonSheetHost
import dev.ishant.cottonsheet.CottonSheetParams
import dev.ishant.cottonsheet.LocalCottonSheetController

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun App() {
    MaterialTheme {
        CottonSheetHost {
            val cottonSheet = LocalCottonSheetController.current
            
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "CottonSheet Customization Demo",
                        style = MaterialTheme.typography.titleMedium
                    )

                    // 1. Standard Sheet (centered on desktop/tablet, full width on mobile)
                    Button(onClick = {
                        cottonSheet.show(
                            params = CottonSheetParams(
                                isDismissable = true,
                                isFullScreen = false
                            )
                        ) { dismiss ->
                            SampleSheetContent(
                                title = "Standard BottomSheet",
                                description = "Centered on desktop/tablet, max width on mobile.",
                                onDismiss = dismiss
                            )
                        }
                    }) {
                        Text("1. Standard Sheet (Responsive)")
                    }

                    // 2. Custom Width (e.g. 460.dp centered)
                    Button(onClick = {
                        cottonSheet.show(
                            params = CottonSheetParams(
                                sheetWidth = 460.dp,
                                isDismissable = true
                            )
                        ) { dismiss ->
                            SampleSheetContent(
                                title = "Custom Width (460dp)",
                                description = "Fixed width of 460dp, always centered.",
                                onDismiss = dismiss
                            )
                        }
                    }) {
                        Text("2. Custom Width (460dp)")
                    }

                    // 3. Floating Card with Padding around BottomSheet
                    Button(onClick = {
                        cottonSheet.show(
                            params = CottonSheetParams(
                                sheetWidth = 500.dp,
                                sheetPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 24.dp),
                                shape = RoundedCornerShape(24.dp),
                                isDismissable = true
                            )
                        ) { dismiss ->
                            SampleSheetContent(
                                title = "Floating BottomSheet",
                                description = "Has 24dp padding around edges with fully rounded corners.",
                                onDismiss = dismiss
                            )
                        }
                    }) {
                        Text("3. Floating Sheet (Padding Around)")
                    }

                    // 4. Full-screen sheet (bounded below status bar, centered on desktop)
                    Button(onClick = {
                        cottonSheet.show(
                            params = CottonSheetParams(
                                isFullScreen = true,
                                isDismissable = true
                            )
                        ) { dismiss ->
                            SampleSheetContent(
                                title = "Full Screen Sheet",
                                description = "Fills full height without overflowing status bar/notch, centered on desktop.",
                                onDismiss = dismiss
                            )
                        }
                    }) {
                        Text("4. Full Screen Sheet")
                    }

                    // 5. Outside Drag Handle
                    Button(onClick = {
                        cottonSheet.show(
                            params = CottonSheetParams(
                                isDragHandleOutside = true,
                                dragHandleOutsideSpacing = 8.dp,
                                sheetWidth = 480.dp,
                                sheetPadding = PaddingValues(bottom = 16.dp, start = 16.dp, end = 16.dp),
                                shape = RoundedCornerShape(24.dp),
                                isDismissable = true
                            )
                        ) { dismiss ->
                            SampleSheetContent(
                                title = "Outside Drag Handle",
                                description = "The drag handle sits outside and above the sheet surface.",
                                onDismiss = dismiss
                            )
                        }
                    }) {
                        Text("5. Outside Drag Handle")
                    }

                    // 6. No Drag Handle
                    Button(onClick = {
                        cottonSheet.show(
                            params = CottonSheetParams(
                                showDragHandle = false,
                                sheetWidth = 480.dp,
                                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                                isDismissable = true
                            )
                        ) { dismiss ->
                            SampleSheetContent(
                                title = "No Drag Handle",
                                description = "The drag handle knob is completely hidden.",
                                onDismiss = dismiss
                            )
                        }
                    }) {
                        Text("6. No Drag Handle")
                    }
                }
            }
        }
    }
}

@Composable
private fun SampleSheetContent(
    title: String,
    description: String,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = description,
            modifier = Modifier.padding(vertical = 12.dp)
        )
        Button(
            onClick = onDismiss,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Close")
        }
    }
}
