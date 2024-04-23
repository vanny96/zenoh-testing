import io.zenoh.Config
import java.net.URL
import java.nio.file.Path

fun getZenohConfigFile(): Config {
    getResourceURL("config.json5")?.let { configUrl ->
        return Config.from(Path.of(configUrl.toURI()))
    } ?: throw RuntimeException()
}

private fun getResourceURL(resourceName: String): URL? = object {}.javaClass.classLoader.getResource(resourceName)