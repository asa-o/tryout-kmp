package views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.asa_o.tryout_kmp.openUrl
import net.asa_o.tryout_kmp.theme.LocalThemeIsDark
import net.asa_o.tryout_kmp.views.MainScreen
import network.ApiOpenAi

class OpenAiChatScreen : Screen {
    @Composable
    override fun Content() {
        var prompt by remember { mutableStateOf("") }
        var answer by remember { mutableStateOf("") }
        val navigator = LocalNavigator.currentOrThrow

        Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)) {

            Row(horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "Prompt",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }

            OutlinedTextField(
                value = prompt,
                onValueChange = { prompt = it },
                label = { Text("Prompt") },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                maxLines = 5,
                minLines = 5
            )

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        ApiOpenAi.chat(prompt).apply {
                            answer = body()?.choices?.get(0)?.message?.content ?: "No answer"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("send")
            }

            OutlinedTextField(
                value = answer,
                onValueChange = { },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                readOnly = true,
                maxLines = 5,
                minLines = 5
            )

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
