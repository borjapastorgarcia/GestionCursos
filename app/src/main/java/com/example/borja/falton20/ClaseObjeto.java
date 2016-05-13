package com.example.borja.falton20;

/**
 * Created by borja on 13/05/2016.
 */
public class ClaseObjeto {
    private String titulo, descripcion, tipo;
    private Float precio;

    public ClaseObjeto(String titulo, String descripcion, String tipo, Float precio) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.precio = precio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }
}
