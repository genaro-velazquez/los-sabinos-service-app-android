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

            var message = "can_not_parse_response"
            if (response.errorBody() != null) {
                val error = response.errorBody()!!.string()
                val errorJson = JSONObject(error)
                val messageError = errorJson.asJSONObject("error").asString("message")
                val codeError = errorJson.asJSONObject("error").asString("code")
                val serverErrorMessage = "$codeError - $messageError"
                if (serverErrorMessage.isNotEmpty()) {
                    message = serverErrorMessage
                }

                if (codeError == "404") {
                    return JSONObject()
                }

            }

            throw Exception(message)
        }
    }
}

class ApiException(message: String, val code: Int) : Exception(message)
class InvalidResponseException(message: String) : Exception(message)