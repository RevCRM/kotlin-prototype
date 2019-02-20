package org.revcrm.util

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import java.net.URL

fun makeJwtVerifier(jwksUrl: String): JwkProvider {
    return JwkProviderBuilder(URL(jwksUrl)).build()
}