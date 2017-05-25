package com.example.luchobolivar.hospitaleleden;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.luchobolivar.hospitaleleden.HttpURLConnection.HttpConnection;
import com.example.luchobolivar.hospitaleleden.modelo.ConsultasPacientes;
import com.example.luchobolivar.hospitaleleden.modelo.DireccionIP;
import com.example.luchobolivar.hospitaleleden.modelo.PersonalMedico;
import com.example.luchobolivar.hospitaleleden.modelo.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityConsultasAtendidas extends AppCompatActivity {

    private Spinner spEspecialistas;
    private ListView lvPacientes;

    private HttpConnection connection;

    private List<PersonalMedico> especialistas;
    private List<Usuario> usuariosEsp;
    private List<ConsultasPacientes> datosConsulta;

    private String enlace;
    private String enlaceEspecialistas;
    private String enlaceUsuarios;

    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas_atendidas);

        spEspecialistas = (Spinner) findViewById(R.id.spEspecialistas);
        lvPacientes = (ListView) findViewById(R.id.lvCitasAtendidas);

        especialistas = new ArrayList<PersonalMedico>();
        usuariosEsp = new ArrayList<Usuario>();
        datosConsulta = new ArrayList<ConsultasPacientes>();

        ip = DireccionIP.getIp();

        listarEspecialistas();

        connection = new HttpConnection();

        spEspecialistas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Usuario sel = (Usuario) spEspecialistas.getSelectedItem();
                llenarConsultas(sel.getIdentificacion());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    private void llenarConsultas(int idEspecialista) {
        enlace = "http://" + ip + "/serviciosWebHospital/ConsultasAtendidas.php?NUMERO_IDENTIFICACION=" + idEspecialista;
        new ListarConsultas().execute(enlace);
    }

    private void listarEspecialistas() {
        enlaceEspecialistas = "http://" + ip + "/serviciosWebHospital/ListarEspecialistas.php?";
        new Especialistas().execute(enlaceEspecialistas);
    }

    private void listarUsuariosEspecialistas() {
        for (int i = 0; i < especialistas.size(); i++) {
            enlaceUsuarios = "http://" + ip + "/serviciosWebHospital/buscarUserRol.php?NUMERO_IDENTIFICACION=" +
                    especialistas.get(i).getIdentificacion() + "&ROL=personal";
            new Usuarios().execute(enlaceUsuarios);
        }
    }

    class Especialistas extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceEspecialistas);
            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int res = obtenerDatosJSONEspecialista(resultado);
            if (res == 1) {
                listarUsuariosEspecialistas();
            } else {
                Toast.makeText(getApplicationContext(), "no hay especialistas registrados", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public int obtenerDatosJSONEspecialista(String respuesta) {
        Log.e("Respuesta", respuesta);
        int resultado = 0;
        try {
            JSONArray json = new JSONArray(respuesta);
            //Verficamos que el tamaño del json sea mayor que 0
            if (json.length() > 0) {
                resultado = 1;
                for (int i = 0; i < json.length(); i++) {
                    JSONObject row = json.getJSONObject(i);

                    int identificacion = row.getInt("USUARIO_NUMERO_IDENTIFICACION");
                    int especializacion = row.getInt("AREA_ESPECIALIZACION_ID");
                    int sede = row.getInt("SEDE");
                    int tipoPersonal = row.getInt("TIPO_PERSONAL_ID");

                    PersonalMedico pm = new PersonalMedico(identificacion, tipoPersonal, especializacion, sede);
                    especialistas.add(pm);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    class Usuarios extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlaceUsuarios);
            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int res = obtenerDatosJSONUsuarios(resultado);
            if (res == 1) {
                ArrayAdapter<Usuario> adaptador = new ArrayAdapter<Usuario>(getApplicationContext(),
                        R.layout.support_simple_spinner_dropdown_item, usuariosEsp);
                spEspecialistas.setAdapter(adaptador);
            }
        }
    }

    public int obtenerDatosJSONUsuarios(String respuesta) {
        Log.e("Respuesta", respuesta);
        int resultado = 0;
        try {
            JSONArray json = new JSONArray(respuesta);
            //Verficamos que el tamaño del json sea mayor que 0
            if (json.length() > 0) {
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

                Usuario user = new Usuario(identificacion, nombre, apellido, telefono, email, direccion, genero, username, password, rol);
                usuariosEsp.add(user);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    class ListarConsultas extends AsyncTask<String, Float, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultado = connection.enviarDatosGet(enlace);
            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            int res = obtenerDatosJSONConsultas(resultado);
            if (res == 1) {
                ArrayAdapter<ConsultasPacientes> adaptador = new ArrayAdapter<ConsultasPacientes>(getApplicationContext(),
                        R.layout.support_simple_spinner_dropdown_item, datosConsulta);
                lvPacientes.setAdapter(adaptador);

            } else {
                Toast.makeText(getApplicationContext(), "No hay pacientes consultados por este médico", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public int obtenerDatosJSONConsultas(String respuesta) {
        Log.e("Respuesta", respuesta);
        int resultado = 0;
        try {
            JSONArray json = new JSONArray(respuesta);
            //Verficamos que el tamaño del json sea mayor que 0
            if (json.length() > 0) {
                resultado = 1;
                for (int i=0; i<json.length(); i++) {
                    JSONObject row = json.getJSONObject(i);

                    String nombre = row.getString("NOMBRE");
                    String apellido = row.getString("APELLIDO");
                    double valor = row.getDouble("VALOR");

                    ConsultasPacientes datos = new ConsultasPacientes(nombre, apellido, valor);
                    datosConsulta.add(datos);

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultado;
    }

}
