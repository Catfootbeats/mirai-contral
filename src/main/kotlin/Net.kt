package xyz.catfootbeats

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*

object Net {
    val normalClient = HttpClient(OkHttp)
    fun closeClient() {
        normalClient.close()
    }
}