package com.example.luchobolivar.hospitaleleden;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
        Intent i = new Intent(this, EditarAdminActivity.class);
        startActivity(i);
    }

}
