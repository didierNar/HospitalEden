package com.example.luchobolivar.hospitaleleden;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.luchobolivar.hospitaleleden.HttpURLConnection.HttpConnection;
import com.example.luchobolivar.hospitaleleden.modelo.Ciudad;
import com.example.luchobolivar.hospitaleleden.modelo.DatosMedico;
import com.example.luchobolivar.hospitaleleden.modelo.Departamento;
import com.example.luchobolivar.hospitaleleden.modelo.DireccionIP;
import com.example.luchobolivar.hospitaleleden.modelo.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Consulta_registroActivity extends AppCompatActivity {

    private Spinner tipoCita;
    private Spinner espe;
    private String enlaceMedicos;
    private String enlaceMedicosGenerales;
    String ip;
    private Spinner medicos;

    List<DatosMedico> datosMedico;
    ArrayAdapter<CharSequence> adapterTipoCita;
    ArrayAdapter<CharSequence> adapterEspe;
    ArrayAdapter<CharSequence> adapterMedicos;
    HttpConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_registro);

        medicos = (Spinner) findViewById(R.id.cbMedicos);
        espe = (Spinner) findViewById(R.id.cbEspecializaciones);
        tipoCita = (Spinner) findViewById(R.id.cbTipoCita);
        espe.setVisibility(View.GONE);
        datosMedico = new ArrayList<DatosMedico>();

        connection = new HttpConnection();

        ip = DireccionIP.getIp();

        adapterTipoCita = ArrayAdapter.createFromResource(this, R.array.tipoCita, android.R.layout.simple_spinner_item);
        adapterTipoCita.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoCita.setAdapter(adapterTipoCita);


        tipoCita.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position == 3 || position == 1){
                    espe.setVisibility(View.VISIBLE);
                    especializacionesCombo();
                } else if(position == 2){
                    espe.setVisibility(View.GONE);
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

    public void llenarMedicos(){
        String especializacion = espe.getSelectedItem().toString();
        enlaceMedicos = "http://"+ip+"/serviciosWebHospital/ConsultaCitaEspe.php?espe="+especializacion;
        new listarMedicos().execute(enlaceMedicos);


    }

    public void llenarMedicosGeneral(){
        enlaceMedicosGenerales = "http://"+ip+"/serviciosWebHospital/medicosCitasGenerales.php";
        new listarMedicos().execute(enlaceMedicos);
    }

    class listarMedicos extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int r = obtenerDatosJSONMedicos(resultado);
            if (r > 0) {
                ArrayAdapter<DatosMedico> spinnerArrayAdapter = new ArrayAdapter<DatosMedico>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, datosMedico);
                medicos.setAdapter(spinnerArrayAdapter);
            } else {
                Toast.makeText(getApplicationContext(), "No hay medicos con dicha especializacion", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceMedicos);
            return resultado;
        }
    }


    public int obtenerDatosJSONMedicos(String respuesta) {
        Log.e("Respuesta", respuesta);
        int resultado = 0;
        try {
            JSONArray json = new JSONArray(respuesta);
            //Verficamos que el tamaño del json sea mayor que 0
            if (json.length() > 0) {
                resultado = 1;
                for (int i = 0; i < json.length(); i++) {
                    JSONObject row = json.getJSONObject(i);

                    int numID = row.getInt("NUMERO_IDENTIFICACION");
                    String nombre = row.getString("NOMBRE");
                    String apellido = row.getString("APELLIDO");
                    String sede = row.getString("DESCRIPCION");

                    DatosMedico d = new DatosMedico(numID, nombre, apellido, sede);
                    datosMedico.add(d);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }



}
