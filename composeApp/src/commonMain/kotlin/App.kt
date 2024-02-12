
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import net.asa_o.tryout_kmp.views.MainScreen
import views.*

@Composable
fun App() {

    Navigator(
        screen = MainScreen(),
    )
    }
