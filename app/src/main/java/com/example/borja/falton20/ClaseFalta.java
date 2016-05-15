package com.example.borja.falton20;

import java.util.Date;

/**
 * Created by borja on 06/05/2016.
 */
public class ClaseFalta {
    private int id;
    private Date fechaFalta;
    private int idAsignatura;
    private int totalFaltasXAsignatura;
    public ClaseFalta(int totalFaltasXAsignatura, int idAsignatura){
        this.totalFaltasXAsignatura=totalFaltasXAsignatura;
        this.idAsignatura=idAsignatura;
    }

    @Override
    public String toString() {
        return "ClaseFalta-->"+String.valueOf(fechaFalta)+" , idASignatura-->"+idAsignatura+", totalFaltasxAsignatura: "+totalFaltasXAsignatura;
    }

    public int getTotalFaltasXAsignatura() {
        return totalFaltasXAsignatura;
    }

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
