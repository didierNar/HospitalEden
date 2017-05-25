package com.example.luchobolivar.hospitaleleden;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.luchobolivar.hospitaleleden.HttpURLConnection.HttpConnection;
import com.example.luchobolivar.hospitaleleden.modelo.CitasPaciente;
import com.example.luchobolivar.hospitaleleden.modelo.DatosMedico;
import com.example.luchobolivar.hospitaleleden.modelo.DireccionIP;
import com.example.luchobolivar.hospitaleleden.modelo.EdicionCita;
import com.example.luchobolivar.hospitaleleden.modelo.UsuarioLogueado;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListadoCitasActivity extends ListActivity{


    private ListView citasView;
    private String enlaceCitasPaciente;

    HttpConnection connection;
    private String ip;

    List<CitasPaciente> citas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        citasView = (ListView) findViewById(R.id.lListaCitas);
        connection = new HttpConnection();
        ip = DireccionIP.getIp();

        citas = new ArrayList<CitasPaciente>();
        listadoCitasPaciente();

        /**citasView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), EdicionCita.class);
                startActivity(intent);
            }
        });*/

    }


    public void listadoCitasPaciente(){

        Log.e("Respuesta1", UsuarioLogueado.getUsuario().getIdentificacion() + "");
        enlaceCitasPaciente = "http://" + ip + "/serviciosWebHospital/ListarCitasPaciente.php?numId=" + UsuarioLogueado.getUsuario().getIdentificacion();
        new listaCitasPaciente().execute(enlaceCitasPaciente);

    }

    class listaCitasPaciente extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int r = obtenerDatosJSONCitasPaciente(resultado);
            if (r > 0) {
                ArrayAdapter<CitasPaciente> lista = new ArrayAdapter<CitasPaciente>(getListView().getContext(),
                        android.R.layout.simple_list_item_1, citas);
                getListView().setAdapter(lista);
             } else {
                Toast.makeText(getApplicationContext(), "No hay medicos con dicha especializacion", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceCitasPaciente);
            return resultado;
        }
    }

    public int obtenerDatosJSONCitasPaciente(String respuesta) {
        Log.e("Respuesta1", respuesta);
        int resultado = 0;
        try {
            JSONArray json = new JSONArray(respuesta);

            if (json.length() > 0) {
                resultado = 1;
                for (int i = 0; i < json.length(); i++) {
                    JSONObject row = json.getJSONObject(i);

                    int id = row.getInt("ID");
                    String fecha = row.getString("FECHA_HORA");

                    CitasPaciente d = new CitasPaciente(id, fecha);
                    citas.add(d);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }
}
