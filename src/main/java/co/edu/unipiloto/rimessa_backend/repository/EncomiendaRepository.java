package co.edu.unipiloto.rimessa_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import co.edu.unipiloto.rimessa_backend.model.Encomienda;

import java.util.List;
import java.util.Map;

public interface EncomiendaRepository extends JpaRepository<Encomienda, String> {

    List<Encomienda> findByUsuarioRemitente(String usuarioRemitente);

    List<Encomienda> findByEstadoIgnoreCase(String estado);

    List<Encomienda> findByRecolectorId(String recolectorId);

    List<Encomienda> findByRecolectorIdAndEstadoIn(String recolectorId, List<String> estados);

    List<Encomienda> findByRecolectorIdAndEstadoIgnoreCase(String recolectorId, String estado);


    @Query("SELECT AVG(e.calificacion) FROM Encomienda e WHERE e.calificacion IS NOT NULL")
    Double obtenerPromedioGlobal();

    @Query("SELECT AVG(e.calificacion) FROM Encomienda e WHERE e.usuarioRemitente = ?1 AND e.calificacion IS NOT NULL")
    Double obtenerPromedioPorUsuario(String usuario);

    @Query("SELECT AVG(e.calificacion) FROM Encomienda e WHERE e.recolectorId = ?1 AND e.calificacion IS NOT NULL")
    Double obtenerPromedioPorRecolector(String recolectorId);

    // Para distribución: se puede manejar desde Java a partir de todos los registros con calificacion > 0
    List<Encomienda> findByCalificacionIsNotNull();
}
