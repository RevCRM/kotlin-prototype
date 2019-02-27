package org.revcrm.config

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import org.revcrm.auth.JWTAuthFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint
import java.net.URL

@Configuration
@EnableWebSecurity
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Value("\${jwt.jwksUrl}")
    private lateinit var jwksUrl: String

    @Value("\${jwt.issuer}")
    private lateinit var jwtIssuer: String

    @Value("\${jwt.audience}")
    private lateinit var jwtAudience: String

    @Bean
    fun buildJwkProvider(): JwkProvider {
        return JwkProviderBuilder(URL(jwksUrl)).build()
    }

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
        http.exceptionHandling().authenticationEntryPoint(Http403ForbiddenEntryPoint())
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http
            .httpBasic().disable()
            .formLogin().disable()
            .logout().disable()
            .addFilter(JWTAuthFilter(
                buildJwkProvider(),
                jwtIssuer,
                jwtAudience,
                authenticationManager()))

        http.authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/static/**").permitAll()
            .antMatchers("/graphiql/**").permitAll()
            .antMatchers("/**").authenticated()
    }
}