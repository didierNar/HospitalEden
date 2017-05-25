package com.example.luchobolivar.hospitaleleden;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.luchobolivar.hospitaleleden.HttpURLConnection.HttpConnection;
import com.example.luchobolivar.hospitaleleden.modelo.DireccionIP;
import com.example.luchobolivar.hospitaleleden.modelo.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegistroActivity extends AppCompatActivity {

    private Intent intent;
    private HttpConnection connection;
    private String enlace;
    private String enlaceRegistro;
    private String enlaceRegistroRegimen;
    private String enlaceEditarPaciente;
    private String enlaceEditarUsu;
    private String enlaceBuscarPaciente;
    private int eps = 0;
    private int regimenPaciente = 0;
    //Datos ingresados
    private EditText etNombre;
    private EditText etApellido;
    private EditText etIdentificacion;
    private EditText etDireccion;
    private EditText etTel;
    private EditText etEmail;
    private EditText etUsername;
    private EditText etPass;
    private Spinner cbGenero;
    private String[] generos = {"Seleccione un genero", "Masculino", "Femenino"};
    private Spinner cbRegimen;
    private String[]regimen = {"Seleccione un Regimen","Contributivo","Subsidiado"};

    String ip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityregistro);

        connection = new HttpConnection();
        etNombre = (EditText) findViewById(R.id.etNombre);
        etApellido = (EditText) findViewById(R.id.etApellido);
        etIdentificacion = (EditText) findViewById(R.id.etIdentificacion);
        etDireccion = (EditText) findViewById(R.id.etDireccion);
        etTel = (EditText) findViewById(R.id.etTelefono);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPass = (EditText) findViewById(R.id.etPassword);
        cbGenero = (Spinner) findViewById(R.id.spGenero);
        cbRegimen = (Spinner)findViewById(R.id.spRegimen);
        eps = 1;

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, generos);
        cbGenero.setAdapter(adaptador);

        ArrayAdapter<String> adaptadorRegimen = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, regimen);
        cbRegimen.setAdapter(adaptadorRegimen);

        ip = DireccionIP.getIp();

    }

    public void volverInicio(View v) {
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void registrar(View v) {
        enlace = "http://"+ip+"/serviciosWebHospital/buscarUser.php?numero_identificacion="
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
        int genero = cbGenero.getSelectedItemPosition();
        String rol = "paciente";

        new Usuario(id, nombre, apellido, tel, email, dir, genero, user, pass, rol);

        enlaceRegistro = "http://"+ip+"/serviciosWebHospital/crearUser.php?NUMERO_IDENTIFICACION="
                + id + "&NOMBRE=" + nombre + "&APELLIDO=" + apellido + "&TELEFONO=" + tel +
                "&EMAIL=" + email + "&DIRECCION=" + dir + "&USER_NAME=" + user + "&GENERO_ID=" + genero +
                "&PASSWORD=" + pass + "&ROL=" +rol;

        Log.e("Enlace", enlaceRegistro);

        int posicion = cbRegimen.getSelectedItemPosition();
        if(posicion == 0){
            Toast.makeText(getApplicationContext(), "seleccione una opcion ", Toast.LENGTH_SHORT).show();
        }else if(posicion == 1){
            regimenPaciente = 1;
        }else if(posicion ==2){
            regimenPaciente = 2;
        }
        enlaceRegistroRegimen = "http://"+ip+"/serviciosWebHospital/crearPaciente.php?id="+etIdentificacion.getText().toString()+
                "&eps="+eps+"&tipo="+regimenPaciente;
        Log.e("enlace ", enlaceRegistroRegimen);

        new registrar().execute(enlaceRegistro);
        new registrarPaciente().execute(enlaceRegistroRegimen);

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
        Log.e("Respuestaa", respuesta);
        int resultado = 0;
        try {
            JSONArray json = new JSONArray(respuesta);
            Log.e("Tamanio", json.length()+"");
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



    class registrarPaciente extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int res = obtenerDatosJSONRegPaciente(resultado);
            if (res == 1) {
                Toast.makeText(getApplicationContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceRegistroRegimen);
            return resultado;
        }
    }


    public int obtenerDatosJSONRegPaciente(String respuesta) {
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

    public void editarPaciente(View v){

        if (etIdentificacion.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "busque previa mente un usuario", Toast.LENGTH_SHORT).show();
        } else {
            //tabla usuario
            int id = Integer.parseInt(etIdentificacion.getText().toString());
            String nombre = etNombre.getText().toString();
            String apellido = etApellido.getText().toString();
            String dir = etDireccion.getText().toString();
            String tel = etTel.getText().toString();
            String email = etEmail.getText().toString();
            String user = etUsername.getText().toString();
            String pass = etPass.getText().toString();
            int genero = cbGenero.getSelectedItemPosition();
            int posicion = cbRegimen.getSelectedItemPosition();

            enlaceEditarUsu = "http://" + ip + "/serviciosWebHospital/editarUsuario.php?NUMERO_IDENTIFICACION="
                    + id + "&NOMBRE=" + nombre + "&APELLIDO=" + apellido + "&TELEFONO=" + tel +
                    "&EMAIL=" + email + "&DIRECCION=" + dir + "&USER_NAME=" + user + "&GENERO_ID=" + genero +
                    "&PASSWORD=" + pass;


            if(posicion == 0){
                Toast.makeText(getApplicationContext(), "seleccione una opcion ", Toast.LENGTH_SHORT).show();
            }else if(posicion == 1){
                regimenPaciente = 1;
            }else if(posicion ==2){
                regimenPaciente = 2;
            }
            enlaceEditarPaciente = "http://" + ip + "/serviciosWebHospital/editarPaciente.php?NUMERO_IDENTIFICACION="+id+"&EPS="+eps+"&REGIMEN="+regimenPaciente;

            new editarUsuario().execute(enlaceEditarUsu);
            new editarPaciente().execute(enlaceEditarPaciente);
        }

    }

    class editarUsuario extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int res = obtenerDatosJSONEditarUsu(resultado);
            if (res == 1) {
                Toast.makeText(getApplicationContext(), "Se ha editado exitosamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "no se ha podido editar correctamente", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceEditarUsu);
            return resultado;
        }
    }

    public int obtenerDatosJSONEditarUsu(String respuesta) {
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


    class editarPaciente extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int res = obtenerDatosJSONEditarPaciente(resultado);
            if (res == 1) {
                Toast.makeText(getApplicationContext(), "Se ha editado exitosamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "no se ha podido editar correctamente", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceEditarPaciente);
            return resultado;
        }
    }

    public int obtenerDatosJSONEditarPaciente(String respuesta) {
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



    public void buscarPaciente(View v){

        if (etIdentificacion.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "ingrese un numero de identificacion", Toast.LENGTH_SHORT).show();
        } else {
            enlaceBuscarPaciente = "http://" + ip + "/serviciosWebHospital/buscarPaciente.php?numero_identificacion="+etIdentificacion.getText().toString();
            new busacarPaciente().execute(enlaceBuscarPaciente);
        }

    }

    class busacarPaciente extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int res = obtenerDatosJSONPaciente(resultado);
            if (res == 1) {
                Toast.makeText(getApplicationContext(), "Paciente Encontrado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "no existe", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceBuscarPaciente);
            return resultado;
        }
    }

    public int obtenerDatosJSONPaciente(String respuesta) {
        Log.e("Respuesta", respuesta);
        int resultado = 0;
        try {
            JSONArray json= new JSONArray(respuesta);
            //Verficamos que el tamaño del json sea mayor que 0
            if (json.length()>0) {
                resultado = 1;
                JSONObject row = json.getJSONObject(0);

                etNombre.setText(row.getString("NOMBRE"));
                etApellido.setText(row.getString("APELLIDO"));
                etTel.setText(row.getString("TELEFONO"));
                etIdentificacion.setText(row.getInt("USUARIO_NUMERO_IDENTIFICACION")+"");
                etEmail.setText(row.getString("EMAIL"));
                etDireccion.setText(row.getString("DIRECCION"));
                etUsername.setText(row.getString("USER_NAME"));
                cbGenero.setSelection(row.getInt("GENERO_ID"));
                cbRegimen.setSelection(row.getInt("TIPO_PACIENTE_EPS_ID"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }
}
