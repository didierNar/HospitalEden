package com.example.luchobolivar.hospitaleleden.modelo;

/**
 * Created by Didier_Narváez on 24/05/2017.
 */

public class TipoPersonal {

    private int id;
    private String descripcion;


    public TipoPersonal() {
    }

    public TipoPersonal(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
