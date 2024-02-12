package views

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
class InputScreen : Screen{
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val data = "Hello, World!"

        Column {
            Text(text = data)
            Button(
                onClick = {
                    navigator.push(MainScreen())
                }
            ) {
                Text(text = "メインスクリーンへ")
            }
        }
    }
}
