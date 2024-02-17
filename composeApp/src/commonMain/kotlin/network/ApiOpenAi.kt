package network

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.POST
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.asa_o.tryout_kmp.BuildKonfig.openAiApiKey

interface ApiInterfaceOpenAi {
    @POST("chat/completions")
    suspend fun chat(
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Authorization") authorization: String = openAiApiKey,
        @Body chatData: ChatData
    ): Response<ChatResponse>
}

@Serializable
data class ChatMessage(
    val role: String,
    val content: String
)

@Serializable
data class ChatData(
    val messages: List<ChatMessage>,
    val model: String,
    val max_tokens: Int = 4000,
    val temperature: Double = 0.7,
    val top_p: Double = 1.0,
    val frequency_penalty: Double = 0.0,
    val presence_penalty: Double = 0.0
)

@Serializable
data class ChatChoice(
    val index: Int,
    val message: ChatMessage,
)

@Serializable
data class ChatResponse(
    val choices: List<ChatChoice>
)

object ApiOpenAi {
    private val ktorfit: Ktorfit
    private val api: ApiInterfaceOpenAi

    init {
        val myClient = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.v(message, null, "HTTP Client")
                    }
                }
                level = LogLevel.ALL
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 60000
            }
        }
        ktorfit = Ktorfit.Builder().httpClient(myClient).baseUrl("https://api.openai.com/v1/").build()
        api = ktorfit.create<ApiInterfaceOpenAi>()
    }

    suspend fun chat(prompt: String): Response<ChatResponse> {
        val chatMessage = ChatMessage("user", prompt)
        val chatData = ChatData(messages = listOf(chatMessage), model = "gpt-3.5-turbo")
        return api.chat(chatData = chatData)
    }
}
