package org.revcrm.config

import com.auth0.jwk.JwkProviderBuilder
import org.revcrm.auth.JWTAuthFilter
import org.revcrm.db.DBService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint
import java.net.URL
import javax.servlet.Filter

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    val dbService: DBService
) : WebSecurityConfigurerAdapter() {

    @Value("\${jwt.jwksUrl}")
    private lateinit var jwksUrl: String

    @Value("\${jwt.issuer}")
    private lateinit var jwtIssuer: String

    @Value("\${jwt.audience}")
    private lateinit var jwtAudience: String

    fun buildAuthFilter(): Filter {
        val jwkProvider = JwkProviderBuilder(URL(jwksUrl)).build()
        return JWTAuthFilter(
            jwkProvider,
            jwtIssuer,
            jwtAudience,
            dbService,
            authenticationManager())
    }

    override fun configure(http: HttpSecurity) {
        val authFilter = buildAuthFilter()

        http.csrf().disable()
        http.exceptionHandling().authenticationEntryPoint(Http403ForbiddenEntryPoint())
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http
            .httpBasic().disable()
            .formLogin().disable()
            .logout().disable()
            .addFilter(authFilter)

        http.authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/static/**").permitAll()
            .antMatchers("/graphiql/**").permitAll()
            .antMatchers("/**").authenticated()
    }
}