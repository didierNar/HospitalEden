package com.example.luchobolivar.hospitaleleden.modelo;

/**
 * Created by LuchoBolivar on 5/22/17.
 */

public class DatosMedico {

    private int numIdentificacion;
    private String nombre;
    private String apellido;
    private String sede;
    private int idSede;

    public DatosMedico(int numIdentificacion, String nombre, String apellido, String sede, int idSede) {
        this.numIdentificacion = numIdentificacion;
        this.nombre = nombre;
        this.apellido = apellido;
        this.sede = sede;
        this.idSede = idSede;
    }

    public int getNumIdentificacion() {
        return numIdentificacion;
    }

    public void setNumIdentificacion(int numIdentificacion) {
        this.numIdentificacion = numIdentificacion;
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

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public int getIdSede() {
        return idSede;
    }

    public void setIdSede(int idSede) {
        this.idSede = idSede;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
