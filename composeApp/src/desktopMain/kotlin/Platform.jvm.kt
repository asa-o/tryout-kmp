package net.asa_o.tryout_kmp

import io.github.aakira.napier.Napier
import java.awt.Desktop
import java.net.URI

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

internal actual fun openUrl(url: String?) {
    val uri = url?.let { URI.create(it) } ?: return
    try {
        Desktop.getDesktop().browse(uri)
    } catch (e:Exception){
        Napier.e(e.toString())
    }
}
