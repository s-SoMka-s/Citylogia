package com.solution.citylogia.services

import com.google.gson.Gson
import com.solution.citylogia.network.StorageService
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthorizationService @Inject constructor(private val storage: StorageService) {
    private val storageTokenKey: String = "STORAGE_TOKENS_KEY"

    fun isLoggedIn(): Boolean {
        val data = getTokens();

        return data != null && !data.isTemporary && !this.isRefreshTokenExpired();
    }

    fun logOut() {
        storage.removeItem(storageTokenKey);
    }

    fun getAccessToken(): String? {
        return getTokens()?.tokens
                          ?.access
                          ?.token
    }

    private fun getRefreshToken(): String? {
        return getTokens()?.tokens
                          ?.refresh
                          ?.token
    }

    fun isTemporaryLoggedIn(): Boolean {
        val data = getTokens();

        return data != null && !data.isTemporary && !this.isRefreshTokenExpired();
    }

    fun isAccessTokenExpired(): Boolean {
        val data = getTokens()

        if (data == null || data.tokens.access == null) {
            return true
        }

        val expireTime = data.tokens.access.expiry * 1000;
        val nowTime = Date().time;

        return expireTime < nowTime
    }

    private fun isRefreshTokenExpired(): Boolean {
        val data = getTokens()

        if (data == null || data.tokens.refresh == null) {
            return true
        }

        val expireTime = data.tokens.refresh.expiry * 1000;
        val nowTime = Date().time;

        return expireTime < nowTime
    }

    private fun getTokens(): ClientAuthData? {
        val value = this.storage.getItem(storageTokenKey)

        if (value != null){
            var gson = Gson()
            return gson.fromJson(value, ClientAuthData::class.java)
        }

        return null
    }
}

data class ClientAuthData(var isTemporary: Boolean, var tokens: Tokens)

data class Tokens(var access: Token, var refresh: Token)

data class Token(var token: String, var expiry: Long)