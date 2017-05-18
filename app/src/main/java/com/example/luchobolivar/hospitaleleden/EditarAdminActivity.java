package com.example.luchobolivar.hospitaleleden;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.luchobolivar.hospitaleleden.HttpURLConnection.HttpConnection;
import com.example.luchobolivar.hospitaleleden.modelo.DireccionIP;
import com.example.luchobolivar.hospitaleleden.modelo.Usuario;
import com.example.luchobolivar.hospitaleleden.modelo.UsuarioLogueado;

import org.json.JSONException;
import org.json.JSONObject;

public class EditarAdminActivity extends AppCompatActivity {

    private String identificacion;
    private String enlace;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_admin);

        etNombre = (EditText) findViewById(R.id.etNombreAdmin);
        etApellido = (EditText) findViewById(R.id.etApellidoAdmin);
        etTel = (EditText) findViewById(R.id.etTelefonoAdmin);
        etDir = (EditText) findViewById(R.id.etDireccionAdmin);
        etEmail = (EditText) findViewById(R.id.etEmailAdmin);
        etUser = (EditText) findViewById(R.id.etUsernameAdmin);
        etPass = (EditText) findViewById(R.id.etPasswordAdmin);
        cbGenero = (Spinner) findViewById(R.id.spGeneroAdmin);

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


}
