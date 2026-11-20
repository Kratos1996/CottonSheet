import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "CottonSheet Sample",
        state = rememberWindowState(width = 800.dp, height = 700.dp)
    ) {
        App()
    }
}
