package com.example.luchobolivar.hospitaleleden.modelo;

/**
 * Created by LuchoBolivar on 5/25/17.
 */

public class CitasHisto {

    private int id;

    private String fecha;

    public CitasHisto(int id, String fecha) {
        this.id = id;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return fecha;
    }
}
