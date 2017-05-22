package com.example.luchobolivar.hospitaleleden.modelo;

/**
 * Created by Didier_Narváez on 21/05/2017.
 */

public class ConsultasPacientes {

    private String  nombre;
    private String apellido;
    private double valor;

    public ConsultasPacientes(String nombre, String apellido, double valor) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
