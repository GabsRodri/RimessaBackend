package co.edu.unipiloto.rimessa_backend.dto;

public class CalificacionRequest {
    private Integer calificacion;
    private String comentario;

    // getters y setters
    public Integer getCalificacion() { return calificacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}
