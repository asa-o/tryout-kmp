import kotlin.random.Random
class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        Random.nextBoolean()
        val firstWord = if (Random.nextBoolean()) "Hi!" else "Hello!"
        return "${firstWord}, ${platform.name}!"
    }
}

