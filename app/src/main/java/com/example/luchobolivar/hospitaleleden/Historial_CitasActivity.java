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
import com.example.luchobolivar.hospitaleleden.modelo.Anotaciones;
import com.example.luchobolivar.hospitaleleden.modelo.CitasHisto;
import com.example.luchobolivar.hospitaleleden.modelo.DireccionIP;
import com.example.luchobolivar.hospitaleleden.modelo.UsuarioLogueado;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Historial_CitasActivity extends AppCompatActivity {

    private Spinner citas;
    private Spinner tipoCitaHisto;
    private Spinner listaAnota;
    private String enlaceCitasHisto;
    private String enlaceAnotacionesHisto;

    private String ip;
    HttpConnection connection;
    ArrayAdapter<CharSequence> adapterTipoCitaHisto;
    List<CitasHisto> listaCitas;
    List<Anotaciones> listaAnotacionesCita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityhistorialcitas);

        citas = (Spinner) findViewById(R.id.cbCitasHisto);
        tipoCitaHisto = (Spinner) findViewById(R.id.cbTipoCitaHisto);
        listaAnota = (Spinner) findViewById(R.id.cbListaAnotaciones);
        connection = new HttpConnection();

        ip = DireccionIP.getIp();

        listaCitas = new ArrayList<CitasHisto>();
        listaAnotacionesCita = new ArrayList<Anotaciones>();

        adapterTipoCitaHisto = ArrayAdapter.createFromResource(this, R.array.tipoCitaHisto, android.R.layout.simple_spinner_item);
        adapterTipoCitaHisto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoCitaHisto.setAdapter(adapterTipoCitaHisto);

        tipoCitaHisto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    llenarComboCitasHisto();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        citas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                llenarComboAnotaciones();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    public void llenarComboCitasHisto(){

        String tipo = tipoCitaHisto.getSelectedItem().toString();
        enlaceCitasHisto = "http://" + ip + "/serviciosWebHospital/listaDeCitasPorEstado.php?numID=" + UsuarioLogueado.getUsuario().getIdentificacion()
                + "&esta=" + tipo;
        new CitasHistoLista().execute(enlaceCitasHisto);

    }

    public void llenarComboAnotaciones(){
        CitasHisto cita = (CitasHisto) citas.getSelectedItem();
        enlaceAnotacionesHisto = "http://" + ip + "/serviciosWebHospital/listaAnotacionesCita.php?numID=" + cita.getId();
        new AnotacionesHistoLista().execute(enlaceAnotacionesHisto);

    }

    class AnotacionesHistoLista extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int r = obtenerDatosJSONAnotaHisto(resultado);
            if (r > 0) {
                ArrayAdapter<Anotaciones> spinnerArrayAdapter = new ArrayAdapter<Anotaciones>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, listaAnotacionesCita);
                listaAnota.setAdapter(spinnerArrayAdapter);
            } else {
                Toast.makeText(getApplicationContext(), "No hay medicos con dicha especializacion", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceAnotacionesHisto);
            return resultado;
        }
    }

    public int obtenerDatosJSONAnotaHisto(String respuesta) {
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
                    String anotac = row.getString("ANOTACIONES");

                    Anotaciones anLista = new Anotaciones(id, anotac);
                    listaAnotacionesCita.add(anLista);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    class CitasHistoLista extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int r = obtenerDatosJSONCitasHisto(resultado);
            if (r > 0) {
                ArrayAdapter<CitasHisto> spinnerArrayAdapter = new ArrayAdapter<CitasHisto>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, listaCitas);
                citas.setAdapter(spinnerArrayAdapter);
            } else {
                Toast.makeText(getApplicationContext(), "No hay medicos con dicha especializacion", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceCitasHisto);
            return resultado;
        }
    }

    public int obtenerDatosJSONCitasHisto(String respuesta) {
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
                    String fecha = row.getString("FECHA_HORA");

                    CitasHisto citasLista = new CitasHisto(id, fecha);
                    listaCitas.add(citasLista);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }

}
