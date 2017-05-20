package com.example.luchobolivar.hospitaleleden.modelo;

/**
 * Created by Didier_Narváez on 20/05/2017.
 */

public class Ciudad {

    private int id;
    private String descripcion;
    private int depto;

    public Ciudad(int id, String descripcion, int depto) {
        this.id = id;
        this.descripcion = descripcion;
        this.depto = depto;
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

    public int getDepto() {
        return depto;
    }

    public void setDepto(int depto) {
        this.depto = depto;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
