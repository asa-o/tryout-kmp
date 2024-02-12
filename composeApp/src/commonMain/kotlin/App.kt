import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import views.*

@Composable
fun App() {

    Navigator(
        screen = MainScreen(),
    )
    }
