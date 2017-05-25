package com.example.luchobolivar.hospitaleleden;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.luchobolivar.hospitaleleden.HttpURLConnection.HttpConnection;
import com.example.luchobolivar.hospitaleleden.modelo.CitaPaciente;
import com.example.luchobolivar.hospitaleleden.modelo.DireccionIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class citasPaciente extends AppCompatActivity {

    private ArrayList<CitaPaciente> listaCitas;
    private HttpConnection connection;
    private String enlaceBusqueda;
    private String ip;
    private ListView listaCitasList;
    private EditText cedula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas_paciente);
        connection = new HttpConnection();
        ip = DireccionIP.getIp();
        listaCitasList = (ListView) findViewById(R.id.lista);
        cedula = (EditText)findViewById(R.id.ide);
    }


    public void liatarCitasPaciente(View v){

        String id = cedula.getText().toString();
        enlaceBusqueda = "http://" + ip + "/serviciosWebHospital/citasPaciente.php?numero_identificacion="+id;
        new ListarTipos().execute(enlaceBusqueda);
    }


    class ListarTipos extends AsyncTask<String, Float, String> {

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceBusqueda);
            Log.e("resul ", resultado);
            return resultado;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int r = obtenerJSONListaCitas(resultado);
            if(r > 0){
                ArrayAdapter<CitaPaciente> adaptador = new ArrayAdapter<CitaPaciente>(citasPaciente.this,android.R.layout.simple_spinner_dropdown_item,listaCitas);
                adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                listaCitasList.setAdapter(adaptador);
            }else{
                Toast.makeText(getApplicationContext(),"no hay citas",Toast.LENGTH_SHORT).show();
            }

        }
    }

    public int obtenerJSONListaCitas(String respuesta){
        Log.e("respuesta",respuesta);
        int resultado = 0;
        try{
            JSONArray json = new JSONArray(respuesta);
            listaCitas = new ArrayList<CitaPaciente>();

            for (int i=0; i<json.length(); i++){

                resultado = 1;
                JSONObject row = json.getJSONObject(i);

                String ide = row.getString("NUMERO_IDENTIFICACION"+"");
                String nombre = row.getString("NOMBRE");
                String motivo = row.getString("MOTIVO");
                listaCitas.add(new CitaPaciente(motivo,nombre,ide));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        return resultado;
    }
}

