package com.example.luchobolivar.hospitaleleden.modelo;

/**
 * Created by LuchoBolivar on 5/23/17.
 */

public class HorarioMedico {

    private String fecha;

    private String horaFin;


    public HorarioMedico(String fecha, String horaFin) {
        this.fecha = fecha;
        this.horaFin = horaFin;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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
