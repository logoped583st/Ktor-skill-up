package entities.auth

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

data class JwtTokenResponse(val accessToken: String)

data class JwtTokenHasher(val login: String, val password: String, val id: String)

fun JwtTokenHasher.stringlify(): String {
    return jacksonObjectMapper().writeValueAsString(this)
}

fun toJwtTokenHasher(tokenHash: String): JwtTokenHasher {
    return jacksonObjectMapper().readValue<JwtTokenHasher>(tokenHash)
}