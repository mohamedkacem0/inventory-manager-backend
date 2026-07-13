package inventory_manager.controller;

import inventory_manager.dto.*;
import inventory_manager.model.Usuario;
import inventory_manager.repository.UsuarioRepository;
import inventory_manager.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Ese email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol("USER"); // por defecto, todo el mundo entra como USER

        usuarioRepository.save(usuario);

        return ResponseEntity.status(201).body("Usuario creado correctamente");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        var usuarioOpt = usuarioRepository.findByEmail(request.getEmail());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Credenciales incorrectas");
        }

        Usuario usuario = usuarioOpt.get();

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            return ResponseEntity.badRequest().body("Credenciales incorrectas");
        }

        String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol());

        return ResponseEntity.ok(
            new AuthResponse(token, usuario.getNombre(), usuario.getEmail(), usuario.getRol())
        );
    }
}