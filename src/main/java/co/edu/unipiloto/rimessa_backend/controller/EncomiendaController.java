package co.edu.unipiloto.rimessa_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unipiloto.rimessa_backend.model.Encomienda;
import co.edu.unipiloto.rimessa_backend.repository.EncomiendaRepository;
import co.edu.unipiloto.rimessa_backend.dto.CalificacionRequest;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/encomiendas")
@CrossOrigin(origins = "*")
public class EncomiendaController {

    private final EncomiendaRepository encomiendaRepository;

    public EncomiendaController(EncomiendaRepository encomiendaRepository) {
        this.encomiendaRepository = encomiendaRepository;
    }

    @GetMapping
    public List<Encomienda> listar() {
        return encomiendaRepository.findAll();
    }

    @PostMapping
    public Encomienda crear(@RequestBody Encomienda encomienda) {
        // default estado si viene vacío
        if (encomienda.getEstado() == null) encomienda.setEstado("SOLICITADO");
        return encomiendaRepository.save(encomienda);
    }

    @GetMapping("/{guia}")
    public ResponseEntity<Encomienda> obtenerPorGuia(@PathVariable String guia) {
        return encomiendaRepository.findById(guia)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/remitente/{usuario}")
    public List<Encomienda> obtenerPorUsuarioRemitente(@PathVariable String usuario) {
        return encomiendaRepository.findByUsuarioRemitente(usuario);
    }

    @GetMapping("/estado/{estado}")
    public List<Encomienda> obtenerPorEstado(@PathVariable String estado) {
        return encomiendaRepository.findByEstadoIgnoreCase(estado);
    }

    @GetMapping("/recolector/{id}")
    public List<Encomienda> obtenerPorRecolector(@PathVariable String id) {
        return encomiendaRepository.findByRecolectorId(id);
    }

    @GetMapping("/sin-recolector")
    public List<Map<String, String>> obtenerSinRecolector() {
        List<Encomienda> all = encomiendaRepository.findAll();
        List<Map<String,String>> lista = new ArrayList<>();
        for (Encomienda e : all) {
            if (e.getRecolectorId() == null || e.getRecolectorId().trim().isEmpty()) {
                Map<String,String> m = new HashMap<>();
                m.put("guia", e.getNumeroGuia());
                m.put("destinatario", e.getDestinatario());
                m.put("direccion_destinatario", e.getDireccionDestinatario());
                m.put("usuario_remitente", e.getUsuarioRemitente());
                lista.add(m);
            }
        }
        return lista;
    }

    @PutMapping("/{guia}/asignar/{recolectorId}")
    public ResponseEntity<Encomienda> asignarRecolector(@PathVariable String guia, @PathVariable String recolectorId) {
        Optional<Encomienda> opt = encomiendaRepository.findById(guia);
        if (!opt.isPresent()) return ResponseEntity.notFound().build();
        Encomienda e = opt.get();
        e.setRecolectorId(recolectorId);
        encomiendaRepository.save(e);
        return ResponseEntity.ok(e);
    }

    @PutMapping("/{guia}/estado/{nuevo}")
    public ResponseEntity<Encomienda> actualizarEstado(@PathVariable String guia, @PathVariable String nuevo) {
        Optional<Encomienda> opt = encomiendaRepository.findById(guia);
        if (!opt.isPresent()) return ResponseEntity.notFound().build();
        Encomienda e = opt.get();
        e.setEstado(nuevo);
        encomiendaRepository.save(e);
        return ResponseEntity.ok(e);
    }

    @PutMapping("/{guia}/calificacion")
    public ResponseEntity<Encomienda> guardarCalificacion(@PathVariable String guia, @RequestBody CalificacionRequest req) {
        Optional<Encomienda> opt = encomiendaRepository.findById(guia);
        if (!opt.isPresent()) return ResponseEntity.notFound().build();
        Encomienda e = opt.get();
        e.setCalificacion(req.getCalificacion());
        e.setComentario(req.getComentario());
        encomiendaRepository.save(e);
        return ResponseEntity.ok(e);
    }

    // Estadísticas
    @GetMapping("/estadisticas/promedio")
    public Map<String, Double> promedioGlobal() {
        Double avg = encomiendaRepository.obtenerPromedioGlobal();
        Map<String, Double> m = new HashMap<>();
        m.put("promedio", avg == null ? 0.0 : avg);
        return m;
    }

    @GetMapping("/estadisticas/usuario/{usuario}")
    public Map<String, Double> promedioPorUsuario(@PathVariable String usuario) {
        Double avg = encomiendaRepository.obtenerPromedioPorUsuario(usuario);
        Map<String, Double> m = new HashMap<>();
        m.put("promedio", avg == null ? 0.0 : avg);
        return m;
    }

    @GetMapping("/estadisticas/recolector/{id}")
    public Map<String, Double> promedioPorRecolector(@PathVariable String id) {
        Double avg = encomiendaRepository.obtenerPromedioPorRecolector(id);
        Map<String, Double> m = new HashMap<>();
        m.put("promedio", avg == null ? 0.0 : avg);
        return m;
    }

    @GetMapping("/estadisticas/distribucion")
    public Map<Integer, Long> distribucionGlobal() {
        List<Encomienda> list = encomiendaRepository.findByCalificacionIsNotNull();
        return list.stream()
                .filter(e -> e.getCalificacion() != null)
                .collect(Collectors.groupingBy(Encomienda::getCalificacion, Collectors.counting()));
    }

    @GetMapping("/estadisticas/distribucion/{recolectorId}")
    public Map<Integer, Long> distribucionPorRecolector(@PathVariable String recolectorId) {
        List<Encomienda> list = encomiendaRepository.findByRecolectorId(recolectorId);
        return list.stream()
                .filter(e -> e.getCalificacion() != null)
                .collect(Collectors.groupingBy(Encomienda::getCalificacion, Collectors.counting()));
    }

    // Encomiendas SOLICITADAS del recolector
@GetMapping("/recolector/{id}/solicitadas")
public List<Encomienda> solicitadasDeRecolector(@PathVariable String id) {
    return encomiendaRepository.findByRecolectorIdAndEstadoIgnoreCase(id, "SOLICITADO");
}

// Encomiendas para entregar (RECOGIDO o EN_TRANSITO)
@GetMapping("/recolector/{id}/entregar")
public List<Encomienda> entregarDeRecolector(@PathVariable String id) {
    return encomiendaRepository.findByRecolectorIdAndEstadoIn(
            id,
            Arrays.asList("RECOGIDO", "EN_TRANSITO")
    );
}

}
