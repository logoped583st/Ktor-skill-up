package utils

import java.nio.charset.Charset
import java.util.*

fun encodeToken(jwtHashString: String): String =
        Base64.getEncoder().encodeToString(jwtHashString.toByteArray(Charset.defaultCharset()))

fun decodeToken(string: String): String = Base64.getDecoder().decode(string).toString(Charset.defaultCharset())