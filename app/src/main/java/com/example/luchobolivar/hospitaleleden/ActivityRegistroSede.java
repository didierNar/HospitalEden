package com.example.luchobolivar.hospitaleleden;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.luchobolivar.hospitaleleden.HttpURLConnection.HttpConnection;
import com.example.luchobolivar.hospitaleleden.R;
import com.example.luchobolivar.hospitaleleden.modelo.Ciudad;
import com.example.luchobolivar.hospitaleleden.modelo.Departamento;
import com.example.luchobolivar.hospitaleleden.modelo.DireccionIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityRegistroSede extends AppCompatActivity {

    private Spinner spCiudades;
    private Spinner spDepartamentos;

    private EditText etLatitud;
    private EditText etLongitud;
    private EditText etDescripcion;

    private List<Departamento> departamentos;
    private List<Ciudad> ciudades;

    private String ip;
    private HttpConnection connection;

    private String enlaceDeptos;
    private String enlaceCiudades;
    private String enlaceRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_sede);

        ip = DireccionIP.getIp();
        connection = new HttpConnection();

        spCiudades = (Spinner) findViewById(R.id.spCiudadSede);
        spDepartamentos = (Spinner) findViewById(R.id.spDeptoSede);

        etDescripcion = (EditText) findViewById(R.id.etDescripcionSede);
        etLatitud = (EditText) findViewById(R.id.etLatitudSede);
        etLongitud = (EditText) findViewById(R.id.etLongitudSede);

        departamentos = new ArrayList<Departamento>();
        ciudades = new ArrayList<Ciudad>();

        llenarDeptos();

        spDepartamentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Departamento d = (Departamento) spDepartamentos.getSelectedItem();
                llenarCiudades(d.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    public void registrarSede (View v){
        Ciudad c = (Ciudad) spCiudades.getSelectedItem();
        String desc = etDescripcion.getText().toString();
        double latitud = Double.parseDouble(etLatitud.getText().toString());
        double longitud = Double.parseDouble(etLongitud.getText().toString());
        int eps = 1;
        enlaceRegistro = "http://"+ip+"/serviciosWebHospital/RegistroSede.php?DESCRIPCION="+desc+
                "&EPS_ID="+eps+"&CIUDAD_ID="+c.getId()+"&LATITUD="+latitud+"&LONGITUD="+longitud;
        new registrar().execute(enlaceRegistro);
    }

    private void llenarDeptos() {
        departamentos = new ArrayList<Departamento>();
        enlaceDeptos = "http://" + ip + "/serviciosWebHospital/ListarDeptos.php?PAIS_ID=1";
        new listarDeptos().execute(enlaceDeptos);
    }

    private void llenarCiudades(int codDepto){
        ciudades = new ArrayList<Ciudad>();
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
                    JSONObject row = json.getJSONObject(i);

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
                    JSONObject row = json.getJSONObject(i);

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

    class registrar extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int res = obtenerDatosJSONReg(resultado);
            if (res == 1) {
                Toast.makeText(getApplicationContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceRegistro);
            return resultado;
        }
    }

    public int obtenerDatosJSONReg(String respuesta) {
        Log.e("Respuesta", respuesta);
        int resultado = 0;
        try {
            JSONObject json = new JSONObject(respuesta);
            //Log.e("Tamaño ", json.getString("registro")+"");
            String res = json.getString("registro");
            //Verficamos que el tamaño del json sea mayor que 0
            if (res.equals("1")) {
                resultado = 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }

}
