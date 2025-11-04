package co.edu.unipiloto.rimessa_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String usuario;
    private String email;
    private String password;
    private String direccion;
    private String celular;
    private String rol;
    private String fechaNacimiento;
    private String genero;
}

