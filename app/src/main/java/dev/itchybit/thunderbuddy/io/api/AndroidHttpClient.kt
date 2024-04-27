package dev.itchybit.thunderbuddy.io.api

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.net.ssl.SSLContext

fun getHttpClient(@ApplicationContext context: Context, json: Json) = HttpClient(Android) {
    expectSuccess = true

    engine {
        sslManager = { httpsURLConnection ->
            httpsURLConnection.sslSocketFactory = SSLContext.getDefault().socketFactory
        }

        connectTimeout = 60_000
        socketTimeout = 60_000
    }

    install(ContentNegotiation) { json(json, contentType = ContentType.Any) }

    install(Logging) {
        logger = Logger.ANDROID
        level = LogLevel.ALL
    }

    install(DefaultRequest) {
        url {
            protocol = URLProtocol.HTTPS
            host = "api.openweathermap.org/data/2.5"
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
        }
    }

    install(HttpCache) {
        val separator = File.separator
        val filepath = "${context.cacheDir.path}${separator}gp_cache$separator"
        val cacheFile = Files.createDirectories(Paths.get(filepath)).toFile()
        publicStorage(FileStorage(cacheFile))
    }
}