package com.natymoo.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de Swagger/OpenAPI. Con esto, springdoc genera automaticamente
 * la documentacion interactiva a partir de los controladores REST existentes.
 *
 * Como Natymoo usa JWT guardado en una cookie httpOnly (no un header
 * "Authorization: Bearer ..."), se documenta un esquema de seguridad tipo
 * "apiKey" ubicado en una cookie. Esto es solo informativo para quien lea la
 * documentacion -- el boton "Try it out" de Swagger UI funciona igual sin
 * configurar nada mas, porque corre en el navegador y ya envia la cookie
 * automaticamente una vez el usuario inicia sesion con POST /api/auth/login
 * desde la misma pagina de Swagger.
 */
@Configuration
public class OpenApiConfig {

    private static final String ESQUEMA_COOKIE_JWT = "cookieAuth";

    @Bean
    public OpenAPI natymooOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Natymoo API")
                        .description(
                                "Documentacion de la API REST de Natymoo (tienda de ropa de descanso). "
                                + "Backend en Spring Boot + PostgreSQL con autenticacion JWT guardada en "
                                + "una cookie httpOnly.\n\n"
                                + "### Como probar endpoints protegidos desde aqui\n"
                                + "1. Abre el grupo **Autenticacion** y usa `POST /api/auth/login` con "
                                + "'Try it out' (o `/api/auth/register` si no tienes cuenta).\n"
                                + "2. Al responder 200, el navegador ya guardo la cookie del JWT "
                                + "automaticamente.\n"
                                + "3. A partir de ahi, cualquier otro endpoint que pruebes desde esta misma "
                                + "pagina ya viaja autenticado, sin que tengas que pegar ningun token a mano.\n\n"
                                + "### Roles\n"
                                + "- Publico (sin login): ver productos, categorias, variantes, imagenes, "
                                + "resenas.\n"
                                + "- Autenticado (cualquier rol): carrito, checkout, direcciones propias, "
                                + "resenas propias, novedades propias, consultar cupones.\n"
                                + "- ADMIN o EMPLEADO: cambiar estado de pedidos, aprobar/rechazar pagos, "
                                + "gestionar stock, resolver novedades.\n"
                                + "- Solo ADMIN: roles, usuarios, cupones (crear/editar/borrar), "
                                + "crear/borrar productos y categorias."
                        )
                        .version("1.0.0")
                        .contact(new Contact().name("Natymoo")))
                .addSecurityItem(new SecurityRequirement().addList(ESQUEMA_COOKIE_JWT))
                .components(new Components()
                        .addSecuritySchemes(ESQUEMA_COOKIE_JWT, new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)
                                .name("natymoo_token")
                                .description("JWT guardado como cookie httpOnly tras iniciar sesion en /api/auth/login")));
    }
}
