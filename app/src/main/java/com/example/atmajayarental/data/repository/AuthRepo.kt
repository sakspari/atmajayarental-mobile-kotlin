package com.example.atmajayarental.data.repository

import com.example.atmajayarental.data.api.AuthApi
import com.example.atmajayarental.data.api.model.AuthResponse
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject

class AuthRepo @Inject constructor(
    private val authApi: AuthApi
){
    suspend fun loginRequest(
        email: String,
        password: String
    ): AuthResponse {
        return authApi.loginRequest(email = email, password = password)
    }
}