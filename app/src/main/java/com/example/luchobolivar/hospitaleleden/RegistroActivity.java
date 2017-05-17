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
import com.example.luchobolivar.hospitaleleden.modelo.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegistroActivity extends AppCompatActivity {

    private Intent intent;
    private HttpConnection connection;
    private String enlace;
    private String enlaceRegistro;

    //Datos ingresados
    private EditText etNombre;
    private EditText etApellido;
    private EditText etIdentificacion;
    private EditText etDireccion;
    private EditText etTel;
    private EditText etEmail;
    private EditText etUsername;
    private EditText etPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        connection = new HttpConnection();

        etNombre = (EditText) findViewById(R.id.etNombre);
        etApellido = (EditText) findViewById(R.id.etApellido);
        etIdentificacion = (EditText) findViewById(R.id.etIdentificacion);
        etDireccion = (EditText) findViewById(R.id.etDireccion);
        etTel = (EditText) findViewById(R.id.etTelefono);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPass = (EditText) findViewById(R.id.etPassword);

    }

    public void volverInicio(View v) {
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void registrar(View v) {
        enlace = "http://192.168.1.9/serviciosWebHospital/buscarUser.php?numero_identificacion="
                + etIdentificacion.getText().toString()+"&user_name="+etUsername.getText().toString();
        Log.e("enlace ", enlace);
        new buscarUsuario().execute(enlace);
    }

    public void agregarUsuario() {

        int id = Integer.parseInt(etIdentificacion.getText().toString());
        String nombre = etNombre.getText().toString();
        String apellido = etApellido.getText().toString();
        String dir = etDireccion.getText().toString();
        String tel = etTel.getText().toString();
        String email = etEmail.getText().toString();
        String user = etUsername.getText().toString();
        String pass = etPass.getText().toString();
        int genero = 1;
        String rol = "paciente";

        new Usuario(id, nombre, apellido, tel, email, dir, genero, user, pass, rol);

        enlaceRegistro = "http://192.168.1.9/serviciosWebHospital/crearUser.php?NUMERO_IDENTIFICACION="
                + id + "&NOMBRE=" + nombre + "&APELLIDO=" + apellido + "&TELEFONO=" + tel +
                "&EMAIL=" + email + "&DIRECCION=" + dir + "&USER_NAME=" + user + "&GENERO_ID=" + genero +
                "&PASSWORD=" + pass + "&ROL=" +rol;

        Log.e("Enlace", enlaceRegistro);

        //connection.enviarDatosGet(enlace);
        new registrar().execute(enlaceRegistro);

    }


    class buscarUsuario extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int r = obtenerDatosJSON(resultado);
            if (r > 0) {
                Toast.makeText(getApplicationContext(), "El número de identificación o el nombre de " +
                        "usuario ingresado ya existe", Toast.LENGTH_SHORT).show();
            } else {
                agregarUsuario();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlace);
            return resultado;
        }
    }

    public int obtenerDatosJSON(String respuesta) {
        Log.e("Respuesta", respuesta);
        int resultado = 0;
        try {
            JSONArray json = new JSONArray(respuesta);
            Log.e("Tamaño ", json.length()+"");
            //Verficamos que el tamaño del json sea mayor que 0
            if (json.length() > 0) {
                resultado = 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
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

}
