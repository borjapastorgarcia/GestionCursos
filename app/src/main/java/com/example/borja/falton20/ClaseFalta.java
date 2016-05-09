package com.example.borja.falton20;

import java.util.Date;

/**
 * Created by borja on 06/05/2016.
 */
public class ClaseFalta {
    private int id;
    private Date fechaFalta;
    private int idAsignatura;

    public ClaseFalta(int id, Date fechaFalta, int idAsignatura) {
        this.id = id;
        this.fechaFalta = fechaFalta;
        this.idAsignatura = idAsignatura;
    }

    public Date getFechaFalta() {
        return fechaFalta;
    }

    public int getId() {
        return id;
    }

    public int getIdAsignatura() {
        return idAsignatura;
    }

}
