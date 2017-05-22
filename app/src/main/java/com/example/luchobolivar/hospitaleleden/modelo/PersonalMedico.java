package com.example.luchobolivar.hospitaleleden.modelo;

/**
 * Created by Didier_Narváez on 21/05/2017.
 */

public class PersonalMedico {

    private int identificacion;

    private int tipoPersonal;

    private int especializacion;

    private int sede;

    public PersonalMedico(int identificacion, int tipoPersonal, int especializacion, int sede) {
        this.identificacion = identificacion;
        this.tipoPersonal = tipoPersonal;
        this.especializacion = especializacion;
        this.sede = sede;
    }

    public int getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(int identificacion) {
        this.identificacion = identificacion;
    }

    public int getTipoPersonal() {
        return tipoPersonal;
    }

    public void setTipoPersonal(int tipoPersonal) {
        this.tipoPersonal = tipoPersonal;
    }

    public int getEspecializacion() {
        return especializacion;
    }

    public void setEspecializacion(int especializacion) {
        this.especializacion = especializacion;
    }

    public int getSede() {
        return sede;
    }

    public void setSede(int sede) {
        this.sede = sede;
    }
}
