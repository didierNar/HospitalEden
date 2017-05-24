package com.example.luchobolivar.hospitaleleden.modelo;

/**
 * Created by Didier_Narváez on 22/05/2017.
 */

public class Sede {

    private int codigo;
    private String descripcion;
    private int eps;
    private double latitud;
    private double longitud;
    private int ciudad;

    public Sede(int codigo, String descripcion, int eps, double latitud, double longitud, int ciudad) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.eps = eps;
        this.latitud = latitud;
        this.longitud = longitud;
        this.ciudad = ciudad;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getEps() {
        return eps;
    }

    public void setEps(int eps) {
        this.eps = eps;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public int getCiudad() {
        return ciudad;
    }

    public void setCiudad(int ciudad) {
        this.ciudad = ciudad;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
