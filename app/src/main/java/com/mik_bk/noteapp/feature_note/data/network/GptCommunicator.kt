package com.mik_bk.noteapp.feature_note.data.network

import com.mik_bk.noteapp.feature_note.data.api_key
import com.mik_bk.noteapp.feature_note.data.models.ChatGPTResponse
import com.mik_bk.noteapp.feature_note.data.models.Message
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray

class GptCommunicator {
    private val json = Json { ignoreUnknownKeys = true }
    private val client = HttpClient(OkHttp){
        install(JsonFeature){
            serializer = KotlinxSerializer(json)
        }
        install(Logging){
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
        install(HttpTimeout){
            connectTimeoutMillis = 30_000
            socketTimeoutMillis = 30_000
        }
    }
    private val textGenerationUrl = "https://api.openai.com/v1/chat/completions"
    private val systemPrompt = Message(role="system", content="You are an clever assistant who responds in a concise, clear and short manner. Try to limit your answers to a maximum of 4-5 sentences, focusing on the most important information.")

    suspend fun fetchGptResponse(prompt: String): Result<ChatGPTResponse> {
        val requestBody = buildJsonObject {
            put("model","gpt-3.5-turbo")
            putJsonArray("messages"){
                addJsonObject {
                    put("role", systemPrompt.role)
                    put("content", systemPrompt.content)
                }
                addJsonObject {
                    put("role", "user")
                    put("content", prompt)
                }
            }
        }

        return runCatching {
            client.post<ChatGPTResponse>(textGenerationUrl){
                header(HttpHeaders.Authorization, "Bearer ${api_key.apiKey}")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                body = requestBody
            }
        }
    }
}