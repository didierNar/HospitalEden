package com.example.luchobolivar.hospitaleleden;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.luchobolivar.hospitaleleden.HttpURLConnection.HttpConnection;
import com.example.luchobolivar.hospitaleleden.modelo.Ciudad;
import com.example.luchobolivar.hospitaleleden.modelo.Departamento;
import com.example.luchobolivar.hospitaleleden.modelo.DireccionIP;
import com.example.luchobolivar.hospitaleleden.modelo.Sede;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityGestionSede extends AppCompatActivity {

    private Spinner spCiudades;
    private Spinner spDepartamentos;
    private Spinner spSedes;

    private EditText etLatitud;
    private EditText etLongitud;
    private EditText etDescripcion;

    private List<Departamento> departamentos;
    private List<Ciudad> ciudades;
    private List<Sede> sedes;

    private String ip;
    private HttpConnection connection;

    private String enlaceDeptos;
    private String enlaceCiudades;
    private String enlaceEditar;
    private String enlaceEiminar;
    private String enlaceSedes;
    private String enlaceBuscar;

    private Sede buscada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_sede);

        buscada = null;

        ip = DireccionIP.getIp();
        connection = new HttpConnection();

        spCiudades = (Spinner) findViewById(R.id.spCiudadesSedeGestion);
        spDepartamentos = (Spinner) findViewById(R.id.spDeptosSedeGestion);
        spSedes = (Spinner) findViewById(R.id.spSedesGestion);

        etDescripcion = (EditText) findViewById(R.id.etDescSedeGestion);
        etLatitud = (EditText) findViewById(R.id.etLatitudSedeGestion);
        etLongitud = (EditText) findViewById(R.id.etLongSedeGestion);

        departamentos = new ArrayList<Departamento>();
        ciudades = new ArrayList<Ciudad>();
        sedes = new ArrayList<Sede>();

        llenarDeptos();

        spDepartamentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Departamento d = (Departamento) spDepartamentos.getSelectedItem();
                llenarCiudades(d.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        spCiudades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Ciudad d = (Ciudad) spCiudades.getSelectedItem();
                llenarSedes(d.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    public void buscarSede (View v){
        Sede s = (Sede) spSedes.getSelectedItem();
        enlaceBuscar = "http://"+ip+"/serviciosWebHospital/BuscarSede.php?ID="+s.getCodigo();
        new buscarSede().execute(enlaceBuscar);
    }

    private void llenarSedes (int codCiudad){
        enlaceSedes = "http://"+ip+"/serviciosWebHospital/ListarSedesPorCiudad.php?CIUDAD_ID="+codCiudad;
        new listarSedes().execute(enlaceSedes);
    }

    public void regresarAdmin (View v){
        Intent i = new Intent(this, AdministradorActivity.class);
        startActivity(i);
    }

    public void editarSede (View v){
        Sede s = (Sede) spSedes.getSelectedItem();
        String desc = etDescripcion.getText().toString();
        Ciudad c = (Ciudad) spCiudades.getSelectedItem();
        double latitud = Double.parseDouble(etLatitud.getText().toString());
        double longitud = Double.parseDouble(etLongitud.getText().toString());

        enlaceEditar = "http://"+ip+"/serviciosWebHospital/EditarSede.php?ID="+s.getCodigo()+"&DESCRIPCION="+desc+
                "&CIUDAD_ID="+c.getId()+"&LATITUD="+latitud+"&LONGITUD="+longitud;
        new editar().execute(enlaceEditar);
    }

    public void eliminarSede (View v){
        Sede s = (Sede) spSedes.getSelectedItem();
        enlaceEiminar = "http://"+ip+"/serviciosWebHospital/EliminarSede.php?ID="+s.getCodigo();
        new eliminar().execute(enlaceEiminar);
    }

    private void llenarDeptos() {
        departamentos = new ArrayList<Departamento>();
        enlaceDeptos = "http://" + ip + "/serviciosWebHospital/ListarDeptos.php?PAIS_ID=1";
        new listarDeptos().execute(enlaceDeptos);
    }

    private void llenarCiudades(int codDepto){
        ciudades = new ArrayList<Ciudad>();
        enlaceCiudades = "http://"+ip+"/serviciosWebHospital/ListarCiudades.php?DEPARTAMENTO_ID="+codDepto;
        new listarCiudades().execute(enlaceCiudades);
    }

    class listarDeptos extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int r = obtenerDatosJSONDepto(resultado);
            if (r > 0) {
                ArrayAdapter<Departamento> spinnerArrayAdapter = new ArrayAdapter<Departamento>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, departamentos);
                spDepartamentos.setAdapter(spinnerArrayAdapter);
            } else {
                Toast.makeText(getApplicationContext(), "No hay departamentos registrados", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceDeptos);
            return resultado;
        }
    }

    class listarCiudades extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int r = obtenerDatosJSONCiudad(resultado);
            if (r > 0) {
                ArrayAdapter<Ciudad> spinnerArrayAdapter = new ArrayAdapter<Ciudad>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, ciudades);
                spCiudades.setAdapter(spinnerArrayAdapter);
            } else {
                Toast.makeText(getApplicationContext(), "No hay ciudades registrados", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceCiudades);
            return resultado;
        }
    }

    public int obtenerDatosJSONDepto(String respuesta) {
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
                    String desc = row.getString("DESCRIPCION");
                    int pais = row.getInt("PAIS_ID");

                    Departamento d = new Departamento(id, desc, pais);
                    departamentos.add(d);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public int obtenerDatosJSONCiudad(String respuesta) {
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
                    String desc = row.getString("DESCRIPCION");
                    int depto = row.getInt("DEPARTAMENTO_ID");

                    Ciudad c = new Ciudad(id, desc, depto);
                    ciudades.add(c);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
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
                etDescripcion.setText("");
                etLatitud.setText("");
                etLongitud.setText("");
                Ciudad c = (Ciudad) spCiudades.getSelectedItem();
                llenarSedes(c.getId());
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceEiminar);
            return resultado;
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

    class editar extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int res = obtenerDatosJSONEditar(resultado);
            if (res == 1) {
                Toast.makeText(getApplicationContext(), "Se ha editado exitosamente", Toast.LENGTH_SHORT).show();
                etDescripcion.setText("");
                etLatitud.setText("");
                etLongitud.setText("");
                Ciudad c = (Ciudad) spCiudades.getSelectedItem();
                llenarSedes(c.getId());
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceEditar);
            return resultado;
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

    class listarSedes extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int res = obtenerDatosJSONListar(resultado);
            if (res == 1) {
                ArrayAdapter<Sede> spinnerArrayAdapter = new ArrayAdapter<Sede>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, sedes);
                spSedes.setAdapter(spinnerArrayAdapter);
                spSedes.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getApplicationContext(), "No hay sedes registradas en esta ciudad", Toast.LENGTH_SHORT).show();
                spSedes.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceSedes);
            return resultado;
        }
    }

    public int obtenerDatosJSONListar (String respuesta){
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
                    String desc = row.getString("DESCRIPCION");
                    int ciudad = row.getInt("CIUDAD_ID");
                    double longitud = row.getDouble("LONGITUD");
                    double latitud = row.getDouble("LATITUD");

                    Sede sede = new Sede(id, desc, 1, latitud, longitud, ciudad);
                    sedes.add(sede);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    class buscarSede extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int r = obtenerDatosJSONBusqueda(resultado);
            if (r > 0) {
                etDescripcion.setText(buscada.getDescripcion());
                etLatitud.setText(buscada.getLatitud()+"");
                etLongitud.setText(buscada.getLongitud()+"");
            } else {
                Toast.makeText(getApplicationContext(), "Debe seleccionar una sede", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceBuscar);
            return resultado;
        }
    }

    public int obtenerDatosJSONBusqueda (String respuesta){
        Log.e("Respuesta", respuesta);
        int resultado = 0;
        try {
            JSONArray json = new JSONArray(respuesta);
            //Verficamos que el tamaño del json sea mayor que 0
            if (json.length() > 0) {
                resultado = 1;
                    JSONObject row = json.getJSONObject(0);

                    int id = row.getInt("ID");
                    String desc = row.getString("DESCRIPCION");
                    int ciudad = row.getInt("CIUDAD_ID");
                    double longitud = row.getDouble("LONGITUD");
                    double latitud = row.getDouble("LATITUD");

                    buscada = new Sede(id, desc, 1, latitud, longitud, ciudad);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }

}
