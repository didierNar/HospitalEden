package com.example.luchobolivar.hospitaleleden;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.luchobolivar.hospitaleleden.HttpURLConnection.HttpConnection;
import com.example.luchobolivar.hospitaleleden.modelo.CitaEspecialista;
import com.example.luchobolivar.hospitaleleden.modelo.DireccionIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class citasAsignadasEspecialista extends AppCompatActivity implements View.OnClickListener{


    private ArrayList<CitaEspecialista>listaCitas;
    Button btnFecha;
    EditText etFecha;
    private HttpConnection connection;
    private String enlaceBusqueda;
    private String ip;
    private int dia, mes, anio;
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas_asignadas_especialista);

        connection = new HttpConnection();
        ip = DireccionIP.getIp();
        btnFecha = (Button) findViewById(R.id.btnFecha);
        etFecha = (EditText) findViewById(R.id.etFecha);
        btnFecha.setOnClickListener(this);
        lista = (ListView)findViewById(R.id.listFechas);
    }

    @Override
    public void onClick(View v) {

        if (v == btnFecha) {
            final Calendar c = Calendar.getInstance();

            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH);
            anio = c.get(Calendar.YEAR) + 117;

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    etFecha.setText(year + "-" + "0"+(monthOfYear + 1) + "-" +dayOfMonth);
                }
            }
                    , dia, mes, anio);
            datePickerDialog.show();
        }
    }


    public void liatarCitasEspecialista(View v){

        String fecha = etFecha.getText().toString();
        enlaceBusqueda = "http://" + ip + "/serviciosWebHospital/citasEspecialistaFecha.php?fecha="+fecha;
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
                ArrayAdapter<CitaEspecialista> adaptador = new ArrayAdapter<CitaEspecialista>(citasAsignadasEspecialista.this,android.R.layout.simple_spinner_item,listaCitas);
                adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                lista.setAdapter(adaptador);
            }else{
                Toast.makeText(getApplicationContext(),"no hay citas para esta fecha",Toast.LENGTH_SHORT).show();
            }

        }
    }

    public int obtenerJSONListaCitas(String respuesta){
        Log.e("respuesta",respuesta);
        int resultado = 0;
        try{
            JSONArray json = new JSONArray(respuesta);
            listaCitas = new ArrayList<CitaEspecialista>();

            for (int i=0; i<json.length(); i++){

                resultado = 1;
                JSONObject row = json.getJSONObject(i);

                String fechaInicio = row.getString("FECHA_HORA");
                listaCitas.add(new CitaEspecialista(fechaInicio));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        return resultado;
    }
}

