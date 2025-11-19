package co.edu.unipiloto.rimessa_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import co.edu.unipiloto.rimessa_backend.model.Usuario;
import co.edu.unipiloto.rimessa_backend.repository.UsuarioRepository;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @PostMapping
    public Usuario crear(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @GetMapping("/{usuario}")
    public ResponseEntity<Usuario> obtener(@PathVariable String usuario) {
        Usuario u = usuarioRepository.findByUsuario(usuario);
        if (u == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(u);
    }

    // Login simple (no producción): consulta por usuario + password
    @GetMapping("/login")
    public ResponseEntity<Usuario> login(@RequestParam String usuario, @RequestParam String password) {
        Usuario u = usuarioRepository.findByUsuario(usuario);
        if (u == null) return ResponseEntity.status(404).build();
        if (!u.getPassword().equals(password)) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(u);
    }

    // Actualizar usuario (por usuario)
    @PutMapping("/{usuario}")
    public ResponseEntity<Usuario> actualizar(@PathVariable String usuario, @RequestBody Usuario payload) {
        Usuario u = usuarioRepository.findByUsuario(usuario);
        if (u == null) return ResponseEntity.notFound().build();

        // campos que permitimos actualizar
        u.setNombre(payload.getNombre());
        u.setEmail(payload.getEmail());
        if (payload.getPassword() != null) u.setPassword(payload.getPassword());
        u.setDireccion(payload.getDireccion());
        u.setCelular(payload.getCelular());
        u.setFechaNacimiento(payload.getFechaNacimiento());
        u.setGenero(payload.getGenero());
        // no actualizamos rol ni usuario (clave primaria lógica)

        Usuario saved = usuarioRepository.save(u);
        return ResponseEntity.ok(saved);
    }

    // Subir/actualizar foto (multipart con base64)
    @PutMapping("/{usuario}/foto")
    public ResponseEntity<Usuario> subirFoto(@PathVariable String usuario, @RequestParam("file") MultipartFile file) throws IOException {
        Usuario u = usuarioRepository.findByUsuario(usuario);
        if (u == null) return ResponseEntity.notFound().build();

        byte[] bytes = file.getBytes();
        String base64 = Base64.getEncoder().encodeToString(bytes);
        u.setFotoBase64(base64);
        usuarioRepository.save(u);
        return ResponseEntity.ok(u);
    }

    // Contar usuarios por rol
    @GetMapping("/rol/{rol}/count")
    public long contarPorRol(@PathVariable String rol) {
        return usuarioRepository.countByRolIgnoreCase(rol);
    }
}
