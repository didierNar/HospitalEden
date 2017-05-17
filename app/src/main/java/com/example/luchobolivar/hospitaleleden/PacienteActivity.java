package com.example.luchobolivar.hospitaleleden;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class PacienteActivity extends AppCompatActivity {

    ImageButton imagenRegistro;
    ImageButton imagenEditar;
    ImageButton imagenHistorial;
    ImageButton imagenListadoSedes;
    ImageButton imagenDirecciones;

    final Intent intentReg = new Intent(this, Consulta_registroActivity.class);
    final Intent intentEdi = new Intent(this, Edicion_CitaActivity.class);
    final Intent intentHisto = new Intent(this, Historial_CitasActivity.class);
    final Intent intentList = new Intent(this, Listado_sedeActivity.class);
    final Intent intentDirecc = new Intent(this, DireccionActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente);

        imagenEditar = (ImageButton) findViewById(R.id.btnEditar);
        imagenRegistro = (ImageButton) findViewById(R.id.btnConsul_registroCita);
        imagenHistorial = (ImageButton) findViewById(R.id.btnHistorial);
        imagenListadoSedes = (ImageButton) findViewById(R.id.btnListadoSedes);
        imagenDirecciones = (ImageButton) findViewById(R.id.btnDirecciones);

        imagenRegistro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(intentReg);
            }
        });

        imagenEditar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(intentEdi);
            }
        });


        imagenHistorial.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(intentHisto);
            }
        });

        imagenListadoSedes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(intentList);
            }
        });

        imagenDirecciones.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(intentDirecc);
            }
        });


    }

    }

