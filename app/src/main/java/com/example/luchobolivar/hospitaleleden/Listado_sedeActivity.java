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
import com.example.luchobolivar.hospitaleleden.modelo.DireccionIP;
import com.example.luchobolivar.hospitaleleden.modelo.Sede;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Listado_sedeActivity extends AppCompatActivity {

    private List<Sede> sedes;

    private Spinner ciudad;
    private Spinner sede;

    private String ip;
    private HttpConnection connection;

    private String enlaceSedes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitylistadosede);

        ip = DireccionIP.getIp();
        connection = new HttpConnection();

        sedes = new ArrayList<Sede>();

        ciudad = (Spinner) findViewById(R.id.cbCiudades);
        sede = (Spinner) findViewById(R.id.cbSedes);



        ciudad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Ciudad d = (Ciudad) ciudad.getSelectedItem();
                llenarSedes(d.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }


    private void llenarSedes (int codCiudad){
        enlaceSedes = "http://"+ip+"/serviciosWebHospital/ListarSedesPorCiudad.php?CIUDAD_ID="+codCiudad;
        new listarSedes().execute(enlaceSedes);
    }

    class listarSedes extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int res = obtenerDatosJSONListar(resultado);
            if (res == 1) {
                ArrayAdapter<Sede> spinnerArrayAdapter = new ArrayAdapter<Sede>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, sedes);
                sede.setAdapter(spinnerArrayAdapter);
                sede.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getApplicationContext(), "No hay sedes registradas en esta ciudad", Toast.LENGTH_SHORT).show();
                sede.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceSedes);
            return resultado;
        }
    }

    public int obtenerDatosJSONListar (String respuesta){
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
                    int ciudad = row.getInt("CIUDAD_ID");
                    double longitud = row.getDouble("LONGITUD");
                    double latitud = row.getDouble("LATITUD");

                    Sede sede = new Sede(id, desc, 1, latitud, longitud, ciudad);
                    sedes.add(sede);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }
}
