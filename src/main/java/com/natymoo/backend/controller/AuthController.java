package com.natymoo.backend.controller;

import com.natymoo.backend.dto.LoginRequest;
import com.natymoo.backend.dto.MessageResponse;
import com.natymoo.backend.dto.RegisterRequest;
import com.natymoo.backend.dto.UsuarioResponse;
import com.natymoo.backend.entity.Rol;
import com.natymoo.backend.entity.Usuario;
import com.natymoo.backend.exception.BadRequestException;
import com.natymoo.backend.repository.RolRepository;
import com.natymoo.backend.repository.UsuarioRepository;
import com.natymoo.backend.security.CookieUtil;
import com.natymoo.backend.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Registro, login, logout y usuario actual. El login guarda el JWT " +
        "en una cookie httpOnly llamada natymoo_token -- usa 'Try it out' aqui primero, y las demas peticiones " +
        "desde esta misma pagina quedaran autenticadas automaticamente.")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CookieUtil cookieUtil;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Registrar usuario",
            description = "Crea un usuario nuevo con rol USER, inicia sesión automáticamente y setea la cookie del JWT.")
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> register(@Valid @RequestBody RegisterRequest request,
                                                      HttpServletResponse response) {

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Ya existe una cuenta con ese email");
        }

        Rol rolUser = rolRepository.findAll().stream()
                .filter(r -> r.getNombre().equalsIgnoreCase("USER"))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("El rol USER no existe. Ejecuta el seed de roles."));

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .telefono(request.getTelefono())
                .rol(rolUser)
                .activo(true)
                .build();

        usuarioRepository.save(usuario);

        // Login automatico despues de registrarse: generamos el JWT y lo mandamos en cookie
        String token = jwtService.generateToken(usuario);
        cookieUtil.crearCookieJwt(response, token, jwtService.getExpirationMs());

        return ResponseEntity.ok(UsuarioResponse.fromEntity(usuario));
    }

    @Operation(summary = "Iniciar sesión",
            description = "Verifica credenciales y setea la cookie httpOnly del JWT. Empieza por aquí para probar rutas protegidas.")
    @PostMapping("/login")
    public ResponseEntity<UsuarioResponse> login(@Valid @RequestBody LoginRequest request,
                                                   HttpServletResponse response) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado"));

        String token = jwtService.generateToken(usuario);
        cookieUtil.crearCookieJwt(response, token, jwtService.getExpirationMs());

        return ResponseEntity.ok(UsuarioResponse.fromEntity(usuario));
    }

    @Operation(summary = "Cerrar sesión", description = "Borra la cookie del JWT.")
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(HttpServletResponse response) {
        cookieUtil.limpiarCookieJwt(response);
        return ResponseEntity.ok(new MessageResponse("Sesion cerrada correctamente"));
    }

    @Operation(summary = "Usuario actual", description = "Devuelve los datos del usuario autenticado según la cookie actual.")
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> me() {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Usuario usuario)) {
            throw new BadRequestException("No autenticado");
        }
        return ResponseEntity.ok(UsuarioResponse.fromEntity(usuario));
    }
}
