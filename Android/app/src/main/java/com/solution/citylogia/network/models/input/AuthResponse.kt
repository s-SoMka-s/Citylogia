package com.solution.citylogia.network.models.input

data class AuthResponse(
        val access: TokenResponse,
        val refresh: TokenResponse
)

data class TokenResponse(
        val token: String,
        val expiry: Long
)
