package com.example.luchobolivar.hospitaleleden.modelo;

/**
 * Created by Didier_Narváez on 25/05/2017.
 */

public class CitaPaciente {

    private String tipoCita;
    private String identificacion;
    private String nombre;

    public CitaPaciente(){


    }

    public CitaPaciente(String tipoCita, String nombre, String identificacion) {
        this.tipoCita = tipoCita;
        this.nombre = nombre;
        this.identificacion = identificacion;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoCita() {
        return tipoCita;
    }

    public void setTipoCita(String tipoCita) {
        this.tipoCita = tipoCita;
    }

}
