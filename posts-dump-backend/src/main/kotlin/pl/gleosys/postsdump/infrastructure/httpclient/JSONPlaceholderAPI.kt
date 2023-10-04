package pl.gleosys.postsdump.infrastructure.httpclient

import com.github.kittinunf.fuel.core.HeaderValues
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.core.Parameters
import com.github.kittinunf.fuel.util.FuelRouting
import pl.gleosys.postsdump.infrastructure.EnvironmentProperty

private enum class JSONPlaceholderAPIProperty(override val envName: String) : EnvironmentProperty {
    API_URL_PROP("JSON_PLACEHOLDER_API_URL"),
}

sealed class JSONPlaceholderAPI : FuelRouting {
    override val basePath: String =
        System.getenv(JSONPlaceholderAPIProperty.API_URL_PROP.envName)

    class GetPosts : JSONPlaceholderAPI()

    override val method: Method
        get() = when (this) {
            is GetPosts -> Method.GET
        }
    override val path: String
        get() = when (this) {
            is GetPosts -> "/posts"
        }
    override val params: Parameters?
        get() = null
    override val headers: Map<String, HeaderValues>?
        get() = null
    override val bytes: ByteArray?
        get() = null
    override val body: String?
        get() = null
}
