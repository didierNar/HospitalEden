package com.example.luchobolivar.hospitaleleden;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.luchobolivar.hospitaleleden.HttpURLConnection.HttpConnection;
import com.example.luchobolivar.hospitaleleden.modelo.DireccionIP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GestionEspecializacion extends AppCompatActivity {

    private Intent intent;
    private HttpConnection connection;
    private String enlaceRegistro;
    private String enlaceBusqueda;
    private String enlaceEditar;
    private String enlaceEliminar;
    private String ip;


    private EditText etCodigo;
    private EditText etespecializacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_especializacion);

        connection = new HttpConnection();
        ip = DireccionIP.getIp();
        etCodigo = (EditText)findViewById(R.id.codigo);
        etespecializacion = (EditText)findViewById(R.id.especializacion);


    }


    public void volverInicioGestion(View v) {
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void registrarEspecializacion(View v){

        int codigo =  Integer.parseInt(etCodigo.getText().toString());
        String especializacion = etespecializacion.getText().toString();

        enlaceRegistro = "http://" + ip + "/serviciosWebHospital/crearEspecializacion.php?codigo="+codigo+"&especializacion="+especializacion;
        new registrarEspe().execute(enlaceRegistro);
    }

    class registrarEspe extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceRegistro);
            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int res = obtenerDatosJSONReg(resultado);
            if (res == 1) {
                Toast.makeText(getApplicationContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "no se pudo registrar", Toast.LENGTH_SHORT).show();
            }
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


    public void buscarEspecializacion(View v){

        if(etCodigo.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "ingrese un codigo de identificacion", Toast.LENGTH_SHORT).show();
        }else{

            int codigo =  Integer.parseInt(etCodigo.getText().toString());
            enlaceBusqueda = "http://"+ip+"/serviciosWebHospital/buscarEspecializacion.php?ID="+codigo;
            new busacarPersonalMedico().execute(enlaceBusqueda);

        }
    }
    class busacarPersonalMedico extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceBusqueda);
            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int res = obtenerDatosJSONEspeciali(resultado);
            if (res == 1) {
                Toast.makeText(getApplicationContext(), "personal Encontrado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "no existe", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public int obtenerDatosJSONEspeciali(String respuesta) {
        Log.e("Respuesta", respuesta);
        int resultado = 0;
        try {
            JSONArray json = new JSONArray(respuesta);
            //Verficamos que el tamaño del json sea mayor que 0
            if (json.length()>0) {
                resultado = 1;

                JSONObject row = json.getJSONObject(0);

                etCodigo.setText(row.getInt("ID")+"");
                etespecializacion.setText(row.getString("DESCRIPCION"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }


    public void editarEspecializacion(View v) {

        if (etCodigo.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "realice una busqueda primero", Toast.LENGTH_SHORT).show();
        } else {

            int codigo =  Integer.parseInt(etCodigo.getText().toString());
            String especializacion = etespecializacion.getText().toString();

            enlaceEditar = "http://" + ip + "/serviciosWebHospital/editarEspecializacion.php?codigo="+codigo+"&especializacion="+especializacion;
            new editar().execute(enlaceEditar);
        }
    }

    class editar extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceEditar);
            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int res = obtenerDatosJSONEditar(resultado);
            if (res == 1) {
                Toast.makeText(getApplicationContext(), "Se ha editado exitosamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "no se pudo editar", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public int obtenerDatosJSONEditar(String respuesta) {
        Log.e("Respuesta", respuesta);
        int resultado = 0;
        try {
            JSONObject json = new JSONObject(respuesta);
            //Log.e("Tamaño ", json.getString("registro")+"");
            String res = json.getString("editado");
            //Verficamos que el tamaño del json sea mayor que 0
            if (res.equals("1")) {
                resultado = 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }


    public void eliminarEspecializacion(View v){
        if (etCodigo.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "realice una busqueda primero", Toast.LENGTH_SHORT).show();
        } else {
            int codigo =  Integer.parseInt(etCodigo.getText().toString());
            enlaceEliminar =  "http://" + ip + "/serviciosWebHospital/eliminarEspecializacion.php?ID="+codigo;
            new eliminar().execute(enlaceEliminar);
        }

    }

    class eliminar extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceEliminar);
            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int res = obtenerDatosJSONEliminar(resultado);
            if (res == 1) {
                Toast.makeText(getApplicationContext(), "Eliminación exitosa", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public int obtenerDatosJSONEliminar(String respuesta) {
        Log.e("Respuesta", respuesta);
        int resultado = 0;
        try {
            JSONObject json = new JSONObject(respuesta);
            //Log.e("Tamaño ", json.getString("registro")+"");
            String res = json.getString("eliminar");
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
