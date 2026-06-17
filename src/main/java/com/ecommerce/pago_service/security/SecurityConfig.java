package com.ecommerce.pago_service.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration

public class SecurityConfig{

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        // **** FUNCIONAMIENTO DE RUTAS DE ENDPOINTS SEGUN EL ROL DE CADA USUARIO
        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/swagger-ui/**","/v3/api-docs/**","/doc/swagger-ui.html").permitAll() //http://localhost:8085/swagger-ui/index.html//
                .requestMatchers(HttpMethod.POST, "/pagos").hasAnyRole("USUARIO", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/pagos/mis-pagos").hasRole("USUARIO")
                .requestMatchers(HttpMethod.GET, "/pagos").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/pagos/{id}").hasAnyRole("USUARIO", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/pagos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/pagos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/pagos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/pagos/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}