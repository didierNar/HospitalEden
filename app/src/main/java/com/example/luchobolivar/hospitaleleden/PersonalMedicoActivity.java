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
import com.example.luchobolivar.hospitaleleden.modelo.AreaEspecializacion;
import com.example.luchobolivar.hospitaleleden.modelo.DireccionIP;
import com.example.luchobolivar.hospitaleleden.modelo.TipoPersonal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PersonalMedicoActivity extends AppCompatActivity {

    private ArrayList<TipoPersonal> tipoPersonalMed;
    private List<AreaEspecializacion> areas;

    private Intent intent;
    private HttpConnection connection;
    private String enlace;
    private String enlaceDos;
    private String enlaceRegistro;
    private String enlaceRegistroPersonal;
    private String enlaceBuscarPersonal;
    private String enlaceListarTipos;
    private String enlaceListarEspe;
    private String enlaceEditarUsuaurio;
    private String enlaceEditarPersonal;
    private String enlaceEliminarUsuario;
    private String enlaceEliminarPersonal;

    private String ip;

    //Datos ingresados
    private EditText etNombre;
    private EditText etApellido;
    private EditText etIdentificacion;
    private EditText etDireccion;
    private EditText etTel;
    private EditText etEmail;
    private EditText etUsername;
    private EditText etPass;
    private Spinner genero;
    private Spinner tipoPersonal;
    private Spinner especializacion;

    private String[] generos = {"seleccione un genero", "Masculino", "Femenino"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypersonalmedico);

        connection = new HttpConnection();
        ip = DireccionIP.getIp();

        etNombre = (EditText) findViewById(R.id.etNombrePer);
        etApellido = (EditText) findViewById(R.id.etApellidoPer);
        etIdentificacion = (EditText) findViewById(R.id.etIdentificacionPer);
        etDireccion = (EditText) findViewById(R.id.etDireccionPer);
        etTel = (EditText) findViewById(R.id.etTelefonoPer);
        etEmail = (EditText) findViewById(R.id.etEmailPer);
        etUsername = (EditText) findViewById(R.id.etUsernamePer);
        etPass = (EditText) findViewById(R.id.etPasswordPer);
        genero = (Spinner) findViewById(R.id.spgenero);
        tipoPersonal = (Spinner) findViewById(R.id.spTipoPersonal);
        especializacion = (Spinner) findViewById(R.id.spEspecializacion);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, generos);
        genero.setAdapter(adaptador);
        cargarTiposPersonal();
        cargarAreasEsp();


    }

    public void volverInicioPer(View v) {
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    /**public void registrar(View v) {
     enlace = "http://" + ip + "/serviciosWebHospital/buscarUser.php?numero_identificacion="
     + etIdentificacion.getText().toString() + "&user_name=" + etUsername.getText().toString();
     Log.e("enlace ", enlace);
     new buscarUsuario().execute(enlace);
     }*/

    public void agregarPersonalMedico(View v) {


        int id = Integer.parseInt(etIdentificacion.getText().toString());
        String nombre = etNombre.getText().toString();
        String apellido = etApellido.getText().toString();
        String dir = etDireccion.getText().toString();
        String tel = etTel.getText().toString();
        String email = etEmail.getText().toString();
        String user = etUsername.getText().toString();
        String pass = etPass.getText().toString();
        int gener = genero.getSelectedItemPosition();
        String rol = "personal";
        TipoPersonal tipo = (TipoPersonal) tipoPersonal.getSelectedItem();
        AreaEspecializacion area = (AreaEspecializacion) especializacion.getSelectedItem();

        enlaceRegistro = "http://" + ip + "/serviciosWebHospital/crearUser.php?NUMERO_IDENTIFICACION=" + id + "&NOMBRE=" + nombre + "&APELLIDO=" + apellido + "&TELEFONO=" + tel +
                "&EMAIL=" + email + "&DIRECCION=" + dir + "&USER_NAME=" + user + "&GENERO_ID=" + genero +
                "&PASSWORD=" + pass + "&ROL=" + rol;

        enlaceRegistroPersonal = "http://" + ip + "/serviciosWebHospital/crearPersonal.php?USUARIO_NUMERO_IDENTIFICACION="
                + id + "&TIPO_PERSONAL_ID=" + tipo.getId() + "&AREA_ESPECIALIZACION_ID=" + area.getId();
        Log.e("Enlace", enlaceRegistro);


        new registrarUsuario().execute(enlaceRegistro);
        new registrarPersonal().execute(enlaceRegistroPersonal);

    }

    public void buscarPersonal(View v) {
        if (etIdentificacion.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "ingrese un numero de identificacion", Toast.LENGTH_SHORT).show();
        } else {
            int id = Integer.parseInt(etIdentificacion.getText().toString());
            enlaceBuscarPersonal = "http://"+ip+"/serviciosWebHospital/buscarPersonal.php?usuario_numero_identificacion="+id;
            new busacarPersonalMedico().execute(enlaceBuscarPersonal);

        }

    }

    public int obtenerDatosJSONPersonal(String respuesta) {
        Log.e("Respuesta", respuesta);
        int resultado = 0;
        try {
            JSONArray json= new JSONArray();
            //Verficamos que el tamaño del json sea mayor que 0
            if (json.length()>0) {
                resultado = 1;
                JSONObject row = json.getJSONObject(0);

                etIdentificacion.setText(row.getInt("NUMERO_IDENTIFICACION"));
                etNombre.setText(row.getString("NOMBRE"));
                etApellido.setText(row.getString("APELLIDO"));
                etTel.setText(row.getString("TELEFONO"));
                etEmail.setText(row.getString("EMAIL"));
                etDireccion.setText(row.getString("DIRECCION"));
                genero.setSelection(row.getInt("GENERO_ID"));
                etUsername.setText(row.getString("USER_NAME"));
                tipoPersonal.setSelection(row.getInt("TIPO_PERSONAL_ID"));
                especializacion.setSelection(row.getInt("AREA_ESPECIALIZACION_ID"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    class busacarPersonalMedico extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int res = obtenerDatosJSONPersonal(resultado);
            if (res == 1) {
                Toast.makeText(getApplicationContext(), "personal Encontrado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "no existe", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceBuscarPersonal);
            return resultado;
        }
    }


    public void editarPersonal(View v) {

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
            int gener = genero.getSelectedItemPosition();

            //table personal
            TipoPersonal tipo = (TipoPersonal) tipoPersonal.getSelectedItem();
            AreaEspecializacion area = (AreaEspecializacion) especializacion.getSelectedItem();


            enlaceEditarUsuaurio = "http://" + ip + "/serviciosWebHospital/editarUsuario.php?NUMERO_IDENTIFICACION="
                    + id + "&NOMBRE=" + nombre + "&APELLIDO=" + apellido + "&TELEFONO=" + tel +
                    "&EMAIL=" + email + "&DIRECCION=" + dir + "&USER_NAME=" + user + "&GENERO_ID=" + gener +
                    "&PASSWORD=" + pass;

            enlaceEditarPersonal = "http://" + ip + "/serviciosWebHospital/editarTablaPersonalMedico.php?TIPO_PERSONAL_ID="
                    + tipo.getId() + "&AREA_ESPECIALIZACION_ID=" + area.getId() + "&NUMERO_IDENTIFICACION=" + id;

            new editarUsuario().execute(enlaceEditarUsuaurio);
            new editarPersonal().execute(enlaceEditarPersonal);

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
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlace);
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


    class editarPersonal extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int res = obtenerDatosJSONEditarPer(resultado);
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

    public int obtenerDatosJSONEditarPer(String respuesta) {
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


    /** class buscarUsuario extends AsyncTask<String, Float, String> {

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
    agregarPersonalMedico();
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
     Log.e("Tamaño ", json.length() + "");
     //Verficamos que el tamaño del json sea mayor que 0
     if (json.length() > 0) {
     resultado = 1;
     }
     } catch (JSONException e) {
     e.printStackTrace();
     }
     return resultado;
     }*/


    class registrarUsuario extends AsyncTask<String, Float, String> {

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

    class registrarPersonal extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int res = obtenerDatosJSONRegPersonal(resultado);
            if (res == 1) {
                Toast.makeText(getApplicationContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
            }
        }


        public int obtenerDatosJSONRegPersonal(String respuesta) {
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


        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceRegistro);
            return resultado;
        }
    }


    public void eliminarPersonal(View v) {


        if (etIdentificacion.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "busque previa mente un usuario", Toast.LENGTH_SHORT).show();
        } else {
            int id = Integer.parseInt(etIdentificacion.getText().toString());
            enlaceEliminarPersonal = "http://"+ip+"eliminarPersonal.php?NUMERO_IDENTIFICACION="+id;
            enlaceEliminarUsuario = "http://"+ip+"/eliminarUsuario.php?NUMERO_IDENTIFICACION="+id;
            new eliminarPersonal().execute(enlaceEliminarPersonal);
            new eliminarUsu().execute(enlaceEliminarUsuario);
        }

    }

    class eliminarUsu extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int res = obtenerDatosJSONEliminarUsu(resultado);
            if (res == 1) {
                Toast.makeText(getApplicationContext(), "Eliminación exitosa", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceListarEspe);
            return resultado;
        }
    }

    public int obtenerDatosJSONEliminarUsu(String respuesta) {
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

    class eliminarPersonal extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int res = obtenerDatosJSONEliminarPer(resultado);
            if (res == 1) {
                Toast.makeText(getApplicationContext(), "Eliminación exitosa", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceListarEspe);
            return resultado;
        }
    }

    public int obtenerDatosJSONEliminarPer(String respuesta) {
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


    public void cargarTiposPersonal(){
        enlaceListarTipos = "http://"+ip+"/serviciosWebHospital/listarTiposPersonal.php";
        new ListarTipos().execute(enlaceListarTipos);
    }

    class ListarTipos extends AsyncTask<String, Float, String> {

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceListarTipos);
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
            int r = obtenerJSONListaTipoPers(resultado);
            if(r > 0){
                ArrayAdapter<TipoPersonal> adaptador = new ArrayAdapter<TipoPersonal>(PersonalMedicoActivity.this,android.R.layout.simple_spinner_item,tipoPersonalMed);
                adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                tipoPersonal.setAdapter(adaptador);
            }else{
                Toast.makeText(getApplicationContext(),"no hay tipos de personal registrados",Toast.LENGTH_SHORT).show();
            }

        }
    }

    public int obtenerJSONListaTipoPers(String respuesta){
        Log.e("respuesta",respuesta);
        int resultado = 0;
        try{
            JSONArray json = new JSONArray(respuesta);
            tipoPersonalMed = new ArrayList<TipoPersonal>();

            for (int i=0; i<json.length(); i++){

                resultado = 1;
                JSONObject row = json.getJSONObject(i);

                int codigo = row.getInt("ID");
                String descripcion = row.getString("DESCRIPCION");
                tipoPersonalMed.add(new TipoPersonal(codigo,descripcion));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        return resultado;
    }

    public void cargarAreasEsp(){
        enlaceListarEspe = "http://"+ip+"/serviciosWebHospital/listarTiposEspecializacion.php";
        new ListarAreasEspe().execute(enlaceListarEspe);
    }


    class ListarAreasEspe extends AsyncTask<String, Float, String> {

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceListarEspe);
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
            int r = obtenerJSONListaAreasEspe(resultado);
            if(r > 0){
                ArrayAdapter<AreaEspecializacion> adaptador = new ArrayAdapter<AreaEspecializacion>(PersonalMedicoActivity.this,android.R.layout.simple_spinner_item,areas);
                adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                especializacion.setAdapter(adaptador);
            }else{
                Toast.makeText(getApplicationContext(),"no hay areas registrados",Toast.LENGTH_SHORT).show();
            }

        }
    }

    public int obtenerJSONListaAreasEspe(String respuesta) {
        Log.e("respuesta", respuesta);
        int resultado = 0;
        // List<TipoPersonal> lista = new ArrayList<TipoPersonal>();
        try {
            JSONArray json = new JSONArray(respuesta);
            areas = new ArrayList<AreaEspecializacion>();

            for (int i = 0; i < json.length(); i++) {

                resultado = 1;
                JSONObject row = json.getJSONObject(i);

                int codigo = row.getInt("ID");
                String descripcion = row.getString("DESCRIPCION");
                areas.add(new AreaEspecializacion(codigo, descripcion));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }
}
