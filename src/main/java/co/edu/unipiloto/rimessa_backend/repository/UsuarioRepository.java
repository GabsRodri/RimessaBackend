package co.edu.unipiloto.rimessa_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.unipiloto.rimessa_backend.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsuario(String usuario);
}
