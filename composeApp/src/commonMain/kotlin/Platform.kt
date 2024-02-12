
interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

internal expect fun openUrl(url: String?)
