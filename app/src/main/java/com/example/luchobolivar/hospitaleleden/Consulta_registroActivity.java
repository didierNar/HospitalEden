package com.example.luchobolivar.hospitaleleden;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Consulta_registroActivity extends AppCompatActivity {

    ImageButton imagenConsulta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_registro);

        imagenConsulta = (ImageButton) findViewById(R.id.btnConsultaCitas);

        imagenConsulta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
    }
}
