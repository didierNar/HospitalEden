package com.example.luchobolivar.hospitaleleden;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.luchobolivar.hospitaleleden.HttpURLConnection.HttpConnection;
import com.example.luchobolivar.hospitaleleden.modelo.Ciudad;
import com.example.luchobolivar.hospitaleleden.modelo.Departamento;
import com.example.luchobolivar.hospitaleleden.modelo.DireccionIP;
import com.example.luchobolivar.hospitaleleden.modelo.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityPacienteRegimenCiudad extends AppCompatActivity {

    private String[] tiposReporte = {"Usuario por regimen", "Usuarios por ciudad"};
    private List<Ciudad> ciudades;
    private List<Departamento> departamentos;

    private Spinner spTipoReporte;
    private Spinner spCiudades;
    private Spinner spDepartamentos;

    private String enlaceDeptos;
    private String enlaceCiudades;
    private String ip;

    private HttpConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente_regimen_ciudad);

        ip = DireccionIP.getIp();
        connection = new HttpConnection();

        spTipoReporte = (Spinner) findViewById(R.id.spTipoReporte);
        spCiudades = (Spinner) findViewById(R.id.spCiudades);
        spDepartamentos = (Spinner) findViewById(R.id.spDepartamentos);

        ciudades = new ArrayList<Ciudad>();
        departamentos = new ArrayList<Departamento>();

        spCiudades.setVisibility(View.INVISIBLE);
        spDepartamentos.setVisibility(View.INVISIBLE);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, tiposReporte);
        spTipoReporte.setAdapter(adaptador);

        spTipoReporte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (spTipoReporte.getSelectedItemPosition() == 1) {
                    llenarDeptos();
                    spDepartamentos.setVisibility(View.VISIBLE);
                } else {
                    spCiudades.setVisibility(View.INVISIBLE);
                    spDepartamentos.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        spDepartamentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                spCiudades.setVisibility(View.VISIBLE);
                llenarCiudades(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    private void llenarDeptos() {
        enlaceDeptos = "http://" + ip + "/serviciosWebHospital/ListarDeptos.php?PAIS_ID=1";
        new listarDeptos().execute(enlaceDeptos);
    }

    private void llenarCiudades(int codDepto){
        enlaceCiudades = "http://"+ip+"/serviciosWebHospital/ListarCiudades.php?DEPARTAMENTO_ID="+codDepto;
        new listarCiudades().execute(enlaceCiudades);
    }

    class listarDeptos extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int r = obtenerDatosJSONDepto(resultado);
            if (r > 0) {
                ArrayAdapter<Departamento> spinnerArrayAdapter = new ArrayAdapter<Departamento>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, departamentos);
                spDepartamentos.setAdapter(spinnerArrayAdapter);
            } else {
                Toast.makeText(getApplicationContext(), "No hay departamentos registrados", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceDeptos);
            return resultado;
        }
    }

    class listarCiudades extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int r = obtenerDatosJSONCiudad(resultado);
            if (r > 0) {
                ArrayAdapter<Ciudad> spinnerArrayAdapter = new ArrayAdapter<Ciudad>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, ciudades);
                spCiudades.setAdapter(spinnerArrayAdapter);
            } else {
                Toast.makeText(getApplicationContext(), "No hay ciudades registrados", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceCiudades);
            return resultado;
        }
    }

    public int obtenerDatosJSONDepto(String respuesta) {
        Log.e("Respuesta", respuesta);
        int resultado = 0;
        try {
            JSONArray json = new JSONArray(respuesta);
            //Verficamos que el tamaño del json sea mayor que 0
            if (json.length() > 0) {
                resultado = 1;
                for (int i = 0; i < json.length(); i++) {
                    JSONObject row = json.getJSONObject(0);

                    int id = row.getInt("ID");
                    String desc = row.getString("DESCRIPCION");
                    int pais = row.getInt("PAIS_ID");

                    Departamento d = new Departamento(id, desc, pais);
                    departamentos.add(d);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public int obtenerDatosJSONCiudad(String respuesta) {
        Log.e("Respuesta", respuesta);
        int resultado = 0;
        try {
            JSONArray json = new JSONArray(respuesta);
            //Verficamos que el tamaño del json sea mayor que 0
            if (json.length() > 0) {
                resultado = 1;
                for (int i = 0; i < json.length(); i++) {
                    JSONObject row = json.getJSONObject(0);

                    int id = row.getInt("ID");
                    String desc = row.getString("DESCRIPCION");
                    int depto = row.getInt("DEPARTAMENTO_ID");

                    Ciudad c = new Ciudad(id, desc, depto);
                    ciudades.add(c);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }

}
