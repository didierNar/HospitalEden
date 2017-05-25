package com.example.luchobolivar.hospitaleleden.modelo;

/**
 * Created by Didier_Narváez on 25/05/2017.
 */

public class CitaEspecialista {


    private String fechaInicio;

    public CitaEspecialista(){

    }

    public CitaEspecialista(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

}
