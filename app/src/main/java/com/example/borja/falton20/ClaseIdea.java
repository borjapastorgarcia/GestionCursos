package com.example.borja.falton20;

/**
 * Created by borja on 09/05/2016.
 */
public class ClaseIdea {
    private int idIdea;
    private String tituloIdea, descripcionIdea;

    public ClaseIdea(int idIdea, String tituloIdea, String descripcionIdea) {
        this.idIdea = idIdea;
        this.tituloIdea = tituloIdea;
        this.descripcionIdea = descripcionIdea;
    }

    public int getIdIdea() {
        return idIdea;
    }

    public void setIdIdea(int idIdea) {
        this.idIdea = idIdea;
    }

    public String getTituloIdea() {
        return tituloIdea;
    }

    public void setTituloIdea(String tituloIdea) {
        this.tituloIdea = tituloIdea;
    }

    public String getDescripcionIdea() {
        return descripcionIdea;
    }

    public void setDescripcionIdea(String descripcionIdea) {
        this.descripcionIdea = descripcionIdea;
    }
}
