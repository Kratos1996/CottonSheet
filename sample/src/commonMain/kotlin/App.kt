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
import dev.ishant.cottonsheet.CottonSheetHost
import dev.ishant.cottonsheet.CottonSheetParams
import dev.ishant.cottonsheet.LocalCottonSheetController

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun App() {
    MaterialTheme {
        CottonSheetHost {
            //navigation graph
            val cottonSheet = LocalCottonSheetController.current
            
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
             ) {
                Button(onClick = {
                    cottonSheet.show(params = CottonSheetParams(isDismissable = false, isFullScreen = true)) { dismiss ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "CottonSheet Content",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                text = "This is a sample CottonSheet using the library.",
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                            Button(
                                onClick = dismiss,
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Close", modifier = Modifier.clickable(){
                                    cottonSheet.show { dismiss2->
                                        //compose ui
                                        Button(onClick = {
                                            cottonSheet.dismissAll()
                                        }){
                                            Text(
                                                text = "CottonSheet 2",
                                                style = MaterialTheme.typography.headlineSmall
                                            )
                                        }
                                    }
                                })

                            }
                        }
                    }
                }) {
                    Text("Show CottonSheet")
                }
            }
        }
    }
}
