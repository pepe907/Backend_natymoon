package com.natymoo.backend.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    @Value("${app.jwt.cookie-name}")
    private String cookieName;

    @Value("${app.jwt.cookie-secure}")
    private boolean cookieSecure;

    /**
     * Crea la cookie httpOnly que guarda el JWT. El navegador la envia
     * automaticamente en cada request, y JavaScript no puede leerla
     * (protege contra robo de token via XSS).
     */
    public void crearCookieJwt(HttpServletResponse response, String token, long maxAgeMs) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);       // true en produccion (HTTPS)
        cookie.setPath("/");
        cookie.setMaxAge((int) (maxAgeMs / 1000));
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);
    }

    /** Elimina la cookie del JWT (usado en logout). */
    public void limpiarCookieJwt(HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    /** Lee el JWT desde las cookies de la request entrante. */
    public String obtenerTokenDeCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
