package com.example.luchobolivar.hospitaleleden.modelo;

/**
 * Created by Didier_Narváez on 20/05/2017.
 */

public class Departamento {

    private int id;

    private String descripcion;

    private int pais;

    public Departamento(int id, String descripcion, int pais) {
        this.id = id;
        this.descripcion = descripcion;
        this.pais = pais;
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

    public int getPais() {
        return pais;
    }

    public void setPais(int pais) {
        this.pais = pais;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
