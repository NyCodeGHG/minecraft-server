package de.nycode.mcserver

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.slf4j.LoggerFactory

object ProtocolVersionParser {

    private val logger = LoggerFactory.getLogger(ProtocolVersionParser::class.java)

    private var versions: Map<Int, Version>? = null

    private val VERSIONS_URL =
        "https://gitlab.bixilon.de/bixilon/minosoft/-/raw/master/src/main/resources/assets/mapping/versions.json"

    private val client = HttpClient(Apache) {
        engine {
            followRedirects = true
        }
        install(JsonFeature)
    }

    fun getVersions(): Map<Int, Version> {
        if (versions == null) {
            versions = runBlocking { fetchVersions() }.associateBy { it.number }
        }

        return versions as Map<Int, Version>
    }

    suspend fun fetchVersions(): List<Version> {
        /*val json: JsonObject = Json.decodeFromString(client.get(VERSIONS_URL))

        val versions = mutableListOf<Version>()
        json.forEach { name, element ->
            versions.add(Version(element.jsonObject.getValue("name").jsonPrimitive.content, name.toInt()))
        }

        return versions*/
        return listOf(
            Version("1.16.4", 754),
            Version("1.8.9", 47)
        )
    }

}

class Version(val name: String, val number: Int)
