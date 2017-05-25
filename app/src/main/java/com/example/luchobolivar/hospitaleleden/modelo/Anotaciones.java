package com.example.luchobolivar.hospitaleleden.modelo;

/**
 * Created by LuchoBolivar on 5/25/17.
 */

public class Anotaciones {

    private int id;

    private String anotacion;

    public Anotaciones(int id, String anotacion) {
        this.id = id;
        this.anotacion = anotacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnotacion() {
        return anotacion;
    }

    public void setAnotacion(String anotacion) {
        this.anotacion = anotacion;
    }

    @Override
    public String toString() {
        return anotacion;
    }
}
