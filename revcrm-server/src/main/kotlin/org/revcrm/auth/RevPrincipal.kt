package org.revcrm.auth

import com.auth0.jwt.interfaces.Payload
import io.ktor.auth.Principal
import org.revcrm.models.RevUserAuth

class JWTPrincipal(val payload: Payload) : Principal

class RevPrincipal (
    val payload: Payload,
    val auth: RevUserAuth
) : Principal