package co.edu.unipiloto.rimessa_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "encomiendas")
public class Encomienda {

    @Id
    private String numeroGuia;

    private String remitente;
    private String usuarioRemitente;
    private String celularRemitente;
    private String direccionRemitenteActual;
    private String destinatario;
    private String celularDestinatario;
    private String direccionDestinatario;
    private String estado;
    private String fechaSolicitud;
    private String fechaEntrega;
    private double peso;
    private double precio;
    private String recolectorId;
}
