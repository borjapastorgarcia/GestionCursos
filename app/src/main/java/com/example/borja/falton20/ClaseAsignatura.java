package com.example.borja.falton20;

/**
 * Created by borja on 10/05/2016.
 */
public class ClaseAsignatura {
private int idAsignatura, porcentajeFaltas, faltasActuales;
    private String nombreAsignatura;

    public ClaseAsignatura(int idAsignatura, int porcentajeFaltas, int faltasActuales, String nombreAsignatura) {
        this.idAsignatura = idAsignatura;
        this.porcentajeFaltas = porcentajeFaltas;
        this.faltasActuales = faltasActuales;
        this.nombreAsignatura = nombreAsignatura;
    }

    @Override
    public String toString() {
        return nombreAsignatura;
    }

    public int getIdAsignatura() {
        return idAsignatura;
    }

    public void setIdAsignatura(int idAsignatura) {
        this.idAsignatura = idAsignatura;
    }

    public int getPorcentajeFaltas() {
        return porcentajeFaltas;
    }

    public void setPorcentajeFaltas(int porcentajeFaltas) {
        this.porcentajeFaltas = porcentajeFaltas;
    }

    public int getFaltasActuales() {
        return faltasActuales;
    }

    public void setFaltasActuales(int faltasActuales) {
        this.faltasActuales = faltasActuales;
    }

    public String getNombreAsignatura() {
        return nombreAsignatura;
    }

    public void setNombreAsignatura(String nombreAsignatura) {
        this.nombreAsignatura = nombreAsignatura;
    }
}
