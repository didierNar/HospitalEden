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

public class MainActivity extends AppCompatActivity {

    private EditText etUsuario;
    private EditText etPass;
    private String enlace;
    private String usuario;
    private String password;
    private HttpConnection connection;
    private Usuario user;


    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsuario = (EditText) findViewById(R.id.etNombreUsuario);
        etPass = (EditText) findViewById(R.id.etPassword);

        connection = new HttpConnection();

    }

    public void validarUsuario(View v){
        usuario = etUsuario.getText().toString();
        password = etPass.getText().toString();
        enlace = "http://192.168.19.2/serviciosWebHospital/login.php?USER_NAME="+ usuario +"&PASSWORD=" + password + "";
        new loginUsuario().execute(enlace);
    }

    public void ventanaRegistro(View v){

        intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }

    public void LogInRol (){
        if(user.getRol().equals("admin")){
            Intent i = new Intent(getApplicationContext(), AdministradorActivity.class);

            i.putExtra("usuario", usuario);
            i.putExtra("password", password);
            startActivity(i);

        } else if (user.getRol().equals("paciente")){

            Intent i = new Intent(getApplicationContext(), PacienteActivity.class);
            i.putExtra("usuario", usuario);
            i.putExtra("password", password);
            startActivity(i);

        } else if (user.getRol().equals("personalMedico")){

            Intent i = new Intent(getApplicationContext(), PersonalMedicoActivity.class);
            i.putExtra("usuario", usuario);
            i.putExtra("password", password);
            startActivity(i);

        }
    }

    class loginUsuario extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int r = obtenerDatosJSON(resultado);
            if (r>0){
                LogInRol();
            } else {
                Toast.makeText(getApplicationContext(), "Usuario o Password incorrecto", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlace);
            return resultado;
        }
    }

    public int obtenerDatosJSON (String respuesta){
        Log.e("Respuesta", respuesta);
        int resultado = 0;
        try{
            JSONArray json = new JSONArray(respuesta);
            //Verficamos que el tamaño del json sea mayor que 0
            if (json.length()>0){
                resultado = 1;
                JSONObject row = json.getJSONObject(0);
                int identificacion = row.getInt("NUMERO_IDENTIFICACION");
                String nombre = row.getString("NOMBRE");
                String apellido = row.getString("APELLIDO");
                String username = row.getString("USER_NAME");
                String password = row.getString("PASSWORD");
                String telefono = row.getString("TELEFONO");
                String email = row.getString("EMAIL");
                String direccion = row.getString("DIRECCION");
                int genero = row.getInt("GENERO_ID");
                String rol = row.getString("ROL");

                user = new Usuario(identificacion, nombre, apellido, telefono, email, direccion, genero, username, password, rol);
                Log.e("usuario ", user.getNombre());

            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        return resultado;
    }


}
