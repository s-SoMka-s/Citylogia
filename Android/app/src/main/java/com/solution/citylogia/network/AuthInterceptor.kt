package com.solution.citylogia.network

import com.solution.citylogia.services.AuthorizationService
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.log

@Singleton
class AuthInterceptor @Inject constructor(private val authorizationService: AuthorizationService) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (canSkip(request)) {
            return chain.proceed(request)
        }

        if (requiresFullyAuthorization(request)) {
            if (this.authorizationService.isLoggedIn()) {
                return this.authorizeFully(request, chain);
            }

            println("Calling this service requires full user authorization. $request")
            throw Exception("Calling this service requires full user authorization");
        }

        if (authorizationService.isLoggedIn()) {
            return this.authorizeFully(request, chain)
        }

        return this.authorizeTemporary(request, chain);
    }

    private fun authorizeTemporary(request: Request, chain: Interceptor.Chain): Response {
        if (authorizationService.isTemporaryLoggedIn()) {
            if (authorizationService.isAccessTokenExpired()){
                // TODO refresh tokens
            }
        }

        return this.continueWithAccessToken(request, chain)
    }

    private fun authorizeFully(request: Request, chain: Interceptor.Chain): Response {
        if (authorizationService.isAccessTokenExpired()){
            // TODO refresh tokens
        }

        return this.continueWithAccessToken(request, chain)
    }

    private fun continueWithAccessToken(request: Request, chain: Interceptor.Chain): Response {
        val token = authorizationService.getAccessToken()
        val res = request.newBuilder()
                         .addHeader("Authorization", "Bearer $token")
                         .build()

        return chain.proceed(res)
    }

    private fun canSkip(request: Request): Boolean {
        val paths = listOf("/Auth/Register", "/Auth/Email", "/Auth/Temporary")

        return paths.any{path -> request.url.toString().contains(path)}
    }

    private fun requiresFullyAuthorization(request: Request): Boolean {
        // TODO Implement
        val paths = listOf("/Users")

        return paths.any{path -> request.url.toString().contains(path)}
    }

}