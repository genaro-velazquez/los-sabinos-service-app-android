package com.lossabinos.data.utilities

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class CurlLoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Construir el CURL
        val curlCommand = StringBuilder("curl -X ${request.method}")

        // Agregar headers
        request.headers.forEach { (name, value) ->
            curlCommand.append(" -H \"$name: $value\"")
        }

        // Agregar body si existe
        request.body?.let { body ->
            val buffer = okio.Buffer()
            body.writeTo(buffer)
            val bodyString = buffer.readUtf8()
            curlCommand.append(" -d '$bodyString'")
        }

        // Agregar URL
        curlCommand.append(" \"${request.url}\"")

        // Imprimir en Logcat
        Log.d("CURL_REQUEST", curlCommand.toString())

        // Ejecutar la petición
        val response = chain.proceed(request)

        // Imprimir respuesta
        Log.d("CURL_RESPONSE", "Status: ${response.code} ${response.message}")

        return response
    }
}
