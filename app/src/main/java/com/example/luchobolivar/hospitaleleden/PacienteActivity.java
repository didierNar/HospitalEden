package com.example.luchobolivar.hospitaleleden;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PacienteActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente);


    }


    public void registro(View view) {
        Intent innte = new Intent(this, ConsultaregistroActivity.class);
        startActivity(innte);
    }

    public void edicionCita(View view) {

        Intent intentEdi = new Intent(this, ListadoCitasActivity.class);
        startActivity(intentEdi);
    }

    public void historialCita(View view) {

        Intent intentHisto = new Intent(this, HistorialCitasActivity.class);
        startActivity(intentHisto);

    }

    public void listadoSedes(View view) {

        Intent intentList = new Intent(this, ListadosedeActivity.class);
        startActivity(intentList);

    }

    public void abrirSedeMapas(View v) {
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);
    }

}

