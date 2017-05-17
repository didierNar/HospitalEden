package com.example.luchobolivar.hospitaleleden.modelo;

/**
 * Created by Didier_Narváez on 19/04/2017.
 */

public class Genero {

    private int codigo;
    private String nombre;

    public Genero(int codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
