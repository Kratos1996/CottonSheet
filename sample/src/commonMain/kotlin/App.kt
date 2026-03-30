import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.ishant.bottomsheet.BottomSheetHost
import dev.ishant.bottomsheet.BottomSheetParams
import dev.ishant.bottomsheet.LocalBottomSheetController

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun App() {
    MaterialTheme {
        BottomSheetHost {
            //navigation graph
            val bottomsheet = LocalBottomSheetController.current
            
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
             ) {
                Button(onClick = {
                    bottomsheet.show(params = BottomSheetParams(isDismissable = false, isFullScreen = true)) { dismiss ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Bottom Sheet Content",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                text = "This is a sample bottom sheet using the library.",
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                            Button(
                                onClick = dismiss,
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Close", modifier = Modifier.clickable(){
                                    bottomsheet.show { dismiss2->
                                        //compose ui
                                        Button(onClick = {
                                            bottomsheet.dismissAll()
                                        }){
                                            Text(
                                                text = "Bottom Sheet2",
                                                style = MaterialTheme.typography.headlineSmall
                                            )
                                        }
                                    }
                                })

                            }
                        }
                    }
                }) {
                    Text("Show Bottom Sheet")
                }
            }
        }
    }
}
