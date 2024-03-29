package net.asa_o.tryout_kmp.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.aakira.napier.Napier
import org.jetbrains.compose.resources.painterResource
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import net.asa_o.tryout_kmp.getPlatform
import org.jetbrains.compose.resources.ExperimentalResourceApi
import kotlin.random.Random
import kotlinx.serialization.Serializable
import network.ApiOpenAi
import network.ApiTest
import views.OpenAiChatScreen

class MainScreen : Screen {
    private val platform = getPlatform()

    fun greet(): String {
        Random.nextBoolean()
        val firstWord = if (Random.nextBoolean()) "Hi!" else "Hello!"
        return "${firstWord}, ${platform.name}!"
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        MaterialTheme {
            var showContent by remember { mutableStateOf(false) }
            var greeting = remember { greet() }
            val (responseText, setResponseText) = remember { mutableStateOf("aaa") }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = {
                        showContent = !showContent
                        Napier.i("ネイピアでログ出力")
                    }) {
                    greeting = greet()
                    Text("Click me!")
                }
                AnimatedVisibility(showContent) {
                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painterResource("compose-multiplatform.xml"), null)
                        Text("Compose: $greeting")
                    }
                }
                Button(
                    onClick = {
                        navigator.push(InputScreen())
                    }
                ) {
                    Text(text = "入力画面へ")
                }

                Button(
                    onClick = {
                        navigator.push(OpenAiChatScreen())
                    }
                ) {
                    Text(text = "OpenAIチャットへ")
                }

                Button(
                    onClick = {
                        setResponseText("api呼び出し中")
                        CoroutineScope(Dispatchers.IO).launch {
                            ApiTest.getExampleApi().apply {
                                setResponseText(body().toString())
                            }
                        }
                    }
                ) {
                    Text(text = "api呼び出し")
                }

                Text(text = responseText)
            }
        }
    }
}
