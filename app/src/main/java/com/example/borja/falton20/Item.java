package com.example.borja.falton20;

import java.util.Date;

/**
 * Created by borja on 06/04/2016.
 */
public class Item {
    private String tipoItem;
    private String tituloItem;
    private String ResumenItem;
    private String tiempoRestanteItem;
    private Date FechaItem;

    public Item(String tipoItem, String tituloItem, String resumenItem, String tiempoRestanteItem, Date fechaItem) {
        this.tipoItem = tipoItem;
        this.tituloItem = tituloItem;
        ResumenItem = resumenItem;
        this.tiempoRestanteItem = tiempoRestanteItem;
        FechaItem = fechaItem;
    }

    public String getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(String tipoItem) {
        this.tipoItem = tipoItem;
    }

    public String getTituloItem() {
        return tituloItem;
    }

    public void setTituloItem(String tituloItem) {
        this.tituloItem = tituloItem;
    }

    public String getResumenItem() {
        return ResumenItem;
    }

    public void setResumenItem(String resumenItem) {
        ResumenItem = resumenItem;
    }

    public String getTiempoRestanteItem() {
        return tiempoRestanteItem;
    }

    public void setTiempoRestanteItem(String tiempoRestanteItem) {
        this.tiempoRestanteItem = tiempoRestanteItem;
    }

    public Date getFechaItem() {
        return FechaItem;
    }

    public void setFechaItem(Date fechaItem) {
        FechaItem = fechaItem;
    }
}
