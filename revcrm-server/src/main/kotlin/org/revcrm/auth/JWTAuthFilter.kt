package org.revcrm.auth

import com.auth0.jwk.JwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import org.revcrm.db.DBService
import org.revcrm.entities.AuthType
import org.revcrm.entities.RevUserAuth
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.security.interfaces.RSAPublicKey

val AUTH_HEADER_PREFIX = "Bearer "

class JWTAuthFilter(
    val jwkProvider: JwkProvider,
    val jwtIssuer: String,
    val jwtAudience: String,
    val dbService: DBService,
    authenticationManager: AuthenticationManager
) : BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        var authToken: String? = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (authToken != null && authToken.startsWith(AUTH_HEADER_PREFIX)) {
            authToken = authToken.replaceFirst(AUTH_HEADER_PREFIX.toRegex(), "")

            val decodedJwt = JWT.decode(authToken)
            val kid = decodedJwt.getKeyId()
            val jwk = jwkProvider.get(kid)
            val algorithm = Algorithm.RSA256(jwk.publicKey as RSAPublicKey, null)
            val validatedJwt = JWT.require(algorithm)
                .build()
                .verify(decodedJwt)

            if (validatedJwt.issuer != jwtIssuer)
                throw JWTVerificationException("invalid jwt issuer: ${validatedJwt.issuer}")
            if (validatedJwt.audience.size < 1 || validatedJwt.audience[0] != jwtAudience)
                throw JWTVerificationException("invalid jwt audience: ${validatedJwt.audience}")

            val userId = validatedJwt.subject

            val auth = dbService.withDB { ds ->
                ds.createQuery(RevUserAuth::class.java)
                    .field("auth_type").equal(AuthType.GOOGLE)
                    .field("auth_id").equal(userId)
                    .get()
            }

            if (auth == null)
                throw JWTVerificationException("unrecognised jwt sub: $userId")

            SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(userId, null, emptyList<GrantedAuthority>())
        }
        chain.doFilter(request, response)
    }
}