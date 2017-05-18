package com.example.luchobolivar.hospitaleleden;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class Consulta_registroActivity extends AppCompatActivity {

    Spinner tipoCita;
    Spinner espe;
    ArrayAdapter<CharSequence> adapterTipoCita;
    ArrayAdapter<CharSequence> adapterEspe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_registro);

        espe = (Spinner) findViewById(R.id.cbEspecializaciones);
        tipoCita = (Spinner) findViewById(R.id.cbTipoCita);
        adapterTipoCita = ArrayAdapter.createFromResource(this, R.array.tipoCita, android.R.layout.simple_spinner_item);
        adapterTipoCita.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoCita.setAdapter(adapterTipoCita);

        tipoCita.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position == 3 || position == 1){
                    especializacionesCombo();
                } else if(position == 2){
                    espe.setAdapter(null);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }

    public void especializacionesCombo(){

        adapterEspe = ArrayAdapter.createFromResource(this, R.array.tipoEspe, android.R.layout.simple_spinner_item);
        adapterEspe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        espe.setAdapter(adapterEspe);

    }

    public void listarMedicosSedes(View view){



    }








}
