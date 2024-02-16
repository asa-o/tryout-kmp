package network

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

interface ExampleApi {
    @GET("get")
    suspend fun get(): Response<ExampleResponse>
}

@Serializable
data class ExampleResponse(val args: Map<String, String>, val headers: Headers, val origin: String, val url: String)

@Serializable
data class Headers(
    @SerialName("Accept") val accept: String,
    @SerialName("Accept-Encoding") val acceptEncoding: String? = null,
    @SerialName("Host") val host: String,
    @SerialName("User-Agent") val userAgent: String,
    @SerialName("X-Amzn-Trace-Id") val xAmznTraceId: String
)

class ApiTest {
    private val ktorfit: Ktorfit


    companion object {
        val instance = ApiTest()
    }

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
                        Napier.d(message, null, "HTTP Client")
                    }
                }
                level = LogLevel.ALL
            }
        }
        ktorfit = Ktorfit.Builder().httpClient(myClient).baseUrl("https://httpbin.org/").build()
    }

    suspend fun getExampleApi(): Response<ExampleResponse> {
        val exampleApi = ktorfit.create<ExampleApi>()
        return exampleApi.get()
    }

}
