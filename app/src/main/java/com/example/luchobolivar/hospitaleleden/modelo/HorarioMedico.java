package com.example.luchobolivar.hospitaleleden.modelo;

/**
 * Created by LuchoBolivar on 5/23/17.
 */

public class HorarioMedico {

    private int id;

    private String fecha;

    private String horaInicio;

    private String horaFin;


    public HorarioMedico(int id, String fecha, String horaInicio, String horaFin) {
        this.id = id;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    @Override
    public String toString() {
        return fecha;
    }
}
