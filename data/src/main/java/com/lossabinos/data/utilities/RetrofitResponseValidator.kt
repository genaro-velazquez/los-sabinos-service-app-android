package com.lossabinos.data.dto.repositories.retrofit

import com.lossabinos.data.dto.utilities.asJSONObject
import com.lossabinos.data.dto.utilities.asString
import org.json.JSONObject
import retrofit2.Response

class RetrofitResponseValidator {
    companion object {
        fun validate(response: Response<String>): JSONObject {
            if (response.isSuccessful) {
                return JSONObject(response.body()!!)
            }

            var message = "No se pudo procesar la respuesta"
            if (response.errorBody() != null) {
                try {
                    val error = response.errorBody()!!.string()
                    val errorJson = JSONObject(error)

                    // Extraer el mensaje de error del servidor
                    val messageError = errorJson.optString("error", "Error desconocido")

                    // Extraer el código (es un array)
                    val codeArray = errorJson.optJSONArray("code")
                    val codeError = codeArray?.optString(0) ?: "error"

                    message = messageError

                    if (codeError == "404") {
                        return JSONObject()
                    }
                } catch (e: Exception) {
                    println("Error parsing error response: ${e.message}")
                    message = "Error al procesar la respuesta del servidor"
                }
            }

            throw Exception(message)
        }
    }
}

class ApiException(message: String, val code: Int) : Exception(message)
class InvalidResponseException(message: String) : Exception(message)