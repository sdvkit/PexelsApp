package com.sdv.kit.pexelsapp.domain.manager

interface FCMManager {
    fun saveToken(token: String)
    fun readToken(): String
}