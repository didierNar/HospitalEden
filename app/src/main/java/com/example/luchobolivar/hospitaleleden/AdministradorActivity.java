package com.example.luchobolivar.hospitaleleden;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.luchobolivar.hospitaleleden.modelo.PersonalMedico;

public class AdministradorActivity extends AppCompatActivity {

    private TextView user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);

        user = (TextView) findViewById(R.id.tvUsuario);
        user.setText("Bienvenido Administrador");

    }

    public void abrirEditarAdmin (View v){
        Intent i = new Intent(this, GestionarAdminActivity.class);
        startActivity(i);
    }

    public void abrirAdminBuscarUsu (View v){
        Intent i = new Intent(this, ActivityPacienteRegimenCiudad.class);
        startActivity(i);
    }

    public void abrirGestionEspecializacion (View v){
        Intent i = new Intent(this, GestionEspecializacion.class);
        startActivity(i);
    }

    public void abrirConsultasAtendidas (View v){
        Intent i = new Intent(this, ActivityConsultasAtendidas.class);
        startActivity(i);
    }

    public void abrirRegistroSedes (View v){
        Intent i = new Intent(this, ActivityRegistroSede.class);
        startActivity(i);
    }

    public void abrirGestionSedes (View v){
        Intent i = new Intent(this, ActivityGestionSede.class);
        startActivity(i);
    }

    public void abrirRegistroPersonal(View v){
        Intent i = new Intent(this, PersonalMedicoActivity.class);
        startActivity(i);
    }

}
