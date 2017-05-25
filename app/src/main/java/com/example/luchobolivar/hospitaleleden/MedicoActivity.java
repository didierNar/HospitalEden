package com.example.luchobolivar.hospitaleleden;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MedicoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico);
    }

    public void abrirCitasMedico (View v){
        Intent i = new Intent(this, citasAsignadasEspecialista.class);
        startActivity(i);
    }

    public void abrirCitasPaciente (View v){
        Intent i = new Intent(this, citasPaciente.class);
        startActivity(i);
    }

}
