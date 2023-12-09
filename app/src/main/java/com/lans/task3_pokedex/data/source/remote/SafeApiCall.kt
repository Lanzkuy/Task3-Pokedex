package com.lans.task3_pokedex.data.source.remote

import com.lans.task3_pokedex.common.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface SafeApiCall {
    suspend fun <T> safeCall(response: suspend () -> T): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(response.invoke())
            } catch (e: Throwable) {
                when (e) {
                    is HttpException -> {
                        Resource.Error(e.message.toString())
                    }

                    is UnknownHostException -> {
                        Resource.Error("Connection failed: Unable to find the server. Please check your internet connection.")
                    }

                    is SocketTimeoutException -> {
                        Resource.Error("Connection Timeout: The server is taking too long to respond. Check your internet connection.")
                    }

                    is ConnectException -> {
                        Resource.Error("Failed to connect: Unable to established a connection to the server")
                    }

                    else -> Resource.Error("Something went wrong")
                }
            }
        }
    }
}