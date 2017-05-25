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
import com.example.luchobolivar.hospitaleleden.modelo.UsuarioLogueado;

import org.json.JSONException;
import org.json.JSONObject;

public class GestionarAdminActivity extends AppCompatActivity {

    private String enlace;
    private String enlaceEliminar;

    private HttpConnection connection;

    EditText etNombre;
    EditText etApellido;
    EditText etTel;
    EditText etDir;
    EditText etEmail;
    EditText etUser;
    EditText etPass;
    Spinner cbGenero;

    Usuario user;

    private String ip;
    private String[] generos = {"Seleccione un genero", "Masculino", "Femenino"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitygestionaradmin);

        etNombre = (EditText) findViewById(R.id.etNombreAdmin);
        etApellido = (EditText) findViewById(R.id.etApellidoAdmin);
        etTel = (EditText) findViewById(R.id.etTelefonoAdmin);
        etDir = (EditText) findViewById(R.id.etDireccionAdmin);
        etEmail = (EditText) findViewById(R.id.etEmailAdmin);
        etUser = (EditText) findViewById(R.id.etUsernameAdmin);
        etPass = (EditText) findViewById(R.id.etPasswordAdmin);
        cbGenero = (Spinner) findViewById(R.id.spGeneroAdmin);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, generos);
        cbGenero.setAdapter(adaptador);

        user = UsuarioLogueado.getUsuario();

        etNombre.setText(user.getNombre());
        etApellido.setText(user.getApellido());
        etDir.setText(user.getDireccion());
        etEmail.setText(user.getEmail());
        etTel.setText(user.getTelefono());
        etUser.setText(user.getUsername());
        etPass.setText(user.getPassword());
        cbGenero.setSelection(user.getGenero());

        connection = new HttpConnection();

        ip = DireccionIP.getIp();

    }

    public void regresar (View v){
        Intent i = new Intent(this, AdministradorActivity.class);
        startActivity(i);
    }

    public void eliminarAdmin(View v){
        enlaceEliminar = "http://"+ip+"/serviciosWebHospital/eliminarAdmin.php?NUMERO_IDENTIFICACION="
                +user.getIdentificacion() + "&ROL=admin";
        new eliminar().execute(enlaceEliminar);
    }

    public void editarAdmin (View v){

        String nombre = etNombre.getText().toString();
        String apellido = etApellido.getText().toString();
        String tel = etTel.getText().toString();
        String dir = etDir.getText().toString();
        String usu = etUser.getText().toString();
        String pass = etPass.getText().toString();
        String email = etEmail.getText().toString();
        int genero = cbGenero.getSelectedItemPosition();


        enlace = "http://"+ip+"/serviciosWebHospital/editarUser.php?NUMERO_IDENTIFICACION="
                + user.getIdentificacion() + "&NOMBRE=" + nombre + "&APELLIDO=" + apellido + "&TELEFONO=" + tel +
                "&EMAIL=" + email + "&DIRECCION=" + dir + "&USER_NAME=" + usu + "&GENERO_ID=" + genero +
                "&PASSWORD=" + pass;

        new editar().execute(enlace);

    }

    public int obtenerDatosJSON(String respuesta) {
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

    class editar extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int res = obtenerDatosJSON(resultado);
            if (res == 1) {
                Toast.makeText(getApplicationContext(), "Se ha editado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlace);
            return resultado;
        }
    }

    class eliminar extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int res = obtenerDatosJSONEliminar(resultado);
            if (res == 1) {
                Toast.makeText(getApplicationContext(), "Eliminación exitosa", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceEliminar);
            return resultado;
        }
    }


}
