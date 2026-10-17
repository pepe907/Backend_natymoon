package com.natymoo.backend.config;

import com.natymoo.backend.security.CustomUserDetailsService;
import com.natymoo.backend.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // usamos JWT en cookie httpOnly, no sesiones con formularios
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Rutas publicas
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        // Documentacion Swagger/OpenAPI: publica, para poder ver la API sin login
                        // (las llamadas reales que se hagan desde el "Try it out" si respetan
                        // la seguridad normal de cada endpoint).
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/v3/api-docs.yaml").permitAll()
                        // Lectura de catalogo publica (ver productos sin login).
                        // IMPORTANTE: se usa el enum HttpMethod.GET (no el texto "GET") para que
                        // Spring realmente filtre por metodo. Con el texto entre comillas, Java
                        // llama a otra version de requestMatchers() que ignora el metodo y termina
                        // aplicando la regla a TODOS los metodos -- ese era el bug que abria
                        // POST/DELETE sin login y bloqueaba el POST normal de los clientes.
                        .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categorias/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/variantes-producto/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/imagenes-producto/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/resenas-producto/**").permitAll()
                        // Solo ADMIN puede gestionar roles, usuarios y catalogo (crear/editar/borrar)
                        .requestMatchers("/api/roles/**").hasRole("ADMIN")
                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/categorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categorias/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/variantes-producto/**").hasRole("ADMIN")
                        // Cupones: crear/editar/borrar es solo de ADMIN, pero CONSULTARLOS (GET)
                        // tiene que quedar abierto a cualquier autenticado, porque el checkout
                        // del cliente necesita poder leer la lista para validar el codigo que
                        // escribe.
                        .requestMatchers(HttpMethod.POST, "/api/cupones/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/cupones/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/cupones/**").hasRole("ADMIN")
                        // ADMIN y EMPLEADO gestionan operacion diaria: editar productos,
                        // cambiar estado de pedidos y resolver novedades
                        .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasAnyRole("ADMIN", "EMPLEADO")
                        .requestMatchers(HttpMethod.POST, "/api/variantes-producto/**").hasAnyRole("ADMIN", "EMPLEADO")
                        .requestMatchers(HttpMethod.PUT, "/api/ordenes-compra/**").hasAnyRole("ADMIN", "EMPLEADO")
                        .requestMatchers(HttpMethod.PUT, "/api/novedades/**").hasAnyRole("ADMIN", "EMPLEADO")
                        .requestMatchers(HttpMethod.DELETE, "/api/novedades/**").hasRole("ADMIN")
                        // Cambiar el ESTADO de un pago (aprobar/rechazar) es cosa de staff,
                        // pero CREAR el pago (POST) lo hace el propio cliente al pagar en el checkout
                        .requestMatchers(HttpMethod.PUT, "/api/pagos/**").hasAnyRole("ADMIN", "EMPLEADO")
                        .requestMatchers(HttpMethod.DELETE, "/api/pagos/**").hasAnyRole("ADMIN", "EMPLEADO")
                        // El historial de la orden tambien lo crea el cliente al confirmar su compra
                        .requestMatchers(HttpMethod.PUT, "/api/historial-estado-orden/**").hasAnyRole("ADMIN", "EMPLEADO")
                        .requestMatchers(HttpMethod.DELETE, "/api/historial-estado-orden/**").hasAnyRole("ADMIN", "EMPLEADO")
                        // PUT sobre variantes-producto queda abierto a cualquier autenticado:
                        // el propio cliente lo usa para descontar el stock al confirmar su compra.
                        // DELETE de variantes si queda exclusivo de ADMIN (regla de arriba).
                        //
                        // Todo lo demas requiere estar autenticado, sin restriccion de rol.
                        // Aqui caen, para cualquier usuario logueado: POST de ordenes-compra,
                        // detalle-orden, pagos, historial-estado-orden, novedades, direcciones-usuario,
                        // carritos-compra, detalle-carrito, lista-deseos, resenas-producto (crear),
                        // y GET de cupones, ordenes-compra, pagos, etc.
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // necesario para que el navegador envie la cookie

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
