package co.edu.unipiloto.rimessa_backend.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import co.edu.unipiloto.rimessa_backend.model.Usuario;
import co.edu.unipiloto.rimessa_backend.repository.UsuarioRepository;

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
    public Usuario obtener(@PathVariable String usuario) {
        return usuarioRepository.findByUsuario(usuario);
    }
}
