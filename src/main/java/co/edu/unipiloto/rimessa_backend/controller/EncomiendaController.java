package co.edu.unipiloto.rimessa_backend.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import co.edu.unipiloto.rimessa_backend.model.Encomienda;
import co.edu.unipiloto.rimessa_backend.repository.EncomiendaRepository;

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
        return encomiendaRepository.save(encomienda);
    }
}
