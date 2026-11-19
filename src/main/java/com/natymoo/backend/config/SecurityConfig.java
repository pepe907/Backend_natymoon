package com.natymoo.backend.config;

import com.natymoo.backend.security.CustomUserDetailsService;
import com.natymoo.backend.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthFilter jwtAuthFilter;

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Value("${app.swagger.enabled:false}")
    private boolean swaggerEnabled;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    @SuppressWarnings("java:S4502")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(h -> h
                        .frameOptions(f -> f.deny())
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000))
                        .referrerPolicy(r -> r.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER)))
                .exceptionHandling(ex -> ex
                        // 401 real cuando no hay token / es invalido (por defecto devolvia 403)
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authorizeHttpRequests(auth -> {
                    // ---- Publico ----
                    auth.requestMatchers("/api/auth/**").permitAll();
                    auth.requestMatchers("/api/public/**").permitAll();
                    if (swaggerEnabled) {
                        auth.requestMatchers("/swagger-ui/**", "/swagger-ui.html",
                                "/v3/api-docs/**", "/v3/api-docs.yaml").permitAll();
                    } else {
                        auth.requestMatchers("/swagger-ui/**", "/swagger-ui.html",
                                "/v3/api-docs/**", "/v3/api-docs.yaml").denyAll();
                    }
                    auth.requestMatchers(HttpMethod.GET,
                            "/api/productos/**",
                            "/api/categorias/**",
                            "/api/variantes-producto/**",
                            "/api/imagenes-producto/**",
                            "/api/resenas-producto/**").permitAll();

                    // ---- Solo ADMIN ----
                    auth.requestMatchers("/api/roles/**", "/api/usuarios/**").hasRole("ADMIN");

                    // Cupones: el cliente solo puede VALIDAR uno (debe ir antes de las reglas de cupones)
                    auth.requestMatchers(HttpMethod.POST, "/api/cupones/validar").authenticated();
                    auth.requestMatchers("/api/cupones/**").hasRole("ADMIN"); // incluye GET: no listar codigos

                    auth.requestMatchers(HttpMethod.POST, "/api/productos/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/categorias/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/categorias/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/categorias/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/variantes-producto/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/imagenes-producto/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/ordenes-compra/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/novedades/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/pagos/**").hasRole("ADMIN");

                    // ---- ADMIN o EMPLEADO ----
                    auth.requestMatchers(HttpMethod.PUT, "/api/productos/**").hasAnyRole("ADMIN", "EMPLEADO");
                    auth.requestMatchers(HttpMethod.POST, "/api/variantes-producto/**").hasAnyRole("ADMIN", "EMPLEADO");
                    auth.requestMatchers(HttpMethod.PUT, "/api/variantes-producto/**").hasAnyRole("ADMIN", "EMPLEADO");
                    auth.requestMatchers(HttpMethod.POST, "/api/imagenes-producto/**").hasAnyRole("ADMIN", "EMPLEADO");
                    auth.requestMatchers(HttpMethod.PUT, "/api/imagenes-producto/**").hasAnyRole("ADMIN", "EMPLEADO");
                    auth.requestMatchers(HttpMethod.PUT, "/api/ordenes-compra/**").hasAnyRole("ADMIN", "EMPLEADO");
                    auth.requestMatchers(HttpMethod.POST, "/api/novedades/**").hasAnyRole("ADMIN", "EMPLEADO");
                    auth.requestMatchers(HttpMethod.PUT, "/api/novedades/**").hasAnyRole("ADMIN", "EMPLEADO");
                    auth.requestMatchers(HttpMethod.PUT, "/api/pagos/**").hasAnyRole("ADMIN", "EMPLEADO");
                    auth.requestMatchers(HttpMethod.POST, "/api/historial-estado-orden/**").hasAnyRole("ADMIN", "EMPLEADO");
                    auth.requestMatchers(HttpMethod.PUT, "/api/historial-estado-orden/**").hasAnyRole("ADMIN", "EMPLEADO");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/historial-estado-orden/**").hasAnyRole("ADMIN", "EMPLEADO");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/resenas-producto/**").hasAnyRole("ADMIN", "EMPLEADO");

                    // PATCH no tenia ninguna regla: por defecto solo staff (ajusta si el cliente necesita alguno)
                    auth.requestMatchers(HttpMethod.PATCH, "/api/**").hasAnyRole("ADMIN", "EMPLEADO");

                    // ---- Resto: usuario autenticado (la propiedad del recurso se valida en el service) ----
                    auth.anyRequest().authenticated();
                })
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        List<String> origins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(o -> !o.isEmpty())
                .toList();

        if (origins.isEmpty() || origins.contains("*")) {
            throw new IllegalStateException("app.cors.allowed-origins debe listar origenes explicitos (sin '*')");
        }

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(origins);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        // El JWT viaja en el header, no en cookies: no hace falta enviar credenciales
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}