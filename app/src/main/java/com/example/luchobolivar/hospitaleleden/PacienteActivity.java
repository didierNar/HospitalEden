package com.example.luchobolivar.hospitaleleden;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class PacienteActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente);


    }


    public void registro(View view){
        Intent innte = new Intent(this, Consulta_registroActivity.class);
        startActivity(innte);
    }

    public void edicionCita(View view){

        Intent intentEdi = new Intent(this, Edicion_CitaActivity.class);
        startActivity(intentEdi);
    }

    public void historialCita(View view){

        Intent intentHisto = new Intent(this, Historial_CitasActivity.class);
        startActivity(intentHisto);

    }

    public void listadoSedes(View view){

        Intent intentList = new Intent(this, Listado_sedeActivity.class);
        startActivity(intentList);

    }

    public void direccion(View view){

        Intent intentDirecc = new Intent(this, DireccionActivity.class);
        startActivity(intentDirecc);

    }

    }

